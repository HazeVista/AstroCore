package com.astro.core.client.integration.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;

import net.minecraft.resources.ResourceLocation;

import vazkii.botania.api.recipe.PureDaisyRecipe;
import vazkii.botania.client.integration.emi.BlendTextureWidget;

import com.astro.core.AstroCore;
import com.astro.core.common.data.block.flower.AstroFlowerBlocks;
import com.astro.core.common.data.recipe.AstroRecipeTypes;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

@EmiEntrypoint
@SuppressWarnings("all")
public class AstroEmiPlugin implements EmiPlugin {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation("botania", "textures/gui/pure_daisy_overlay.png");

    public static final EmiRecipeCategory CORRUPT_DAISY = new EmiRecipeCategory(
            new ResourceLocation(AstroCore.MOD_ID, "corrupt_daisy"),
            EmiStack.of(AstroFlowerBlocks.CORRUPT_DAISY_ITEM.get()));

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(CORRUPT_DAISY);
        registry.addWorkstation(CORRUPT_DAISY, EmiStack.of(AstroFlowerBlocks.CORRUPT_DAISY_BLOCK.get()));

        for (PureDaisyRecipe recipe : registry.getRecipeManager()
                .getAllRecipesFor(AstroRecipeTypes.CORRUPT_DAISY_TYPE.get())) {
            registry.addRecipe(new CorruptDaisyEmiRecipe(recipe));
        }
    }

    public static class CorruptDaisyEmiRecipe implements EmiRecipe {

        private final ResourceLocation id;
        private final List<EmiIngredient> input;
        private final List<EmiStack> output;
        private final EmiStack flower;

        public CorruptDaisyEmiRecipe(PureDaisyRecipe recipe) {
            this.id = recipe.getId();
            this.flower = EmiStack.of(AstroFlowerBlocks.CORRUPT_DAISY_ITEM.get());
            this.input = List.of(EmiIngredient.of(recipe.getInput().getDisplayed().stream().map(s -> {
                if (s.getFluidState().isEmpty()) {
                    return EmiStack.of(s.getBlock());
                } else {
                    return EmiStack.of(s.getFluidState().getType());
                }
            }).collect(Collectors.toList())));
            this.output = List.of(EmiStack.of(recipe.getOutputState().getBlock()));
        }

        @Override
        public EmiRecipeCategory getCategory() {
            return CORRUPT_DAISY;
        }

        @Override
        public @Nullable ResourceLocation getId() {
            return id;
        }

        @Override
        public List<EmiIngredient> getInputs() {
            return input;
        }

        @Override
        public List<EmiStack> getOutputs() {
            return output;
        }

        @Override
        public List<EmiIngredient> getCatalysts() {
            return List.of(flower);
        }

        @Override
        public int getDisplayWidth() {
            return 96;
        }

        @Override
        public int getDisplayHeight() {
            return 44;
        }

        @Override
        public void addWidgets(WidgetHolder widgets) {
            widgets.add(new BlendTextureWidget(TEXTURE, 17, 0, 65, 44, 0, 0));
            widgets.addSlot(input.get(0), 10, 13).drawBack(false);
            widgets.addSlot(flower, 39, 13).catalyst(true).drawBack(false);
            widgets.addSlot(output.get(0), 68, 13).drawBack(false).recipeContext(this);
        }
    }
}