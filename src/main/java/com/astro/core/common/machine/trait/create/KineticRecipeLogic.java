package com.astro.core.common.machine.trait.create;

import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.ActionResult;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;

import net.minecraft.network.chat.Component;

import com.astro.core.common.machine.multiblock.kinetic.KineticParallelMultiblockMachine;

public class KineticRecipeLogic extends RecipeLogic {

    public KineticRecipeLogic(IRecipeLogicMachine machine) {
        super(machine);
    }

    private GTRecipe stripEU(GTRecipe recipe) {
        GTRecipe copy = recipe.copy();
        copy.inputs.remove(EURecipeCapability.CAP);
        copy.tickInputs.remove(EURecipeCapability.CAP);
        return copy;
    }

    @Override
    protected ActionResult matchRecipe(GTRecipe recipe) {
        return RecipeHelper.matchContents(machine, stripEU(recipe));
    }

    @Override
    public boolean checkMatchedRecipeAvailable(GTRecipe match) {
        var modified = machine.fullModifyRecipe(match);
        if (modified != null) {
            modified = stripEU(modified);
            var recipeMatch = checkRecipe(modified);
            if (recipeMatch.isSuccess()) {
                setupRecipe(modified);
            } else {
                putFailureReason(this, match, recipeMatch.reason());
            }
            if (lastRecipe != null && getStatus() == Status.WORKING) {
                lastOriginRecipe = match;
                lastFailedMatches = null;
                return true;
            }
        }
        return false;
    }

    @Override
    public void findAndHandleRecipe() {
        lastFailedMatches = null;
        if (!recipeDirty && lastRecipe != null && checkRecipe(lastRecipe).isSuccess()) {
            if (machine instanceof KineticParallelMultiblockMachine kineticMachine) {
                int activeParallels = Math.max(1, lastRecipe.parallels);
                float requiredSU = activeParallels * KineticParallelMultiblockMachine.SU_PER_PARALLEL;
                if (kineticMachine.getAvailableSU() < requiredSU) {
                    recipeDirty = false;
                    return;
                }
            }
            GTRecipe recipe = lastRecipe;
            lastRecipe = null;
            lastOriginRecipe = null;
            setupRecipe(recipe);
        } else {
            failureReasonMap.clear();
            lastRecipe = null;
            lastOriginRecipe = null;
            handleSearchingRecipes(searchRecipe());
        }
        recipeDirty = false;
    }

    @Override
    public void handleRecipeWorking() {
        if (machine instanceof KineticParallelMultiblockMachine kineticMachine && lastRecipe != null) {
            int activeParallels = Math.max(1, lastRecipe.parallels);
            float requiredSU = activeParallels * KineticParallelMultiblockMachine.SU_PER_PARALLEL;
            if (kineticMachine.getAvailableSU() < requiredSU) {
                setWaiting(Component.translatable("astrogreg.machine.kinetic_machine.no_su"));
                return;
            }
        }
        super.handleRecipeWorking();
    }

    @Override
    protected void regressRecipe() {}

    @Override
    public void onRecipeFinish() {
        machine.afterWorking();
        if (lastRecipe != null) {
            runAttempt = 0;
            runDelay = 0;
            consecutiveRecipes++;
            handleRecipeIO(lastRecipe, IO.OUT);
            if (suspendAfterFinish) {
                setStatus(Status.SUSPEND);
                consecutiveRecipes = 0;
                progress = 0;
                duration = 0;
                isActive = false;
                lastRecipe = null;
                return;
            }
            if (machine.alwaysTryModifyRecipe() && lastOriginRecipe != null) {
                var modified = machine.fullModifyRecipe(lastOriginRecipe.copy());
                lastRecipe = modified != null ? stripEU(modified) : null;
                if (modified == null) markLastRecipeDirty();
            }
            var recipeCheck = lastRecipe != null ? checkRecipe(lastRecipe) : ActionResult.FAIL_NO_REASON;
            if (!recipeDirty && recipeCheck.isSuccess()) {
                setupRecipe(lastRecipe);
            } else {
                setStatus(Status.IDLE);
                consecutiveRecipes = 0;
                progress = 0;
                duration = 0;
                isActive = false;
            }
        }
    }
}
