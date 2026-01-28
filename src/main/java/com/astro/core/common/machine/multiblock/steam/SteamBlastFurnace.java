package com.astro.core.common.machine.multiblock.steam;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;
import com.gregtechceu.gtceu.common.machine.multiblock.steam.SteamParallelMultiblockMachine;
import com.gregtechceu.gtceu.config.ConfigHolder;

import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.network.chat.Component;

import com.astro.core.common.data.AstroRecipeTypes;
import com.astro.core.common.data.configs.AstroConfigs;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import javax.annotation.Nonnull;

public class SteamBlastFurnace extends SteamParallelMultiblockMachine {

    public static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            SteamBlastFurnace.class);

    @Persisted
    private int targetParallel = ConfigHolder.INSTANCE.machines.steamMultiParallelAmount;

    @Persisted
    private int activeParallels = 0;

    public SteamBlastFurnace(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
        this.setMaxParallels(clampTargetParallel(this.targetParallel));
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Nullable
    public static ModifierFunction recipeModifier(MetaMachine machine, @Nonnull GTRecipe recipe) {
        if (!(machine instanceof SteamBlastFurnace steamMachine)) {
            return ModifierFunction.NULL;
        }
        if (recipe.getType() != AstroRecipeTypes.STEAM_BLAST_FURNACE_RECIPES) {
            return ModifierFunction.NULL;
        }

        // Clamp to 1..8 so configs or old NBT can't exceed the intended max.
        int maxParallel = clampTargetParallel(steamMachine.targetParallel);
        steamMachine.setMaxParallels(maxParallel);
        if (steamMachine.targetParallel != maxParallel) {
            steamMachine.targetParallel = maxParallel;
        }
        int parallels = ParallelLogic.getParallelAmount(machine, recipe, maxParallel);

        if (parallels == 0) return ModifierFunction.NULL;

        // Store for UI/Jade display.
        steamMachine.activeParallels = parallels;

        // English: Set to 1.0 for speed default. Lower values make it faster.
        // Español: Establecido en 1.0 para velocidad estándar. Valores más bajos lo hacen más rápido.
        double durationMultiplier = AstroConfigs.INSTANCE.Steam.SBFRecipeSpeed;

        return ModifierFunction.builder()
                .modifyAllContents(ContentModifier.multiplier(parallels))
                .durationMultiplier(durationMultiplier)
                .eutMultiplier(parallels)
                .parallels(parallels)
                .build();
    }

    private static int clampTargetParallel(int v) {
        return Math.min(8, Math.max(1, v));
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        int p = Math.max(1, this.activeParallels);
        int perParallel = 6;
        int total = perParallel * p;
        textList.add(Component.literal("Using " + total + " mB/t Steam (" + perParallel + " mB/t x " + p + ")"));
        textList.add(Component.literal("Parallels: ")
                .append(Component.literal(String.valueOf(clampTargetParallel(this.targetParallel))))
                .append(Component.literal(" (active: " + p + ") "))
                .append(ComponentPanelWidget.withButton(Component.literal("[-] "), "parallelSub"))
                .append(ComponentPanelWidget.withButton(Component.literal("[+]"), "parallelAdd")));
    }

    @Override
    public void handleDisplayClick(String componentData, ClickData clickData) {
        if (!clickData.isRemote) {
            if (componentData.equals("parallelSub")) {
                this.targetParallel = clampTargetParallel(this.targetParallel / 2);
                this.setMaxParallels(this.targetParallel);
            } else if (componentData.equals("parallelAdd")) {
                this.targetParallel = clampTargetParallel(this.targetParallel * 2);
                this.setMaxParallels(this.targetParallel);
            }
        }
    }
}
