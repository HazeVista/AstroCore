package com.astro.core.integration.kubejs;

import com.gregtechceu.gtceu.integration.kjs.recipe.GTRecipeSchema.GTRecipeJS;

import com.astro.core.common.data.machine.conditions.OxygenatedCondition;

/**
 * Static KubeJS bindings for AstroGreg recipe conditions.
 * Exposed to scripts as {@code AstroRecipeSchemaBindings}. Use these if the
 * fluent methods on the recipe object are not available in your context.
 * Example usage in a KubeJS script:
 * 
 * <pre>
 * AstroRecipeSchemaBindings.isOxygenated(recipe, true)
 * </pre>
 */
@SuppressWarnings("unused")
public final class AstroRecipeSchemaBindings {

    private AstroRecipeSchemaBindings() {}

    /**
     * Adds an oxygenated condition to the recipe.
     *
     * @param recipe         the recipe being built.
     * @param requiresOxygen true = must have oxygen nearby; false = must not.
     */
    public static GTRecipeJS isOxygenated(GTRecipeJS recipe, boolean requiresOxygen) {
        return recipe.addCondition(new OxygenatedCondition(false, requiresOxygen));
    }

    /**
     * Adds an oxygenated condition to the recipe with optional inversion.
     *
     * @param recipe         the recipe being built.
     * @param requiresOxygen true = must have oxygen nearby; false = must not.
     * @param reverse        if true, inverts the result of the check.
     */
    public static GTRecipeJS isOxygenated(GTRecipeJS recipe, boolean requiresOxygen, boolean reverse) {
        return recipe.addCondition(new OxygenatedCondition(reverse, requiresOxygen));
    }
}
