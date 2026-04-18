package com.astro.core.mixin.accessor;

import net.minecraft.world.level.LevelAccessor;

import com.simibubi.create.content.kinetics.KineticNetwork;
import com.simibubi.create.content.kinetics.TorquePropagator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(value = TorquePropagator.class, remap = false)
public interface TorquePropagatorAccessor {

    @Accessor(value = "networks", remap = false)
    static Map<LevelAccessor, Map<Long, KineticNetwork>> getNetworks() {
        throw new AssertionError();
    }
}
