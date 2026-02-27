package com.astro.core.mixin;

import com.gregtechceu.gtceu.common.data.GTMaterials;

import earth.terrarium.adastra.common.entities.vehicles.Rocket;
import earth.terrarium.botarium.common.fluid.FluidConstants;
import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import earth.terrarium.botarium.common.fluid.impl.SimpleFluidContainer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.BiPredicate;

@Mixin(value = Rocket.class, remap = false)
public abstract class RocketFuelMixin {

    @Shadow
    @Final
    private SimpleFluidContainer fluidContainer;

    @Redirect(
              method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;Learth/terrarium/adastra/common/entities/vehicles/Rocket$RocketProperties;)V",
              at = @At(value = "NEW", target = "earth/terrarium/botarium/common/fluid/impl/SimpleFluidContainer"))
    private SimpleFluidContainer astro$redirectFluidContainer(long capacity, int slots,
                                                              BiPredicate<Long, FluidHolder> validator) {
        return new SimpleFluidContainer(
                FluidConstants.fromMillibuckets(3000),
                1,
                (amount, fluid) -> fluid.getFluid() == GTMaterials.RocketFuel.getFluid());
    }

    @Overwrite
    public boolean consumeFuel(boolean simulate) {
        if (((Rocket) (Object) this).level().isClientSide()) return false;
        long buckets = FluidConstants.fromMillibuckets(3000L);
        return fluidContainer.extractFluid(
                fluidContainer.getFirstFluid().copyWithAmount(buckets), simulate).getFluidAmount() >= buckets;
    }
}
