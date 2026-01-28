package com.astro.core.common.machine.hatches;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.common.machine.multiblock.part.FluidHatchPartMachine;

import net.minecraftforge.fluids.FluidType;

import com.astro.core.common.data.tag.AstroTags;

@SuppressWarnings("all")
public class AstroManaHatches extends FluidHatchPartMachine {

    public static final int INITIAL_TANK_CAPACITY = 8 * FluidType.BUCKET_VOLUME;

    public AstroManaHatches(IMachineBlockEntity holder, int tier, Object... args) {
        this(holder, tier, IO.IN, args);
    }

    public AstroManaHatches(IMachineBlockEntity holder, int tier, IO io, Object... args) {
        super(holder, tier, io, INITIAL_TANK_CAPACITY, 1, args);
    }

    public static int getTankCapacity(int tier) {
        return INITIAL_TANK_CAPACITY;
    }

    @Override
    protected NotifiableFluidTank createTank(int initialCapacity, int slots, Object... args) {
        int tierCapacity = getTankCapacity(this.getTier());
        return super.createTank(tierCapacity, slots)
                .setFilter(fluidStack -> fluidStack.getFluid().is(AstroTags.EXOTIC_MATTER));
    }
}
