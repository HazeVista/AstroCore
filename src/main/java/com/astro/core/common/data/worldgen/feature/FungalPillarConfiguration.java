package com.astro.core.common.data.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraftforge.registries.ForgeRegistries;

public record FungalPillarConfiguration(Block block, IntProvider reach, IntProvider height) implements FeatureConfiguration {

    public static final Codec<FungalPillarConfiguration> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ForgeRegistries.BLOCKS.getCodec()
                            .fieldOf("block")
                            .forGetter(FungalPillarConfiguration::block),
                    IntProvider.CODEC
                            .fieldOf("reach")
                            .forGetter(FungalPillarConfiguration::reach),
                    IntProvider.CODEC
                            .fieldOf("height")
                            .forGetter(FungalPillarConfiguration::height)
            ).apply(instance, FungalPillarConfiguration::new)
    );

    public BlockState blockState() {
        return block.defaultBlockState();
    }
}