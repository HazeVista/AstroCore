package com.astro.core.integration.jade;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import net.minecraft.world.level.block.Block;

import com.astro.core.integration.jade.provider.*;
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
        registration.registerBlockDataProvider(new KineticSteamEngineProvider(), MetaMachineBlockEntity.class);
        registration.registerBlockDataProvider(new KineticCombustionEngineProvider(), MetaMachineBlockEntity.class);
        registration.registerBlockDataProvider(new KineticAlternatorProvider(), MetaMachineBlockEntity.class);
        registration.registerBlockDataProvider(new ObservatoryProvider(), MetaMachineBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(new ProcessingCoreProvider(), Block.class);
        registration.registerBlockComponent(new SolarBoilerProvider(), Block.class);
        registration.registerBlockComponent(new FaradayGeneratorProvider(), Block.class);
        registration.registerBlockComponent(new KineticSteamEngineProvider(), Block.class);
        registration.registerBlockComponent(new KineticCombustionEngineProvider(), Block.class);
        registration.registerBlockComponent(new KineticAlternatorProvider(), Block.class);
        registration.registerBlockComponent(new ObservatoryProvider(), Block.class);
    }
}
