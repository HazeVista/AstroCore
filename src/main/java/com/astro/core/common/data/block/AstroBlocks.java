package com.astro.core.common.data.block;

import com.gregtechceu.gtceu.api.block.ActiveBlock;
import com.gregtechceu.gtceu.api.block.property.GTBlockStateProperties;
import com.gregtechceu.gtceu.data.recipe.CustomTags;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.client.model.generators.ModelFile;

import com.astro.core.AstroCore;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;

import static com.astro.core.common.registry.AstroRegistry.REGISTRATE;

@SuppressWarnings("all")
public class AstroBlocks {

    public record FireboxInfo(String name, ResourceLocation top, ResourceLocation bottom, ResourceLocation side) {}

    public static BlockEntry<Block> ASTEROID_STONE;
    public static BlockEntry<Block> HARD_ASTEROID_STONE;

    public static BlockEntry<Block> MANASTEEL_MACHINE_CASING;
    public static BlockEntry<Block> MANASTEEL_PIPE_CASING;
    public static BlockEntry<ActiveBlock> FIREBOX_MANASTEEL;

    public static BlockEntry<Block> TERRASTEEL_MACHINE_CASING;
    public static BlockEntry<Block> TERRASTEEL_PIPE_CASING;
    public static BlockEntry<ActiveBlock> FIREBOX_TERRASTEEL;

    public static BlockEntry<Block> ALFSTEEL_MACHINE_CASING;
    public static BlockEntry<Block> ALFSTEEL_PIPE_CASING;
    public static BlockEntry<Block> ALFSTEEL_GEARBOX_CASING;
    public static BlockEntry<ActiveBlock> FIREBOX_ALFSTEEL;

    public static BlockEntry<Block> MACHINE_CASING_PAI;
    public static BlockEntry<Block> PIPE_CASING_PAI;
    public static BlockEntry<Block> MACHINE_CASING_RHODIUM_PLATED_PALLADIUM;
    public static BlockEntry<Block> PIPE_CASING_RHODIUM_PLATED_PALLADIUM;
    public static BlockEntry<Block> GEARBOX_CASING_RHODIUM_PLATED_PALLADIUM;

    public static BlockEntry<Block> SOLAR_CELL;
    public static BlockEntry<Block> SOLAR_CELL_ETRIUM;
    public static BlockEntry<Block> SOLAR_CELL_VESNIUM;
    public static BlockEntry<Block> SOLAR_CELL_NAQ;

    public static void init() {
        REGISTRATE.creativeModeTab(() -> AstroCore.ASTRO_CREATIVE_TAB);

        // 1. Stones
        ASTEROID_STONE = createStone("asteroid_stone", "Asteroid Stone", "rocks/asteroid_stone",
                MapColor.TERRACOTTA_PURPLE, 2.0F);
        HARD_ASTEROID_STONE = createStone("hard_asteroid_stone", "Hard Asteroid Stone", "rocks/hard_asteroid_stone",
                MapColor.TERRACOTTA_PURPLE, 4.0F);

        // 2. Machine Casings & Gearboxes
        MANASTEEL_MACHINE_CASING = createCasing("manasteel_brick_machine_casing",
                "generators/machine_casing_manasteel_plated_bricks", "§9Manasteel§r-Plated Brick Casing");
        TERRASTEEL_MACHINE_CASING = createCasing("solid_terrasteel_machine_casing", "generators/terrasteel_casing",
                "§2Terrasteel§r Machine Casing");
        ALFSTEEL_MACHINE_CASING = createCasing("solid_alfsteel_machine_casing",
                "generators/machine_casing_turbine_alfsteel", "§dAlfsteel§r Engine Casing");
        MACHINE_CASING_RHODIUM_PLATED_PALLADIUM = createSidedCasingBlock(
                "Pristine Rhodium Plated Palladium Machine Casing", "machine_casing_pristine_rhodium_plated_palladium",
                "casings/machine_casing_pristine_rhodium_plated_palladium", BlockItem::new);
        MACHINE_CASING_PAI = createSidedCasingBlock("Thermochemically Stable PAI Machine Casing",
                "machine_casing_super_inert_pai", "casings/machine_casing_super_inert_pai", BlockItem::new);
        // MACHINE_CASING_NAQUADAH_ALLOY = createSidedCasingBlock( "Invariant Naquadah Alloy Machine Casing",
        // "machine_casing_invariant_naquadah_alloy", "machine_casing_invariant_naquadah_alloy", BlockItem::new);
        ALFSTEEL_GEARBOX_CASING = createSidedCasingBlock("§dAlfsteel§r Gearbox", "alfsteel_gearbox_casing",
                "generators/machine_casing_gearbox_alfsteel", BlockItem::new);
        GEARBOX_CASING_RHODIUM_PLATED_PALLADIUM = createSidedCasingBlock("Rhodium Plated Palladium Gearbox Casing",
                "gearbox_casing_rhodium_plated_palladium", "casings/gearbox_casing_pristine_rhodium_plated_palladium",
                BlockItem::new);
        // GEARBOX_CASING_NAQUADAH_ALLOY = createSidedCasingBlock( "Naquadah Alloy Gearbox Casing",
        // "gearbox_casing_invariant_naquadah_alloy", "gearbox_casing_invariant_naquadah_alloy", BlockItem::new);
        // EXAMPLE_CASING = createSidedCasingBlock( "", "", "", BlockItem::new);

        // 3. Pipe Casings
        MANASTEEL_PIPE_CASING = createSidedCasingBlock("§9Manasteel§r Pipe Casing", "manasteel_pipe_casing",
                "generators/machine_casing_pipe_manasteel", BlockItem::new);
        TERRASTEEL_PIPE_CASING = createSidedCasingBlock("§2Terrasteel§r Pipe Casing", "terrasteel_pipe_casing",
                "generators/machine_casing_pipe_terrasteel", BlockItem::new);
        ALFSTEEL_PIPE_CASING = createSidedCasingBlock("§dAlfsteel§r Pipe Casing", "alfsteel_pipe_casing",
                "generators/machine_casing_pipe_alfsteel", BlockItem::new);
        PIPE_CASING_PAI = createSidedCasingBlock("PAI Pipe Casing", "pipe_casing_super_inert_pai",
                "casings/pipe_casing_super_inert_pai", BlockItem::new);
        PIPE_CASING_RHODIUM_PLATED_PALLADIUM = createSidedCasingBlock("Rhodium Plated Palladium Pipe Casing",
                "pipe_casing_rhodium_plated_palladium", "casings/pipe_casing_pristine_rhodium_plated_palladium",
                BlockItem::new);
        // PIPE_CASING_NAQUADAH_ALLOY = createSidedCasingBlock( "Naquadah Alloy Pipe Casing",
        // "pipe_casing_invariant_naquadah_alloy", "pipe_casing_invariant_naquadah_alloy", BlockItem::new);

        // 4. Fireboxes
        FIREBOX_MANASTEEL = createFirebox(new FireboxInfo("manasteel_firebox",
                AstroCore.id("block/generators/machine_casing_manasteel_plated_bricks"),
                AstroCore.id("block/generators/machine_casing_manasteel_plated_bricks"),
                AstroCore.id("block/generators/machine_casing_firebox_manasteel")), "§9Manasteel§r Firebox Casing");

        FIREBOX_TERRASTEEL = createFirebox(new FireboxInfo("terrasteel_firebox",
                AstroCore.id("block/generators/terrasteel_casing"),
                AstroCore.id("block/generators/terrasteel_casing"),
                AstroCore.id("block/generators/machine_casing_firebox_terrasteel")), "§2Terrasteel§r Firebox Casing");

        FIREBOX_ALFSTEEL = createFirebox(new FireboxInfo("alfsteel_firebox",
                AstroCore.id("block/generators/machine_casing_turbine_alfsteel"),
                AstroCore.id("block/generators/machine_casing_turbine_alfsteel"),
                AstroCore.id("block/generators/machine_casing_firebox_alfsteel")), "§dAlfsteel§r Firebox Casing");

        // 5. Solar Cells
        SOLAR_CELL = createSolar("solar_cell_silver", "Solar Cell MK I");
        SOLAR_CELL_ETRIUM = createSolar("solar_cell_etrium", "Solar Cell MK II");
        SOLAR_CELL_VESNIUM = createSolar("solar_cell_vesnium", "Solar Cell MK III");
        SOLAR_CELL_NAQ = createSolar("solar_cell_enriched_naquadah", "Solar Cell MK IV");
    }

    // --- Helpers ---
    private static BlockEntry<Block> createSidedCasingBlock(String name, String id, String texture,
                                                            NonNullBiFunction<Block, Item.Properties, ? extends BlockItem> func) {
        return REGISTRATE.block(id, Block::new)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
                .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(),
                        prov.models().cubeAll(ctx.getName(), AstroCore.id("block/" + texture))))
                .tag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
                .lang(name)
                .item(func).build().register();
    }

    private static BlockEntry<Block> createStone(String id, String name, String tex, MapColor color, float strength) {
        return REGISTRATE.block(id, Block::new)
                .initialProperties(() -> Blocks.STONE)
                .properties(
                        p -> p.mapColor(color).strength(strength).sound(SoundType.STONE).requiresCorrectToolForDrops())
                .blockstate((ctx, prov) -> prov.simpleBlock(ctx.get(),
                        prov.models().cubeAll(ctx.getName(), AstroCore.id("block/" + tex))))
                .lang(name)
                .item(BlockItem::new).build().register();
    }

    private static BlockEntry<Block> createCasing(String id, String tex, String lang) {
        return REGISTRATE.block(id, Block::new)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .blockstate((ctx, prov) -> prov.simpleBlock(ctx.get(),
                        prov.models().cubeAll(ctx.getName(), AstroCore.id("block/" + tex))))
                .lang(lang)
                .tag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
                .item(BlockItem::new).build().register();
    }

    private static BlockEntry<ActiveBlock> createFirebox(FireboxInfo info, String lang) {
        return REGISTRATE.block(info.name + "_casing", ActiveBlock::new)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .addLayer(() -> RenderType::cutoutMipped)
                .blockstate((ctx, prov) -> {
                    ModelFile inactive = prov.models().cubeBottomTop(ctx.getName(), info.side, info.bottom, info.top);
                    ModelFile active = prov.models()
                            .withExistingParent(ctx.getName() + "_active",
                                    new ResourceLocation("gtceu", "block/fire_box_active"))
                            .texture("side", info.side).texture("bottom", info.bottom).texture("top", info.top);
                    prov.getVariantBuilder(ctx.getEntry())
                            .partialState().with(GTBlockStateProperties.ACTIVE, false).modelForState()
                            .modelFile(inactive).addModel()
                            .partialState().with(GTBlockStateProperties.ACTIVE, true).modelForState().modelFile(active)
                            .addModel();
                })
                .lang(lang)
                .tag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
                .item(BlockItem::new).build().register();
    }

    private static BlockEntry<Block> createSolar(String id, String name) {
        ResourceLocation side = new ResourceLocation("gtceu", "block/casings/solid/machine_casing_solid_steel");
        return REGISTRATE.block(id, Block::new)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .blockstate((ctx, prov) -> prov.simpleBlock(ctx.get(),
                        prov.models()
                                .cube(ctx.getName(), side, AstroCore.id("block/generators/" + id), side, side, side,
                                        side)
                                .texture("particle", side)))
                .lang(name)
                .item(BlockItem::new).build().register();
    }

    public static final FireboxInfo MANASTEEL_FIREBOX_REC = new FireboxInfo("manasteel_firebox",
            AstroCore.id("block/generators/machine_casing_manasteel_plated_bricks"),
            AstroCore.id("block/generators/machine_casing_manasteel_plated_bricks"),
            AstroCore.id("block/generators/machine_casing_firebox_manasteel"));

    public static final FireboxInfo TERRASTEEL_FIREBOX_REC = new FireboxInfo("terrasteel_firebox",
            AstroCore.id("block/generators/terrasteel_casing"),
            AstroCore.id("block/generators/terrasteel_casing"),
            AstroCore.id("block/generators/machine_casing_firebox_terrasteel"));

    public static final FireboxInfo ALFSTEEL_FIREBOX_REC = new FireboxInfo("alfsteel_firebox",
            AstroCore.id("block/generators/machine_casing_turbine_alfsteel"),
            AstroCore.id("block/generators/machine_casing_turbine_alfsteel"),
            AstroCore.id("block/generators/machine_casing_firebox_alfsteel"));
}
