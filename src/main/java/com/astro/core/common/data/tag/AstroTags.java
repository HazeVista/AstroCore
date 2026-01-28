package com.astro.core.common.data.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

@SuppressWarnings("all")
public class AstroTags {

    public static final TagKey<Fluid> EXOTIC_MATTER = TagKey.create(Registries.FLUID,
            new ResourceLocation("forge", "exotic_matter"));
}
