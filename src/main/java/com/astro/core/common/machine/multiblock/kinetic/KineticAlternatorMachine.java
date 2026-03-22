package com.astro.core.common.machine.multiblock.kinetic;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.UITemplate;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IDisplayUIMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.gregtechceu.gtceu.utils.FormattingUtil;

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
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import com.astro.core.common.machine.part.KineticInputHatch;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("all")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class KineticAlternatorMachine extends WorkableMultiblockMachine implements IDisplayUIMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            KineticAlternatorMachine.class, WorkableMultiblockMachine.MANAGED_FIELD_HOLDER);

    public static final int EU_PER_PARALLEL = 6;
    public static final int MAX_PARALLELS = 8;
    public static final float SU_PER_PARALLEL = KineticInputHatch.SU_PER_PARALLEL;
    public static final float REQUIRED_RPM = KineticInputHatch.REQUIRED_RPM;

    @Persisted
    private int targetParallel = MAX_PARALLELS;

    public KineticAlternatorMachine(IMachineBlockEntity holder, Object... args) {
        super(holder);
        subscribeServerTick(this::alternatorTick);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        if (recipeLogic.isActive()) {
            recipeLogic.setWaiting(null);
        }
    }

    // ======== Kinetic Input ========

    private float getAvailableSU() {
        for (var part : getParts()) {
            if (part instanceof KineticInputHatch hatch) {
                return Math.abs(hatch.getKineticHolder().getSpeed()) * SU_PER_PARALLEL / REQUIRED_RPM;
            }
        }
        return 0f;
    }

    public int getAvailableParallels() {
        float su = getAvailableSU();
        int parallels = (int) (su / SU_PER_PARALLEL);
        return Math.max(0, Math.min(targetParallel, parallels));
    }

    // ======== Energy Output ========

    private NotifiableEnergyContainer getOutputEnergyContainer() {
        for (var part : getParts()) {
            if (!PartAbility.OUTPUT_ENERGY.isApplicable(part.self().getDefinition().getBlock())) continue;
            for (var handlerList : part.getRecipeHandlers()) {
                if (!handlerList.isValid(IO.OUT)) continue;
                for (var handler : handlerList.getCapability(EURecipeCapability.CAP)) {
                    if (handler instanceof NotifiableEnergyContainer container) {
                        return container;
                    }
                }
            }
        }
        return null;
    }

    public long getMaxEUt() {
        var container = getOutputEnergyContainer();
        return container != null ? container.getOutputVoltage() * container.getOutputAmperage() : 0;
    }

    public long getOutputVoltage() {
        var container = getOutputEnergyContainer();
        return container != null ? container.getOutputVoltage() : 0;
    }

    private int getOutputTier() {
        var container = getOutputEnergyContainer();
        if (container == null) return GTValues.ULV;
        long voltage = container.getOutputVoltage();
        for (int i = GTValues.V.length - 1; i >= 0; i--) {
            if (voltage >= GTValues.V[i]) return i;
        }
        return GTValues.ULV;
    }

    private void pushEnergyToHatches(int euPerTick) {
        var container = getOutputEnergyContainer();
        if (container == null) return;
        long space = container.getEnergyCapacity() - container.getEnergyStored();
        if (space <= 0) return;
        container.addEnergy(Math.min(euPerTick, space));
    }

    // ======== Tick ========

    private void alternatorTick() {
        if (!isFormed() || !isWorkingEnabled()) {
            setIdleStatus();
            return;
        }

        int parallels = getAvailableParallels();

        if (parallels <= 0) {
            setIdleStatus();
            return;
        }

        pushEnergyToHatches(parallels * EU_PER_PARALLEL);
        recipeLogic.setStatus(RecipeLogic.Status.WORKING);
    }

    private void setIdleStatus() {
        if (recipeLogic.getStatus() != RecipeLogic.Status.IDLE) {
            recipeLogic.setWaiting(null);
        }
    }

    // ======== GUI ========

    private static int clampParallel(int v) {
        return Math.min(MAX_PARALLELS, Math.max(1, v));
    }

    @Override
    public void handleDisplayClick(String componentData, ClickData clickData) {
        if (!clickData.isRemote) {
            if (componentData.equals("parallelSub")) {
                targetParallel = clampParallel(targetParallel / 2);
            } else if (componentData.equals("parallelAdd")) {
                targetParallel = clampParallel(targetParallel * 2);
            }
        }
    }

    @Override
    public IGuiTexture getScreenTexture() {
        return GuiTextures.DISPLAY_STEAM.get(ConfigHolder.INSTANCE.machines.steelSteamMultiblocks);
    }

    @Override
    public ModularUI createUI(Player player) {
        var screen = new DraggableScrollableWidgetGroup(7, 4, 206, 121)
                .setBackground(getScreenTexture());
        screen.addWidget(new LabelWidget(4, 5,
                self().getBlockState().getBlock().getDescriptionId()));
        screen.addWidget(new ComponentPanelWidget(4, 17, this::addDisplayText)
                .setMaxWidthLimit(198)
                .clickHandler(this::handleDisplayClick));
        return new ModularUI(220, 216, this, player)
                .background(GuiTextures.BACKGROUND_STEAM.get(ConfigHolder.INSTANCE.machines.steelSteamMultiblocks))
                .widget(screen)
                .widget(UITemplate.bindPlayerInventory(player.getInventory(),
                        GuiTextures.SLOT_STEAM.get(ConfigHolder.INSTANCE.machines.steelSteamMultiblocks),
                        30, 134, true));
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        IDisplayUIMachine.super.addDisplayText(textList);

        if (!isFormed()) {
            textList.add(Component.translatable("gtceu.multiblock.invalid_structure")
                    .withStyle(ChatFormatting.RED));
            return;
        }

        int parallels = getAvailableParallels();
        int currentEUt = recipeLogic.isWorking() ? parallels * EU_PER_PARALLEL : 0;
        int availableSU = (int) getAvailableSU();
        int maxSU = (int) (clampParallel(targetParallel) * SU_PER_PARALLEL);
        int maxEUt = 48;
        int tier = getOutputTier();

        textList.add(Component.translatable("gtceu.multiblock.max_energy_per_tick",
                FormattingUtil.formatNumbers(maxEUt), GTValues.VNF[tier])
                .withStyle(ChatFormatting.GRAY));

        textList.add(Component.translatable("gtceu.multiblock.max_recipe_tier",
                GTValues.VNF[GTValues.ULV])
                .withStyle(ChatFormatting.GRAY));

        textList.add(Component.translatable("astrogreg.machine.kinetic_machine.su_input",
                availableSU, maxSU)
                .withStyle(ChatFormatting.AQUA));

        textList.add(Component.translatable("gtceu.multiblock.turbine.energy_per_tick",
                FormattingUtil.formatNumbers(currentEUt), FormattingUtil.formatNumbers(maxEUt)));

        textList.add(Component.translatable("astrogreg.machine.steam_blast_furnace.parallels",
                clampParallel(targetParallel), parallels)
                .append(ComponentPanelWidget.withButton(Component.literal(" [-] "), "parallelSub"))
                .append(ComponentPanelWidget.withButton(Component.literal("[+]"), "parallelAdd")));

        if (parallels > 0 && isWorkingEnabled()) {
            textList.add(Component.translatable("gtceu.multiblock.running")
                    .withStyle(ChatFormatting.GREEN));
        } else if (!isWorkingEnabled()) {
            textList.add(Component.translatable("gtceu.multiblock.work_paused")
                    .withStyle(ChatFormatting.YELLOW));
        } else {
            textList.add(Component.translatable("astrogreg.machine.kinetic_machine.no_su")
                    .withStyle(ChatFormatting.RED));
        }
    }
}
