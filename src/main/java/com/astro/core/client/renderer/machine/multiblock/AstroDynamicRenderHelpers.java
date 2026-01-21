package com.astro.core.client.renderer.machine.multiblock;

import com.gregtechceu.gtceu.client.renderer.machine.DynamicRender;

import com.astro.core.client.renderer.machine.AstroFluidRender;

public class AstroDynamicRenderHelpers {

    public static DynamicRender<?, ?> getCustomFluidRenderer() {
        return AstroFluidRender.INSTANCE;
    }
}
