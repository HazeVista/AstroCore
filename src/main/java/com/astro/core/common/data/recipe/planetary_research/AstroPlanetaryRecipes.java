package com.astro.core.common.data.recipe.planetary_research;

import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.data.recipe.CustomTags;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import com.astro.core.common.data.machine.conditions.PlanetaryResearchCondition;
import com.astro.core.common.data.recipe.AstroRecipeTypes;
import com.astro.core.common.data.recipe.generated.ObservatoryResearchBuilder;
import earth.terrarium.adastra.common.registry.ModItems;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTItems.TOOL_DATA_STICK;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static earth.terrarium.adastra.common.registry.ModItems.TIER_1_ROCKET;
import static earth.terrarium.adastra.common.registry.ModItems.TIER_2_ROCKET;

@SuppressWarnings("all")
public class AstroPlanetaryRecipes {

    public static final String PLUTO_ID = "Pluto";
    public static final String NEPTUNE_ID = "Neptune";
    public static final String URANUS_ID = "Uranus";
    public static final String SATURN_ID = "Saturn";
    public static final String JUPITER_ID = "Jupiter";
    public static final String CERES_ID = "Ceres";
    public static final String MARS_ID = "Mars";
    public static final String MOON_ID = "The Moon";
    public static final String EARTH_ID = "Earth";
    public static final String VENUS_ID = "Venus";
    public static final String MERCURY_ID = "Mercury";

    public static void init(Consumer<FinishedRecipe> provider) {
        var tier1Builder = AstroRecipeTypes.ASTROPORT_RECIPES.recipeBuilder("tier_1_rocket")
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
                .outputItems(TIER_1_ROCKET)
                .duration(1200).EUt(VA[HV])
                .addCondition(new PlanetaryResearchCondition().setPlanetId(PLUTO_ID))
                .addData(PlanetaryResearchCondition.RECIPE_DATA_KEY, PLUTO_ID);
        ObservatoryResearchBuilder.applyTo(tier1Builder, b -> b
                .researchStack(GTItems.SENSOR_MV.asStack())
                .CWUt(1, 1200)
                .EUt(VA[HV])
                .planetName("Pluto")  // this part tells the tooltip what name to display
                .researchItemType("disk") // can choose disk, stick, orb, or module
                .planetDisplayItem(new ResourceLocation("astrogreg", "pluto"))); // shows planet png
        tier1Builder.save(provider);

        var tier2Builder = AstroRecipeTypes.ASTROPORT_RECIPES.recipeBuilder("tier_2_rocket")
                .inputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("astrogreg", "basic_rocket_nose_cone")))
                .inputItems(GTBlocks.CASING_TEMPERED_GLASS.asStack(), 2)
                .inputItems(frameGt, Aluminium, 8)
                .inputItems(plateDouble, StainlessSteel, 24)
                .inputItems(plate, StainlessSteel, 48)
                .inputItems(GTBlocks.HERMETIC_CASING_HV.asStack(), 4)
                .inputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("astrogreg", "basic_rocket_fin")), 4)
                .inputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("astrogreg", "hv_rocket_engine")), 1)
                .inputItems(CustomTags.HV_CIRCUITS, 4)
                .inputItems(cableGtSingle, Gold, 32)
                .inputFluids(SolderingAlloy.getFluid(L * 12))
                .outputItems(TIER_2_ROCKET)
                .duration(900)
                .EUt(VA[EV])
                 .scannerResearch(b -> b
                 .researchStack(new ItemStack(TIER_1_ROCKET.get()))
                 .dataStack(TOOL_DATA_STICK.asStack())
                 .EUt(VA[HV]))
                .addCondition(new PlanetaryResearchCondition().setPlanetId(NEPTUNE_ID))
                .addData(PlanetaryResearchCondition.RECIPE_DATA_KEY, NEPTUNE_ID);
        ObservatoryResearchBuilder.applyTo(tier2Builder, b -> b
                .researchStack(GTItems.SENSOR_HV.asStack())
                .CWUt(2, 1600)
                .EUt(VA[EV])
                .planetName("Neptune")
                .researchItemType("stick")
                .planetDisplayItem(new ResourceLocation("astrogreg", "neptune")));
        tier2Builder.save(provider);

        var tier3Builder = AstroRecipeTypes.ASTROPORT_RECIPES.recipeBuilder("tier_3_rocket")
                .inputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("astrogreg", "advanced_rocket_nose_cone")))
                .inputItems(GTBlocks.CASING_TEMPERED_GLASS.asStack(), 2)
                .inputItems(frameGt, StainlessSteel, 8)
                .inputItems(plateDouble, Titanium, 24)
                .inputItems(plate, Titanium, 48)
                .inputItems(GTBlocks.HERMETIC_CASING_EV.asStack(), 4)
                .inputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("astrogreg", "advanced_rocket_fin")), 4)
                .inputItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation("astrogreg", "ev_rocket_engine")), 1)
                .inputItems(CustomTags.EV_CIRCUITS, 4)
                .inputItems(cableGtSingle, Aluminium, 32)
                .inputFluids(SolderingAlloy.getFluid(L * 15))
                .outputItems(ModItems.TIER_3_ROCKET)
                .duration(1200)
                .EUt(VA[IV])
                .scannerResearch(b -> b
                        .researchStack(new ItemStack(TIER_2_ROCKET.get()))
                        .dataStack(TOOL_DATA_STICK.asStack())
                        .EUt(VA[EV]))
                .addCondition(new PlanetaryResearchCondition().setPlanetId(URANUS_ID))
                .addData(PlanetaryResearchCondition.RECIPE_DATA_KEY, URANUS_ID);
        ObservatoryResearchBuilder.applyTo(tier2Builder, b -> b
                .researchStack(GTItems.SENSOR_HV.asStack())
                .CWUt(4, 2000)
                .EUt(VA[IV])
                .planetName("Uranus")
                .researchItemType("orb")
                .planetDisplayItem(new ResourceLocation("astrogreg", "uranus")));
        tier3Builder.save(provider);
    }
}
