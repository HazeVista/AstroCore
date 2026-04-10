package com.astro.core.common.data.worldgen;

import com.astro.core.AstroCore;
import com.astro.core.common.data.worldgen.feature.FungalPillarConfiguration;
import com.astro.core.common.data.worldgen.feature.FungalPillarFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AstroFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(ForgeRegistries.FEATURES, AstroCore.MOD_ID);

    public static final RegistryObject<Feature<FungalPillarConfiguration>> FUNGAL_PILLAR =
            FEATURES.register("fungal_pillar",
                    () -> new FungalPillarFeature(FungalPillarConfiguration.CODEC));

    public static void init() {}
}