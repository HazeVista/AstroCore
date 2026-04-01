package com.astro.core.common.machine.trait.miner;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.common.machine.trait.miner.LargeMinerLogic;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;

import java.util.List;

public class AstroMinerLogic extends LargeMinerLogic {

    public AstroMinerLogic(IRecipeLogicMachine machine, int fortune, int speed, int maximumRadius) {
        super(machine, fortune, speed, maximumRadius);
    }

    @Override
    protected void dropPostProcessing(NonNullList<ItemStack> blockDrops, List<ItemStack> outputs,
                                      BlockState blockState, LootParams.Builder builder) {
        for (ItemStack stack : outputs) {
            if (ChemicalHelper.getPrefix(stack.getItem()) == TagPrefix.crushed) {
                blockDrops.add(stack);
            }
        }
    }
}
