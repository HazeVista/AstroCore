package com.astro.core.common.machine.multiblock.kinetic;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.UITemplate;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IDisplayUIMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.api.transfer.fluid.IFluidHandlerModifiable;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;

import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fluids.FluidStack;

import com.astro.core.common.machine.part.KineticOutputHatch;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("all")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class KineticCombustionEngineMachine extends WorkableMultiblockMachine implements IDisplayUIMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            KineticCombustionEngineMachine.class, WorkableMultiblockMachine.MANAGED_FIELD_HOLDER);

    public static final float BASE_OUTPUT_SU = 450_000f;
    public static final float BOOSTED_OUTPUT_SU = 1_000_000f;
    public static final float CAPACITY_PER_RPM = BOOSTED_OUTPUT_SU / 256f;

    private static final int OXYGEN_MB_PER_TICK = 1;
    public static final int LUBRICANT_MB_PER_HOUR = 1_000;

    @Getter
    @Persisted
    @DescSynced
    private boolean oxygenBoosted = false;

    private int runningTimer = 0;

    public KineticCombustionEngineMachine(IMachineBlockEntity holder, Object... args) {
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
        stopKineticOutput();
        runningTimer = 0;
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        stopKineticOutput();
        oxygenBoosted = false;
        runningTimer = 0;
    }

    // ======== Recipe Logic ========

    @Override
    public boolean onWorking() {
        boolean value = super.onWorking();

        if (runningTimer % 72 == 0) {
            var lubRecipe = GTRecipeBuilder.ofRaw()
                    .inputFluids(GTMaterials.Lubricant.getFluid(1))
                    .buildRawRecipe();
            if (!RecipeHelper.handleRecipeIO(this, lubRecipe, IO.IN, recipeLogic.getChanceCaches()).isSuccess()) {
                recipeLogic.interruptRecipe();
                return false;
            }
        }

        oxygenBoosted = tryDrainOxygen();
        runningTimer++;
        if (runningTimer > 72000) runningTimer %= 72000;

        return value;
    }

    @Override
    public boolean regressWhenWaiting() {
        return false;
    }

    private boolean tryDrainOxygen() {
        FluidStack wanted = GTMaterials.Oxygen.getFluid(OXYGEN_MB_PER_TICK);
        for (var part : getParts()) {
            for (var hl : part.getRecipeHandlers()) {
                if (!hl.isValid(IO.IN)) continue;
                for (var handler : hl.getCapability(FluidRecipeCapability.CAP)) {
                    if (!(handler instanceof NotifiableFluidTank nft)) continue;
                    for (int i = 0; i < nft.getTanks(); i++) {
                        if (nft.getFluidInTank(i).getFluid() == wanted.getFluid()) {
                            FluidStack drained = nft.drainInternal(wanted,
                                    IFluidHandlerModifiable.FluidAction.EXECUTE);
                            if (drained.getAmount() >= OXYGEN_MB_PER_TICK) return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static ModifierFunction recipeModifier(@NotNull MetaMachine machine, @NotNull GTRecipe recipe) {
        if (!(machine instanceof KineticCombustionEngineMachine engine)) {
            return ModifierFunction.NULL;
        }
        var lubRecipe = GTRecipeBuilder.ofRaw()
                .inputFluids(GTMaterials.Lubricant.getFluid(1))
                .buildRawRecipe();
        if (engine.isIntakesObstructed() || !RecipeHelper.matchRecipe(engine, lubRecipe).isSuccess()) {
            return ModifierFunction.NULL;
        }
        return ModifierFunction.IDENTITY;
    }

    // ======== Kinetic Output ========

    public float getCurrentOutputSU() {
        return oxygenBoosted ? BOOSTED_OUTPUT_SU : BASE_OUTPUT_SU;
    }

    private void kineticTick() {
        if (!isFormed() || !isWorkingEnabled()) {
            stopKineticOutput();
            return;
        }
        if (recipeLogic.isActive() && recipeLogic.getStatus() == RecipeLogic.Status.WORKING) {
            updateKineticOutput();
        } else {
            oxygenBoosted = false;
            stopKineticOutput();
        }
    }

    private void updateKineticOutput() {
        float su = getCurrentOutputSU();
        getKineticOutputHatches().forEach(hatch -> hatch.setOutputSU(su, CAPACITY_PER_RPM));
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
        return GuiTextures.DISPLAY;
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
                .background(GuiTextures.BACKGROUND)
                .widget(screen)
                .widget(UITemplate.bindPlayerInventory(player.getInventory(),
                        GuiTextures.SLOT, 7, 134, true));
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        IDisplayUIMachine.super.addDisplayText(textList);

        if (!isFormed()) {
            textList.add(Component.translatable("gtceu.multiblock.invalid_structure")
                    .withStyle(ChatFormatting.RED));
            return;
        }

        if (recipeLogic.isActive()) {
            int progress = recipeLogic.getProgress();
            int duration = recipeLogic.getDuration();
            int percent = duration > 0 ? (int) ((progress / (float) duration) * 100) : 0;
            String progressSec = String.format("%.2f", progress / 20f);
            String durationSec = String.format("%.2f", duration / 20f);
            textList.add(Component.translatable("astrogreg.machine.recipe_progress.tooltip",
                    progressSec, durationSec, percent));
        }

        int displaySU = recipeLogic.isActive() ? (int) getCurrentOutputSU() : 0;
        textList.add(Component.translatable("astrogreg.machine.kinetic_steam_engine.su_output", displaySU));

        if (recipeLogic.isActive() && oxygenBoosted) {
            textList.add(Component.translatable("gtceu.multiblock.large_combustion_engine.oxygen_boosted"));
        } else {
            textList.add(Component.translatable(
                    "gtceu.multiblock.large_combustion_engine.supply_oxygen_to_boost")
                    .withStyle(ChatFormatting.GRAY));
        }

        if (isIntakesObstructed()) {
            textList.add(Component.translatable("gtceu.multiblock.large_combustion_engine.obstructed")
                    .withStyle(ChatFormatting.RED));
        }

        if (recipeLogic.isActive()) {
            textList.add(Component.translatable("gtceu.multiblock.running")
                    .withStyle(ChatFormatting.GREEN));
        } else if (!recipeLogic.isWorkingEnabled()) {
            textList.add(Component.translatable("gtceu.multiblock.work_paused")
                    .withStyle(ChatFormatting.YELLOW));
        } else {
            textList.add(Component.translatable("gtceu.multiblock.idling")
                    .withStyle(ChatFormatting.GOLD));
        }
    }

    private boolean isIntakesObstructed() {
        var facing = getFrontFacing();
        var level = getLevel();
        if (level == null) return false;
        var center = getPos().relative(facing);
        for (int a = -1; a <= 1; a++) {
            for (int b = -1; b <= 1; b++) {
                if (a == 0 && b == 0) continue;
                var check = switch (facing.getAxis()) {
                    case X -> center.offset(0, b, a);
                    case Z -> center.offset(a, b, 0);
                    default -> center.offset(a, 0, b);
                };
                if (!level.getBlockState(check).isAir()) return true;
            }
        }
        return false;
    }
}
