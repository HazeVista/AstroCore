package com.astro.core.mixin;

import com.astro.core.integration.create.WaterWheelDisplaySU;
import com.simibubi.create.content.kinetics.waterwheel.WaterWheelBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = WaterWheelBlockEntity.class, remap = false)
public abstract class WaterWheelFieldMixin implements WaterWheelDisplaySU {
    @Unique
    private float astrogreg_penaltyFactor = 1f;

    @Override
    public float astrogreg$getPenaltyFactor() { return astrogreg_penaltyFactor; }

    @Override
    public void astrogreg$setPenaltyFactor(float value) { astrogreg_penaltyFactor = value; }
}