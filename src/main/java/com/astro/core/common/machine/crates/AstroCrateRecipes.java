package com.astro.core.common.machine.crates;

import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.plate;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.rodLong;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.ASSEMBLER_RECIPES;

public class AstroCrateRecipes {

    public static final void init(Consumer<FinishedRecipe> provider) {
        register(provider);
    }

    private static void register(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addShapedRecipe(provider, true,
                "rhodium_plated_palladium_crate",
                AstroCrates.RHODIUM_PLATED_PALLADIUM_CRATE.asStack(),
                "RPR",
                "PhP",
                "RPR",
                'P', new MaterialEntry(plate, GTMaterials.RhodiumPlatedPalladium),
                'R', new MaterialEntry(rodLong, GTMaterials.RhodiumPlatedPalladium)

        );

        ASSEMBLER_RECIPES.recipeBuilder("rhodium_plated_palladium_crate")
                .EUt(16)
                .inputItems(rodLong, GTMaterials.RhodiumPlatedPalladium, 4)
                .inputItems(plate, GTMaterials.RhodiumPlatedPalladium, 4)
                .outputItems(AstroCrates.RHODIUM_PLATED_PALLADIUM_CRATE)
                .duration(200)
                .circuitMeta(1)
                .addMaterialInfo(true)
                .save(provider);

        VanillaRecipeHelper.addShapedRecipe(provider, true,
                "naquadah_alloy_crate",
                AstroCrates.NAQUADAH_ALLOY_CRATE.asStack(),
                "RPR",
                "PhP",
                "RPR",
                'P', new MaterialEntry(plate, GTMaterials.NaquadahAlloy),
                'R', new MaterialEntry(rodLong, GTMaterials.NaquadahAlloy)

        );

        ASSEMBLER_RECIPES.recipeBuilder("naquadah_alloy_crate")
                .EUt(16)
                .inputItems(rodLong, GTMaterials.NaquadahAlloy, 4)
                .inputItems(plate, GTMaterials.NaquadahAlloy, 4)
                .outputItems(AstroCrates.NAQUADAH_ALLOY_CRATE)
                .duration(200)
                .circuitMeta(1)
                .addMaterialInfo(true)
                .save(provider);

        VanillaRecipeHelper.addShapedRecipe(provider, true,
                "darmstadtium_crate",
                AstroCrates.DARMSTADTIUM_CRATE.asStack(),
                "RPR",
                "PhP",
                "RPR",
                'P', new MaterialEntry(plate, GTMaterials.Darmstadtium),
                'R', new MaterialEntry(rodLong, GTMaterials.Darmstadtium)

        );

        ASSEMBLER_RECIPES.recipeBuilder("darmstadtium_crate")
                .EUt(16)
                .inputItems(rodLong, GTMaterials.Darmstadtium, 4)
                .inputItems(plate, GTMaterials.Darmstadtium, 4)
                .outputItems(AstroCrates.DARMSTADTIUM_CRATE)
                .duration(200)
                .circuitMeta(1)
                .addMaterialInfo(true)
                .save(provider);

        VanillaRecipeHelper.addShapedRecipe(provider, true,
                "neutronium_crate",
                AstroCrates.NEUTRONIUM_CRATE.asStack(),
                "RPR",
                "PhP",
                "RPR",
                'P', new MaterialEntry(plate, GTMaterials.Neutronium),
                'R', new MaterialEntry(rodLong, GTMaterials.Neutronium)

        );

        ASSEMBLER_RECIPES.recipeBuilder("neutronium_crate")
                .EUt(16)
                .inputItems(rodLong, GTMaterials.Neutronium, 4)
                .inputItems(plate, GTMaterials.Neutronium, 4)
                .outputItems(AstroCrates.NEUTRONIUM_CRATE)
                .duration(200)
                .circuitMeta(1)
                .addMaterialInfo(true)
                .save(provider);
    }
}
