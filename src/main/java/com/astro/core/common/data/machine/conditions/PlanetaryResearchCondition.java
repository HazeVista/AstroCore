package com.astro.core.common.data.machine.conditions;

import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeCondition;
import com.gregtechceu.gtceu.api.recipe.condition.RecipeConditionType;

import net.minecraft.network.chat.Component;

import com.astro.core.common.data.machine.AstroRecipeConditions;
import com.astro.core.common.machine.multiblock.electric.planetary_research.AstroPortMachine;
import com.mojang.serialization.Codec;
import org.jetbrains.annotations.NotNull;

public class PlanetaryResearchCondition extends RecipeCondition<PlanetaryResearchCondition> {

    public static final String RECIPE_DATA_KEY = "planet_research_id";

    public static final Codec<PlanetaryResearchCondition> CODEC = RecipeCondition
            .simpleCodec(PlanetaryResearchCondition::new);

    public PlanetaryResearchCondition() {
        super(false);
    }

    public PlanetaryResearchCondition(boolean isReverse) {
        super(isReverse);
    }

    @Override
    public RecipeConditionType<PlanetaryResearchCondition> getType() {
        return AstroRecipeConditions.PLANETARY;
    }

    @Override
    public Component getTooltips() {
        return Component.translatable("astrogreg.recipe.condition.planetary_research");
    }

    @Override
    public PlanetaryResearchCondition createTemplate() {
        return new PlanetaryResearchCondition();
    }

    @Override
    protected boolean testCondition(@NotNull GTRecipe recipe, @NotNull RecipeLogic recipeLogic) {
        String requiredPlanetId = recipe.data.getString(RECIPE_DATA_KEY);
        if (requiredPlanetId.isEmpty()) {
            return true;
        }
        if (!(recipeLogic.getMachine() instanceof IMultiController controller) || !controller.isFormed()) {
            return false;
        }
        if (!(controller.self() instanceof AstroPortMachine astroPort)) {
            return false;
        }
        return astroPort.hasPlanetaryResearchData(requiredPlanetId);
    }
}
