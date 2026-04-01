package com.astro.core.common.data.recipe.planetary_research;

import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.data.recipe.CustomTags;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import com.astro.core.common.data.machine.conditions.PlanetaryResearchCondition;
import com.astro.core.common.data.recipe.AstroRecipeTypes;
import com.astro.core.common.data.recipe.generated.ObservatoryResearchBuilder;
import earth.terrarium.adastra.common.registry.ModItems;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;

@SuppressWarnings("all")
public class AstroPlanetaryRecipes {

    public static final String PLUTO_ID = "Pluto";

    public static void init(Consumer<FinishedRecipe> provider) {
        var builder = AstroRecipeTypes.ASTROPORT_RECIPES.recipeBuilder("tier_1_rocket")
                .inputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("astrogreg", "basic_rocket_nose_cone")))
                .inputItems(GTBlocks.CASING_TEMPERED_GLASS.asStack(), 2)
                .inputItems(frameGt, Steel, 8)
                .inputItems(plateDouble, Aluminium, 16)
                .inputItems(plate, Aluminium, 32)
                .inputItems(GTBlocks.HERMETIC_CASING_MV.asStack(), 2)
                .inputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("astrogreg", "basic_rocket_fin")), 4)
                .inputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("astrogreg", "mv_rocket_engine")), 1)
                .inputItems(CustomTags.MV_CIRCUITS, 4)
                .inputItems(cableGtSingle, Copper, 16)
                .inputFluids(SolderingAlloy.getFluid(L * 9))
                .outputItems(ModItems.TIER_1_ROCKET)
                .duration(1200).EUt(VA[HV])
                // .stationResearch(b -> b
                // .researchStack(GTItems.SENSOR_MV.asStack())
                // .dataStack(TOOL_DATA_STICK.asStack())
                // .CWUt(16, 2000)
                // .EUt(VA[HV])) // here as an example so that it can be referenced in future astroport recipes
                .addCondition(new PlanetaryResearchCondition().setPlanetId(PLUTO_ID))
                .addData(PlanetaryResearchCondition.RECIPE_DATA_KEY, PLUTO_ID);
        ObservatoryResearchBuilder.applyTo(builder, b -> b
                .researchStack(GTItems.SENSOR_MV.asStack())
                .CWUt(1, 1200)
                .EUt(VA[HV])
                .planetName("Pluto")  // this part tells the tooltip what name to display
                .researchItemType("disk") // can choose disk, stick, orb, or module
                .planetDisplayItem(new ResourceLocation("astrogreg", "pluto"))); // shows planet png

        builder.save(provider);
    }
}
