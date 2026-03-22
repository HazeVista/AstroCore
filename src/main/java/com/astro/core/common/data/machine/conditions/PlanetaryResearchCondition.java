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
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class PlanetaryResearchCondition extends RecipeCondition<PlanetaryResearchCondition> {

    public static final String RECIPE_DATA_KEY = "planet_research_id";

    public static final Codec<PlanetaryResearchCondition> CODEC = RecordCodecBuilder
            .create(instance -> isReverse(instance).and(
                    Codec.STRING.fieldOf(RECIPE_DATA_KEY).forGetter(c -> c.planetId)).apply(instance, (reverse, id) -> {
                        PlanetaryResearchCondition c = new PlanetaryResearchCondition();
                        c.setReverse(reverse);
                        c.planetId = id;
                        return c;
                    }));

    @Getter
    private String planetId = "";

    public PlanetaryResearchCondition() {}

    public PlanetaryResearchCondition setPlanetId(String planetId) {
        this.planetId = planetId;
        return this;
    }

    @Override
    public RecipeConditionType<PlanetaryResearchCondition> getType() {
        return AstroRecipeConditions.PLANETARY;
    }

    @Override
    public Component getTooltips() {
        return Component.translatable("astrogreg.recipe_condition.planetary_research");
    }

    @Override
    protected boolean testCondition(@NotNull GTRecipe recipe, @NotNull RecipeLogic recipeLogic) {
        if (planetId == null || planetId.isEmpty()) return true;
        var machine = recipeLogic.getMachine();
        if (!(machine instanceof IMultiController controller) || !controller.isFormed()) return false;
        if (!(controller instanceof AstroPortMachine astroPort)) return false;
        return astroPort.hasPlanetaryResearchData(planetId);
    }

    @Override
    public PlanetaryResearchCondition createTemplate() {
        return new PlanetaryResearchCondition();
    }
}
