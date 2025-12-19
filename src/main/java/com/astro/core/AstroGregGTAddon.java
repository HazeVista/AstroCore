package com.astro.core;

import com.gregtechceu.gtceu.api.addon.GTAddon;
import com.gregtechceu.gtceu.api.addon.IGTAddon;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;

import net.minecraft.data.recipes.FinishedRecipe;

import com.astro.core.common.data.AstroMachineRecipes;
import com.astro.core.common.data.block.AstroBlocks;
import com.astro.core.common.data.materials.AstroElements;
import com.astro.core.common.data.recipe.run.AstroWireRecipeRunner;
import com.astro.core.common.machine.AstroHatchesAndBuses;
import com.astro.core.common.machine.crates.AstroCrateRecipes;
import com.astro.core.common.machine.drums.AstroDrumRecipes;
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
        AstroBlocks.init();
    }

    @Override
    public String addonModId() {
        return AstroCore.MOD_ID;
    }

    @Override
    public void registerTagPrefixes() {}

    @Override
    public void addRecipes(Consumer<FinishedRecipe> provider) {
        AstroMachineRecipes.init(provider);
        AstroWireRecipeRunner.init(provider);
        AstroDrumRecipes.init(provider);
        AstroCrateRecipes.init(provider);
        AstroHatchesAndBuses.init(provider);
    }

    @Override
    public void registerElements() {
        IGTAddon.super.registerElements();
        AstroElements.init();
    }

    // If you have custom ingredient types, uncomment this & change to match your capability.
    // KubeJS WILL REMOVE YOUR RECIPES IF THESE ARE NOT REGISTERED.
    /*
     * public static final ContentJS<Double> PRESSURE_IN = new ContentJS<>(NumberComponent.ANY_DOUBLE,
     * CustomRecipeCapabilities.PRESSURE, false);
     * public static final ContentJS<Double> PRESSURE_OUT = new ContentJS<>(NumberComponent.ANY_DOUBLE,
     * CustomRecipeCapabilities.PRESSURE, true);
     * 
     * @Override
     * public void registerRecipeKeys(KJSRecipeKeyEvent event) {
     * event.registerKey(CustomRecipeCapabilities.PRESSURE, Pair.of(PRESSURE_IN, PRESSURE_OUT));
     * }
     */
}
