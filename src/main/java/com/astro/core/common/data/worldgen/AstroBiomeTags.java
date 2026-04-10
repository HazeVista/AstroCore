package com.astro.core.common.data.worldgen;

import com.astro.core.AstroCore;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

@SuppressWarnings("all")
public class AstroBiomeTags {

    public static final TagKey<Biome> IS_PLUTO = create("is_pluto");

    private static TagKey<Biome> create(String name) {
        return TagKey.create(Registries.BIOME, new ResourceLocation(AstroCore.MOD_ID, name));
    }
}
