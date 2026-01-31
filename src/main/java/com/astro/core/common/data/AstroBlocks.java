package com.astro.core.common.data;

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
    public static BlockEntry<Block> ALFSTEEL_ENGINE_CASING;
    public static BlockEntry<Block> ALFSTEEL_PIPE_CASING;
    public static BlockEntry<Block> ALFSTEEL_GEARBOX_CASING;
    public static BlockEntry<ActiveBlock> FIREBOX_ALFSTEEL;

    public static BlockEntry<Block> MACHINE_CASING_SILICONE_RUBBER;
    public static BlockEntry<Block> MACHINE_CASING_POLYVINYL_CHLORIDE;
    public static BlockEntry<Block> MACHINE_CASING_CARBON_FIBER_MESH;
    public static BlockEntry<Block> MACHINE_CASING_RED_STEEL;
    public static BlockEntry<Block> MACHINE_CASING_BLUE_STEEL;
    public static BlockEntry<Block> MACHINE_CASING_BLACK_STEEL;
    public static BlockEntry<Block> MACHINE_CASING_STYRENE_BUTADIENE;
    public static BlockEntry<Block> MACHINE_CASING_BISMUTH_BRONZE;
    public static BlockEntry<Block> MACHINE_CASING_COBALT_BRASS;
    public static BlockEntry<Block> MACHINE_CASING_VANADIUM_STEEL;
    public static BlockEntry<Block> MACHINE_CASING_ULTIMET;
    public static BlockEntry<Block> MACHINE_CASING_ROSE_GOLD;

    public static BlockEntry<Block> INDUSTRIAL_PROCESSING_CORE_MK1;
    public static BlockEntry<Block> INDUSTRIAL_PROCESSING_CORE_MK2;
    public static BlockEntry<Block> INDUSTRIAL_PROCESSING_CORE_MK3;

    public static BlockEntry<Block> MACHINE_CASING_PAI;
    public static BlockEntry<Block> PIPE_CASING_PAI;

    public static BlockEntry<Block> MACHINE_CASING_RHODIUM_PLATED_PALLADIUM;
    public static BlockEntry<Block> PIPE_CASING_RHODIUM_PLATED_PALLADIUM;
    public static BlockEntry<Block> TURBINE_CASING_RHODIUM_PLATED_PALLADIUM;
    public static BlockEntry<Block> GEARBOX_CASING_RHODIUM_PLATED_PALLADIUM;

    public static BlockEntry<Block> MACHINE_CASING_NAQUADAH_ALLOY;
    public static BlockEntry<Block> PIPE_CASING_NAQUADAH_ALLOY;
    public static BlockEntry<Block> GEARBOX_CASING_NAQUADAH_ALLOY;
    public static BlockEntry<Block> TURBINE_CASING_NAQUADAH_ALLOY;
    public static BlockEntry<ActiveBlock> ULTIMATE_INTAKE_CASING;

    public static BlockEntry<Block> MACHINE_CASING_NETHERITE_MESH;

    public static BlockEntry<ActiveBlock> BRONZE_CRUSHING_WHEELS;

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
                "Solid §2Terrasteel§r Casing");
        ALFSTEEL_MACHINE_CASING = createCasing("machine_casing_alfsteel",
                "generators/machine_casing_solid_alfsteel", "Solid §dAlfsteel§r Casing");
        MACHINE_CASING_VANADIUM_STEEL = createCasing("industrial_vanadium_steel_casing",
                "casings/industrial_casings/machine_casing_vanadium_steel", "Industrial Vanadium Steel Casing");
        MACHINE_CASING_ULTIMET = createCasing("industrial_ultimet_casing",
                "casings/industrial_casings/machine_casing_ultimet", "Industrial Ultimet Casing");
        MACHINE_CASING_ROSE_GOLD = createCasing("industrial_rose_gold_casing",
                "casings/industrial_casings/machine_casing_rose_gold", "Industrial Rose Gold Casing");
        MACHINE_CASING_BLACK_STEEL = createCasing("industrial_black_steel_casing",
                "casings/industrial_casings/machine_casing_black_steel", "Industrial Black Steel Casing");
        MACHINE_CASING_RED_STEEL = createCasing("industrial_red_steel_casing",
                "casings/industrial_casings/machine_casing_red_steel", "Industrial Red Steel Casing");
        MACHINE_CASING_BLUE_STEEL = createCasing("industrial_blue_steel_casing",
                "casings/industrial_casings/machine_casing_blue_steel", "Industrial Blue Steel Casing");
        MACHINE_CASING_BISMUTH_BRONZE = createCasing("industrial_bismuth_bronze_casing",
                "casings/industrial_casings/machine_casing_bismuth_bronze", "Industrial Bismuth Bronze Casing");
        MACHINE_CASING_COBALT_BRASS = createCasing("industrial_cobalt_brass_casing",
                "casings/industrial_casings/machine_casing_cobalt_brass", "Industrial Cobalt Brass Casing");
        MACHINE_CASING_CARBON_FIBER_MESH = createCasing("industrial_carbon_fiber_casing",
                "casings/industrial_casings/machine_casing_carbon_fiber_mesh", "Industrial Carbon Fiber Mesh Casing");
        MACHINE_CASING_POLYVINYL_CHLORIDE = createCasing("industrial_polyvinyl_chloride_casing",
                "casings/industrial_casings/machine_casing_polyvinyl_chloride",
                "Industrial Polyvinyl Chloride Coated Casing");
        MACHINE_CASING_SILICONE_RUBBER = createCasing("industrial_silicone_rubber_casing",
                "casings/industrial_casings/machine_casing_silicone_rubber",
                "Industrial Silicone Rubber Coated Casing");
        MACHINE_CASING_STYRENE_BUTADIENE = createCasing("industrial_styrene_butadiene_rubber_casing",
                "casings/industrial_casings/machine_casing_styrene_butadiene_rubber",
                "Industrial Styrene Butadiene Rubber Coated Casing");
        MACHINE_CASING_RHODIUM_PLATED_PALLADIUM = createSidedCasingBlock(
                "Pristine Rhodium Plated Palladium Machine Casing", "machine_casing_pristine_rhodium_plated_palladium",
                "casings/machine_casing_pristine_rhodium_plated_palladium", BlockItem::new);
        MACHINE_CASING_NAQUADAH_ALLOY = createSidedCasingBlock("Invariant Naquadah Alloy Machine Casing",
                "machine_casing_invariant_naquadah_alloy", "casings/machine_casing_invariant_naquadah_alloy",
                BlockItem::new);
        MACHINE_CASING_NETHERITE_MESH = createCasing("machine_casing_netherite_mesh",
                "casings/machine_casing_runic_netherite", "Netherite Mesh Casing");
        MACHINE_CASING_PAI = createSidedCasingBlock("Thermochemically Stable PAI Machine Casing",
                "machine_casing_super_inert_pai", "casings/machine_casing_super_inert_pai", BlockItem::new);
        ALFSTEEL_GEARBOX_CASING = createSidedCasingBlock("§dAlfsteel§r Gearbox", "alfsteel_gearbox_casing",
                "generators/machine_casing_gearbox_alfsteel", BlockItem::new);
        GEARBOX_CASING_RHODIUM_PLATED_PALLADIUM = createSidedCasingBlock("Rhodium Plated Palladium Gearbox Casing",
                "gearbox_casing_rhodium_plated_palladium", "casings/gearbox_casing_pristine_rhodium_plated_palladium",
                BlockItem::new);
        GEARBOX_CASING_NAQUADAH_ALLOY = createSidedCasingBlock("Naquadah Alloy Gearbox Casing",
                "gearbox_casing_invariant_naquadah_alloy", "casings/gearbox_casing_invariant_naquadah_alloy",
                BlockItem::new);
        ALFSTEEL_ENGINE_CASING = createCasing("machine_casing_turbine_alfsteel",
                "generators/machine_casing_turbine_alfsteel", "§dAlfsteel§r Engine Casing");
        TURBINE_CASING_RHODIUM_PLATED_PALLADIUM = createSidedCasingBlock("Rhodium Plated Palladium Turbine Casing",
                "machine_casing_rhodium_plated_palladium", "generators/machine_casing_turbine_rhodium_plated_palladium",
                BlockItem::new);
        TURBINE_CASING_NAQUADAH_ALLOY = createSidedCasingBlock("Naquadah Alloy Turbine Casing",
                "machine_casing_turbine_naquadah_alloy", "generators/machine_casing_turbine_naquadah_alloy",
                BlockItem::new);
        // EXAMPLE_CASING = createCasing( "", "", "");

        // 3. Pipe Casings
        MANASTEEL_PIPE_CASING = createSidedCasingBlock("§9Manasteel§r Pipe Casing", "manasteel_pipe_casing",
                "generators/machine_casing_pipe_manasteel", BlockItem::new);
        TERRASTEEL_PIPE_CASING = createSidedCasingBlock("§2Terrasteel§r Pipe Casing", "terrasteel_pipe_casing",
                "generators/machine_casing_pipe_terrasteel", BlockItem::new);
        ALFSTEEL_PIPE_CASING = createSidedCasingBlock("§dAlfsteel§r Pipe Casing", "alfsteel_pipe_casing",
                "generators/machine_casing_pipe_alfsteel", BlockItem::new);
        PIPE_CASING_RHODIUM_PLATED_PALLADIUM = createSidedCasingBlock("Rhodium Plated Palladium Pipe Casing",
                "pipe_casing_rhodium_plated_palladium", "casings/pipe_casing_pristine_rhodium_plated_palladium",
                BlockItem::new);
        PIPE_CASING_NAQUADAH_ALLOY = createSidedCasingBlock("Naquadah Alloy Pipe Casing",
                "pipe_casing_invariant_naquadah_alloy", "casings/pipe_casing_invariant_naquadah_alloy", BlockItem::new);
        PIPE_CASING_PAI = createSidedCasingBlock("PAI Pipe Casing", "pipe_casing_super_inert_pai",
                "casings/pipe_casing_super_inert_pai", BlockItem::new);

        // 4. Fireboxes
        FIREBOX_MANASTEEL = createManaFirebox(new FireboxInfo("manasteel_firebox",
                AstroCore.id("block/generators/machine_casing_manasteel_plated_bricks"),
                AstroCore.id("block/generators/machine_casing_manasteel_plated_bricks"),
                AstroCore.id("block/generators/machine_casing_firebox_manasteel")), "§9Manasteel§r Firebox Casing");
        FIREBOX_TERRASTEEL = createManaFirebox(new FireboxInfo("terrasteel_firebox",
                AstroCore.id("block/generators/terrasteel_casing"),
                AstroCore.id("block/generators/terrasteel_casing"),
                AstroCore.id("block/generators/machine_casing_firebox_terrasteel")), "§2Terrasteel§r Firebox Casing");
        FIREBOX_ALFSTEEL = createManaFirebox(new FireboxInfo("alfsteel_firebox",
                AstroCore.id("block/generators/machine_casing_solid_alfsteel"),
                AstroCore.id("block/generators/machine_casing_solid_alfsteel"),
                AstroCore.id("block/generators/machine_casing_firebox_alfsteel")), "§dAlfsteel§r Firebox Casing");

        // 5. Functional Casings
        BRONZE_CRUSHING_WHEELS = createFunctionalCasing("bronze_crushing_wheels", "gcym/industrial_steam_casing",
                "Bronze Crushing Wheels");
        ULTIMATE_INTAKE_CASING = createFunctionalCasing("machine_casing_ultimate_engine_intake",
                "astrogreg:functional_casings/machine_casing_ultimate_engine_intake", "Ultimate Engine Intake Casing");

        // 6. Solar Cells
        SOLAR_CELL = createSolar("solar_cell_silver", "Solar Cell MK I");
        SOLAR_CELL_ETRIUM = createSolar("solar_cell_etrium", "Solar Cell MK II");
        SOLAR_CELL_VESNIUM = createSolar("solar_cell_vesnium", "Solar Cell MK III");
        SOLAR_CELL_NAQ = createSolar("solar_cell_enriched_naquadah", "Solar Cell MK IV");

        // 7. Industrial Processing Cores
        INDUSTRIAL_PROCESSING_CORE_MK1 = createCoreBlock("hv_industrial_processing_core",
                "industrial_processing_core_1", "Industrial Processing Core MK I");
        INDUSTRIAL_PROCESSING_CORE_MK2 = createCoreBlock("ev_industrial_processing_core",
                "industrial_processing_core_2", "Industrial Processing Core MK II");
        INDUSTRIAL_PROCESSING_CORE_MK3 = createCoreBlock("iv_industrial_processing_core",
                "industrial_processing_core_3", "Industrial Processing Core MK III");
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

    private static BlockEntry<Block> createStone(String id, String name, String texture, MapColor color,
                                                 float strength) {
        return REGISTRATE.block(id, Block::new)
                .initialProperties(() -> Blocks.STONE)
                .properties(
                        p -> p.mapColor(color).strength(strength).sound(SoundType.STONE).requiresCorrectToolForDrops())
                .blockstate((ctx, prov) -> prov.simpleBlock(ctx.get(),
                        prov.models().cubeAll(ctx.getName(), AstroCore.id("block/" + texture))))
                .lang(name)
                .item(BlockItem::new).build().register();
    }

    private static BlockEntry<Block> createCasing(String id, String texture, String lang) {
        return REGISTRATE.block(id, Block::new)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .blockstate((ctx, prov) -> prov.simpleBlock(ctx.get(),
                        prov.models().cubeAll(ctx.getName(), AstroCore.id("block/" + texture))))
                .lang(lang)
                .tag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
                .item(BlockItem::new).build().register();
    }

    private static BlockEntry<ActiveBlock> createManaFirebox(FireboxInfo info, String lang) {
        return REGISTRATE.block(info.name + "_casing", ActiveBlock::new)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .addLayer(() -> RenderType::cutoutMipped)
                .blockstate((ctx, prov) -> {
                    ModelFile inactive = prov.models().cubeBottomTop(ctx.getName(), info.side, info.bottom, info.top);
                    ModelFile active = prov.models()
                            .withExistingParent(ctx.getName() + "_active",
                                    new ResourceLocation("astrogreg", "block/mana_fire_box_active"))
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

    private static BlockEntry<ActiveBlock> createFunctionalCasing(String id, String sideTexture, String name) {
        ResourceLocation side = new ResourceLocation(sideTexture.contains(":") ? sideTexture.split(":")[0] : "gtceu",
                "block/casings/" + (sideTexture.contains(":") ? sideTexture.split(":")[1] : sideTexture));
        return REGISTRATE.block(id, ActiveBlock::new)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .blockstate((ctx, prov) -> {
                    ModelFile inactive = prov.models()
                            .cube(ctx.getName(), side, AstroCore.id("block/casings/functional_casings/" + id), side,
                                    side, side, side)
                            .texture("particle", side);
                    ModelFile active = prov.models()
                            .cube(ctx.getName() + "_active", side,
                                    AstroCore.id("block/casings/functional_casings/" + id + "_active"), side, side,
                                    side, side)
                            .texture("particle", side);
                    prov.getVariantBuilder(ctx.getEntry())
                            .partialState().with(GTBlockStateProperties.ACTIVE, false).modelForState()
                            .modelFile(inactive).addModel()
                            .partialState().with(GTBlockStateProperties.ACTIVE, true).modelForState()
                            .modelFile(active).addModel();
                })
                .lang(name)
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

    private static BlockEntry<Block> createCoreBlock(String id, String texture, String name) {
        ResourceLocation side = AstroCore.id("block/casings/industrial_casings/" + texture);
        ResourceLocation top = AstroCore.id("block/casings/industrial_casings/" + texture + "_top");
        ResourceLocation bottom = AstroCore.id("block/casings/industrial_casings/" + texture + "_bottom");
        return REGISTRATE.block(id, Block::new)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .blockstate((ctx, prov) -> prov.simpleBlock(ctx.get(),
                        prov.models()
                                .cubeBottomTop(ctx.getName(), side, bottom, top)
                                .texture("side", side).texture("bottom", bottom).texture("top", top)))
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
            AstroCore.id("block/generators/machine_casing_solid_alfsteel"),
            AstroCore.id("block/generators/machine_casing_solid_alfsteel"),
            AstroCore.id("block/generators/machine_casing_firebox_alfsteel"));
}
