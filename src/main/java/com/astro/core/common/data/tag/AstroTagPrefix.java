package com.astro.core.common.data.tag;

import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialStack;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.ForgeRegistries;

import com.astro.core.AstroCore;
import com.astro.core.common.data.materials.AstroMaterials;
import com.drd.ad_extendra.common.registry.ModBlocks;

import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.dust;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.oreTagPrefix;

@SuppressWarnings("all")
public class AstroTagPrefix {

    public static final TagPrefix oreAsteroidStone = oreTagPrefix("asteroid_stone", BlockTags.MINEABLE_WITH_PICKAXE)
            .langValue("Asteroid Stone %s Ore")
            .registerOre(
                    () -> ForgeRegistries.BLOCKS.getValue(new ResourceLocation("asteroid_stone")).defaultBlockState(),
                    () -> AstroMaterials.ASTEROID_STONE,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.TERRACOTTA_PURPLE)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.STONE)
                            .strength(3.0F, 3.0F),
                    AstroCore.id("block/asteroid_stone"),
                    false, false, true);

    public static final TagPrefix oreHardAsteroidStone = oreTagPrefix("hard_asteroid_stone",
            BlockTags.MINEABLE_WITH_PICKAXE)
            .langValue("Hard Asteroid Stone %s Ore")
            .registerOre(
                    () -> ForgeRegistries.BLOCKS.getValue(new ResourceLocation("asteroid_stone")).defaultBlockState(),
                    () -> AstroMaterials.ASTEROID_STONE,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.TERRACOTTA_PURPLE)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.STONE)
                            .strength(3.8F, 3.8F),
                    AstroCore.id("block/hard_asteroid_stone"),
                    true, false, true);

    public static final TagPrefix orePlutoStone = oreTagPrefix("pluto_stone", BlockTags.MINEABLE_WITH_PICKAXE)
            .langValue("Pluto Stone %s Ore")
            .registerOre(
                    () -> ModBlocks.PLUTO_STONE.get().defaultBlockState(), () -> AstroMaterials.PLUTO_STONE,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.TERRACOTTA_ORANGE)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.STONE)
                            .strength(3.0F, 3.0F),
                    new ResourceLocation("ad_extendra", "block/pluto_stone"),
                    false, false, true);

    public static final TagPrefix oreNeptuneStone = oreTagPrefix("neptune_stone", BlockTags.MINEABLE_WITH_PICKAXE)
            .langValue("Neptune Stone %s Ore")
            .registerOre(
                    () -> ModBlocks.NEPTUNE_STONE.get().defaultBlockState(), () -> AstroMaterials.NEPTUNE_STONE,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.TERRACOTTA_BLUE)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.STONE)
                            .strength(3.0F, 3.0F),
                    new ResourceLocation("ad_extendra", "block/neptune_stone"),
                    false, false, true);

    public static final TagPrefix oreUranusStone = oreTagPrefix("uranus_stone", BlockTags.MINEABLE_WITH_PICKAXE)
            .langValue("Uranus Stone %s Ore")
            .registerOre(
                    () -> ModBlocks.URANUS_STONE.get().defaultBlockState(), () -> AstroMaterials.URANUS_STONE,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.TERRACOTTA_LIGHT_BLUE)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.STONE)
                            .strength(3.0F, 3.0F),
                    new ResourceLocation("ad_extendra", "block/uranus_stone"),
                    false, false, true);

    public static final TagPrefix oreSaturnStone = oreTagPrefix("saturn_stone", BlockTags.MINEABLE_WITH_PICKAXE)
            .langValue("Saturn Stone %s Ore")
            .registerOre(
                    () -> ModBlocks.SATURN_STONE.get().defaultBlockState(), () -> AstroMaterials.SATURN_STONE,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.TERRACOTTA_BROWN)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.STONE)
                            .strength(3.0F, 3.0F),
                    new ResourceLocation("ad_extendra", "block/saturn_stone"),
                    false, false, true);

    public static final TagPrefix oreJupiterStone = oreTagPrefix("jupiter_stone", BlockTags.MINEABLE_WITH_PICKAXE)
            .langValue("Jupiter Stone %s Ore")
            .registerOre(
                    () -> ModBlocks.JUPITER_STONE.get().defaultBlockState(), () -> AstroMaterials.JUPITER_STONE,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.TERRACOTTA_ORANGE)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.STONE)
                            .strength(3.0F, 3.0F),
                    new ResourceLocation("ad_extendra", "block/jupiter_stone"),
                    false, false, true);

    public static final TagPrefix oreCeresStone = oreTagPrefix("ceres_stone", BlockTags.MINEABLE_WITH_PICKAXE)
            .langValue("Ceres Stone %s Ore")
            .registerOre(
                    () -> ModBlocks.CERES_STONE.get().defaultBlockState(), () -> AstroMaterials.CERES_STONE,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.STONE)
                            .strength(3.0F, 3.0F),
                    new ResourceLocation("ad_extendra", "block/ceres_stone"),
                    false, false, true);

    public static final TagPrefix oreMarsStone = oreTagPrefix("mars_stone", BlockTags.MINEABLE_WITH_PICKAXE)
            .langValue("Mars Stone %s Ore")
            .registerOre(
                    () -> ForgeRegistries.BLOCKS.getValue(new ResourceLocation("ad_astra", "mars_stone"))
                            .defaultBlockState(),
                    () -> AstroMaterials.MARS_STONE, BlockBehaviour.Properties.of()
                            .mapColor(MapColor.TERRACOTTA_RED)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.STONE)
                            .strength(3.0F, 3.0F),
                    new ResourceLocation("ad_astra", "block/mars_stone"),
                    false, false, true);

    public static final TagPrefix oreMoonStone = oreTagPrefix("moon_stone", BlockTags.MINEABLE_WITH_PICKAXE)
            .langValue("Moon Stone %s Ore")
            .registerOre(
                    () -> ForgeRegistries.BLOCKS.getValue(new ResourceLocation("ad_astra", "moon_stone"))
                            .defaultBlockState(),
                    () -> AstroMaterials.MOON_STONE, BlockBehaviour.Properties.of()
                            .mapColor(MapColor.TERRACOTTA_RED)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.STONE)
                            .strength(3.0F, 3.0F),
                    new ResourceLocation("ad_astra", "block/moon_stone"),
                    false, false, true);

    public static final TagPrefix oreVenusStone = oreTagPrefix("venus_stone", BlockTags.MINEABLE_WITH_PICKAXE)
            .langValue("Venus Stone %s Ore")
            .registerOre(
                    () -> ForgeRegistries.BLOCKS.getValue(new ResourceLocation("ad_astra", "venus_stone"))
                            .defaultBlockState(),
                    () -> AstroMaterials.VENUS_STONE, BlockBehaviour.Properties.of()
                            .mapColor(MapColor.TERRACOTTA_RED)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.STONE)
                            .strength(3.0F, 3.0F),
                    new ResourceLocation("ad_astra", "block/venus_stone"),
                    false, false, true);

    public static final TagPrefix oreMercuryStone = oreTagPrefix("mercury_stone", BlockTags.MINEABLE_WITH_PICKAXE)
            .langValue("Mercury Stone %s Ore")
            .registerOre(
                    () -> ForgeRegistries.BLOCKS.getValue(new ResourceLocation("ad_astra", "mercury_stone"))
                            .defaultBlockState(),
                    () -> AstroMaterials.MERCURY_STONE, BlockBehaviour.Properties.of()
                            .mapColor(MapColor.TERRACOTTA_BROWN)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.STONE)
                            .strength(3.0F, 3.0F),
                    new ResourceLocation("ad_astra", "block/mercury_stone"),
                    false, false, true);

    public static void init() {
        oreAsteroidStone.addSecondaryMaterial(new MaterialStack(AstroMaterials.ASTEROID_STONE, dust.materialAmount()));
        oreHardAsteroidStone
                .addSecondaryMaterial(new MaterialStack(AstroMaterials.ASTEROID_STONE, dust.materialAmount()));
        orePlutoStone.addSecondaryMaterial(new MaterialStack(AstroMaterials.PLUTO_STONE, dust.materialAmount()));
        oreNeptuneStone.addSecondaryMaterial(new MaterialStack(AstroMaterials.NEPTUNE_STONE, dust.materialAmount()));
        oreUranusStone.addSecondaryMaterial(new MaterialStack(AstroMaterials.URANUS_STONE, dust.materialAmount()));
        oreSaturnStone.addSecondaryMaterial(new MaterialStack(AstroMaterials.SATURN_STONE, dust.materialAmount()));
        oreJupiterStone.addSecondaryMaterial(new MaterialStack(AstroMaterials.JUPITER_STONE, dust.materialAmount()));
        oreCeresStone.addSecondaryMaterial(new MaterialStack(AstroMaterials.CERES_STONE, dust.materialAmount()));
        oreMarsStone.addSecondaryMaterial(new MaterialStack(AstroMaterials.MARS_STONE, dust.materialAmount()));
        oreMoonStone.addSecondaryMaterial(new MaterialStack(AstroMaterials.MOON_STONE, dust.materialAmount()));
        oreVenusStone.addSecondaryMaterial(new MaterialStack(AstroMaterials.VENUS_STONE, dust.materialAmount()));
        oreMercuryStone.addSecondaryMaterial(new MaterialStack(AstroMaterials.MERCURY_STONE, dust.materialAmount()));
    }
}
