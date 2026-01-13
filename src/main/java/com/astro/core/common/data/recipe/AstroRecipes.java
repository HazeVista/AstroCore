package com.astro.core.common.data.recipe;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.data.recipes.FinishedRecipe;

import com.astro.core.common.data.AstroRecipeTypes;

import java.util.function.Consumer;

public class AstroRecipes {

    public static void init(Consumer<FinishedRecipe> provider) {
        steamBlastFurnaceRecipes(provider);
    }

    private static void steamBlastFurnaceRecipes(Consumer<FinishedRecipe> provider) {
        /**
         * // Example recipe: Iron Ore -> Iron Ingot
         * GTMaterials.IRON.oreTag(TagPrefix.ORE).forEach(oreTag -> {
         * AstroRecipeTypes.STEAM_BLAST_FURNACE_RECIPES.builder()
         * .setInput(oreTag)
         * .setOutput(GTMaterials.IRON.ingotTag(TagPrefix.INGOT), 1)
         * .setEnergyCost(64) // Example energy cost
         * .setProcessingTime(200) // Example processing time
         * .build(provider, "steam_blast_furnace/iron_ingot_from_ore");
         * });
         * 
         * // Example recipe: Gold Ore -> Gold Ingot
         * GTMaterials.GOLD.oreTag(TagPrefix.ORE).forEach(oreTag -> {
         * AstroRecipeTypes.STEAM_BLAST_FURNACE_RECIPES.builder()
         * .setInput(oreTag)
         * .setOutput(GTMaterials.GOLD.ingotTag(TagPrefix.INGOT), 1)
         * .setEnergyCost(128) // Example energy cost
         * .setProcessingTime(300) // Example processing time
         * .build(provider, "steam_blast_furnace/gold_ingot_from_ore");
         * });
         * 
         * // Additional recipes can be added here following the same pattern, this is just a poor example.
         */
    }
}
