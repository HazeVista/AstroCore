package com.astro.core.common.machine.multiblock.electric;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;


public class ProcessingCoreMachine {

    public static @NotNull ModifierFunction processingCoreOverclock(@NotNull MetaMachine machine,
                                                                    @NotNull GTRecipe recipe,
                                                                    Block mk1Block,
                                                                    Block mk2Block,
                                                                    Block mk3Block) {
        if (!(machine instanceof WorkableElectricMultiblockMachine workableMachine)) {
            return RecipeModifier.nullWrongType(WorkableElectricMultiblockMachine.class, machine);
        }

        int coreTier = getProcessingCoreTier(workableMachine, mk1Block, mk2Block, mk3Block);
        if (coreTier == -1) {
            return ModifierFunction.IDENTITY;
        }

        long maxVoltage = getMaxVoltageForCoreTier(coreTier);
        long overclockVoltage = Math.min(workableMachine.getOverclockVoltage(), maxVoltage);

        return OverclockingLogic.PERFECT_OVERCLOCK_SUBTICK.getModifier(machine, recipe, overclockVoltage);
    }

    private static int getProcessingCoreTier(WorkableElectricMultiblockMachine machine,
                                             Block mk1Block,
                                             Block mk2Block,
                                             Block mk3Block) {
        var level = machine.getLevel();
        if (level == null || !machine.isFormed()) return -1;

        BlockPos controllerPos = machine.getPos();

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos checkPos = controllerPos.offset(x, y, z);
                    Block block = level.getBlockState(checkPos).getBlock();

                    if (block == mk1Block) return GTValues.HV;
                    if (block == mk2Block) return GTValues.EV;
                    if (block == mk3Block) return GTValues.IV;
                }
            }
        }

        return -1;
    }

    private static long getMaxVoltageForCoreTier(int tier) {
        return GTValues.V[tier];
    }
}