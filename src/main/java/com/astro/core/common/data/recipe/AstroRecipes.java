package com.astro.core.common.data.recipe;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import com.astro.core.common.data.AstroRecipeTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class AstroRecipes {

    private static final Logger LOGGER = LoggerFactory.getLogger("AstroSteamBlastFurnaceRecipes");

    public static void init(Consumer<FinishedRecipe> provider) {
        // output, input, base seconds
        add(provider, "iron_ingot_to_steel_ingot", "gtceu:steel_ingot", "minecraft:iron_ingot", 90);
        add(provider, "wrought_iron_ingot_to_steel_ingot", "gtceu:steel_ingot", "gtceu:wrought_iron_ingot", 40);
        add(provider, "iron_block_to_steel_block", "gtceu:steel_block", "minecraft:iron_block", 810);
        add(provider, "wrought_iron_block_to_steel_block", "gtceu:steel_block", "gtceu:wrought_iron_block", 360);
        add(provider, "steel_ingot_to_damascus_steel_ingot", "gtceu:damascus_steel_ingot", "gtceu:steel_ingot", 60);
        add(provider, "manasteel_dust_to_manasteel_ingot", "botania:manasteel_ingot", "gtbotania:manasteel_dust", 45);
    }

    private static void add(Consumer<FinishedRecipe> provider, String id, String outputId, String inputId,
                            int seconds) {
        ResourceLocation inRL = ResourceLocation.tryParse(inputId);
        ResourceLocation outRL = ResourceLocation.tryParse(outputId);

        Item in = BuiltInRegistries.ITEM.get(inRL);
        Item out = BuiltInRegistries.ITEM.get(outRL);

        AstroRecipeTypes.STEAM_BLAST_FURNACE_RECIPES.recipeBuilder(id)
                .inputItems(in)
                .outputItems(out)
                // Duration is in ticks. 20 ticks = 1 second.
                .duration(seconds * 20)
                // Steam cost is handled at runtime by SteamEnergyRecipeHandler.
                // With conversionRate=2.0 (HP), EUt=3 results in ~6 mB/t steam usage per parallel.
                .EUt(3)
                .save(provider);
    }
}
