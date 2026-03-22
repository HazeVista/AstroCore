package com.astro.core.common.data.recipe;

import com.astro.core.common.data.machine.conditions.PlanetaryResearchCondition;
import com.astro.core.common.data.recipe.generated.ObservatoryResearchBuilder;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import earth.terrarium.adastra.common.registry.ModItems;
import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;

@SuppressWarnings("all")
public class AstroTestRecipes {

    public static final String PLUTO_ID = "ad_extendra:pluto";

    public static void init(Consumer<FinishedRecipe> provider) {


        var builder = AstroRecipeTypes.ASTROPORT_RECIPES.recipeBuilder("tier_1_rocket")
                .inputItems(ChemicalHelper.get(plate, GTMaterials.Steel, 4))
                .inputItems(ChemicalHelper.get(rod, GTMaterials.Steel, 4))
                .inputItems(GTItems.ELECTRIC_MOTOR_HV.asStack())
                .outputItems(ModItems.TIER_1_ROCKET)
                .duration(400).EUt(VA[HV])
                .stationResearch(b -> b
                        .researchStack(ChemicalHelper.get(plate, GTMaterials.Steel))
                        .CWUt(16, 2000)
                        .EUt(VA[HV]))
                .addCondition(new PlanetaryResearchCondition().setPlanetId(PLUTO_ID))
                .addData(PlanetaryResearchCondition.RECIPE_DATA_KEY, PLUTO_ID);
        ObservatoryResearchBuilder.applyTo(builder, b -> b
                .researchStack(GTItems.SENSOR_MV.asStack())
                .CWUt(1, 1200)
                .EUt(VA[HV])
                .planetName("Pluto")
                .planetDisplayItem(new net.minecraft.resources.ResourceLocation("astrogreg", "pluto")));

        builder.save(provider);
    }
}