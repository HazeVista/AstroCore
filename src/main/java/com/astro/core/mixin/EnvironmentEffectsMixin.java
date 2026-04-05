package com.astro.core.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

import com.astro.core.common.data.tag.AstroBlockTags;
import earth.terrarium.adastra.common.systems.EnvironmentEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EnvironmentEffects.class, remap = false)
public class EnvironmentEffectsMixin {

    @Inject(method = "tickBlock", at = @At("HEAD"), cancellable = true)
    private static void astro$survives_tickBlock(ServerLevel level, BlockPos pos, BlockState state, CallbackInfo ci) {
        if (state.is(AstroBlockTags.SURVIVES_IN_SPACE)) {
            ci.cancel();
        }
    }

    @Inject(method = "tickHot", at = @At("HEAD"), cancellable = true)
    private static void astro$survives_tickHot(ServerLevel level, BlockPos pos, BlockState state, CallbackInfo ci) {
        if (state.is(AstroBlockTags.SURVIVES_IN_SPACE)) {
            ci.cancel();
        }
    }

    @Inject(method = "tickCold", at = @At("HEAD"), cancellable = true)
    private static void astro$survives_tickCold(ServerLevel level, BlockPos pos, BlockState state, CallbackInfo ci) {
        if (state.is(AstroBlockTags.SURVIVES_IN_SPACE)) {
            ci.cancel();
        }
    }
}
