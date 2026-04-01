package com.astro.core.common.machine.multiblock.electric.planetary_research;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.astro.core.common.data.AstroBlocks.*;

public class IndustrialObservatoryMachine extends ObservatoryMachine {

    private static final int[][] CORE_OFFSETS = {
            { 1, 6, 1 },
            { 1, 6, -1 },
            { -1, 6, 1 },
            { -1, 6, -1 },
    };

    private int coreTier = -1;

    public IndustrialObservatoryMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        scanCores();
    }

    @Override
    public void onStructureInvalid() {
        coreTier = -1;
        super.onStructureInvalid();
    }

    private void scanCores() {
        coreTier = -1;
        var level = getLevel();
        if (level == null) return;

        int lowestTier = Integer.MAX_VALUE;
        boolean foundAny = false;
        for (int[] offset : CORE_OFFSETS) {
            BlockPos corePos = resolveOffset(getPos(), getFrontFacing(), offset[0], offset[1], offset[2]);
            Block block = level.getBlockState(corePos).getBlock();
            int tier = getCoreBlockTier(block);
            if (tier != -1) {
                foundAny = true;
                lowestTier = Math.min(lowestTier, tier);
            }
        }
        this.coreTier = foundAny ? lowestTier : -1;
    }

    private static BlockPos resolveOffset(BlockPos origin, Direction facing, int forward, int up, int right) {
        Direction rightDir = facing.getClockWise();
        return origin
                .relative(facing, forward)
                .above(up)
                .relative(rightDir, right);
    }

    private static int getCoreBlockTier(Block block) {
        if (block == INDUSTRIAL_PROCESSING_CORE_MK3.get()) return GTValues.IV;
        if (block == INDUSTRIAL_PROCESSING_CORE_MK2.get()) return GTValues.EV;
        if (block == INDUSTRIAL_PROCESSING_CORE_MK1.get()) return GTValues.HV;
        return -1;
    }

    public static @NotNull ModifierFunction recipeModifier(@NotNull MetaMachine machine,
                                                           @NotNull GTRecipe recipe) {
        if (!(machine instanceof IndustrialObservatoryMachine obs)) {
            return ModifierFunction.IDENTITY;
        }
        if (obs.coreTier == -1) return ModifierFunction.IDENTITY;
        if (RecipeHelper.getRecipeEUtTier(recipe) > obs.coreTier) return ModifierFunction.IDENTITY;
        long maxVoltage = GTValues.V[obs.coreTier];
        long overclockVoltage = Math.min(obs.getOverclockVoltage(), maxVoltage);
        return OverclockingLogic.PERFECT_OVERCLOCK_SUBTICK.getModifier(machine, recipe, overclockVoltage);
    }

    @Override
    protected void addExtraDisplayInfo(List<Component> tl) {
        if (isFormed() && coreTier != -1) {
            tl.add(Component.translatable("astrogreg.machine.processing_core.core",
                    GTValues.VNF[coreTier]).withStyle(ChatFormatting.AQUA));
        }
    }
}
