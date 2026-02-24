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
public class KineticInputHatch extends AstroKineticPartMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            KineticInputHatch.class, AstroKineticPartMachine.MANAGED_FIELD_HOLDER);

    // 1024 SU per parallel @ 256su, max 8 parallels = 8192 SU max consumption
    public static final float SU_PER_PARALLEL = 1024f;
    public static final float REQUIRED_RPM = 32f;
    public static final int MAX_PARALLELS = 8;

    public KineticInputHatch(IMachineBlockEntity holder, int tier, Object... args) {
        super(holder, tier, IO.IN, args);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    protected AstroNotifiableStressTrait createStressTrait(Object... args) {
        return new AstroNotifiableStressTrait(this, IO.IN, IO.IN);
    }

    /**
     * Returns how many parallels the current stress input can support.
     * Requires 32 RPM and 1024 SU per parallel.
     */
    public int getAvailableParallels() {
        float speed = Math.abs(getKineticHolder().getSpeed());
        if (speed < REQUIRED_RPM) return 0;
        float availableSU = speed * getKineticDefinition().getTorque();
        return (int) Math.min(MAX_PARALLELS, Math.floor(availableSU / SU_PER_PARALLEL));
    }
}
