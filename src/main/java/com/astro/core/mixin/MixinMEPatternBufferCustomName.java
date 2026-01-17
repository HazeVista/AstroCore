package com.astro.core.mixin;

import com.gregtechceu.gtceu.integration.ae2.machine.MEPatternBufferPartMachine;

import com.astro.core.api.CustomNameAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = MEPatternBufferPartMachine.class, remap = false)
public abstract class MixinMEPatternBufferCustomName implements CustomNameAccess {

    @Shadow(remap = false)
    private String customName;

    @Override
    public String astro$getCustomName() {
        return this.customName;
    }
}
