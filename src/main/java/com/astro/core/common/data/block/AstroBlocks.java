package com.astro.core.common.data.block;

import com.gregtechceu.gtceu.api.block.ActiveBlock;
import com.gregtechceu.gtceu.common.block.BoilerFireboxType;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.models.GTModels;
import com.gregtechceu.gtceu.data.recipe.CustomTags;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;

import com.astro.core.AstroCore;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;

import static com.astro.core.common.registry.AstroRegistry.REGISTRATE;

@SuppressWarnings("all")
public class AstroBlocks {

    static {
        REGISTRATE.creativeModeTab(() -> AstroCore.ASTRO_CREATIVE_TAB);
    }

    // builders
    private static BlockEntry<Block> createSidedCasingBlock(String name, String id, String texture,
                                                            NonNullBiFunction<Block, Item.Properties, ? extends BlockItem> func) {
        return REGISTRATE
                .block(id, Block::new)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
                .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(),
                        prov.models().cubeAll(ctx.getName(), AstroCore.id("block/" + texture))))
                .tag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
                .lang(name)
                .item(func)
                .build()
                .register();
    }

    private static BlockEntry<ActiveBlock> createFireboxCasing(@SuppressWarnings("SameParameterValue") BoilerFireboxType type,
                                                               String lang) {
        var block = REGISTRATE
                .block("%s_casing".formatted(type.name()), ActiveBlock::new)
                .lang(lang)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
                .addLayer(() -> RenderType::cutoutMipped)
                .blockstate(GTModels.createFireboxModel(type))
                .tag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
                .item(BlockItem::new)
                .build()
                .register();
        GTBlocks.ALL_FIREBOXES.put(type, block);
        return block;
    }

    private static BlockEntry<Block> createStoneBlock(String id, String name, String texture, MapColor mapColor,
                                                      float hardness, float resistance) {
        return REGISTRATE
                .block(id, Block::new)
                .initialProperties(() -> Blocks.STONE)
                .properties(p -> p
                        .mapColor(mapColor)
                        .requiresCorrectToolForDrops()
                        .strength(hardness, resistance)
                        .sound(SoundType.STONE))
                .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(),
                        prov.models().cubeAll(ctx.getName(), AstroCore.id("block/" + texture))))
                .tag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
                .lang(name)
                .item(BlockItem::new)
                .build()
                .register();
    }

    private static BlockEntry<Block> createSolarCasingBlock(String id, String name) {
        return REGISTRATE.block(id, Block::new)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(p -> p
                        .mapColor(MapColor.METAL)
                        .strength(2.0f, 1.0f)
                        .sound(SoundType.METAL)
                        .requiresCorrectToolForDrops()
                        .isValidSpawn((state, level, pos, ent) -> false))
                .addLayer(() -> RenderType::solid)
                .blockstate((ctx, prov) -> {
                    prov.simpleBlock(ctx.get(), prov.models().cube(ctx.getName(),
                            prov.mcLoc("gtceu:block/casings/solid/machine_casing_solid_steel"),
                            prov.modLoc("block/generators/" + id),
                            prov.mcLoc("gtceu:block/casings/solid/machine_casing_solid_steel"),
                            prov.mcLoc("gtceu:block/casings/solid/machine_casing_solid_steel"),
                            prov.mcLoc("gtceu:block/casings/solid/machine_casing_solid_steel"),
                            prov.mcLoc("gtceu:block/casings/solid/machine_casing_solid_steel"))
                            .texture("particle", prov.mcLoc("gtceu:block/casings/solid/machine_casing_solid_steel")));
                })
                .lang(name)
                .tag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
                .item(BlockItem::new)
                .build()
                .register();
    }

    // Block Entries
    // Asteroid Stones
    public static final BlockEntry<Block> ASTEROID_STONE = createStoneBlock(
            "asteroid_stone", "Asteroid Stone", "rocks/asteroid_stone",
            MapColor.TERRACOTTA_PURPLE, 2.0F, 2.0F);
    public static final BlockEntry<Block> HARD_ASTEROID_STONE = createStoneBlock(
            "hard_asteroid_stone", "Hard Asteroid Stone", "rocks/hard_asteroid_stone",
            MapColor.TERRACOTTA_PURPLE, 3.0F, 3.0F);

    // Generator Casings
    public static final BlockEntry<Block> MANASTEEL_MACHINE_CASING = createSidedCasingBlock(
            "§9Manasteel§r-Plated Brick Casing", "manasteel_brick_machine_casing",
            "generators/machine_casing_manasteel_plated_bricks", BlockItem::new);
    public static final BlockEntry<Block> MANASTEEL_PIPE_CASING = createSidedCasingBlock(
            "§9Manasteel§r Pipe Casing", "manasteel_pipe_casing",
            "generators/machine_casing_pipe_manasteel", BlockItem::new);
    public static final BoilerFireboxType MANASTEEL_FIREBOX = new BoilerFireboxType(
            "manasteel_firebox",
            AstroCore.id("block/generators/machine_casing_manasteel_plated_bricks"),
            AstroCore.id("block/generators/machine_casing_manasteel_plated_bricks"),
            AstroCore.id("block/generators/machine_casing_firebox_manasteel"));
    public static final BlockEntry<ActiveBlock> FIREBOX_MANASTEEL = createFireboxCasing(MANASTEEL_FIREBOX,
            "§9Manasteel§r Firebox Casing");

    public static final BlockEntry<Block> TERRASTEEL_MACHINE_CASING = createSidedCasingBlock(
            "§2Terrasteel§r Machine Casing", "solid_terrasteel_machine_casing",
            "generators/terrasteel_casing", BlockItem::new);
    public static final BlockEntry<Block> TERRASTEEL_PIPE_CASING = createSidedCasingBlock(
            "§2Terrasteel§r Pipe Casing", "terrasteel_pipe_casing",
            "generators/machine_casing_pipe_terrasteel", BlockItem::new);
    public static final BoilerFireboxType TERRASTEEL_FIREBOX = new BoilerFireboxType(
            "terrasteel_firebox",
            AstroCore.id("block/generators/machine_casing_solid_terrasteel"),
            AstroCore.id("block/generators/machine_casing_solid_terrasteel"),
            AstroCore.id("block/generators/machine_casing_firebox_terrasteel"));
    public static final BlockEntry<ActiveBlock> FIREBOX_TERRASTEEL = createFireboxCasing(TERRASTEEL_FIREBOX,
            "§2Terrasteel§r Firebox Casing");

    public static final BlockEntry<Block> ALFSTEEL_MACHINE_CASING = createSidedCasingBlock(
            "§dAlfsteel§r Engine Casing", "solid_alfsteel_machine_casing",
            "generators/machine_casing_turbine_alfsteel", BlockItem::new);
    public static final BlockEntry<Block> ALFSTEEL_PIPE_CASING = createSidedCasingBlock(
            "§dAlfsteel§r Pipe Casing", "alfsteel_pipe_casing",
            "generators/machine_casing_pipe_alfsteel", BlockItem::new);
    public static final BlockEntry<Block> ALFSTEEL_GEARBOX_CASING = createSidedCasingBlock(
            "§dAlfsteel§r Gearbox", "alfsteel_gearbox_casing",
            "generators/machine_casing_gearbox_alfsteel", BlockItem::new);
    public static final BoilerFireboxType ALFSTEEL_FIREBOX = new BoilerFireboxType(
            "alfsteel_firebox",
            AstroCore.id("block/generators/machine_casing_turbine_alfsteel"),
            AstroCore.id("block/generators/machine_casing_turbine_alfsteel"),
            AstroCore.id("block/generators/machine_casing_firebox_alfsteel"));
    public static final BlockEntry<ActiveBlock> FIREBOX_ALFSTEEL = createFireboxCasing(ALFSTEEL_FIREBOX,
            "§dAlfsteel§r Firebox Casing");

    // Solar Cells
    public static final BlockEntry<Block> SOLAR_CELL = createSolarCasingBlock("solar_cell_silver", "Solar Cell MK I");
    public static final BlockEntry<Block> SOLAR_CELL_ETRIUM = createSolarCasingBlock("solar_cell_etrium",
            "Solar Cell MK II");
    public static final BlockEntry<Block> SOLAR_CELL_VESNIUM = createSolarCasingBlock("solar_cell_vesnium",
            "Solar Cell MK III");

    public static void init() {}
}
