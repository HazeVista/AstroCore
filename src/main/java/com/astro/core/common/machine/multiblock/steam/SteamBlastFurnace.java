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
import net.minecraft.util.Mth;

import com.astro.core.common.data.AstroRecipeTypes;

import java.util.List;

public class SteamBlastFurnace extends SteamParallelMultiblockMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            SteamBlastFurnace.class, SteamParallelMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Persisted
    private int targetParallel = 1;

    public SteamBlastFurnace(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    public static ModifierFunction recipeModifier(MetaMachine machine, GTRecipe recipe) {
        if (!(machine instanceof SteamBlastFurnace steamMachine)) {
            return ModifierFunction.NULL;
        }

        if (recipe.getType() != AstroRecipeTypes.STEAM_BLAST_FURNACE_RECIPES) {
            return ModifierFunction.NULL;
        }

        int hardCap = 8;
        int configCap = ConfigHolder.INSTANCE.machines.steamMultiParallelAmount;
        int maxParallel = Math.min(Math.min(steamMachine.targetParallel, hardCap), configCap);
        maxParallel = Math.max(1, maxParallel);

        int parallels = ParallelLogic.getParallelAmount(machine, recipe, maxParallel);
        if (parallels <= 0) return ModifierFunction.NULL;

        double durationMultiplier = 1.0;

        int steamPerParallel = 5;
        int desiredSteamMbPerTick = steamPerParallel * parallels;
        int eutTotal = (int) Math.ceil(desiredSteamMbPerTick / SteamParallelMultiblockMachine.CONVERSION_RATE);
        eutTotal = Math.max(1, eutTotal);

        long baseEUt = recipe.getInputEUt().getTotalEU();
        if (baseEUt <= 0) {
            return ModifierFunction.NULL;
        }

        double eutMultiplier = eutTotal / (double) baseEUt;

        return ModifierFunction.builder()
                .modifyAllContents(ContentModifier.multiplier(parallels))
                .durationMultiplier(durationMultiplier)
                .eutMultiplier(eutMultiplier)
                .parallels(parallels)
                .build();
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        if (this.isFormed()) {
            textList.add(Component.literal("Parallels: " + targetParallel + " ")
                    .append(ComponentPanelWidget.withButton(Component.literal("[-]"), "parallelSub"))
                    .append(Component.literal(" "))
                    .append(ComponentPanelWidget.withButton(Component.literal("[+]"), "parallelAdd")));
        }
    }

    @Override
    public void handleDisplayClick(String componentData, ClickData clickData) {
        super.handleDisplayClick(componentData, clickData); // ✅ Llama al padre primero

        if (!clickData.isRemote) {
            int hardCap = 8;
            int configCap = ConfigHolder.INSTANCE.machines.steamMultiParallelAmount;
            int maxAllowed = Math.min(hardCap, configCap);

            if (componentData.equals("parallelSub")) {
                targetParallel = Math.max(1, targetParallel - 1);
            } else if (componentData.equals("parallelAdd")) {
                targetParallel = Math.min(maxAllowed, targetParallel + 1);
            }

            targetParallel = Mth.clamp(targetParallel, 1, maxAllowed);
            markDirty();
        }
    }
}
