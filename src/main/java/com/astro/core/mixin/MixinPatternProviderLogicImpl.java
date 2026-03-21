package com.astro.core.mixin;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IHasCircuitSlot;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;

import appeng.api.crafting.IPatternDetails;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import yuuki1293.pccard.impl.PatternProviderLogicImpl;
import yuuki1293.pccard.wrapper.IAEPattern;

import java.util.List;

@Mixin(value = PatternProviderLogicImpl.class, remap = false)
public class MixinPatternProviderLogicImpl {

    /**
     * @author AstroCore
     * @reason Set the PCCard to ONLY recognize singleblocks, excluding multiblock hatches and buses.
     */
    @Overwrite(remap = false)
    public static void setPCNumber(IPatternDetails patternDetails, BlockEntity be, List<BlockPos> blockPoses) {
        try {
            if (!(patternDetails instanceof IAEPattern patternDetailsW)) return;

            var level = be.getLevel();
            if (level == null) return;

            for (var blockPos : blockPoses) {
                var gtMachine = MetaMachine.getMachine(level, blockPos);
                if (gtMachine == null) continue;

                if (gtMachine instanceof MultiblockPartMachine) continue;

                if (gtMachine instanceof IHasCircuitSlot machine) {
                    var inv = machine.getCircuitInventory();
                    var machineStack = GTItems.PROGRAMMED_CIRCUIT.asStack();
                    IntCircuitBehaviour.setCircuitConfiguration(machineStack, patternDetailsW.pCCard$getNumber());
                    inv.setStackInSlot(0, machineStack);
                }
            }
        } catch (Exception e) {
            // silent fail
        }
    }
}
