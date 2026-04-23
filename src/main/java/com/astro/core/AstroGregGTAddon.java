package com.astro.core;

import com.astro.core.common.data.recipe.botania.CorruptDaisyRecipes;
import com.gregtechceu.gtceu.api.addon.GTAddon;
import com.gregtechceu.gtceu.api.addon.IGTAddon;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;

import net.minecraft.data.recipes.FinishedRecipe;

import com.astro.core.common.data.materials.AstroElements;
import com.astro.core.common.data.recipe.AstroRecipes;
import com.astro.core.common.data.recipe.generated.AstroCrateRecipes;
import com.astro.core.common.data.recipe.generated.AstroDrumRecipes;
import com.astro.core.common.data.recipe.generated.AstroGeneratorRecipeHandler;
import com.astro.core.common.data.recipe.generated.ComposterRecipes;
import com.astro.core.common.data.recipe.planetary_research.AstroPlanetaryRecipes;
import com.astro.core.common.data.recipe.run.AstroSleeveRecipeRunner;
import com.astro.core.common.data.recipe.run.AstroWireRecipeRunner;
import com.astro.core.common.data.tag.AstroTagPrefix;
import com.astro.core.common.data.worldgen.AstroWorldgen;
import com.astro.core.common.machine.part.AstroHatchesAndBuses;
import com.astro.core.common.registry.AstroRegistry;

import java.util.function.Consumer;

@SuppressWarnings("unused")
@GTAddon
public class AstroGregGTAddon implements IGTAddon {

    @Override
    public GTRegistrate getRegistrate() {
        return AstroRegistry.REGISTRATE;
    }

    @Override
    public void initializeAddon() {
        ComposterRecipes.init();
    }

    @Override
    public void registerTagPrefixes() {
        AstroTagPrefix.init();
    }

    @Override
    public void registerOreVeins() {
        AstroWorldgen.lateInit();
    }

    public String addonModId() {
        return AstroCore.MOD_ID;
    }

    @Override
    public void addRecipes(Consumer<FinishedRecipe> provider) {
        AstroWireRecipeRunner.init(provider);
        AstroSleeveRecipeRunner.init(provider);
        AstroDrumRecipes.init(provider);
        AstroCrateRecipes.init(provider);
        AstroHatchesAndBuses.init(provider);
        AstroRecipes.init(provider);
        CorruptDaisyRecipes.init(provider);
        AstroPlanetaryRecipes.init(provider);
        AstroGeneratorRecipeHandler.init(provider);
    }

    @Override
    public void registerElements() {
        IGTAddon.super.registerElements();
        AstroElements.init();
    }
}
