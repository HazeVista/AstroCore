package com.astro.core.integration.jade;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import net.minecraft.world.level.block.Block;

import com.astro.core.integration.jade.provider.FaradayGeneratorProvider;
import com.astro.core.integration.jade.provider.ProcessingCoreProvider;
import com.astro.core.integration.jade.provider.SolarBoilerProvider;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
@SuppressWarnings("all")
public class AstroJadePlugin implements IWailaPlugin {

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(new ProcessingCoreProvider(), MetaMachineBlockEntity.class);
        registration.registerBlockDataProvider(new SolarBoilerProvider(), MetaMachineBlockEntity.class);
        registration.registerBlockDataProvider(new FaradayGeneratorProvider(), MetaMachineBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(new ProcessingCoreProvider(), Block.class);
        registration.registerBlockComponent(new SolarBoilerProvider(), Block.class);
        registration.registerBlockComponent(new FaradayGeneratorProvider(), Block.class);
    }
}
