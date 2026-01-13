package com.astro.core.common.machine.multiblock.steam;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.config.ConfigHolder;

import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.network.chat.Component;

import com.astro.core.common.data.configs.AstroConfigs;
import com.astro.core.common.machine.multiblock.base.SteamMultiMachineBase;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import javax.annotation.Nonnull;

public class SteamBlastFurnace extends SteamMultiMachineBase {

    @Persisted
    private int targetParallel = ConfigHolder.INSTANCE.machines.steamMultiParallelAmount; // Default to 8 parallels /
                                                                                          // Predeterminado a 8
                                                                                          // paralelos

    public SteamBlastFurnace(IMachineBlockEntity holder, Object... args) {
        super(holder, false, args);
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
        if (recipe.getType() != GTRecipeTypes.PRIMITIVE_BLAST_FURNACE_RECIPES) {
            return ModifierFunction.NULL;
        }

        int maxParallel = steamMachine.targetParallel;
        int parallels = ParallelLogic.getParallelAmount(machine, recipe, maxParallel);

        if (parallels == 0) return ModifierFunction.NULL;

        // English: Set to 1.0 for speed default. Lower values make it faster.
        // Español: Establecido en 1.0 para velocidad estándar. Valores más bajos lo hacen más rápido.
        double durationMultiplier = AstroConfigs.INSTANCE.features.SBFRecipeSpeed;

        return ModifierFunction.builder()
                .modifyAllContents(ContentModifier.multiplier(parallels))
                .durationMultiplier(durationMultiplier)
                // English: Multiplies steam consumption. Without this, 16x parallel costs same as 1x.
                // Español: Multiplica el consumo de vapor. Sin esto, 16x paralelo cuesta lo mismo que 1x.
                // .eutMultiplier(parallels) (can u disable or enable for steam scale // Puedes deshabilitar o habilitar
                // la escala de Steam)
                .parallels(parallels)
                .build();
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        if (this.isFormed()) {
            textList.add(Component.literal("Parallels: ")
                    .append(ComponentPanelWidget.withButton(Component.literal("[-] "), "parallelSub"))
                    .append(ComponentPanelWidget.withButton(Component.literal("[+]"), "parallelAdd")));
        }
    }

    @Override
    public void handleDisplayClick(String componentData, ClickData clickData) {
        if (!clickData.isRemote) {
            if (componentData.equals("parallelSub")) {
                this.targetParallel = Math.max(1, this.targetParallel / 2);
            } else if (componentData.equals("parallelAdd")) {
                this.targetParallel = Math.min(16, this.targetParallel * 2);
            }
        }
    }
}
