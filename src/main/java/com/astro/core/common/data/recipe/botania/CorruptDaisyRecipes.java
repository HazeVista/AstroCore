package com.astro.core.common.data.recipe.botania;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;

import com.astro.core.AstroCore;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.crafting.StateIngredientHelper;

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

        conversion(provider, "crying_to_weeping_obsidian",
                StateIngredientHelper.of(Blocks.CRYING_OBSIDIAN),
                lookupState("betternether", "weeping_obsidian"));
    }

    private static void conversion(Consumer<FinishedRecipe> provider, String name,
                                   StateIngredient input, BlockState output) {
        if (output == null) return;
        new CorruptDaisyRecipeBuilder(
                new ResourceLocation(AstroCore.MOD_ID, "corrupt_daisy/" + name),
                input, output, 600)
                .save(provider);
    }

    private static @Nullable BlockState lookupState(String modid, String path) {
        ResourceLocation rl = new ResourceLocation(modid, path);
        if (ForgeRegistries.BLOCKS.containsKey(rl)) {
            return ForgeRegistries.BLOCKS.getValue(rl).defaultBlockState();
        }
        return null;
    }
}