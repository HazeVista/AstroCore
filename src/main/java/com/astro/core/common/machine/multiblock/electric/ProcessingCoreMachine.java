package com.astro.core.common.machine.multiblock.electric;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;

import org.jetbrains.annotations.NotNull;

import static com.astro.core.common.data.AstroBlocks.*;

public class ProcessingCoreMachine extends WorkableElectricMultiblockMachine {

    private int coreTier = -1;

    public ProcessingCoreMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    public void onStructureFormed() {
        super.onStructureFormed();

        var level = getLevel();
        if (level == null) {
            this.coreTier = -1;
            return;
        }

        for (BlockPos pos : getMultiblockState().getCache()) {
            Block block = level.getBlockState(pos).getBlock();
            if (block == INDUSTRIAL_PROCESSING_CORE_MK1.get()) {
                this.coreTier = GTValues.HV;
                getRecipeLogic().updateTickSubscription();
                return;
            } else if (block == INDUSTRIAL_PROCESSING_CORE_MK2.get()) {
                this.coreTier = GTValues.EV;
                getRecipeLogic().updateTickSubscription();
                return;
            } else if (block == INDUSTRIAL_PROCESSING_CORE_MK3.get()) {
                this.coreTier = GTValues.IV;
                getRecipeLogic().updateTickSubscription();
                return;
            }
        }
        this.coreTier = -1;
    }

    public int getCoreTier() {
        return coreTier;
    }

    public static @NotNull ModifierFunction processingCoreOverclock(@NotNull MetaMachine machine,
                                                                    @NotNull GTRecipe recipe) {
        if (!(machine instanceof ProcessingCoreMachine coreMachine)) {
            return ModifierFunction.IDENTITY;
        }

        int coreTier = coreMachine.getCoreTier();
        if (coreTier == -1) {
            return ModifierFunction.IDENTITY;
        }

        if (RecipeHelper.getRecipeEUtTier(recipe) > coreTier) {
            return ModifierFunction.IDENTITY;
        }

        long maxVoltage = GTValues.V[coreTier];
        long overclockVoltage = Math.min(coreMachine.getOverclockVoltage(), maxVoltage);

        return OverclockingLogic.PERFECT_OVERCLOCK_SUBTICK.getModifier(machine, recipe, overclockVoltage);
    }
}
