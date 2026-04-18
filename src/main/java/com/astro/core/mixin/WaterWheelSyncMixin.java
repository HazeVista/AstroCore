package com.astro.core.mixin;

import net.minecraft.nbt.CompoundTag;

import com.astro.core.integration.create.WaterWheelDisplaySU;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.waterwheel.WaterWheelBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("all")
@Mixin(value = KineticBlockEntity.class, remap = false)
public abstract class WaterWheelSyncMixin {

    @Inject(method = "write", at = @At("TAIL"), remap = false)
    private void astrogreg$writePenalizedCapacity(CompoundTag tag, boolean clientPacket, CallbackInfo ci) {
        if (!(((Object) this) instanceof WaterWheelBlockEntity self)) return;
        tag.putFloat("AstroWheelPenalty", ((WaterWheelDisplaySU) self).astrogreg$getPenaltyFactor());
    }

    @Inject(method = "read", at = @At("TAIL"), remap = false)
    private void astrogreg$readPenalizedCapacity(CompoundTag tag, boolean clientPacket, CallbackInfo ci) {
        if (!(((Object) this) instanceof WaterWheelBlockEntity self)) return;
        if (tag.contains("AstroWheelPenalty")) {
            ((WaterWheelDisplaySU) self).astrogreg$setPenaltyFactor(tag.getFloat("AstroWheelPenalty"));
        }
    }
}
