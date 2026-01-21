package com.astro.core.client.renderer.machine.multiblock;

import com.astro.core.client.renderer.machine.AstroFluidRender;
import com.gregtechceu.gtceu.client.renderer.machine.DynamicRender;

public class AstroDynamicRenderHelpers {

    public static DynamicRender<?, ?> getCustomFluidRenderer() {
        return AstroFluidRender.INSTANCE;
    }

}
