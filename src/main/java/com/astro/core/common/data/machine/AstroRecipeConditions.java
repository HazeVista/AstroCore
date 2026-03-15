package com.astro.core.common.data.machine;

import com.gregtechceu.gtceu.api.recipe.RecipeCondition;
import com.gregtechceu.gtceu.api.recipe.condition.RecipeConditionType;
import com.gregtechceu.gtceu.api.registry.GTRegistries;

import com.astro.core.common.data.machine.conditions.OxygenatedCondition;
import com.astro.core.common.data.machine.conditions.PlanetaryResearchCondition;
import com.mojang.serialization.Codec;

public final class AstroRecipeConditions {

    private AstroRecipeConditions() {}

    public static RecipeConditionType<OxygenatedCondition> OXYGENATED;
    public static RecipeConditionType<PlanetaryResearchCondition> PLANETARY;

    public static void init() {
        OXYGENATED = register("oxygenated", OxygenatedCondition::new, OxygenatedCondition.CODEC);
        PLANETARY = register("planetary_research", PlanetaryResearchCondition::new, PlanetaryResearchCondition.CODEC);
    }

    private static <T extends RecipeCondition<T>> RecipeConditionType<T> register(
                                                                                  String name,
                                                                                  RecipeConditionType.ConditionFactory<T> factory,
                                                                                  Codec<T> codec) {
        return GTRegistries.RECIPE_CONDITIONS.register(name, new RecipeConditionType<>(factory, codec));
    }
}
