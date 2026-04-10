package com.astro.core.common.data.worldgen;

import com.astro.core.AstroCore;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("all")
@Mod.EventBusSubscriber(modid = AstroCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstroBiomes {

    public static final DeferredRegister<Biome> BIOMES =
            DeferredRegister.create(Registries.BIOME, AstroCore.MOD_ID);

    //  Pluto biomes
    public static final RegistryObject<Biome> PLUTONIAN_MOUNTAINS =
            BIOMES.register("plutonian_mountains", AstroBiomes::makePlutoBiome);

    public static final RegistryObject<Biome> PLUTONIAN_ROCKY_PLAINS =
            BIOMES.register("plutonian_rocky_plains", AstroBiomes::makePlutoBiome);

    public static final RegistryObject<Biome> PLUTONIAN_DEEP_CANYONS =
            BIOMES.register("plutonian_deep_canyons", AstroBiomes::makePlutoBiome);

    public static final RegistryObject<Biome> PLUTONIAN_MUSHROOM_FOREST =
            BIOMES.register("plutonian_mushroom_forest", AstroBiomes::makePlutoBiome);

    public static final RegistryObject<Biome> PLUTO_ICE_RIVER =
            BIOMES.register("pluto_ice_river", AstroBiomes::makePlutoBiome);

    public static final ResourceKey<Biome> KEY_PLUTONIAN_MOUNTAINS      = key("plutonian_mountains");
    public static final ResourceKey<Biome> KEY_PLUTONIAN_ROCKY_PLAINS   = key("plutonian_rocky_plains");
    public static final ResourceKey<Biome> KEY_PLUTONIAN_DEEP_CANYONS   = key("plutonian_deep_canyons");
    public static final ResourceKey<Biome> KEY_PLUTONIAN_MUSHROOM_FOREST = key("plutonian_mushroom_forest");
    public static final ResourceKey<Biome> KEY_PLUTO_ICE_RIVER          = key("pluto_ice_river");

    //  Helpers
    private static ResourceKey<Biome> key(String name) {
        return ResourceKey.create(Registries.BIOME, new ResourceLocation(AstroCore.MOD_ID, name));
    }

    private static Biome makePlutoBiome() {
        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(-2.0f)
                .downfall(0.0f)
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .skyColor(0x7A7264)
                        .fogColor(0x6B636D)
                        .waterColor(0x5F5F5F)
                        .waterFogColor(0x50503A)
                        .grassColorOverride(0x979365)
                        .foliageColorOverride(0x979365)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .build())
                .mobSpawnSettings(new MobSpawnSettings.Builder().build())
                .generationSettings(BiomeGenerationSettings.EMPTY)
                .build();
    }

    public static void init() {
    }
}
