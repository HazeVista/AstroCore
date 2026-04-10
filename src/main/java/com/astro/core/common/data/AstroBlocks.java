package com.astro.core.common.data;

import com.astro.core.common.data.block.foliage.PlutonianShrubBlock;
import com.astro.core.common.data.block.foliage.ScorchGrassBlock;
import com.gregtechceu.gtceu.api.block.ActiveBlock;
import com.gregtechceu.gtceu.api.block.property.GTBlockStateProperties;
import com.gregtechceu.gtceu.data.recipe.CustomTags;

import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.Tags;

import com.astro.core.AstroCore;
import com.astro.core.common.data.block.AstroFallingBlock;
import com.astro.core.common.data.block.KuiperSlimeBlock;
import com.astro.core.common.data.block.foliage.ResinwortBlock;
import com.tterrag.registrate.util.entry.BlockEntry;

import static com.astro.core.common.registry.AstroRegistry.REGISTRATE;

@SuppressWarnings("all")
public class AstroBlocks {

    public record FireboxInfo(String name, ResourceLocation top, ResourceLocation bottom, ResourceLocation side) {}

    public static BlockEntry<Block> ASTEROID_STONE;
    public static BlockEntry<Block> HARD_ASTEROID_STONE;
    public static BlockEntry<AstroFallingBlock> ASTEROID_SAND;
    public static BlockEntry<AstroFallingBlock> ASTEROID_GRAVEL;
    public static BlockEntry<Block> COBBLED_ASTEROID_STONE;
    public static BlockEntry<Block> SMOOTH_ASTEROID_STONE;

    public static BlockEntry<KuiperSlimeBlock> KUIPER_SLIME;
    public static BlockEntry<Block> LIVINGBRICKS;
    public static BlockEntry<Block> SHIMMERBRICKS;

    public static BlockEntry<Block> MACHINE_CASING_KINETIC;

    public static BlockEntry<Block> STEAM_ENGINE_GRATING;

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

    public static BlockEntry<Block> MACHINE_CASING_GAIASTEEL;
    public static BlockEntry<Block> MACHINE_CASING_GAIASTEEL_BRICKS;
    public static BlockEntry<Block> GAIASTEEL_CASING_PIPE;
    public static BlockEntry<Block> GAIASTEEL_CASING_GEARBOX;
    public static BlockEntry<ActiveBlock> FIREBOX_GAIASTEEL;

    public static BlockEntry<Block> STEEL_CONTROL_CASING;
    public static BlockEntry<Block> TUNGSTENSTEEL_CONTROL_CASING;

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

    public static BlockEntry<ActiveBlock> FUTURA_COMPUTER_CASING;

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

    public static BlockEntry<ActiveBlock> FARADAY_GENERATOR_COIL;
    public static BlockEntry<Block> ELECTROMAGNET_MK1;
    public static BlockEntry<Block> ELECTROMAGNET_MK2;
    public static BlockEntry<Block> ELECTROMAGNET_MK3;

    public static BlockEntry<ResinwortBlock> RESINWORT;
    public static BlockEntry<PlutonianShrubBlock> PLUTONIAN_SHRUB;
    public static BlockEntry<ScorchGrassBlock> SCORCH_GRASS;

    public static void init() {
        REGISTRATE.creativeModeTab(() -> AstroCore.ASTRO_CREATIVE_TAB);
        // 1. Misc
        ASTEROID_STONE = createStone("asteroid_stone", "Asteroid Stone", "rocks/asteroid_stone",
                MapColor.TERRACOTTA_PURPLE, 2.0F);
        HARD_ASTEROID_STONE = createStone("hard_asteroid_stone", "Hard Asteroid Stone", "rocks/hard_asteroid_stone",
                MapColor.TERRACOTTA_PURPLE, 4.0F);
        ASTEROID_SAND = REGISTRATE.block("asteroid_sand", p -> new AstroFallingBlock(p, 0x8B7355))
                .initialProperties(() -> Blocks.SAND)
                .properties(p -> p.mapColor(MapColor.TERRACOTTA_PURPLE)
                        .strength(0.8F).sound(SoundType.SAND))
                .blockstate((ctx, prov) -> prov.simpleBlock(ctx.get(),
                        prov.models().cubeAll(ctx.getName(), AstroCore.id("block/rocks/asteroid_sand"))))
                .tag(BlockTags.SAND)
                .tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .lang("Asteroid Sand")
                .item(BlockItem::new).build().register();
        ASTEROID_GRAVEL = REGISTRATE.block("asteroid_gravel", p -> new AstroFallingBlock(p, 0x7A6E6E))
                .initialProperties(() -> Blocks.GRAVEL)
                .properties(p -> p.mapColor(MapColor.TERRACOTTA_PURPLE)
                        .strength(0.8F).sound(SoundType.GRAVEL))
                .blockstate((ctx, prov) -> prov.simpleBlock(ctx.get(),
                        prov.models().cubeAll(ctx.getName(), AstroCore.id("block/rocks/asteroid_gravel"))))
                .tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .lang("Asteroid Gravel")
                .item(BlockItem::new).build().register();
        COBBLED_ASTEROID_STONE = REGISTRATE.block("cobbled_asteroid_stone", Block::new)
                .initialProperties(() -> Blocks.COBBLESTONE)
                .properties(p -> p.mapColor(MapColor.TERRACOTTA_PURPLE).strength(0.8F))
                .blockstate((ctx, prov) -> prov.simpleBlock(ctx.get(),
                        prov.models().cubeAll(ctx.getName(), AstroCore.id("block/rocks/cobbled_asteroid_stone"))))
                .tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .tag(Tags.Blocks.STONE)
                .lang("Cobbled Asteroid Stone")
                .item(BlockItem::new).build().register();
        SMOOTH_ASTEROID_STONE = REGISTRATE.block("smooth_asteroid_stone", Block::new)
                .initialProperties(() -> Blocks.STONE)
                .properties(p -> p.mapColor(MapColor.TERRACOTTA_PURPLE).strength(0.8F))
                .blockstate((ctx, prov) -> prov.simpleBlock(ctx.get(),
                        prov.models().cubeAll(ctx.getName(), AstroCore.id("block/rocks/polished_asteroid_stone"))))
                .tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .lang("Smooth Asteroid Stone")
                .item(BlockItem::new).build().register();

        KUIPER_SLIME = REGISTRATE.block("kuiper_slime_block", KuiperSlimeBlock::new)
                .initialProperties(() -> Blocks.SLIME_BLOCK)
                .addLayer(() -> RenderType::translucent)
                .blockstate((ctx, prov) -> prov.simpleBlock(ctx.get(),
                        prov.models()
                                .withExistingParent(ctx.getName(),
                                        new ResourceLocation("minecraft", "block/slime_block"))
                                .texture("particle", AstroCore.id("block/misc/kuiper_slime_block"))
                                .texture("texture", AstroCore.id("block/misc/kuiper_slime_block"))))
                .lang("Kuiper Slime Block")
                .item(BlockItem::new).build().register();

        LIVINGBRICKS = createStone("livingbricks", "Livingbricks", "casings/livingbricks",
                MapColor.TERRACOTTA_LIGHT_GRAY, 1.2F);
        SHIMMERBRICKS = createStone("shimmerbricks", "Shimmerbricks", "casings/shimmerbricks",
                MapColor.TERRACOTTA_WHITE, 10.0F);

        // 2. Machine Casings & Gearboxes
        MACHINE_CASING_KINETIC = createCasing("machine_casing_kinetic",
                "casings/machine_casing_kinetic", "Kinetic Machine Casing");
        STEAM_ENGINE_GRATING = createCasing("steam_engine_grating",
                "casings/machine_casing_steam_grate", "Steam Engine Grating");
        MANASTEEL_MACHINE_CASING = createCasing("manasteel_brick_machine_casing",
                "generators/machine_casing_manasteel_plated_bricks", "§9Manasteel§r-Plated Brick Casing");
        TERRASTEEL_MACHINE_CASING = createCasing("solid_terrasteel_machine_casing", "generators/terrasteel_casing",
                "Solid §2Terrasteel§r Casing");
        ALFSTEEL_MACHINE_CASING = createCasing("machine_casing_alfsteel",
                "generators/machine_casing_solid_alfsteel", "Solid §dAlfsteel§r Casing");
        MACHINE_CASING_GAIASTEEL_BRICKS = createCasing("machine_casing_gaiasteel_bricks",
                "casings/machine_casing_gaiasteel_bricks", "§cGaiasteel§r-Plated Brick Casing");
        MACHINE_CASING_GAIASTEEL = createCasing("machine_casing_gaiasteel",
                "casings/machine_casing_gaiasteel", "Solid §cGaiasteel§r Casing");
        MACHINE_CASING_STYRENE_BUTADIENE = createCasing("industrial_styrene_butadiene_rubber_casing",
                "casings/industrial_casings/machine_casing_styrene_butadiene_rubber",
                "Industrial Styrene Butadiene Rubber Coated Casing");
        MACHINE_CASING_ULTIMET = createCasing("industrial_ultimet_casing",
                "casings/industrial_casings/machine_casing_ultimet", "Industrial Ultimet Casing");
        MACHINE_CASING_RED_STEEL = createCasing("industrial_red_steel_casing",
                "casings/industrial_casings/machine_casing_red_steel", "Industrial Red Steel Casing");
        MACHINE_CASING_POLYVINYL_CHLORIDE = createCasing("industrial_polyvinyl_chloride_casing",
                "casings/industrial_casings/machine_casing_polyvinyl_chloride",
                "Industrial Polyvinyl Chloride Coated Casing");
        MACHINE_CASING_BLUE_STEEL = createCasing("industrial_blue_steel_casing",
                "casings/industrial_casings/machine_casing_blue_steel", "Industrial Blue Steel Casing");
        MACHINE_CASING_BLACK_STEEL = createCasing("industrial_black_steel_casing",
                "casings/industrial_casings/machine_casing_black_steel", "Industrial Black Steel Casing");
        MACHINE_CASING_SILICONE_RUBBER = createCasing("industrial_silicone_rubber_casing",
                "casings/industrial_casings/machine_casing_silicone_rubber",
                "Industrial Silicone Rubber Coated Casing");
        MACHINE_CASING_ROSE_GOLD = createCasing("industrial_rose_gold_casing",
                "casings/industrial_casings/machine_casing_rose_gold", "Industrial Rose Gold Casing");
        MACHINE_CASING_CARBON_FIBER_MESH = createCasing("industrial_carbon_fiber_casing",
                "casings/industrial_casings/machine_casing_carbon_fiber_mesh", "Industrial Carbon Fiber Mesh Casing");
        MACHINE_CASING_VANADIUM_STEEL = createCasing("industrial_vanadium_steel_casing",
                "casings/industrial_casings/machine_casing_vanadium_steel", "Industrial Vanadium Steel Casing");
        MACHINE_CASING_BISMUTH_BRONZE = createCasing("industrial_bismuth_bronze_casing",
                "casings/industrial_casings/machine_casing_bismuth_bronze", "Industrial Bismuth Bronze Casing");
        MACHINE_CASING_COBALT_BRASS = createCasing("industrial_cobalt_brass_casing",
                "casings/industrial_casings/machine_casing_cobalt_brass", "Industrial Cobalt Brass Casing");
        FUTURA_COMPUTER_CASING = createEmissiveFunctionalCasing("futura_computer_housing",
                "astrogreg:functional_casings/futura_computer_housing", "Futura Computer Casing");
        MACHINE_CASING_RHODIUM_PLATED_PALLADIUM = createCasing(
                "machine_casing_pristine_rhodium_plated_palladium",
                "casings/machine_casing_pristine_rhodium_plated_palladium",
                "Pristine Rhodium Plated Palladium Machine Casing");
        MACHINE_CASING_NAQUADAH_ALLOY = createCasing("machine_casing_invariant_naquadah_alloy",
                "casings/machine_casing_invariant_naquadah_alloy", "Invariant Naquadah Alloy Machine Casing");
        MACHINE_CASING_NETHERITE_MESH = createCasing("machine_casing_netherite_mesh",
                "casings/machine_casing_runic_netherite", "Netherite Mesh Casing");
        MACHINE_CASING_PAI = createCasing("machine_casing_super_inert_pai",
                "casings/machine_casing_super_inert_pai", "Thermochemically Stable PAI Machine Casing");
        ALFSTEEL_GEARBOX_CASING = createCasing("alfsteel_gearbox_casing",
                "generators/machine_casing_gearbox_alfsteel", "§dAlfsteel§r Gearbox Casing");
        GAIASTEEL_CASING_GEARBOX = createCasing("gaiasteel_gearbox_casing",
                "generators/machine_casing_gearbox_gaiasteel", "§cGaiasteel§r Gearbox Casing");
        GEARBOX_CASING_RHODIUM_PLATED_PALLADIUM = createCasing("gearbox_casing_rhodium_plated_palladium",
                "casings/gearbox_casing_pristine_rhodium_plated_palladium", "Rhodium Plated Palladium Gearbox Casing");
        GEARBOX_CASING_NAQUADAH_ALLOY = createCasing("gearbox_casing_invariant_naquadah_alloy",
                "casings/gearbox_casing_invariant_naquadah_alloy", "Naquadah Alloy Gearbox Casing");
        ALFSTEEL_ENGINE_CASING = createCasing("machine_casing_turbine_alfsteel",
                "generators/machine_casing_turbine_alfsteel", "§dAlfsteel§r Engine Casing");
        TURBINE_CASING_RHODIUM_PLATED_PALLADIUM = createCasing("machine_casing_rhodium_plated_palladium",
                "generators/machine_casing_turbine_rhodium_plated_palladium",
                "Rhodium Plated Palladium Turbine Casing");
        TURBINE_CASING_NAQUADAH_ALLOY = createCasing("machine_casing_turbine_naquadah_alloy",
                "generators/machine_casing_turbine_naquadah_alloy", "Naquadah Alloy Turbine Casing");
        // _CASING = createCasing( "", "", "");

        // 3. Pipe Casings
        MANASTEEL_PIPE_CASING = createCasing("manasteel_pipe_casing",
                "generators/machine_casing_pipe_manasteel", "§9Manasteel§r Pipe Casing");
        TERRASTEEL_PIPE_CASING = createCasing("terrasteel_pipe_casing",
                "generators/machine_casing_pipe_terrasteel", "§2Terrasteel§r Pipe Casing");
        ALFSTEEL_PIPE_CASING = createCasing("alfsteel_pipe_casing",
                "generators/machine_casing_pipe_alfsteel", "§dAlfsteel§r Pipe Casing");
        GAIASTEEL_CASING_PIPE = createCasing("gaiasteel_pipe_casing",
                "generators/machine_casing_pipe_gaiasteel", "§cGaiasteel§r Pipe Casing");
        PIPE_CASING_RHODIUM_PLATED_PALLADIUM = createCasing("pipe_casing_rhodium_plated_palladium",
                "casings/pipe_casing_pristine_rhodium_plated_palladium", "Rhodium Plated Palladium Pipe Casing");
        PIPE_CASING_NAQUADAH_ALLOY = createCasing("pipe_casing_invariant_naquadah_alloy",
                "casings/pipe_casing_invariant_naquadah_alloy", "Naquadah Alloy Pipe Casing");
        PIPE_CASING_PAI = createCasing("pipe_casing_super_inert_pai",
                "casings/pipe_casing_super_inert_pai", "PAI Pipe Casing");

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
        FIREBOX_GAIASTEEL = createManaFirebox(new FireboxInfo("gaiasteel_firebox",
                AstroCore.id("block/casings/machine_casing_gaiasteel"),
                AstroCore.id("block/casings/machine_casing_gaiasteel"),
                AstroCore.id("block/generators/machine_casing_firebox_gaiasteel")), "§cGaiasteel§r Firebox Casing");

        // 5. Control Casings
        STEEL_CONTROL_CASING = createCasing("steel_control_casing",
                "casings/steel_control_casing", "Basic Control Casing");
        TUNGSTENSTEEL_CONTROL_CASING = createCasing("tungstensteel_control_casing",
                "casings/tungstensteel_control_casing", "Advanced Control Casing");

        // 6. Functional Casings
        BRONZE_CRUSHING_WHEELS = createFunctionalCasing("bronze_crushing_wheels", "gcym/industrial_steam_casing",
                "Bronze Crushing Wheels");
        ULTIMATE_INTAKE_CASING = createFunctionalCasing("machine_casing_ultimate_engine_intake",
                "astrogreg:functional_casings/machine_casing_ultimate_engine_intake", "Ultimate Engine Intake Casing");

        // 7. Solar Cells
        SOLAR_CELL = createSolar("solar_cell_silver", "Solar Cell MK I");
        SOLAR_CELL_ETRIUM = createSolar("solar_cell_etrium", "Solar Cell MK II");
        SOLAR_CELL_VESNIUM = createSolar("solar_cell_vesnium", "Solar Cell MK III");
        SOLAR_CELL_NAQ = createSolar("solar_cell_enriched_naquadah", "Solar Cell MK IV");

        // 8. Industrial Processing Cores
        INDUSTRIAL_PROCESSING_CORE_MK1 = createCoreBlock("hv_industrial_processing_core",
                "industrial_processing_core_1", "§6Industrial Processing Core MK I");
        INDUSTRIAL_PROCESSING_CORE_MK2 = createCoreBlock("ev_industrial_processing_core",
                "industrial_processing_core_2", "§5Industrial Processing Core MK II");
        INDUSTRIAL_PROCESSING_CORE_MK3 = createCoreBlock("iv_industrial_processing_core",
                "industrial_processing_core_3", "§9Industrial Processing Core MK III");

        // 9. Electromagnetic Generator Blocks
        ELECTROMAGNET_MK1 = createCasing("iv_faraday_magnet",
                "generators/neodymium_feg_magnet", "§9Faraday Generator Magnet MK I");
        ELECTROMAGNET_MK2 = createCasing("luv_faraday_magnet",
                "generators/samarium_feg_magnet", "§dFaraday Generator Magnet MK II");
        ELECTROMAGNET_MK3 = createCasing("zpm_faraday_magnet",
                "generators/neutronium_feg_magnet", "§cFaraday Generator Magnet MK III");
        FARADAY_GENERATOR_COIL = createBloomCoilBlock("faraday_generator_coil",
                "generators/faraday_generator_coil", "Faraday Generator Coil");

        // 10. Crops
        RESINWORT = REGISTRATE.block("resinwort", p -> new ResinwortBlock(p, () -> AstroItems.RESINWORT_SEEDS.get()))
                .initialProperties(() -> Blocks.WHEAT)
                .blockstate((ctx, prov) -> {
                    for (int i = 0; i <= 5; i++) {
                        final int age = i;
                        prov.getVariantBuilder(ctx.get())
                                .partialState().with(ResinwortBlock.AGE, age).modelForState()
                                .modelFile(prov.models()
                                        .withExistingParent("resinwort_stage" + age, AstroCore.id("block/crop_cross"))
                                        .texture("crop", AstroCore.id("block/crops/resinwort_" + age))
                                        .renderType("cutout"))
                                .addModel();
                    }
                })
                .loot((tables, block) -> {
                    tables.add(block, LootTable.lootTable()
                            .withPool(LootPool.lootPool()
                                    .when(LootItemBlockStatePropertyCondition
                                            .hasBlockStateProperties(block)
                                            .setProperties(StatePropertiesPredicate.Builder.properties()
                                                    .hasProperty(ResinwortBlock.AGE, 5)))
                                    .add(LootItem.lootTableItem(AstroItems.RESINWORT_POD.get())
                                            .apply(SetItemCountFunction.setCount(
                                                    UniformGenerator.between(2, 4))))));
                })
                .addLayer(() -> RenderType::cutout)
                .lang("Resinwort")
                .register();

        PLUTONIAN_SHRUB = REGISTRATE.block("plutonian_shrub",
                        p -> new PlutonianShrubBlock(p, () -> AstroItems.PLUTONIAN_SHRUB_SEEDS.get()))
                .initialProperties(() -> Blocks.DEAD_BUSH)
                .blockstate((ctx, prov) -> {
                    for (int i = 0; i <= 2; i++) {
                        final int age = i;
                        prov.getVariantBuilder(ctx.get())
                                .partialState().with(PlutonianShrubBlock.AGE, age).modelForState()
                                .modelFile(prov.models()
                                        .cross("plutonian_shrub_stage" + age,
                                                AstroCore.id("block/crops/plutonian_shrub_" + age))
                                        .renderType("cutout"))
                                .addModel();
                    }
                })
                .addLayer(() -> RenderType::cutout)
                .loot((tables, block) -> {
                    tables.add(block, LootTable.lootTable()
                            .withPool(LootPool.lootPool()
                                    .when(InvertedLootItemCondition.invert(
                                            MatchTool.toolMatches(ItemPredicate.Builder.item()
                                                    .of(Tags.Items.SHEARS))))
                                    .when(InvertedLootItemCondition.invert(
                                            MatchTool.toolMatches(ItemPredicate.Builder.item()
                                                    .of(ItemTags.SHOVELS))))
                                    .add(LootItem.lootTableItem(AstroItems.PLUTONIAN_SHRUB_SEEDS.get())
                                            .apply(SetItemCountFunction.setCount(
                                                    UniformGenerator.between(1, 2)))))
                            .withPool(LootPool.lootPool()
                                    .when(LootItemBlockStatePropertyCondition
                                            .hasBlockStateProperties(block)
                                            .setProperties(StatePropertiesPredicate.Builder.properties()
                                                    .hasProperty(PlutonianShrubBlock.AGE, 2)))
                                    .when(AnyOfCondition.anyOf(
                                            MatchTool.toolMatches(ItemPredicate.Builder.item()
                                                    .of(Tags.Items.SHEARS)),
                                            MatchTool.toolMatches(ItemPredicate.Builder.item()
                                                    .of(ItemTags.SHOVELS))))
                                    .add(LootItem.lootTableItem(AstroItems.PLUTONIAN_SHRUB.get()))));
                })
                .lang("Plutonian Shrub")
                .register();

        SCORCH_GRASS = REGISTRATE.block("scorch_grass", ScorchGrassBlock::new)
                .initialProperties(() -> Blocks.DEAD_BUSH)
                .blockstate((ctx, prov) -> prov.simpleBlock(ctx.get(),
                        prov.models()
                                .cross("scorch_grass", AstroCore.id("block/crops/scorch_grass"))
                                .renderType("cutout")))
                .addLayer(() -> RenderType::cutout)
                .loot((tables, block) -> {
                    tables.add(block, LootTable.lootTable()
                            .withPool(LootPool.lootPool()
                                    .when(MatchTool.toolMatches(ItemPredicate.Builder.item()
                                            .of(net.minecraftforge.common.Tags.Items.SHEARS)))
                                    .add(LootItem.lootTableItem(AstroItems.SCORCH_GRASS.get()))));
                })
                .lang("Scorch Grass")
                .register();
    }

    // --- Helpers ---
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
                .addLayer(() -> RenderType::translucent)
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
                .addLayer(() -> RenderType::cutoutMipped)
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

    private static BlockEntry<ActiveBlock> createEmissiveFunctionalCasing(String id, String sideTexture, String name) {
        ResourceLocation side = new ResourceLocation(sideTexture.contains(":") ? sideTexture.split(":")[0] : "gtceu",
                "block/casings/" + (sideTexture.contains(":") ? sideTexture.split(":")[1] : sideTexture));
        ResourceLocation funcTex = AstroCore.id("block/casings/functional_casings/" + id);
        ResourceLocation funcTexActive = AstroCore.id("block/casings/functional_casings/" + id + "_active");
        ResourceLocation funcTexEmissive = AstroCore.id("block/casings/functional_casings/" + id + "_active_emissive");

        return REGISTRATE.block(id, ActiveBlock::new)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .addLayer(() -> RenderType::cutoutMipped)
                .blockstate((ctx, prov) -> {
                    ModelFile inactive = prov.models()
                            .cube(ctx.getName(), side, side, funcTex, funcTex, funcTex, funcTex)
                            .texture("particle", side);

                    ModelFile active = prov.models().cubeAll(ctx.getName() + "_active", funcTexActive);

                    ModelFile emissive = prov.models()
                            .getBuilder(ctx.getName() + "_active_emissive")
                            .parent(new ModelFile.UncheckedModelFile(AstroCore.id("block/cube_emissive")))
                            .texture("all", funcTexEmissive.toString());

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

    private static BlockEntry<ActiveBlock> createBloomCoilBlock(String id, String texture, String lang) {
        return REGISTRATE.block(id, ActiveBlock::new)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
                .addLayer(() -> RenderType::cutoutMipped)
                .blockstate((ctx, prov) -> {
                    ModelFile inactive = prov.models().cubeAll(ctx.getName(),
                            AstroCore.id("block/" + texture));
                    ModelFile active = prov.models().cubeAll(ctx.getName() + "_active",
                            AstroCore.id("block/" + texture + "_bloom"));
                    prov.getVariantBuilder(ctx.getEntry())
                            .partialState().with(GTBlockStateProperties.ACTIVE, false).modelForState()
                            .modelFile(inactive).addModel()
                            .partialState().with(GTBlockStateProperties.ACTIVE, true).modelForState()
                            .modelFile(active).addModel();
                })
                .tag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
                .lang(lang)
                .item(BlockItem::new)
                .build()
                .register();
    }

    public static final FireboxInfo MANASTEEL_FIREBOX_REC = new FireboxInfo("manasteel_firebox",
            AstroCore.id("block/generators/machine_casing_ma/nasteel_plated_bricks"),
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
    public static final FireboxInfo GAIASTEEL_FIREBOX_REC = new FireboxInfo("gaiasteel_firebox",
            AstroCore.id("block/casings/machine_casing_gaiasteel"),
            AstroCore.id("block/casings/machine_casing_gaiasteel"),
            AstroCore.id("block/generators/machine_casing_firebox_gaiasteel"));
}
