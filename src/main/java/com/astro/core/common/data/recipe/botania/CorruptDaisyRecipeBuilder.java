package com.astro.core.common.data.recipe.botania;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.state.BlockState;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.crafting.StateIngredientHelper;

import java.util.function.Consumer;

@SuppressWarnings("all")
public class CorruptDaisyRecipeBuilder {

    private final ResourceLocation id;
    private final StateIngredient input;
    private final BlockState output;
    private final int time;

    public CorruptDaisyRecipeBuilder(ResourceLocation id, StateIngredient input,
                                     BlockState output, int time) {
        this.id = id;
        this.input = input;
        this.output = output;
        this.time = time;
    }

    public void save(Consumer<FinishedRecipe> provider) {
        provider.accept(new Result(id, input, output, time));
    }

    public static class Result implements FinishedRecipe {

        private final ResourceLocation id;
        private final StateIngredient input;
        private final BlockState output;
        private final int time;

        public Result(ResourceLocation id, StateIngredient input,
                      BlockState output, int time) {
            this.id = id;
            this.input = input;
            this.output = output;
            this.time = time;
        }

        @Override
        public void serializeRecipeData(@NotNull JsonObject json) {
            json.add("input", input.serialize());
            json.add("output", StateIngredientHelper.serializeBlockState(output));
            json.addProperty("time", time);
        }

        @Override
        @NotNull
        public ResourceLocation getId() {
            return id;
        }

        @Override
        @NotNull
        public RecipeSerializer<?> getType() {
            return CorruptDaisyRecipe.SERIALIZER.get();
        }

        @Override
        @Nullable
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Override
        @Nullable
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}
