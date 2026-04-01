package com.astro.core.common.machine.multiblock.kinetic;

import com.gregtechceu.gtceu.api.capability.IMiner;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.UITemplate;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IDisplayUIMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.transfer.fluid.IFluidHandlerModifiable;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.machine.trait.miner.LargeMinerLogic;
import com.gregtechceu.gtceu.config.ConfigHolder;

import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fluids.FluidStack;

import com.astro.core.common.machine.part.KineticInputHatch;
import com.astro.core.common.machine.trait.miner.AstroMinerLogic;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.MACERATOR_RECIPES;

@SuppressWarnings("all")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class KineticMinerMachine extends WorkableMultiblockMachine implements IMiner, IDisplayUIMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            KineticMinerMachine.class, WorkableMultiblockMachine.MANAGED_FIELD_HOLDER);

    public static final int BASE_RPM = 32;
    public static final int MAX_PARALLELS = 8;
    public static final int WATER_PER_PARALLEL = 6;
    public static final int BASE_SPEED = 480;
    public static final int CHUNK_RADIUS = 1;
    public static final int BLOCK_RADIUS = 24;
    public static final int FORTUNE = 0;

    @Persisted
    private int targetParallel = 1;
    @Persisted
    private int activeParallels = 1;

    public KineticMinerMachine(IMachineBlockEntity holder, Object... args) {
        super(holder);
        subscribeServerTick(this::minerTick);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    protected RecipeLogic createRecipeLogic(Object... args) {
        return new AstroMinerLogic(this, FORTUNE, BASE_SPEED, BLOCK_RADIUS);
    }

    @Override
    public GTRecipeType getRecipeType() {
        return MACERATOR_RECIPES;
    }

    @Override
    public LargeMinerLogic getRecipeLogic() {
        return (LargeMinerLogic) super.getRecipeLogic();
    }

    // ======== Structure ========

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        getRecipeLogic().setDir(Direction.DOWN);
        getRecipeLogic().initPos(getPos(), getRecipeLogic().getCurrentRadius());
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
    }

    // ======== IMiner ========

    @Override
    public boolean drainInput(boolean simulate) {
        int parallels = activeParallels;
        if (getAvailableParallels() <= 0) return false;

        boolean hasWater = hasWater(parallels);
        boolean hasDrilling = hasDrillingFluid(parallels);
        if (!hasWater && !hasDrilling) return false;
        if (hasWater && hasDrilling) return false;

        int fluidNeeded = WATER_PER_PARALLEL * parallels;
        if (hasDrilling) {
            NotifiableFluidTank tank = findDrillingFluidTank();
            if (tank == null) return false;
            if (!simulate) {
                tank.drainInternal(new FluidStack(GTMaterials.DrillingFluid.getFluid(), fluidNeeded),
                        IFluidHandlerModifiable.FluidAction.EXECUTE);
            }
        } else {
            NotifiableFluidTank tank = findWaterTank();
            if (tank == null) return false;
            if (!simulate) {
                tank.drainInternal(new FluidStack(GTMaterials.Water.getFluid(), fluidNeeded),
                        IFluidHandlerModifiable.FluidAction.EXECUTE);
            }
        }
        return true;
    }

    // ======== Kinetic Input ========

    private float getAvailableRPM() {
        for (var part : getParts()) {
            if (part instanceof KineticInputHatch hatch) {
                return Math.abs(hatch.getKineticHolder().getSpeed());
            }
        }
        return 0f;
    }

    private int getAvailableParallels() {
        float rpm = getAvailableRPM();
        if (rpm < BASE_RPM) return 0;
        int tier = 0;
        float threshold = BASE_RPM;
        while (tier < 3 && rpm >= threshold * 2) {
            threshold *= 2;
            tier++;
        }
        return 1 << tier;
    }

    private int getRequiredRPM(int parallels) {
        return BASE_RPM * parallels;
    }

    // ======== Fluid Tanks ========

    private NotifiableFluidTank findWaterTank() {
        for (var part : getParts()) {
            for (var hl : part.getRecipeHandlers()) {
                if (!hl.isValid(IO.IN)) continue;
                for (var h : hl.getCapability(FluidRecipeCapability.CAP)) {
                    if (h instanceof NotifiableFluidTank nft) {
                        var fluid = nft.getFluidInTank(0);
                        if (!fluid.isEmpty() && fluid.getFluid() == GTMaterials.Water.getFluid()) return nft;
                    }
                }
            }
        }
        return null;
    }

    private NotifiableFluidTank findDrillingFluidTank() {
        for (var part : getParts()) {
            for (var hl : part.getRecipeHandlers()) {
                if (!hl.isValid(IO.IN)) continue;
                for (var h : hl.getCapability(FluidRecipeCapability.CAP)) {
                    if (h instanceof NotifiableFluidTank nft) {
                        var fluid = nft.getFluidInTank(0);
                        if (!fluid.isEmpty() && fluid.getFluid() == GTMaterials.DrillingFluid.getFluid()) return nft;
                    }
                }
            }
        }
        return null;
    }

    private boolean hasWater(int parallels) {
        NotifiableFluidTank tank = findWaterTank();
        if (tank == null) return false;
        return tank.getFluidInTank(0).getAmount() >= WATER_PER_PARALLEL * parallels;
    }

    private boolean hasDrillingFluid(int parallels) {
        NotifiableFluidTank tank = findDrillingFluidTank();
        if (tank == null) return false;
        return tank.getFluidInTank(0).getAmount() >= WATER_PER_PARALLEL * parallels;
    }

    // ======== Mining Logic ========

    private void minerTick() {
        if (!isFormed() || !isWorkingEnabled()) return;
        int available = getAvailableParallels();
        int clamped = Math.min(clampParallel(targetParallel), available);
        if (!getRecipeLogic().isWorking()) {
            activeParallels = clamped;
        }
        int ticks = activeParallels > 0 ? activeParallels : 1;
        for (int i = 0; i < ticks; i++) {
            getRecipeLogic().serverTick();
        }
    }

    // ======== GUI ========

    private static int clampParallel(int v) {
        return Math.min(MAX_PARALLELS, Math.max(1, v));
    }

    private static int prevPowerOfTwo(int v) {
        if (v <= 1) return 1;
        int p = Integer.highestOneBit(v);
        return p == v ? p / 2 : p;
    }

    private static int nextPowerOfTwo(int v) {
        int p = Integer.highestOneBit(v);
        return Math.min(p == v ? p * 2 : p * 2, MAX_PARALLELS);
    }

    @Override
    public void handleDisplayClick(String componentData, ClickData clickData) {
        if (!clickData.isRemote) {
            if (componentData.equals("parallelSub")) {
                targetParallel = clampParallel(prevPowerOfTwo(targetParallel));
            } else if (componentData.equals("parallelAdd")) {
                targetParallel = clampParallel(nextPowerOfTwo(targetParallel));
            } else if (componentData.equals("silk_touch")) {
                if (!getRecipeLogic().isWorking())
                    getRecipeLogic().setSilkTouchMode(!getRecipeLogic().isSilkTouchMode());
            } else if (componentData.equals("chunk_mode")) {
                if (!getRecipeLogic().isWorking()) {
                    getRecipeLogic().setChunkMode(!getRecipeLogic().isChunkMode());
                }
            }
        }
    }

    @Override
    public IGuiTexture getScreenTexture() {
        return GuiTextures.DISPLAY_STEAM.get(ConfigHolder.INSTANCE.machines.steelSteamMultiblocks);
    }

    @Override
    public ModularUI createUI(Player player) {
        var screen = new DraggableScrollableWidgetGroup(7, 4, 206, 166)
                .setBackground(getScreenTexture());
        screen.addWidget(new LabelWidget(4, 5,
                self().getBlockState().getBlock().getDescriptionId()));
        screen.addWidget(new ComponentPanelWidget(4, 17, this::addDisplayText)
                .setMaxWidthLimit(198)
                .clickHandler(this::handleDisplayClick));
        return new ModularUI(220, 261, this, player)
                .background(GuiTextures.BACKGROUND_STEAM
                        .get(ConfigHolder.INSTANCE.machines.steelSteamMultiblocks))
                .widget(screen)
                .widget(UITemplate.bindPlayerInventory(player.getInventory(),
                        GuiTextures.SLOT_STEAM.get(ConfigHolder.INSTANCE.machines.steelSteamMultiblocks),
                        30, 179, true));
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        IDisplayUIMachine.super.addDisplayText(textList);

        if (!isFormed()) {
            textList.add(Component.translatable("gtceu.multiblock.invalid_structure")
                    .withStyle(ChatFormatting.RED));
            return;
        }

        int parallels = clampParallel(targetParallel);
        int availableParallels = getAvailableParallels();
        int rpm = (int) getAvailableRPM();
        int requiredRpm = getRequiredRPM(parallels);
        boolean water = hasWater(parallels);
        boolean drilling = hasDrillingFluid(parallels);
        boolean conflict = water && drilling;

        textList.add(Component.translatable("astrogreg.machine.kinetic_machine.su_input",
                rpm, requiredRpm)
                .withStyle(ChatFormatting.GOLD));

        if (drilling) {
            textList.add(Component.translatable("astrogreg.machine.miner.drilling_fluid_usage",
                    WATER_PER_PARALLEL * parallels)
                    .withStyle(ChatFormatting.GREEN));
            textList.add(Component.translatable("astrogreg.machine.steam_miner.tooltip.bonus")
                    .withStyle(ChatFormatting.LIGHT_PURPLE));
        } else if (!conflict) {
            textList.add(Component.translatable("astrogreg.machine.steam_miner.water_usage",
                    WATER_PER_PARALLEL * parallels)
                    .withStyle(ChatFormatting.AQUA));
        }

        textList.add(Component.translatable("astrogreg.machine.parallels",
                Component.literal(String.valueOf(parallels)))
                .withStyle(ChatFormatting.WHITE)
                .append(ComponentPanelWidget.withButton(Component.literal(" [-] "), "parallelSub"))
                .append(ComponentPanelWidget.withButton(Component.literal("[+]"), "parallelAdd")));

        if (conflict) {
            textList.add(Component.translatable("astrogreg.machine.miner.fluid_conflict")
                    .withStyle(ChatFormatting.RED));
        } else if (getRecipeLogic().isWorking()) {
            textList.add(Component.translatable("gtceu.multiblock.running")
                    .withStyle(ChatFormatting.GREEN));
            var progressLine = getRecipeLogic().getCustomProgressLine();
            if (progressLine != null) {
                textList.add(progressLine.copy().withStyle(ChatFormatting.WHITE));
            }
        } else if (!isWorkingEnabled()) {
            textList.add(Component.translatable("gtceu.multiblock.work_paused")
                    .withStyle(ChatFormatting.YELLOW));
        } else if (availableParallels <= 0) {
            textList.add(Component.translatable("astrogreg.machine.kinetic_machine.no_su")
                    .withStyle(ChatFormatting.RED));
        } else if (getRecipeLogic().isDone()) {
            textList.add(Component.translatable("gtceu.multiblock.large_miner.done")
                    .withStyle(ChatFormatting.GREEN));
        } else if (getRecipeLogic().isInventoryFull()) {
            textList.add(Component.translatable("gtceu.recipe_logic.insufficient_out")
                    .withStyle(ChatFormatting.RED));
        } else {
            textList.add(Component.translatable("gtceu.multiblock.large_miner.needsfluid")
                    .withStyle(ChatFormatting.RED));
        }

        textList.add(Component.translatable("gtceu.machine.miner.startx",
                getRecipeLogic().getMineX() == Integer.MAX_VALUE ? 0 : getRecipeLogic().getMineX())
                .withStyle(ChatFormatting.WHITE));
        textList.add(Component.translatable("gtceu.machine.miner.starty",
                getRecipeLogic().getMineY() == Integer.MAX_VALUE ? 0 : getRecipeLogic().getMineY())
                .withStyle(ChatFormatting.WHITE));
        textList.add(Component.translatable("gtceu.machine.miner.startz",
                getRecipeLogic().getMineZ() == Integer.MAX_VALUE ? 0 : getRecipeLogic().getMineZ())
                .withStyle(ChatFormatting.WHITE));

        textList.add(Component.translatable("gtceu.universal.tooltip.silk_touch")
                .append(ComponentPanelWidget.withButton(Component.literal("[")
                        .append(getRecipeLogic().isSilkTouchMode() ?
                                Component.translatable("gtceu.creative.activity.on") :
                                Component.translatable("gtceu.creative.activity.off"))
                        .append(Component.literal("]")), "silk_touch")));

        textList.add(Component.translatable("gtceu.universal.tooltip.chunk_mode")
                .append(ComponentPanelWidget.withButton(Component.literal("[")
                        .append(getRecipeLogic().isChunkMode() ?
                                Component.translatable("gtceu.creative.activity.on") :
                                Component.translatable("gtceu.creative.activity.off"))
                        .append(Component.literal("]")), "chunk_mode")));

        if (getRecipeLogic().isChunkMode()) {
            int chunkDiam = CHUNK_RADIUS * 2 + 1;
            textList.add(Component.translatable("gtceu.universal.tooltip.working_area_chunks",
                    chunkDiam, chunkDiam)
                    .withStyle(ChatFormatting.GRAY));
        } else {
            int blockDiam = BLOCK_RADIUS * 2 + 1;
            textList.add(Component.translatable("gtceu.universal.tooltip.working_area",
                    blockDiam, blockDiam)
                    .withStyle(ChatFormatting.GRAY));
        }
    }
}
