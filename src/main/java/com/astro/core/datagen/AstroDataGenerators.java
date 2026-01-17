package com.astro.core.datagen;

import com.gregtechceu.gtceu.api.registry.registrate.SoundEntryBuilder;

import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.astro.core.AstroCore;

@Mod.EventBusSubscriber(modid = AstroCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstroDataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();

        if (event.includeClient()) {
            event.getGenerator().addProvider(
                    true,
                    new SoundEntryBuilder.SoundEntryProvider(packOutput, AstroCore.MOD_ID));
        }
    }
}
