package com.astro.core.common.machine.multiblock.steam;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IFluidRenderMulti;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.machine.multiblock.steam.SteamParallelMultiblockMachine;
import com.gregtechceu.gtceu.config.ConfigHolder;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.RequireRerender;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("all")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SteamWasher extends SteamParallelMultiblockMachine implements IFluidRenderMulti {

    private static final double STEAM_TO_EU = 2.0;

    @Getter
    @Setter
    @DescSynced
    @RequireRerender
    private @NotNull Set<BlockPos> fluidBlockOffsets = new HashSet<>();

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            SteamWasher.class,
            SteamParallelMultiblockMachine.MANAGED_FIELD_HOLDER);

    private static final FluidStack WATER_STACK;
    static {
        Fluid waterFluid = GTMaterials.Water.getFluid();
        if (waterFluid != null) {
            WATER_STACK = new FluidStack(waterFluid, 1000);
        } else {
            WATER_STACK = FluidStack.EMPTY;
        }
    }

    public SteamWasher(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
        setMaxParallels(ConfigHolder.INSTANCE.machines.steamMultiParallelAmount);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        fluidBlockOffsets = saveOffsets();
        IFluidRenderMulti.super.onStructureFormed();
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        IFluidRenderMulti.super.onStructureInvalid();
    }

    @Override
    public double getConversionRate() {
        return STEAM_TO_EU;
    }

    @NotNull
    @Override
    public Set<BlockPos> saveOffsets() {
        Direction up = RelativeDirection.UP.getRelative(getFrontFacing(), getUpwardsFacing(), isFlipped());
        Direction back = getFrontFacing().getOpposite();
        Direction right = RelativeDirection.RIGHT.getRelative(getFrontFacing(), getUpwardsFacing(), isFlipped());

        BlockPos pos = getPos();

        Set<BlockPos> offsets = new HashSet<>();

        BlockPos startPos = pos
                .relative(up, -1)
                .relative(back, 1)
                .relative(right.getOpposite(), 2);

        int width = 5;
        int depth = 3;
        int height = 3;

        for (int dx = 0; dx < width; dx++) {
            for (int dz = 0; dz < depth; dz++) {
                for (int dy = 0; dy < height; dy++) {

                    BlockPos currentPos = startPos.offset(
                            right.getStepX() * dx + back.getStepX() * dz,
                            up.getStepY() * dy,
                            right.getStepZ() * dx + back.getStepZ() * dz);

                    offsets.add(currentPos.subtract(pos));
                }
            }
        }

        return offsets;
    }

    public static ModifierFunction recipeModifier(@NotNull MetaMachine machine, @NotNull GTRecipe recipe) {
        if (RecipeHelper.getRecipeEUtTier(recipe) > GTValues.LV) return ModifierFunction.IDENTITY;
        int parallel = ParallelLogic.getParallelAmount(machine, recipe,
                ConfigHolder.INSTANCE.machines.steamMultiParallelAmount);
        return ModifierFunction.builder()
                .inputModifier(ContentModifier.multiplier(parallel))
                .outputModifier(ContentModifier.multiplier(parallel))
                .durationMultiplier(1.5)
                .parallels(parallel)
                .build();
    }
}
