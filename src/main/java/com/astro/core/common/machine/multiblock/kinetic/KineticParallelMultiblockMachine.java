package com.astro.core.common.machine.multiblock.kinetic;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.UITemplate;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IDisplayUIMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;
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
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import com.astro.core.common.machine.hatches.KineticInputHatch;
import com.astro.core.common.machine.trait.create.KineticRecipeLogic;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("all")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class KineticParallelMultiblockMachine extends WorkableMultiblockMachine implements IDisplayUIMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            KineticParallelMultiblockMachine.class, WorkableMultiblockMachine.MANAGED_FIELD_HOLDER);

    public static final int SU_PER_PARALLEL = 1024;
    public static final int RPM_PER_PARALLEL = 32;
    public static final int MAX_PARALLELS = 8;

    @Getter
    @Setter
    @Persisted
    private int maxParallels = MAX_PARALLELS;

    @Persisted
    private int targetParallel = MAX_PARALLELS;

    public KineticParallelMultiblockMachine(IMachineBlockEntity holder, Object... args) {
        super(holder);
        try {
            var kineticLogic = new KineticRecipeLogic(this);

            var field = WorkableMultiblockMachine.class.getDeclaredField("recipeLogic");
            field.setAccessible(true);
            RecipeLogic oldLogic = (RecipeLogic) field.get(this);
            field.set(this, kineticLogic);

            var traits = getTraits();
            traits.remove(oldLogic);
            traits.add(kineticLogic);

            kineticLogic.updateTickSubscription();
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject KineticRecipeLogic", e);
        }
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    // ======== Kinetic Input ========

    public float getAvailableSU() {
        for (var part : getParts()) {
            if (part instanceof KineticInputHatch hatch) {
                return hatch.getKineticHolder().getSpeed() * SU_PER_PARALLEL / RPM_PER_PARALLEL;
            }
        }
        return 0;
    }

    protected int getAvailableParallels() {
        float su = getAvailableSU();
        int parallels = (int) (su / SU_PER_PARALLEL);
        return Math.max(0, Math.min(targetParallel, parallels));
    }

    // ======== Recipe Modifier ========

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
    }

    @Override
    public boolean onWorking() {
        if (recipeLogic.getLastRecipe() != null) {
            int activeParallels = recipeLogic.getLastRecipe().parallels;
            float requiredSU = activeParallels * SU_PER_PARALLEL;
            if (getAvailableSU() < requiredSU) {
                return false;
            }
        }
        return super.onWorking();
    }

    @Nullable
    public static ModifierFunction recipeModifier(@NotNull MetaMachine machine, @NotNull GTRecipe recipe) {
        if (!(machine instanceof KineticParallelMultiblockMachine kineticMachine)) {
            return ModifierFunction.IDENTITY;
        }
        if (RecipeHelper.getRecipeEUtTier(recipe) > GTValues.ULV) return ModifierFunction.IDENTITY;

        int parallels = kineticMachine.getAvailableParallels();
        if (parallels <= 0) return ModifierFunction.IDENTITY;

        parallels = ParallelLogic.getParallelAmount(machine, recipe, parallels);
        if (parallels <= 0) return ModifierFunction.IDENTITY;

        return ModifierFunction.builder()
                .inputModifier(ContentModifier.multiplier(parallels))
                .outputModifier(ContentModifier.multiplier(parallels))
                .durationMultiplier(1.5)
                .eutMultiplier(0)
                .parallels(parallels)
                .build();
    }

    // ======== GUI ========

    private static int clampParallel(int v) {
        return Math.min(MAX_PARALLELS, Math.max(1, v));
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        IDisplayUIMachine.super.addDisplayText(textList);
        if (!isFormed()) {
            textList.add(Component.translatable("gtceu.multiblock.invalid_structure")
                    .withStyle(ChatFormatting.RED));
            return;
        }

        float availableSU = getAvailableSU();
        int availableParallels = getAvailableParallels();
        int activeParallels = recipeLogic.isActive() ?
                recipeLogic.getLastRecipe() != null ?
                        recipeLogic.getLastRecipe().parallels : 0 :
                0;

        textList.add(Component.translatable("astrogreg.machine.kinetic_machine.su_input",
                (int) availableSU, availableParallels * SU_PER_PARALLEL)
                .withStyle(ChatFormatting.AQUA));

        textList.add(Component.translatable("astrogreg.machine.steam_blast_furnace.parallels",
                clampParallel(targetParallel), availableParallels)
                .append(ComponentPanelWidget.withButton(Component.literal(" [-] "), "parallelSub"))
                .append(ComponentPanelWidget.withButton(Component.literal("[+]"), "parallelAdd")));

        if (recipeLogic.isActive() && recipeLogic.getLastRecipe() != null) {
            int duration = recipeLogic.getLastRecipe().duration;
            int progress = recipeLogic.getProgress();
            int progressPercent = duration > 0 ? (progress * 100 / duration) : 0;

            textList.add(Component.translatable("astrogreg.machine.parallels", activeParallels)
                    .withStyle(ChatFormatting.WHITE));
            textList.add(Component.translatable("astrogreg.machine.recipe_progress.tooltip",
                    String.format("%.2f", progress / 20.0),
                    String.format("%.2f", duration / 20.0),
                    String.valueOf(progressPercent))
                    .withStyle(ChatFormatting.WHITE));
            textList.add(Component.translatable("gtceu.multiblock.running")
                    .withStyle(ChatFormatting.GREEN));
        } else if (availableSU < SU_PER_PARALLEL) {
            textList.add(Component.translatable("astrogreg.machine.kinetic_machine.no_su")
                    .withStyle(ChatFormatting.RED));
        } else {
            textList.add(Component.translatable("gtceu.multiblock.idling")
                    .withStyle(ChatFormatting.YELLOW));
        }
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
    public ModularUI createUI(Player player) {
        var screen = new DraggableScrollableWidgetGroup(7, 4, 206, 121)
                .setBackground(getScreenTexture());
        screen.addWidget(new LabelWidget(4, 5,
                self().getBlockState().getBlock().getDescriptionId()));
        screen.addWidget(new ComponentPanelWidget(4, 17, this::addDisplayText)
                .setMaxWidthLimit(198)
                .clickHandler(this::handleDisplayClick));
        return new ModularUI(220, 216, this, player)
                .background(GuiTextures.BACKGROUND_STEAM
                        .get(ConfigHolder.INSTANCE.machines.steelSteamMultiblocks))
                .widget(screen)
                .widget(UITemplate.bindPlayerInventory(player.getInventory(),
                        GuiTextures.SLOT_STEAM.get(ConfigHolder.INSTANCE.machines.steelSteamMultiblocks),
                        30, 134, true));
    }

    @Override
    public IGuiTexture getScreenTexture() {
        return GuiTextures.DISPLAY_STEAM.get(ConfigHolder.INSTANCE.machines.steelSteamMultiblocks);
    }
}
