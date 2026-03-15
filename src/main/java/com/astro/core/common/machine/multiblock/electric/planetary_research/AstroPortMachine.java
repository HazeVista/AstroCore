package com.astro.core.common.machine.multiblock.electric.planetary_research;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IDisplayUIMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.DataAccessHatchMachine;
import com.gregtechceu.gtceu.utils.ResearchManager;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import com.astro.core.common.data.recipe.AstroRecipeTypes;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class AstroPortMachine extends WorkableElectricMultiblockMachine
                                       implements IDisplayUIMachine {

    public AstroPortMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    public boolean hasPlanetaryResearchData(String requiredPlanetId) {
        for (var part : getParts()) {
            if (!(part instanceof DataAccessHatchMachine hatch)) continue;
            IItemHandler items = hatch.importItems.storage;
            for (int i = 0; i < items.getSlots(); i++) {
                ItemStack stack = items.getStackInSlot(i);
                if (stack.isEmpty()) continue;
                ResearchManager.ResearchItem data = ResearchManager.readResearchId(stack);
                if (data == null) continue;
                if (!requiredPlanetId.equals(data.researchId())) continue;
                if (AstroRecipeTypes.OBSERVATORY_RECIPES.equals(data.recipeType())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void addDisplayText(List<Component> textList) {}

    protected static BlockPos resolveOffset(BlockPos origin, Direction front,
                                            int forward, int up, int lateral) {
        Direction right = front.getClockWise();
        return origin
                .relative(front, forward)
                .above(up)
                .relative(right, lateral);
    }
}
