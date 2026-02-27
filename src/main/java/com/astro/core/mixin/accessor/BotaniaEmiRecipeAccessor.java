package com.astro.core.mixin.accessor;

import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import vazkii.botania.client.integration.emi.BotaniaEmiRecipe;

import java.util.List;

@Mixin(value = BotaniaEmiRecipe.class, remap = false)
public interface BotaniaEmiRecipeAccessor {

    @Accessor("input")
    List<EmiIngredient> astro$getInput();

    @Mutable
    @Accessor("input")
    void astro$setInput(List<EmiIngredient> input);

    @Accessor("output")
    List<EmiStack> astro$getOutput();
}
