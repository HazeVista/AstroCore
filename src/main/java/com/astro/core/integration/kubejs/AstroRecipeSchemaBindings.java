package com.astro.core.integration.kubejs;

import com.gregtechceu.gtceu.integration.kjs.recipe.GTRecipeSchema.GTRecipeJS;

import com.astro.core.common.data.machine.conditions.OxygenatedCondition;

@SuppressWarnings("unused")
public final class AstroRecipeSchemaBindings {

    private AstroRecipeSchemaBindings() {}

    public static GTRecipeJS isOxygenated(GTRecipeJS recipe, boolean requiresOxygen) {
        return recipe.addCondition(new OxygenatedCondition(false, requiresOxygen));
    }

    public static GTRecipeJS isOxygenated(GTRecipeJS recipe, boolean requiresOxygen, boolean reverse) {
        return recipe.addCondition(new OxygenatedCondition(reverse, requiresOxygen));
    }
}
