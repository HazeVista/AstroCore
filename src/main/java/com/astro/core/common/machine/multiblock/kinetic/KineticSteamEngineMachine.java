package com.astro.core.common.machine.multiblock.kinetic;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.UITemplate;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IDisplayUIMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.transfer.fluid.IFluidHandlerModifiable;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.config.ConfigHolder;

import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fluids.FluidStack;

import com.astro.core.common.machine.hatches.KineticOutputHatch;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.gregtechceu.gtceu.common.data.GTBlocks.CASING_BRONZE_GEARBOX;
import static com.gregtechceu.gtceu.common.data.GTBlocks.CASING_STEEL_GEARBOX;

@SuppressWarnings("all")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class KineticSteamEngineMachine extends WorkableMultiblockMachine implements IDisplayUIMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            KineticSteamEngineMachine.class, WorkableMultiblockMachine.MANAGED_FIELD_HOLDER);

    public static final int STEAM_PER_LAYER = 25;
    public static final int SU_PER_LAYER = 25000;
    public static final int MAX_LAYERS = 16;
    public static final int MIN_LAYERS = 1;

    @Getter
    @Persisted
    private int layerCount = 0;

    @Nullable
    private NotifiableFluidTank steamTank = null;

    public KineticSteamEngineMachine(IMachineBlockEntity holder, Object... args) {
        super(holder);
        subscribeServerTick(this::kineticTick);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    // ======== Structure ========

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        layerCount = calculateLayerCount();

        steamTank = null;
        for (var part : getParts()) {
            if (!PartAbility.STEAM.isApplicable(part.self().getDefinition().getBlock())) continue;
            for (var hl : part.getRecipeHandlers()) {
                if (!hl.isValid(IO.IN)) continue;
                for (var handler : hl.getCapability(FluidRecipeCapability.CAP)) {
                    if (!(handler instanceof NotifiableFluidTank nft)) continue;
                    if (nft.isFluidValid(0, GTMaterials.Steam.getFluid(1))) {
                        steamTank = nft;
                        break;
                    }
                }
                if (steamTank != null) break;
            }
            if (steamTank != null) break;
        }

        if (steamTank == null) {
            onStructureInvalid();
        }
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        stopKineticOutput();
        steamTank = null;
        layerCount = 0;
    }

    @Persisted
    private int bronzeLayerCount = 0;
    @Persisted
    private int steelLayerCount = 0;

    private int calculateLayerCount() {
        BlockPos controllerPos = getHolder().getSelf().getBlockPos();
        Direction back = getFrontFacing().getOpposite();
        BlockPos startPos = controllerPos.relative(back, 1).below(2);

        bronzeLayerCount = 0;
        steelLayerCount = 0;

        for (int i = 0; i <= MAX_LAYERS; i++) {
            BlockPos checkPos = startPos.relative(back, i);
            Block block = getLevel().getBlockState(checkPos).getBlock();
            if (block == CASING_BRONZE_GEARBOX.get()) {
                bronzeLayerCount++;
            } else if (block == CASING_STEEL_GEARBOX.get()) {
                steelLayerCount++;
            } else {
                break;
            }
        }
        return Math.max(MIN_LAYERS, bronzeLayerCount + steelLayerCount);
    }

    // ======== Tick ========

    private void kineticTick() {
        if (!isFormed() || !isWorkingEnabled() || steamTank == null) {
            stopKineticOutput();
            if (recipeLogic.isActive()) recipeLogic.setWaiting(null);
            return;
        }

        int steamNeeded = calculateSteamPerTick();
        FluidStack steamInTank = steamTank.getFluidInTank(0);

        if (steamInTank.isEmpty() || steamInTank.getAmount() < steamNeeded) {
            stopKineticOutput();
            if (recipeLogic.isActive()) recipeLogic.setWaiting(null);
            return;
        }

        FluidStack drained = steamTank.drainInternal(
                new FluidStack(GTMaterials.Steam.getFluid(), steamNeeded),
                IFluidHandlerModifiable.FluidAction.EXECUTE);

        if (drained.getAmount() >= steamNeeded) {
            updateKineticOutput();
            recipeLogic.setStatus(RecipeLogic.Status.WORKING);
        } else {
            stopKineticOutput();
            recipeLogic.setWaiting(null);
        }
    }

    // ======== Kinetic Output ========

    public float calculateTotalSU() {
        return (bronzeLayerCount * KineticOutputHatch.SU_PER_LAYER) +
                (steelLayerCount * KineticOutputHatch.SU_PER_LAYER * 1.5f);
    }

    public int calculateSteamPerTick() {
        return (bronzeLayerCount * STEAM_PER_LAYER) +
                (steelLayerCount * (int) (STEAM_PER_LAYER * 1.2f));
    }

    private void updateKineticOutput() {
        float totalSU = calculateTotalSU();
        float capacityPerRPM = totalSU / 256f;
        getKineticOutputHatches().forEach(hatch -> hatch.setOutputSU(totalSU, capacityPerRPM));
    }

    private void stopKineticOutput() {
        getKineticOutputHatches().forEach(hatch -> hatch.getKineticHolder().stopWorking());
    }

    private List<KineticOutputHatch> getKineticOutputHatches() {
        return getParts().stream()
                .filter(KineticOutputHatch.class::isInstance)
                .map(KineticOutputHatch.class::cast)
                .toList();
    }

    // ======== GUI ========

    @Override
    public IGuiTexture getScreenTexture() {
        return GuiTextures.DISPLAY_STEAM.get(ConfigHolder.INSTANCE.machines.steelSteamMultiblocks);
    }

    @Override
    public ModularUI createUI(Player player) {
        var screen = new DraggableScrollableWidgetGroup(7, 4, 188, 121)
                .setBackground(getScreenTexture());
        screen.addWidget(new LabelWidget(4, 5,
                self().getBlockState().getBlock().getDescriptionId()));
        screen.addWidget(new ComponentPanelWidget(4, 17, this::addDisplayText)
                .setMaxWidthLimit(180)
                .clickHandler(this::handleDisplayClick));
        return new ModularUI(202, 216, this, player)
                .background(GuiTextures.BACKGROUND_STEAM
                        .get(ConfigHolder.INSTANCE.machines.steelSteamMultiblocks))
                .widget(screen)
                .widget(UITemplate.bindPlayerInventory(player.getInventory(),
                        GuiTextures.SLOT_STEAM.get(ConfigHolder.INSTANCE.machines.steelSteamMultiblocks),
                        20, 134, true));
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        IDisplayUIMachine.super.addDisplayText(textList);
        if (isFormed()) {
            textList.add(Component.translatable(
                    layerCount == 1 ? "astrogreg.machine.kinetic_steam_engine.layer" :
                            "astrogreg.machine.kinetic_steam_engine.layers",
                    layerCount));
            textList.add(Component.translatable("astrogreg.machine.kinetic_steam_engine.steam_usage",
                    calculateSteamPerTick()));
            textList.add(Component.translatable(
                    "astrogreg.machine.kinetic_steam_engine.su_output",
                    recipeLogic.isActive() ? (int) calculateTotalSU() : 0));
        }
        if (!isFormed()) {
            textList.add(Component.translatable("gtceu.multiblock.invalid_structure")
                    .withStyle(ChatFormatting.RED));
            return;
        }
        if (recipeLogic.isActive()) {
            textList.add(Component.translatable("gtceu.multiblock.running")
                    .withStyle(ChatFormatting.GREEN));
        } else {
            textList.add(Component.translatable("gtceu.multiblock.large_miner.steam")
                    .withStyle(ChatFormatting.RED));
        }
    }
}
