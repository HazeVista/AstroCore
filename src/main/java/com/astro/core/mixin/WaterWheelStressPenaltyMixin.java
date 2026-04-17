package com.astro.core.mixin;

import com.astro.core.integration.create.WaterWheelDisplaySU;
import net.minecraft.world.level.Level;

import com.astro.core.mixin.accessor.TorquePropagatorAccessor;
import com.simibubi.create.content.kinetics.KineticNetwork;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.waterwheel.WaterWheelBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@SuppressWarnings("all")
@Mixin(value = KineticBlockEntity.class, remap = false)
public abstract class WaterWheelStressPenaltyMixin {

    @Inject(method = "calculateAddedStressCapacity", at = @At("RETURN"), cancellable = true, remap = false)
    private void astrogreg$applyWaterWheelNetworkPenalty(CallbackInfoReturnable<Float> cir) {
        if (!(((Object) this) instanceof WaterWheelBlockEntity self)) return;

        Level level = self.getLevel();
        if (level == null || level.isClientSide()) return;
        if (self.network == null) return;

        Map<Long, KineticNetwork> networks = TorquePropagatorAccessor.getNetworks().get(level);
        if (networks == null) return;

        KineticNetwork kineticNetwork = networks.get(self.network);
        if (kineticNetwork == null) return;

        long wheelCount = kineticNetwork.sources.keySet().stream()
                .filter(be -> be instanceof WaterWheelBlockEntity)
                .count();

        if (wheelCount <= 1) {
            ((WaterWheelDisplaySU) self).astrogreg$setPenaltyFactor(1f);
            return;
        }

        float base = cir.getReturnValue();
        float penalty = (float) Math.pow(0.95, wheelCount - 1);

        ((WaterWheelDisplaySU) self).astrogreg$setPenaltyFactor(penalty);
        cir.setReturnValue((float) Math.floor(base * penalty));
    }
}