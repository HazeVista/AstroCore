package com.astro.core.common.machine.hatches;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.MethodsReturnNonnullByDefault;

import com.astro.core.common.machine.trait.create.AstroKineticPartMachine;
import com.astro.core.integration.create.AstroNotifiableStressTrait;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("all")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class KineticOutputHatch extends AstroKineticPartMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            KineticOutputHatch.class, AstroKineticPartMachine.MANAGED_FIELD_HOLDER);

    public static final float SU_PER_LAYER = 25_000f;

    public KineticOutputHatch(IMachineBlockEntity holder, int tier, Object... args) {
        super(holder, tier, IO.OUT, args);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    protected AstroNotifiableStressTrait createStressTrait(Object... args) {
        return new AstroNotifiableStressTrait(this, IO.OUT, IO.OUT);
    }

    public void setOutputSU(float su, float capacityPerRPM) {
        getKineticHolder().scheduleWorking(su, capacityPerRPM);
    }
}
