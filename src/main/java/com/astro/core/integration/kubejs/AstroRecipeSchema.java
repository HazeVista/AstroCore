package com.astro.core.integration.kubejs;

import com.gregtechceu.gtceu.integration.kjs.recipe.GTRecipeSchema;

import com.astro.core.common.data.machine.conditions.OxygenatedCondition;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import lombok.experimental.Accessors;

import static com.gregtechceu.gtceu.integration.kjs.recipe.GTRecipeSchema.*;

public interface AstroRecipeSchema {

    @SuppressWarnings({ "unused", "UnusedReturnValue" })
    @Accessors(chain = true, fluent = true)
    class AstroRecipeJS extends GTRecipeSchema.GTRecipeJS {

        public GTRecipeSchema.GTRecipeJS isOxygenated(boolean requiresOxygen) {
            return this.addCondition(new OxygenatedCondition(false, requiresOxygen));
        }

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
