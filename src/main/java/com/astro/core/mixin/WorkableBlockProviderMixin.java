package com.astro.core.mixin;

import com.gregtechceu.gtceu.integration.jade.provider.WorkableBlockProvider;

import net.minecraft.nbt.CompoundTag;

import com.astro.core.common.machine.multiblock.electric.planetary_research.ObservatoryMachine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorkableBlockProvider.class)
public class WorkableBlockProviderMixin {

    @Inject(method = "write(Lnet/minecraft/nbt/CompoundTag;Ljava/lang/Object;)V", at = @At("TAIL"), remap = false)
    private void astro$markObservatoryAsResearch(CompoundTag data, Object capability, CallbackInfo ci) {
        if (capability instanceof ObservatoryMachine) {
            data.putBoolean("Research", true);
        }
    }
}
