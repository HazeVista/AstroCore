package com.astro.core.common.machine.multiblock;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.property.GTMachineModelProperties;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.MultiblockShapeInfo;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.client.renderer.machine.DynamicRenderHelper;
import com.gregtechceu.gtceu.client.renderer.machine.impl.BoilerMultiPartRender;
import com.gregtechceu.gtceu.common.block.BoilerFireboxType;
import com.gregtechceu.gtceu.common.data.*;
import com.gregtechceu.gtceu.common.machine.multiblock.steam.SteamParallelMultiblockMachine;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.model.generators.BlockModelBuilder;

import com.astro.core.AstroCore;
import com.astro.core.common.data.AstroRecipeTypes;
import com.astro.core.common.machine.multiblock.electric.FluidDrillMachine;
import com.astro.core.common.machine.multiblock.electric.LargeMinerMachine;
import com.astro.core.common.machine.multiblock.generator.AstroSolarBoilers;
import com.astro.core.common.machine.multiblock.primitive.CokeOvenMachine;
import com.astro.core.common.machine.multiblock.steam.SteamBlastFurnace;
import com.astro.core.common.machine.multiblock.steam.SteamGrinder;
import com.astro.core.common.machine.multiblock.steam.SteamWasher;
import com.astro.core.common.machine.multiblock.electric.ProcessingCoreMachine;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.astro.core.common.data.AstroBlocks.*;
import static com.astro.core.common.machine.hatches.AstroHatches.WATER_HATCH;
import static com.astro.core.common.registry.AstroRegistry.REGISTRATE;
import static com.gregtechceu.gtceu.api.machine.multiblock.PartAbility.*;
import static com.gregtechceu.gtceu.api.pattern.Predicates.*;
import static com.gregtechceu.gtceu.common.data.GCYMBlocks.CASING_INDUSTRIAL_STEAM;
import static com.gregtechceu.gtceu.common.data.GTBlocks.*;
import static com.gregtechceu.gtceu.common.data.GTMachines.COKE_OVEN_HATCH;
import static com.gregtechceu.gtceu.common.data.GTMachines.ITEM_EXPORT_BUS;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.COKE_OVEN_RECIPES;
import static com.gregtechceu.gtceu.common.data.models.GTMachineModels.createWorkableCasingMachineModel;
import static com.gregtechceu.gtceu.utils.FormattingUtil.formatNumbers;
import static com.gregtechceu.gtceu.common.data.GTMachines.*;

@SuppressWarnings("all")
public class AGEMultiMachines {

    public static final MultiblockMachineDefinition COKE_OVEN = REGISTRATE
            .multiblock("coke_oven", CokeOvenMachine::new)
            .rotationState(RotationState.ALL)
            .recipeType(COKE_OVEN_RECIPES)
            .appearanceBlock(CASING_COKE_BRICKS)
            .recipeModifier(CokeOvenMachine::recipeModifier)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("AAA", "AAA", "AAA")
                    .aisle("AAA", "A#A", "AAA").setRepeatable(1, 16)
                    .aisle("AAA", "A@A", "AAA")
                    .where("@", controller(blocks(definition.get())))
                    .where("A", blocks(CASING_COKE_BRICKS.get())
                            .or(blocks(COKE_OVEN_HATCH.get())))
                    .where("#", Predicates.air())
                    .build())
            .shapeInfos(definition -> {
                List<MultiblockShapeInfo> shapeInfos = new ArrayList<>();
                var builder = MultiblockShapeInfo.builder()
                        .where('C', definition, Direction.NORTH)
                        .where('B', CASING_COKE_BRICKS.getDefaultState())
                        .where('#', Blocks.AIR.defaultBlockState());
                for (int height = 3; height <= 18; height++) {
                    List<String[]> aisles = new ArrayList<>();
                    aisles.add(new String[] { "BBB", "BCB", "BBB" });
                    for (int i = 1; i < height - 1; i++) {
                        aisles.add(new String[] { "BBB", "B#B", "BBB" });
                    }
                    aisles.add(new String[] { "BBB", "BBB", "BBB" });
                    var copy = builder.shallowCopy();
                    for (String[] aisle : aisles) {
                        copy.aisle(aisle);
                    }
                    shapeInfos.add(copy.build());
                }
                return shapeInfos;
            })
            .tooltipBuilder((stack, tooltip) -> {
                tooltip.add(Component.translatable("astrogreg.machine.coke_oven_description.tooltip"));
                tooltip.add(Component
                        .translatable("astrogreg.machine.coke_oven_parallels.tooltip"));
            })
            .workableCasingModel(GTCEu.id("block/casings/solid/machine_coke_bricks"),
                    GTCEu.id("block/multiblock/coke_oven"))
            .register();

    public static final MultiblockMachineDefinition STEAM_BLAST_FURNACE = REGISTRATE
            .multiblock("steam_blast_furnace", SteamBlastFurnace::new)
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeType(AstroRecipeTypes.STEAM_BLAST_FURNACE_RECIPES)
            .recipeModifier(SteamBlastFurnace::recipeModifier)
            .addOutputLimit(ItemRecipeCapability.CAP, 1)
            .appearanceBlock(CASING_BRONZE_BRICKS)
            .hasBER(true)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("FFF", "XXX", "XXX", "XXX")
                    .aisle("FFF", "X#X", "X#X", "XMX")
                    .aisle("FFF", "X@X", "XXX", "XXX")
                    .where('@', controller(blocks(definition.getBlock())))
                    .where('X', blocks(CASING_BRONZE_BRICKS.get()).setMinGlobalLimited(6)
                            .or(abilities(STEAM_IMPORT_ITEMS).setPreviewCount(1))
                            .or(abilities(STEAM_EXPORT_ITEMS).setPreviewCount(1)))
                    .where('F', blocks(GTBlocks.FIREBOX_BRONZE.get())
                            .or(abilities(STEAM).setExactLimit(1)))
                    .where('#', air())
                    .where('M', abilities(MUFFLER))
                    .build())
            .modelProperty(GTMachineModelProperties.RECIPE_LOGIC_STATUS, RecipeLogic.Status.IDLE)
            .model(createWorkableCasingMachineModel(
                    GTCEu.id("block/casings/solid/machine_casing_bronze_plated_bricks"),
                    AstroCore.id("block/multiblock/steam_blast_furnace"))
                    .andThen(b -> b.addDynamicRenderer(
                            () -> new BoilerMultiPartRender(
                                    BoilerFireboxType.BRONZE_FIREBOX, CASING_BRONZE_BRICKS))))
            .register();

    public static final MultiblockMachineDefinition STEAM_MACERATOR = REGISTRATE
            .multiblock("large_steam_macerator", SteamGrinder::new)
            .langValue("Large Steam Grinder")
            .addOutputLimit(ItemRecipeCapability.CAP, 1)
            .rotationState(RotationState.ALL)
            .recipeType(GTRecipeTypes.MACERATOR_RECIPES)
            .recipeModifier(SteamGrinder::recipeModifier, true)
            .appearanceBlock(CASING_BRONZE_BRICKS)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("XXXXX", "XXXXX", "XXXXX")
                    .aisle("XXXXX", "XGGGX", "X   X")
                    .aisle("XXXXX", "XGGGX", "X   X")
                    .aisle("XXXXX", "XGGGX", "X   X")
                    .aisle("XXXXX", "XX@XX", "XXXXX")
                    .where("@", controller(blocks(definition.get())))
                    .where('X', blocks(CASING_BRONZE_BRICKS.get()).setMinGlobalLimited(14)
                            .or(abilities(STEAM).setExactLimit(1).setPreviewCount(1))
                            .or(abilities(STEAM_IMPORT_ITEMS).setPreviewCount(1).setMinGlobalLimited(1)
                                    .setMaxGlobalLimited(2))
                            .or(abilities(STEAM_EXPORT_ITEMS).setPreviewCount(1).setMinGlobalLimited(1)
                                    .setMaxGlobalLimited(2)))
                    .where(" ", air())
                    .where("G", blocks(BRONZE_CRUSHING_WHEELS.get()))
                    .build())
            .workableCasingModel(GTCEu.id("block/casings/solid/machine_casing_bronze_plated_bricks"),
                    GTCEu.id("block/multiblock/steam_grinder"))
            .register();

    public static final MultiblockMachineDefinition STEAM_COMPRESSOR = REGISTRATE
            .multiblock("large_steam_compressor", SteamParallelMultiblockMachine::new)
            .rotationState(RotationState.ALL)
            .recipeType(GTRecipeTypes.COMPRESSOR_RECIPES)
            .recipeModifier(SteamParallelMultiblockMachine::recipeModifier, true)
            .appearanceBlock(CASING_BRONZE_BRICKS)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle(" XXX ", " XXX ", "  X  ")
                    .aisle("XXXXX", "X###X", " XXX ")
                    .aisle("XXXXX", "X###X", "XXXXX")
                    .aisle("XXXXX", "X###X", " XXX ")
                    .aisle(" XXX ", " X@X ", "  X  ")
                    .where("@", controller(blocks(definition.get())))
                    .where('X', blocks(CASING_BRONZE_BRICKS.get()).setMinGlobalLimited(14)
                            .or(abilities(STEAM).setExactLimit(1).setPreviewCount(1))
                            .or(abilities(STEAM_IMPORT_ITEMS).setPreviewCount(1).setMinGlobalLimited(1)
                                    .setMaxGlobalLimited(2))
                            .or(abilities(STEAM_EXPORT_ITEMS).setPreviewCount(1).setMinGlobalLimited(1)
                                    .setMaxGlobalLimited(2)))
                    .where(" ", any())
                    .where("#", air())
                    .build())
            .workableCasingModel(GTCEu.id("block/casings/solid/machine_casing_bronze_plated_bricks"),
                    AstroCore.id("block/multiblock/steam_compressor"))
            .register();

    public static final MultiblockMachineDefinition STEAM_SEPARATOR = REGISTRATE
            .multiblock("large_steam_centrifuge", SteamParallelMultiblockMachine::new)
            .langValue("Large Steam Separator")
            .rotationState(RotationState.ALL)
            .recipeType(GTRecipeTypes.CENTRIFUGE_RECIPES)
            .recipeModifier(SteamParallelMultiblockMachine::recipeModifier, true)
            .appearanceBlock(CASING_INDUSTRIAL_STEAM)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("#XXX#", "XXXXX", "#XXX#")
                    .aisle("XXXXX", "XAPAX", "XXXXX")
                    .aisle("XXXXX", "XPAPX", "XXXXX")
                    .aisle("XXXXX", "XAPAX", "XXXXX")
                    .aisle("#XXX#", "XX@XX", "#XXX#")
                    .where("@", controller(blocks(definition.get())))
                    .where("P", blocks(CASING_BRONZE_GEARBOX.get()))
                    .where("X", blocks(CASING_INDUSTRIAL_STEAM.get()).setMinGlobalLimited(40)
                            .or(abilities(STEAM).setExactLimit(1).setPreviewCount(1))
                            .or(abilities(STEAM_IMPORT_ITEMS).setPreviewCount(1).setMinGlobalLimited(1)
                                    .setMaxGlobalLimited(3))
                            .or(abilities(STEAM_EXPORT_ITEMS).setPreviewCount(1).setMinGlobalLimited(1)
                                    .setMaxGlobalLimited(3)))
                    .where("A", air())
                    .where("#", any())
                    .build())
            .workableCasingModel(GTCEu.id("block/casings/gcym/industrial_steam_casing"),
                    AstroCore.id("block/multiblock/steam_centrifuge"))
            .register();

    public static final MultiblockMachineDefinition STEAM_WASHER = REGISTRATE
            .multiblock("large_steam_ore_washer", SteamWasher::new)
            .langValue("Large Steam Washer")
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeType(GTRecipeTypes.ORE_WASHER_RECIPES)
            .recipeModifier(SteamWasher::recipeModifier, true)
            .appearanceBlock(CASING_INDUSTRIAL_STEAM)
            .hasBER(true)
            .allowFlip(false)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("XXXXXXX", "XXXXXXX", "XXXXXXX")
                    .aisle("XXXXXXX", "XP###PX", "X#####X")
                    .aisle("XXXXXXX", "XP###PX", "X#####X")
                    .aisle("XXXXXXX", "XP###PX", "X#####X")
                    .aisle("XXXXXXX", "XXX@XXX", "XXXXXXX")
                    .where("@", controller(blocks(definition.get())))
                    .where("P", blocks(CASING_BRONZE_PIPE.get()))
                    .where("X", blocks(CASING_INDUSTRIAL_STEAM.get()).setMinGlobalLimited(55)
                            .or(abilities(STEAM).setExactLimit(1))
                            .or(abilities(STEAM_IMPORT_ITEMS).setPreviewCount(1).setMinGlobalLimited(1)
                                    .setMaxGlobalLimited(3))
                            .or(abilities(STEAM_EXPORT_ITEMS).setPreviewCount(1).setMinGlobalLimited(1)
                                    .setMaxGlobalLimited(3))
                            .or(blocks(WATER_HATCH.get()).setPreviewCount(1).setMaxGlobalLimited(2)))
                    .where("#", air())
                    .build())
            .modelProperty(GTMachineModelProperties.RECIPE_LOGIC_STATUS, RecipeLogic.Status.IDLE)
            .model(createWorkableCasingMachineModel(GTCEu.id("block/casings/gcym/industrial_steam_casing"),
                    AstroCore.id("block/multiblock/steam_ore_washer"))
                    .andThen(b -> b.addDynamicRenderer(DynamicRenderHelper::makeRecipeFluidAreaRender)))
            .register();

    public static final MultiblockMachineDefinition SOLAR_BOILER_ARRAY = REGISTRATE
            .multiblock("solar_boiler_array", AstroSolarBoilers::new)
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeType(GTRecipeTypes.DUMMY_RECIPES)
            .appearanceBlock(GTBlocks.CASING_STEEL_SOLID)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("AAA")
                    .aisle("ABA")
                    .aisle("A@A")
                    .where('@', controller(blocks(definition.get())))
                    .where('A', blocks(GTBlocks.CASING_STEEL_SOLID.get())
                            .or(abilities(IMPORT_FLUIDS)).setMaxGlobalLimited(2)
                            .or(abilities(EXPORT_FLUIDS)).setMaxGlobalLimited(2)
                            .or(abilities(MAINTENANCE)).setExactLimit(1))
                    .where('B', blocks(SOLAR_CELL.get())
                            .or(blocks(SOLAR_CELL_ETRIUM.get()))
                            .or(blocks(SOLAR_CELL_VESNIUM.get())))
                    .build())
            .shapeInfos(definition -> {
                var shapes = new java.util.ArrayList<MultiblockShapeInfo>();
                for (int size = 5; size <= 35; size += 2) {
                    shapes.add(createSolarBoilerShape(definition, size));
                }

                return shapes;
            })
            .allowFlip(false)
            .workableCasingModel(GTCEu.id("block/casings/solid/machine_casing_solid_steel"),
                    GTCEu.id("block/multiblock/generator/large_steel_boiler"))
            .tooltipBuilder((stack, tooltip) -> {
                tooltip.add(Component
                        .translatable("astrogreg.machine.solar_boiler_array_sunlit_info.tooltip")
                        .withStyle(ChatFormatting.WHITE));
                tooltip.add(Component
                        .translatable("astrogreg.machine.solar_boiler_array_heat_speed.tooltip")
                        .withStyle(ChatFormatting.WHITE));
                tooltip.add(Component
                        .translatable(
                                "astrogreg.machine.solar_boiler_array_heat_scaling.tooltip")
                        .withStyle(ChatFormatting.AQUA));
                tooltip.add(Component.translatable("astrogreg.machine.solar_boiler_array_max_cells.tooltip")
                        .withStyle(ChatFormatting.AQUA));
            })
            .register();

    public static final MultiblockMachineDefinition INDUSTRIAL_AUTOCLAVE = registerIndustrialMachine(
            "industrial_autoclave",
            "Industrial Autoclave",
            MACHINE_CASING_STYRENE_BUTADIENE,
            AstroCore.id("block/casings/industrial_casings/machine_casing_styrene_butadiene_rubber"),
            AstroCore.id("block/multiblock/autoclave"),
            GTRecipeTypes.AUTOCLAVE_RECIPES);

    public static final MultiblockMachineDefinition INDUSTRIAL_BENDER = registerIndustrialMachine(
            "industrial_bender",
            "Industrial Material Press",
            MACHINE_CASING_ULTIMET,
            AstroCore.id("block/casings/industrial_casings/machine_casing_ultimet"),
            AstroCore.id("block/multiblock/bender"),
            GTRecipeTypes.BENDER_RECIPES, GTRecipeTypes.FORGE_HAMMER_RECIPES);

    public static final MultiblockMachineDefinition INDUSTRIAL_CENTRIFUGE = registerIndustrialMachine(
            "industrial_centrifuge",
            "Industrial Centrifugal Unit",
            MACHINE_CASING_RED_STEEL,
            AstroCore.id("block/casings/industrial_casings/machine_casing_red_steel"),
            AstroCore.id("block/multiblock/centrifuge"),
            GTRecipeTypes.CENTRIFUGE_RECIPES, GTRecipeTypes.THERMAL_CENTRIFUGE_RECIPES);

    public static final MultiblockMachineDefinition INDUSTRIAL_ELECTROLYZER = registerIndustrialMachine(
            "industrial_electrolyzer",
            "Industrial Electrolyzer",
            MACHINE_CASING_BLUE_STEEL,
            AstroCore.id("block/casings/industrial_casings/machine_casing_blue_steel"),
            AstroCore.id("block/multiblock/electrolyzer"),
            GTRecipeTypes.ELECTROLYZER_RECIPES);

    public static final MultiblockMachineDefinition INDUSTRIAL_CHEMICAL_BATH = registerIndustrialMachine(
            "industrial_chemical_bath",
            "Industrial Chemical Bath",
            MACHINE_CASING_POLYVINYL_CHLORIDE,
            AstroCore.id("block/casings/industrial_casings/machine_casing_polyvinyl_chloride"),
            AstroCore.id("block/multiblock/ore_washer"),
            GTRecipeTypes.ORE_WASHER_RECIPES, GTRecipeTypes.CHEMICAL_BATH_RECIPES);

    public static final MultiblockMachineDefinition INDUSTRIAL_EXTRUDER = registerIndustrialMachine(
            "industrial_extruder",
            "Industrial Extruder",
            MACHINE_CASING_BLACK_STEEL,
            AstroCore.id("block/casings/industrial_casings/machine_casing_black_steel"),
            AstroCore.id("block/multiblock/extruder"),
            GTRecipeTypes.EXTRUDER_RECIPES);

    public static final MultiblockMachineDefinition INDUSTRIAL_FLUID_SOLIDIFIER = registerIndustrialMachine(
            "industrial_fluid_solidifier",
            "Industrial Fluid Solidifier",
            MACHINE_CASING_SILICONE_RUBBER,
            AstroCore.id("block/casings/industrial_casings/machine_casing_silicone_rubber"),
            AstroCore.id("block/multiblock/fluid_solidifier"),
            GTRecipeTypes.FLUID_SOLIDFICATION_RECIPES);

    public static final MultiblockMachineDefinition INDUSTRIAL_LATHE = registerIndustrialMachine(
            "industrial_lathe",
            "Industrial Lathe",
            MACHINE_CASING_ROSE_GOLD,
            AstroCore.id("block/casings/industrial_casings/machine_casing_rose_gold"),
            AstroCore.id("block/multiblock/lathe"),
            GTRecipeTypes.LATHE_RECIPES);

    public static final MultiblockMachineDefinition INDUSTRIAL_MACERATOR = registerIndustrialMachine(
            "industrial_",
            "Industrial ",
            MACHINE_CASING_CARBON_FIBER_MESH,
            AstroCore.id("block/casings/industrial_casings/machine_casing_carbon_fiber_mesh"),
            AstroCore.id("block/multiblock/macerator"),
            GTRecipeTypes.MACERATOR_RECIPES);

    public static final MultiblockMachineDefinition INDUSTRIAL_MIXER = registerIndustrialMachine(
            "industrial_mixer",
            "Industrial Mixer",
            MACHINE_CASING_VANADIUM_STEEL,
            AstroCore.id("block/casings/industrial_casings/machine_casing_vanadium_steel"),
            AstroCore.id("block/multiblock/mixer"),
            GTRecipeTypes.MIXER_RECIPES);

    public static final MultiblockMachineDefinition INDUSTRIAL_SIFTER = registerIndustrialMachine(
            "industrial_sifter",
            "Industrial Sifter",
            MACHINE_CASING_BISMUTH_BRONZE,
            AstroCore.id("block/casings/industrial_casings/machine_casing_bismuth_bronze"),
            AstroCore.id("block/multiblock/sifter"),
            GTRecipeTypes.SIFTER_RECIPES);

    public static final MultiblockMachineDefinition INDUSTRIAL_WIREMILL = registerIndustrialMachine(
            "industrial_wiremill",
            "Industrial Wiremill",
            MACHINE_CASING_COBALT_BRASS,
            AstroCore.id("block/casings/industrial_casings/machine_casing_cobalt_brass"),
            AstroCore.id("block/multiblock/wiremill"),
            GTRecipeTypes.WIREMILL_RECIPES);

    public static final MultiblockMachineDefinition FLUID_DRILLING_RIG_IV = REGISTRATE
            .multiblock("fluid_drilling_rig_iv", holder -> new FluidDrillMachine(holder, GTValues.IV))
            .rotationState(RotationState.ALL)
            .langValue("§9Elite Fluid Drilling Rig")
            .recipeType(GTRecipeTypes.DUMMY_RECIPES)
            .tooltips(
                    Component.translatable("astrogreg.machine.fluid_drilling_rig.iv.tooltip"),
                    Component.translatable("gtceu.machine.fluid_drilling_rig.description"),
                    Component.translatable("gtceu.machine.fluid_drilling_rig.depletion", formatNumbers(3)),
                    Component.translatable("gtceu.universal.tooltip.energy_tier_range", GTValues.VNF[GTValues.LuV],
                            GTValues.VNF[GTValues.ZPM]),
                    Component.translatable("gtceu.machine.fluid_drilling_rig.production", 256,
                            formatNumbers(384)))
            .appearanceBlock(() -> MACHINE_CASING_RHODIUM_PLATED_PALLADIUM.get())
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("#XXX#", "#####", "#####", "#####", "#####", "#####", "#####", "#####", "#####", "#####")
                    .aisle("XXXXX", "#FFF#", "#FFF#", "#FFF#", "##F##", "##F##", "##F##", "#####", "#####", "#####")
                    .aisle("XXXXX", "#FCF#", "#FCF#", "#FXF#", "#FCF#", "#FCF#", "#FCF#", "##F##", "##F##", "##F##")
                    .aisle("XXXXX", "#FFF#", "#FFF#", "#FFF#", "##F##", "##F##", "##F##", "#####", "#####", "#####")
                    .aisle("#XSX#", "#####", "#####", "#####", "#####", "#####", "#####", "#####", "#####", "#####")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(MACHINE_CASING_RHODIUM_PLATED_PALLADIUM.get()).setMinGlobalLimited(3)
                            .or(abilities(INPUT_ENERGY).setMinGlobalLimited(1).setMaxGlobalLimited(2))
                            .or(abilities(EXPORT_FLUIDS).setMaxGlobalLimited(1)))
                    .where('C', blocks(MACHINE_CASING_RHODIUM_PLATED_PALLADIUM.get()))
                    .where('F', frames(GTMaterials.RhodiumPlatedPalladium))
                    .where('#', any())
                    .build())
            .workableCasingModel(AstroCore.id("block/casings/machine_casing_pristine_rhodium_plated_palladium"),
                    GTCEu.id("block/multiblock/fluid_drilling_rig"))
            .register();

    public static final MultiblockMachineDefinition LARGE_MINER_ZPM = REGISTRATE
            .multiblock("large_miner_zpm", holder -> new LargeMinerMachine(holder, GTValues.ZPM, 64 / GTValues.ZPM,
                    2 * GTValues.ZPM - 5, GTValues.ZPM, 6))
            .rotationState(RotationState.NON_Y_AXIS)
            .langValue("§cElite Large Ore Miner III")
            .recipeType(GTRecipeTypes.MACERATOR_RECIPES)
            .appearanceBlock(() -> MACHINE_CASING_RHODIUM_PLATED_PALLADIUM.get())
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("#XXX#", "#####", "#####", "#####", "#####", "#####", "#####", "#####", "#####", "#####")
                    .aisle("XXXXX", "#FFF#", "#FFF#", "#FFF#", "##F##", "##F##", "##F##", "#####", "#####", "#####")
                    .aisle("XXXXX", "#FCF#", "#FCF#", "#FCF#", "#FCF#", "#FCF#", "#FCF#", "##F##", "##F##", "##F##")
                    .aisle("XXXXX", "#FFF#", "#FFF#", "#FFF#", "##F##", "##F##", "##F##", "#####", "#####", "#####")
                    .aisle("#XSX#", "#####", "#####", "#####", "#####", "#####", "#####", "#####", "#####", "#####")
                    .where('S', controller(blocks(definition.getBlock())))
                    .where('X', blocks(MACHINE_CASING_RHODIUM_PLATED_PALLADIUM.get())
                            .or(abilities(EXPORT_ITEMS).setExactLimit(1).setPreviewCount(1))
                            .or(abilities(IMPORT_FLUIDS).setExactLimit(1).setPreviewCount(1))
                            .or(abilities(INPUT_ENERGY).setMinGlobalLimited(1).setMaxGlobalLimited(2)
                                    .setPreviewCount(1)))
                    .where('C', blocks(MACHINE_CASING_RHODIUM_PLATED_PALLADIUM.get()))
                    .where('F', frames(GTMaterials.RhodiumPlatedPalladium))
                    .where('#', any())
                    .build())
            .allowExtendedFacing(true)
            .modelProperty(GTMachineModelProperties.RECIPE_LOGIC_STATUS, RecipeLogic.Status.IDLE)
            .model(createWorkableCasingMachineModel(
                    AstroCore.id("block/casings/machine_casing_pristine_rhodium_plated_palladium"),
                    GTCEu.id("block/multiblock/large_miner"))
                    .andThen((ctx, prov, modelBuilder) -> {
                        modelBuilder.replaceForAllStates((state, models) -> {
                            if (!state.getValue(GTMachineModelProperties.IS_FORMED)) {
                                return models;
                            }
                            var parentModel = prov.models()
                                    .getExistingFile(GTCEu.id("block/machine/large_miner_active"));
                            for (var model : models) {
                                ((BlockModelBuilder) model.model)
                                        .parent(parentModel);
                            }
                            return models;
                        });
                    }))
            .tooltips(Component.translatable("astrogreg.machine.large_miner.zpm.tooltip"),
                    Component.translatable("gtceu.machine.miner.multi.description"))
            .tooltipBuilder((stack, tooltip) -> {
                int workingAreaChunks = 2 * GTValues.ZPM - 5;
                tooltip.add(Component.translatable("gtceu.machine.miner.multi.modes"));
                tooltip.add(Component.translatable("gtceu.machine.miner.multi.production"));
                tooltip.add(Component.translatable("gtceu.machine.miner.fluid_usage", 6,
                        GTMaterials.DrillingFluid.getLocalizedName()));
                tooltip.add(Component.translatable("gtceu.universal.tooltip.working_area_chunks", workingAreaChunks,
                        workingAreaChunks));
                tooltip.add(Component.translatable("gtceu.universal.tooltip.energy_tier_range",
                        GTValues.VNF[GTValues.UV], GTValues.VNF[GTValues.UHV]));
            })
            .register();

    private static MultiblockShapeInfo createSolarBoilerShape(MultiblockMachineDefinition definition, int size) {
        var builder = MultiblockShapeInfo.builder();
        int center = size / 2;
        for (int z = 0; z < size; z++) {
            var layer = new StringBuilder();
            for (int x = 0; x < size; x++) {
                if (z == 0) {
                    if (x == center - 1) layer.append('D');
                    else if (x == center) layer.append('@');
                    else if (x == center + 1) layer.append('C');
                    else if (x == 0) layer.append('E');
                    else layer.append('A');
                } else if (z == size - 1 || x == 0 || x == size - 1) {
                    layer.append('A');
                } else {
                    layer.append('B');
                }
            }
            builder.aisle(layer.toString());
        }
        return builder
                .where('@', definition, Direction.NORTH)
                .where('A', GTBlocks.CASING_STEEL_SOLID.getDefaultState())
                .where('B', SOLAR_CELL.get().defaultBlockState())
                .where('C', GTMachines.FLUID_EXPORT_HATCH[GTValues.LV], Direction.NORTH)
                .where('D', GTMachines.FLUID_IMPORT_HATCH[GTValues.LV], Direction.NORTH)
                .where('E', GTMachines.MAINTENANCE_HATCH, Direction.NORTH)
                .build();
    }

    public static MultiblockMachineDefinition registerIndustrialMachine(String id, String lang,
                                                                        Supplier<? extends Block> casing,
                                                                        ResourceLocation baseTexture,
                                                                        ResourceLocation overlayTexture,
                                                                        GTRecipeType... recipeTypes) {
        return REGISTRATE
                .multiblock(id, WorkableElectricMultiblockMachine::new)
                .langValue(lang)
                .rotationState(RotationState.ALL)
                .recipeTypes(recipeTypes)
                .recipeModifiers((machine, recipe) -> ProcessingCoreMachine.processingCoreOverclock(
                        machine, recipe,
                        INDUSTRIAL_PROCESSING_CORE_MK1.get(),
                        INDUSTRIAL_PROCESSING_CORE_MK2.get(),
                        INDUSTRIAL_PROCESSING_CORE_MK3.get()),
                        GTRecipeModifiers.OC_PERFECT_SUBTICK, GTRecipeModifiers.BATCH_MODE)
                .appearanceBlock(casing)
                .pattern(definition -> FactoryBlockPattern.start()
                        .aisle("XXX", "XXX", "XXX")
                        .aisle("XXX", "XCX", "XXX")
                        .aisle("XXX", "X@X", "XXX")
                        .where('@', controller(blocks(definition.getBlock())))
                        .where('X', blocks(casing.get()).setMinGlobalLimited(15)
                                .or(abilities(INPUT_ENERGY).setExactLimit(1))
                                .or(abilities(MAINTENANCE).setExactLimit(1))
                                .or(abilities(IMPORT_ITEMS).setMaxGlobalLimited(2).setPreviewCount(1))
                                .or(abilities(EXPORT_ITEMS).setMaxGlobalLimited(2).setPreviewCount(1))
                                .or(abilities(IMPORT_FLUIDS).setMaxGlobalLimited(2).setPreviewCount(1))
                                .or(abilities(EXPORT_FLUIDS).setMaxGlobalLimited(2).setPreviewCount(1)))
                        .where('C', blocks(INDUSTRIAL_PROCESSING_CORE_MK1.get())
                                .or(blocks(INDUSTRIAL_PROCESSING_CORE_MK2.get()))
                                .or(blocks(INDUSTRIAL_PROCESSING_CORE_MK3.get())))
                        .build())
                .tooltips(Component.translatable("astrogreg.machine.industrial_core.tooltip"))
                .tooltips(recipeTypes.length == 1 ?
                        Component.translatable("gtceu.machine.available_recipe_map_1.tooltip",
                                Component.translatable("gtceu." + recipeTypes[0].registryName.getPath().replace("_recipes", ""))) :
                        Component.translatable("gtceu.machine.available_recipe_map_2.tooltip",
                                Component.translatable("gtceu." + recipeTypes[0].registryName.getPath().replace("_recipes", "")),
                                Component.translatable("gtceu." + recipeTypes[1].registryName.getPath().replace("_recipes", ""))))
                .tooltips(Component.translatable("gtceu.multiblock.exact_hatch_1.tooltip"))
                .workableCasingModel(baseTexture, overlayTexture)
                .register();
    }

    public static void init() {}
}
