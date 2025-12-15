package com.astro.core.common.machine.drums;

import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.plate;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.rodLong;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.ASSEMBLER_RECIPES;

public class AstroDrumRecipes {

    public static final void init(Consumer<FinishedRecipe> provider) {
        register(provider);
    }

    public static void register(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapelessNBTClearingRecipe(provider, "drum_nbt_rhodium_plated_palladium",
                AstroDrums.RHODIUM_PLATED_PALLADIUM_DRUM.asStack(), AstroDrums.RHODIUM_PLATED_PALLADIUM_DRUM.asStack());

        ASSEMBLER_RECIPES.recipeBuilder("rhodium_plated_palladium_drum")
                .EUt(16)
                .duration(200)
                .inputItems(rodLong, GTMaterials.RhodiumPlatedPalladium, 2)
                .inputItems(plate, GTMaterials.RhodiumPlatedPalladium, 4)
                .circuitMeta(2)
                .outputItems(AstroDrums.RHODIUM_PLATED_PALLADIUM_DRUM)
                .addMaterialInfo(true)
                .save(provider);

        VanillaRecipeHelper.addShapelessNBTClearingRecipe(provider, "drum_nbt_naquadah_alloy",
                AstroDrums.NAQUADAH_ALLOY_DRUM.asStack(), AstroDrums.NAQUADAH_ALLOY_DRUM.asStack());

        ASSEMBLER_RECIPES.recipeBuilder("naquadah_alloy_drum")
                .EUt(16)
                .duration(200)
                .inputItems(rodLong, GTMaterials.NaquadahAlloy, 2)
                .inputItems(plate, GTMaterials.NaquadahAlloy, 4)
                .circuitMeta(2)
                .outputItems(AstroDrums.NAQUADAH_ALLOY_DRUM)
                .addMaterialInfo(true)
                .save(provider);

        VanillaRecipeHelper.addShapelessNBTClearingRecipe(provider, "drum_nbt_darmstadtium",
                AstroDrums.DARMSTADTIUM_DRUM.asStack(), AstroDrums.DARMSTADTIUM_DRUM.asStack());

        ASSEMBLER_RECIPES.recipeBuilder("darmstadtium_drum")
                .EUt(16)
                .duration(200)
                .inputItems(rodLong, GTMaterials.Darmstadtium, 2)
                .inputItems(plate, GTMaterials.Darmstadtium, 4)
                .circuitMeta(2)
                .outputItems(AstroDrums.DARMSTADTIUM_DRUM)
                .addMaterialInfo(true)
                .save(provider);

        VanillaRecipeHelper.addShapelessNBTClearingRecipe(provider, "drum_nbt_neutronium",
                AstroDrums.NEUTRONIUM_DRUM.asStack(), AstroDrums.NEUTRONIUM_DRUM.asStack());

        ASSEMBLER_RECIPES.recipeBuilder("neutronium_drum")
                .EUt(16)
                .duration(200)
                .inputItems(rodLong, GTMaterials.Neutronium, 2)
                .inputItems(plate, GTMaterials.Neutronium, 4)
                .circuitMeta(2)
                .outputItems(AstroDrums.NEUTRONIUM_DRUM)
                .addMaterialInfo(true)
                .save(provider);
    }
}
