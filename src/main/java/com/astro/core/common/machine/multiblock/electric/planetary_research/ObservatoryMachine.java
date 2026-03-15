package com.astro.core.common.machine.multiblock.electric.planetary_research;

import com.gregtechceu.gtceu.api.capability.IOpticalComputationProvider;
import com.gregtechceu.gtceu.api.capability.IOpticalComputationReceiver;
import com.gregtechceu.gtceu.api.capability.forge.GTCapability;
import com.gregtechceu.gtceu.api.capability.recipe.CWURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IDisplayUIMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.ActionResult;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.utils.ResearchManager;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import com.astro.core.common.data.item.AstroPlanetaryDataItem;
import com.astro.core.common.data.recipe.AstroRecipeTypes;
import com.astro.core.common.machine.part.ObservatoryObjectHolderMachine;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

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
            part.self().holder.self()
                    .getCapability(GTCapability.CAPABILITY_COMPUTATION_PROVIDER)
                    .ifPresent(provider -> this.computationProvider = provider);
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

    @Override
    public void addDisplayText(List<Component> textList) {}

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

            CompoundTag data = lastRecipe.data;
            if (data != null && data.contains(AstroPlanetaryDataItem.NBT_PLANET_ID)) {
                String planetId = data.getString(AstroPlanetaryDataItem.NBT_PLANET_ID);

                holder.setHeldItem(ItemStack.EMPTY);

                ItemStack dataItem = holder.getDataItem(false);
                if (!dataItem.isEmpty()) {
                    CompoundTag tag = dataItem.getOrCreateTag();
                    ResearchManager.writeResearchToNBT(tag, planetId, AstroRecipeTypes.OBSERVATORY_RECIPES);
                    tag.putString(AstroPlanetaryDataItem.NBT_PLANET_ID, planetId);
                    holder.setDataItem(dataItem);
                }
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
    }
}
