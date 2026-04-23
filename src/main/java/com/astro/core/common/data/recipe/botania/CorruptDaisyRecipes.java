package com.astro.core.common.data.recipe.botania;

import com.gregtechceu.gtceu.common.data.GTBlocks;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;

import net.minecraftforge.common.Tags;
import vazkii.botania.common.crafting.StateIngredientHelper;

import com.astro.core.AstroCore;

import java.util.function.Consumer;

@SuppressWarnings("all")
public class CorruptDaisyRecipes {

    public static void init(Consumer<FinishedRecipe> provider) {

        conversion(provider, "stone_to_netherrack",
                StateIngredientHelper.of(Tags.Blocks.COBBLESTONE),
                Blocks.NETHERRACK.defaultBlockState());

        conversion(provider, "sand_to_soul_sand",
                StateIngredientHelper.of(Tags.Blocks.SAND),
                Blocks.SOUL_SAND.defaultBlockState());

        conversion(provider, "bricks_to_nether_bricks",
                StateIngredientHelper.of(Blocks.BRICKS),
                Blocks.NETHER_BRICKS.defaultBlockState());

        conversion(provider, "dirt_to_soul_soil",
                StateIngredientHelper.of(Blocks.DIRT),
                Blocks.SOUL_SOIL.defaultBlockState());

        conversion(provider, "obsidian_to_crying_obsidian",
                StateIngredientHelper.of(Blocks.OBSIDIAN),
                Blocks.CRYING_OBSIDIAN.defaultBlockState());

        conversion(provider, "calcite_to_bone",
                StateIngredientHelper.of(Blocks.CALCITE),
                Blocks.BONE_BLOCK.defaultBlockState());
    }

    private static void conversion(Consumer<FinishedRecipe> provider, String name,
                                   vazkii.botania.api.recipe.StateIngredient input,
                                   net.minecraft.world.level.block.state.BlockState output) {
        new CorruptDaisyRecipeBuilder(
                new ResourceLocation(AstroCore.MOD_ID, "corrupt_daisy/" + name),
                input, output, 600)
                .save(provider);
    }
}