package com.astro.core.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import earth.terrarium.adastra.common.blockentities.base.RecipeMachineBlockEntity;
import earth.terrarium.adastra.common.blockentities.machines.OxygenLoaderBlockEntity;
import earth.terrarium.adastra.common.recipes.machines.OxygenLoadingRecipe;
import earth.terrarium.botarium.common.fluid.base.BotariumFluidBlock;
import earth.terrarium.botarium.common.fluid.impl.WrappedBlockFluidContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Supplier;

@Mixin(value = OxygenLoaderBlockEntity.class, remap = false)
public abstract class OxygenLoaderMixin extends RecipeMachineBlockEntity<OxygenLoadingRecipe>
                                        implements BotariumFluidBlock<WrappedBlockFluidContainer> {

    public OxygenLoaderMixin(BlockPos pos, BlockState state, int containerSize,
                             Supplier<RecipeType<OxygenLoadingRecipe>> recipeType) {
        super(pos, state, containerSize, recipeType);
    }

    @Shadow
    private WrappedBlockFluidContainer fluidContainer;

    @ModifyExpressionValue(method = "recipeTick",
                           at = @At(value = "INVOKE",
                                    target = "Learth/terrarium/adastra/common/blockentities/machines/OxygenLoaderBlockEntity;canCraft()Z"))
    private boolean astro$modifyCanCraftCheck(boolean original) {
        return original && (recipe.result().getFluidAmount() == fluidContainer.internalInsert(recipe.result(), true));
    }
}
