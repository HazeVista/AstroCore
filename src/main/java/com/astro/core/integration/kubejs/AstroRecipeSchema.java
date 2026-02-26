package com.astro.core.integration.kubejs;

import com.gregtechceu.gtceu.integration.kjs.recipe.GTRecipeSchema;

import com.astro.core.common.data.machine.conditions.OxygenatedCondition;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import lombok.experimental.Accessors;

import static com.gregtechceu.gtceu.integration.kjs.recipe.GTRecipeSchema.*;

/**
 * KubeJS RecipeSchema extension for AstroGreg.
 * Provides a fluent API for adding custom recipe conditions in KubeJS scripts:
 * 
 * <pre>
 *   event.recipes.gtceu.aether_engine("my_recipe_id")
 *       .isOxygenated(true)
 *       ...
 * </pre>
 *
 */
public interface AstroRecipeSchema {

    @SuppressWarnings({ "unused", "UnusedReturnValue" })
    @Accessors(chain = true, fluent = true)
    class AstroRecipeJS extends GTRecipeSchema.GTRecipeJS {

        /**
         * Requires the machine to be adjacent to an oxygenated block.
         *
         * @param requiresOxygen true = must have oxygen nearby; false = must not have oxygen nearby.
         */
        public GTRecipeSchema.GTRecipeJS isOxygenated(boolean requiresOxygen) {
            return this.addCondition(new OxygenatedCondition(false, requiresOxygen));
        }

        /**
         * @param requiresOxygen true = must have oxygen nearby; false = must not have oxygen nearby.
         * @param reverse        if true, inverts the result.
         */
        public GTRecipeSchema.GTRecipeJS isOxygenated(boolean requiresOxygen, boolean reverse) {
            return this.addCondition(new OxygenatedCondition(reverse, requiresOxygen));
        }
    }

    RecipeSchema SCHEMA = new RecipeSchema(
            AstroRecipeJS.class,
            AstroRecipeJS::new,
            DURATION, DATA, CONDITIONS, ALL_INPUTS, ALL_TICK_INPUTS, ALL_OUTPUTS, ALL_TICK_OUTPUTS,
            INPUT_CHANCE_LOGICS, OUTPUT_CHANCE_LOGICS, TICK_INPUT_CHANCE_LOGICS, TICK_OUTPUT_CHANCE_LOGICS, CATEGORY)
            .constructor((recipe, schemaType, keys, from) -> recipe.id(from.getValue(recipe, ID)), ID)
            .constructor(DURATION, CONDITIONS, ALL_INPUTS, ALL_OUTPUTS, ALL_TICK_INPUTS, ALL_TICK_OUTPUTS);
}
