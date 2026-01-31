package com.astro.core.client;

import com.gregtechceu.gtceu.client.renderer.machine.DynamicRenderManager;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;

import com.astro.core.AstroCore;
import com.astro.core.client.renderer.machine.AstroBoilerMultiPartRender;
import com.astro.core.client.renderer.machine.AstroFluidRender;

@SuppressWarnings("all")
@Mod.EventBusSubscriber(modid = AstroCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class AstroClient {

    private AstroClient() {}

    public static void init(IEventBus modBus) {
        DynamicRenderManager.register(AstroCore.id("astro_fluid_render"), AstroFluidRender.TYPE);
        DynamicRenderManager.register(AstroCore.id("astro_boiler_render"), AstroBoilerMultiPartRender.TYPE);
    }
}
