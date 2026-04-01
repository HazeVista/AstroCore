package com.astro.core.common.machine.multiblock.electric.planetary_research;

import com.gregtechceu.gtceu.api.capability.IOpticalComputationProvider;
import com.gregtechceu.gtceu.api.capability.IOpticalComputationReceiver;
import com.gregtechceu.gtceu.api.capability.forge.GTCapability;
import com.gregtechceu.gtceu.api.capability.recipe.CWURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IDisplayUIMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockDisplayText;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.ActionResult;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import com.astro.core.common.data.recipe.AstroRecipeTypes;
import com.astro.core.common.machine.part.CWUInputHatch;
import com.astro.core.common.machine.part.ObservatoryObjectHolderMachine;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("all")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class ObservatoryMachine extends WorkableElectricMultiblockMachine
                                         implements IOpticalComputationReceiver, IDisplayUIMachine {

    @Getter
    private IOpticalComputationProvider computationProvider;

    @Getter
    private ObservatoryObjectHolderMachine dataHolder;

    public ObservatoryMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    protected RecipeLogic createRecipeLogic(Object... args) {
        return new ObservatoryRecipeLogic(this);
    }

    @Override
    public ObservatoryRecipeLogic getRecipeLogic() {
        return (ObservatoryRecipeLogic) super.getRecipeLogic();
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        for (IMultiPart part : getParts()) {
            if (part instanceof ObservatoryObjectHolderMachine observatoryHolder) {
                this.dataHolder = observatoryHolder;
            }
            if (this.computationProvider == null) {
                part.self().holder.self()
                        .getCapability(GTCapability.CAPABILITY_COMPUTATION_PROVIDER)
                        .ifPresent(provider -> this.computationProvider = provider);
            }
            if (this.computationProvider == null && part instanceof CWUInputHatch cwuHatch) {
                this.computationProvider = cwuHatch.computationTrait;
            }
        }
        if (computationProvider == null || dataHolder == null) {
            onStructureInvalid();
        }
    }

    @Override
    public void onStructureInvalid() {
        computationProvider = null;
        if (dataHolder != null) {
            dataHolder.setLocked(false);
        }
        dataHolder = null;
        super.onStructureInvalid();
    }

    @Override
    public boolean regressWhenWaiting() {
        return false;
    }

    protected void addExtraDisplayInfo(List<Component> tl) {}

    @Override
    public void addDisplayText(List<Component> textList) {
        MultiblockDisplayText.builder(textList, isFormed())
                .setWorkingStatus(recipeLogic.isWorkingEnabled(), recipeLogic.isActive())
                .setWorkingStatusKeys(
                        "gtceu.multiblock.idling",
                        "gtceu.multiblock.work_paused",
                        "gtceu.multiblock.research_station.researching")
                .addEnergyUsageLine(energyContainer)
                .addCustom(tl -> addExtraDisplayInfo(tl))
                .addProgressLineOnlyPercent(recipeLogic.getProgressPercent())
                .addWorkingStatusLine();
    }

    public static class ObservatoryRecipeLogic extends RecipeLogic {

        public ObservatoryRecipeLogic(ObservatoryMachine machine) {
            super(machine);
        }

        @NotNull
        @Override
        public ObservatoryMachine getMachine() {
            return (ObservatoryMachine) super.getMachine();
        }

        @Override
        protected ActionResult matchRecipe(GTRecipe recipe) {
            var match = matchRecipeNoOutput(recipe);
            if (!match.isSuccess()) return match;
            return matchTickRecipeNoOutput(recipe);
        }

        @Override
        public boolean checkMatchedRecipeAvailable(GTRecipe match) {
            var modified = machine.fullModifyRecipe(match);
            if (modified != null) {
                if (!modified.inputs.containsKey(CWURecipeCapability.CAP) &&
                        !modified.tickInputs.containsKey(CWURecipeCapability.CAP)) {
                    return true;
                }
                var recipeMatch = checkRecipe(modified);
                if (recipeMatch.isSuccess()) {
                    setupRecipe(modified);
                } else {
                    setWaiting(recipeMatch.reason());
                }
                if (lastRecipe != null && getStatus() == Status.WORKING) {
                    lastOriginRecipe = match;
                    lastFailedMatches = null;
                    return true;
                }
            }
            return false;
        }

        protected ActionResult matchRecipeNoOutput(GTRecipe recipe) {
            if (!machine.hasCapabilityProxies()) return ActionResult.FAIL_NO_CAPABILITIES;
            return RecipeHelper.handleRecipe(machine, recipe, IO.IN, recipe.inputs,
                    Collections.emptyMap(), false, true);
        }

        protected ActionResult matchTickRecipeNoOutput(GTRecipe recipe) {
            if (recipe.hasTick()) {
                if (!machine.hasCapabilityProxies()) return ActionResult.FAIL_NO_CAPABILITIES;
                return RecipeHelper.handleRecipe(machine, recipe, IO.IN, recipe.tickInputs,
                        Collections.emptyMap(), false, true);
            }
            return ActionResult.SUCCESS;
        }

        @Override
        protected ActionResult handleRecipeIO(GTRecipe recipe, IO io) {
            ObservatoryObjectHolderMachine holder = getMachine().getDataHolder();

            if (io == IO.IN) {
                if (holder != null) holder.setLocked(true);
                return ActionResult.SUCCESS;
            }

            if (holder == null) return ActionResult.SUCCESS;

            if (lastRecipe == null) {
                holder.setLocked(false);
                return ActionResult.SUCCESS;
            }

            holder.setHeldItem(ItemStack.EMPTY);
            ItemStack outputItem = ItemStack.EMPTY;
            var contents = lastRecipe.getOutputContents(ItemRecipeCapability.CAP);
            if (!contents.isEmpty()) {
                outputItem = ItemRecipeCapability.CAP.of(contents.get(contents.size() - 1).content).getItems()[0];
            }
            if (!outputItem.isEmpty()) {
                String planetItemId = lastRecipe.data.getString(AstroRecipeTypes.OBSERVATORY_PLANET_ITEM_KEY);
                if (!planetItemId.isEmpty()) {
                    outputItem.getOrCreateTag().putString(AstroRecipeTypes.OBSERVATORY_PLANET_ITEM_KEY, planetItemId);
                }
                holder.setDataItem(outputItem.copy());
            }
            holder.setLocked(false);
            return ActionResult.SUCCESS;
        }

        @Override
        protected ActionResult handleTickRecipeIO(GTRecipe recipe, IO io) {
            if (io != IO.OUT) {
                return super.handleTickRecipeIO(recipe, io);
            }
            return ActionResult.SUCCESS;
        }

        @Override
        public void onRecipeFinish() {
            super.onRecipeFinish();
            ObservatoryObjectHolderMachine holder = getMachine().getDataHolder();
            if (holder != null) holder.setLocked(false);
        }

        @Override
        public void interruptRecipe() {
            super.interruptRecipe();
            ObservatoryObjectHolderMachine holder = getMachine().getDataHolder();
            if (holder != null) holder.setLocked(false);
        }
    }
}
