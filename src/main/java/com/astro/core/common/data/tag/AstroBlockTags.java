package com.astro.core.common.data.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import com.astro.core.AstroCore;

public class AstroBlockTags {

    public static final TagKey<Block> SURVIVES_IN_SPACE = TagKey.create(
            Registries.BLOCK,
            AstroCore.id("survives_in_space"));
}
