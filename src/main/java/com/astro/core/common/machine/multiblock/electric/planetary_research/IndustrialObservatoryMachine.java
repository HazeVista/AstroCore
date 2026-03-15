package com.astro.core.common.machine.multiblock.electric.planetary_research;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockDisplayText;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.astro.core.common.data.AstroBlocks.*;

public class IndustrialObservatoryMachine extends ObservatoryMachine {

    private int coreTier = -1;

    public IndustrialObservatoryMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        scanCores();
    }

    @Override
    public void onStructureInvalid() {
        coreTier = -1;
        super.onStructureInvalid();
    }

    private void scanCores() {
        coreTier = -1;
        var level = getLevel();
        if (level == null) return;
        for (var pos : getMultiblockState().getCache()) {
            Block block = level.getBlockState(pos).getBlock();
            if (block == INDUSTRIAL_PROCESSING_CORE_MK3.get()) {
                coreTier = GTValues.IV;
                return;
            } else if (block == INDUSTRIAL_PROCESSING_CORE_MK2.get()) {
                coreTier = GTValues.EV;
                return;
            } else if (block == INDUSTRIAL_PROCESSING_CORE_MK1.get()) {
                coreTier = GTValues.HV;
                return;
            }
        }
    }

    public static @NotNull ModifierFunction recipeModifier(@NotNull MetaMachine machine,
                                                           @NotNull GTRecipe recipe) {
        if (!(machine instanceof IndustrialObservatoryMachine obs)) {
            return ModifierFunction.IDENTITY;
        }
        if (obs.coreTier == -1) return ModifierFunction.IDENTITY;
        if (RecipeHelper.getRecipeEUtTier(recipe) > obs.coreTier) return ModifierFunction.IDENTITY;
        long maxVoltage = GTValues.V[obs.coreTier];
        long overclockVoltage = Math.min(obs.getOverclockVoltage(), maxVoltage);
        return OverclockingLogic.PERFECT_OVERCLOCK_SUBTICK.getModifier(machine, recipe, overclockVoltage);
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        MultiblockDisplayText.builder(textList, isFormed())
                .setWorkingStatus(recipeLogic.isWorkingEnabled(), recipeLogic.isActive())
                .addCustom(tl -> {
                    if (isFormed() && coreTier != -1) {
                        tl.add(Component.translatable("astrogreg.machine.processing_core.core",
                                GTValues.VNF[coreTier])
                                .withStyle(ChatFormatting.AQUA));
                    }
                })
                .setWorkingStatusKeys(
                        "gtceu.multiblock.idling",
                        "gtceu.multiblock.work_paused",
                        "gtceu.multiblock.research_station.researching")
                .addEnergyUsageLine(energyContainer)

                .addWorkingStatusLine()
                .addProgressLineOnlyPercent(recipeLogic.getProgressPercent());
    }
}
