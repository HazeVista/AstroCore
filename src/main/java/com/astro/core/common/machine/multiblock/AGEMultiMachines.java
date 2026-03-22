package com.astro.core.common.machine.multiblock;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.property.GTMachineModelProperties;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.MultiblockShapeInfo;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.client.renderer.machine.DynamicRenderHelper;
import com.gregtechceu.gtceu.client.renderer.machine.impl.BoilerMultiPartRender;
import com.gregtechceu.gtceu.common.block.BoilerFireboxType;
import com.gregtechceu.gtceu.common.data.*;
import com.gregtechceu.gtceu.common.machine.multiblock.steam.SteamParallelMultiblockMachine;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.model.generators.BlockModelBuilder;

import com.astro.core.AstroCore;
import com.astro.core.client.AstroMachineModels;
import com.astro.core.client.renderer.machine.AEMultiPartRender;
import com.astro.core.common.data.recipe.AstroRecipeTypes;
import com.astro.core.common.machine.multiblock.electric.FluidDrillMachine;
import com.astro.core.common.machine.multiblock.electric.LargeMinerMachine;
import com.astro.core.common.machine.multiblock.electric.ProcessingCoreMachine;
import com.astro.core.common.machine.multiblock.electric.planetary_research.IndustrialAstroPortMachine;
import com.astro.core.common.machine.multiblock.electric.planetary_research.IndustrialObservatoryMachine;
import com.astro.core.common.machine.multiblock.kinetic.*;
import com.astro.core.common.machine.multiblock.primitive.CokeOvenMachine;
import com.astro.core.common.machine.multiblock.steam.SteamBlastFurnace;
import com.astro.core.common.machine.multiblock.steam.SteamGrinder;
import com.astro.core.common.machine.multiblock.steam.SteamWasher;
import com.astro.core.common.machine.trait.AstroPartAbility;
import earth.terrarium.adastra.common.registry.ModBlocks;

import java.util.ArrayList;
import java.util.List;

import static com.astro.core.common.data.AstroBlocks.*;
import static com.astro.core.common.data.recipe.AstroRecipeTypes.ASTROPORT_RECIPES;
import static com.astro.core.common.data.recipe.AstroRecipeTypes.OBSERVATORY_RECIPES;
import static com.astro.core.common.machine.part.AstroHatches.*;
import static com.astro.core.common.registry.AstroRegistry.REGISTRATE;
import static com.gregtechceu.gtceu.api.machine.multiblock.PartAbility.*;
import static com.gregtechceu.gtceu.api.pattern.Predicates.*;
import static com.gregtechceu.gtceu.api.pattern.util.RelativeDirection.*;
import static com.gregtechceu.gtceu.common.data.GCYMBlocks.CASING_INDUSTRIAL_STEAM;
import static com.gregtechceu.gtceu.common.data.GTBlocks.*;
import static com.gregtechceu.gtceu.common.data.GTMachines.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeModifiers.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeModifiers.PARALLEL_HATCH;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.COKE_OVEN_RECIPES;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.DUMMY_RECIPES;
import static com.gregtechceu.gtceu.common.data.machines.GTResearchMachines.DATA_ACCESS_HATCH;
import static com.gregtechceu.gtceu.common.data.models.GTMachineModels.createWorkableCasingMachineModel;
import static com.gregtechceu.gtceu.utils.FormattingUtil.formatNumbers;

@SuppressWarnings("all")
public class AGEMultiMachines {

    // Primitive Machines
    public static final MultiblockMachineDefinition COKE_OVEN = REGISTRATE
            .multiblock("coke_oven", CokeOvenMachine::new)
            .rotationState(RotationState.ALL)
            .recipeType(COKE_OVEN_RECIPES)
            .appearanceBlock(CASING_COKE_BRICKS)
            .recipeModifier(CokeOvenMachine::recipeModifier)
            .pattern(definition -> FactoryBlockPattern.start(LEFT, UP, BACK)
                    .aisle("AAA", "A@A", "AAA")
                    .aisle("AAA", "A#A", "AAA").setRepeatable(1, 16)
                    .aisle("AAA", "AAA", "AAA")
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

    // Steam Machines
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
                    .where('@', controller(blocks(definition.get())))
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

    // Kinetic Machines
    public static final MultiblockMachineDefinition KINETIC_STEAM_ENGINE = REGISTRATE
            .multiblock("kinetic_steam_engine", KineticSteamEngineMachine::new)
            .langValue("Kinetic Steam Engine")
            .rotationState(RotationState.ALL)
            .recipeType(GTRecipeTypes.DUMMY_RECIPES)
            .appearanceBlock(CASING_BRONZE_BRICKS)
            .pattern(definition -> FactoryBlockPattern.start(LEFT, UP, FRONT)
                    .aisle("FFF", "TOT", "GGG", " C ")
                    .aisle("FFF", "TBT", "GPG", " C ").setRepeatable(1, 16)
                    .aisle("FFF", "TIT", "GGG", " @ ")
                    .where('@', controller(blocks(definition.get())))
                    .where('F', blocks(FIREBOX_BRONZE.get()))
                    .where('T', blocks(CASING_TEMPERED_GLASS.get())
                            .or(blocks(CASING_BRONZE_BRICKS.get())))
                    .where('B', Predicates.blocks(CASING_BRONZE_GEARBOX.get())
                            .or(Predicates.blocks(CASING_STEEL_GEARBOX.get())))
                    .where('I', abilities(STEAM))
                    .where('O', abilities(AstroPartAbility.KINETIC_OUTPUT))
                    .where('G', blocks(STEAM_ENGINE_GRATING.get()))
                    .where('C', blocks(CASING_BRONZE_BRICKS.get()))
                    .where('P', blocks(CASING_BRONZE_PIPE.get()))
                    .where(' ', any())
                    .build())
            .workableCasingModel(GTCEu.id("block/casings/solid/machine_casing_bronze_plated_bricks"),
                    AstroCore.id("block/multiblock/steam_engine"))
            .shapeInfos(definition -> {
                List<MultiblockShapeInfo> shapeInfos = new ArrayList<>();

                for (int length = 1; length <= 16; length++) {
                    MultiblockShapeInfo.ShapeInfoBuilder builder = MultiblockShapeInfo.builder()
                            .where('@', definition, Direction.NORTH)
                            .where('F', FIREBOX_BRONZE.getDefaultState())
                            .where('T', CASING_TEMPERED_GLASS.getDefaultState())
                            .where('B', CASING_BRONZE_GEARBOX.getDefaultState())
                            .where('I', STEAM_HATCH, Direction.NORTH)
                            .where('O', KINETIC_OUTPUT_HATCH, Direction.SOUTH)
                            .where('G', STEAM_ENGINE_GRATING.getDefaultState())
                            .where('C', CASING_BRONZE_BRICKS.getDefaultState())
                            .where('P', CASING_BRONZE_PIPE.getDefaultState())
                            .where(' ', Blocks.AIR.defaultBlockState());

                    builder.aisle("FFF", "TIT", "GGG", " @ ");
                    builder.aisle("FFF", "TBT", "GPG", " C ");

                    for (int i = 0; i < length - 1; i++) {
                        builder.aisle("FFF", "TBT", "GPG", " C ");
                    }

                    builder.aisle("FFF", "TOT", "GGG", " C ");
                    shapeInfos.add(builder.build());
                }

                return shapeInfos;
            })
            .tooltips(Component.translatable("astrogreg.machine.kinetic_steam_engine_production.tooltip"),
                    Component.translatable("astrogreg.machine.kinetic_steam_engine_steel_gearbox.tooltips"),
                    Component.translatable("astrogreg.machine.kinetic_steam_engine_max_layers.tooltip"))
            .register();

    public static final MultiblockMachineDefinition KINETIC_MIXING_VESSEL = REGISTRATE
            .multiblock("large_kinetic_mixer", KineticMixerMachine::new)
            .langValue("Kinetic Mixing Vessel")
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeType(GTRecipeTypes.MIXER_RECIPES)
            .appearanceBlock(MACHINE_CASING_KINETIC)
            .recipeModifier(KineticMixerMachine::recipeModifier, true)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("#XXX#", "#XXX#", "#XXX#", "#XXX#", "#XXX#", "##F##")
                    .aisle("XXXXX", "XAPAX", "XAAAX", "XAPAX", "XAAAX", "##F##")
                    .aisle("XXXXX", "XPPPX", "XAPAX", "XPPPX", "XAGAX", "FFGFF")
                    .aisle("XXXXX", "XAPAX", "XAAAX", "XAPAX", "XAAAX", "##F##")
                    .aisle("#XXX#", "#X@X#", "#XXX#", "#XXX#", "#XXX#", "##F##")
                    .where('@', controller(blocks(definition.get())))
                    .where('X', blocks(MACHINE_CASING_KINETIC.get()).setMinGlobalLimited(50)
                            .or(abilities(EXPORT_ITEMS).setMaxGlobalLimited(2))
                            .or(abilities(IMPORT_ITEMS).setMaxGlobalLimited(2))
                            .or(abilities(EXPORT_FLUIDS).setMaxGlobalLimited(2))
                            .or(abilities(IMPORT_FLUIDS).setMaxGlobalLimited(2))
                            .or(abilities(AstroPartAbility.KINETIC_INPUT).setExactLimit(1)))
                    .where('F', frames(GTMaterials.Bronze))
                    .where('G', blocks(CASING_BRONZE_GEARBOX.get()))
                    .where('P', blocks(CASING_BRONZE_PIPE.get()))
                    .where('A', Predicates.air())
                    .where('#', Predicates.any())
                    .build())
            .tooltips(Component.translatable("astrogreg.machine.large_kinetic_machine_parallels.tooltip"),
                    Component.translatable("astrogreg.machine.large_kinetic_machine_recipes.tooltip"))
            .model(createWorkableCasingMachineModel(AstroCore.id("block/casings/machine_casing_kinetic"),
                    AstroCore.id("block/multiblock/mixer"))
                    .andThen(b -> b.addDynamicRenderer(DynamicRenderHelper::makeRecipeFluidAreaRender)))
            .register();

    public static final MultiblockMachineDefinition KINETIC_CONCRETE_PLANT = REGISTRATE
            .multiblock("kinetic_concrete_plant", KineticParallelMultiblockMachine::new)
            .langValue("Kinetic Concrete Plant")
            .rotationState(RotationState.ALL)
            .recipeType(AstroRecipeTypes.CONCRETE_PLANT)
            .appearanceBlock(MACHINE_CASING_KINETIC)
            .recipeModifier(KineticParallelMultiblockMachine::recipeModifier, true)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle(" XXX ", "XXXXX", "XXKXX", "XXXXX", " XXX ")
                    .aisle(" XXX ", "X###X", "X#P#X", "X###X", " XXX ")
                    .aisle(" XXX ", "X#G#X", "XGPGX", "X#G#X", " XXX ")
                    .aisle(" XXX ", "X###X", "X#P#X", "X###X", " XXX ")
                    .aisle(" XXX ", "XX#XX", "XXXXX", "XXXXX", " XXX ")
                    .aisle(" XXX ", " X@X ", "  X  ", "     ", "     ")
                    .where("@", controller(blocks(definition.get())))
                    .where("K", abilities(AstroPartAbility.KINETIC_INPUT))
                    .where("X", blocks(MACHINE_CASING_KINETIC.get()).setMinGlobalLimited(50)
                            .or(abilities(IMPORT_FLUIDS).setMaxGlobalLimited(2).setPreviewCount(1))
                            .or(abilities(IMPORT_ITEMS).setMaxGlobalLimited(2).setPreviewCount(1))
                            .or(abilities(EXPORT_FLUIDS).setMaxGlobalLimited(2).setPreviewCount(1))
                            .or(abilities(EXPORT_ITEMS).setMaxGlobalLimited(2).setPreviewCount(1)))
                    .where("G", blocks(CASING_BRONZE_GEARBOX.get()))
                    .where("P", blocks(CASING_BRONZE_PIPE.get()))
                    .where("#", air())
                    .where(" ", any())
                    .build())
            .tooltips(Component.translatable("astrogreg.machine.large_kinetic_machine_parallels.tooltip"),
                    Component.translatable("astrogreg.machine.large_kinetic_machine_recipes.tooltip"))
            .workableCasingModel(AstroCore.id("block/casings/machine_casing_kinetic"),
                    AstroCore.id("block/multiblock/concrete_mixer"))
            .register();

    public static final MultiblockMachineDefinition KINETIC_LARGE_ALTERNATOR = REGISTRATE
            .multiblock("large_kinetic_alternator", KineticAlternatorMachine::new)
            .langValue("Large Kinetic Alternator")
            .rotationState(RotationState.ALL)
            .recipeType(DUMMY_RECIPES)
            .appearanceBlock(MACHINE_CASING_KINETIC)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("XXXX", "XXXX", "XXXX")
                    .aisle("XXXX", "HGGH", "XXXX")
                    .aisle("XXXX", "X@XX", "XXXX")
                    .where("@", controller(blocks(definition.get())))
                    .where("X", blocks(MACHINE_CASING_KINETIC.get()))
                    .where("G", blocks(CASING_BRONZE_GEARBOX.get()))
                    .where("H", abilities(AstroPartAbility.KINETIC_INPUT).setExactLimit(1)
                            .or(abilities(OUTPUT_ENERGY).setExactLimit(1)))
                    .build())
            .tooltips(Component.translatable("astrogreg.machine.large_kinetic_alternator_production.tooltip"),
                    Component.translatable("astrogreg.machine.large_kinetic_alternator_max_production.tooltip"))
            .workableCasingModel(AstroCore.id("block/casings/machine_casing_kinetic"),
                    AstroCore.id("block/multiblock/kinetic_alternator"))
            .register();

    public static final MultiblockMachineDefinition KINETIC_COMBUSTION_ENGINE = REGISTRATE
            .multiblock("kinetic_combustion_engine", KineticCombustionEngineMachine::new)
            .langValue("Kinetic Combustion Engine")
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeType(AstroRecipeTypes.KINETIC_COMBUSTION_RECIPES)
            .recipeModifiers(KineticCombustionEngineMachine::recipeModifier, BATCH_MODE)
            .appearanceBlock(CASING_TITANIUM_STABLE)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("XXX", "XKX", "XXX")
                    .aisle("XHX", "HGH", "XHX")
                    .aisle("XHX", "HGH", "XHX")
                    .aisle("EEE", "E@E", "EEE")
                    .where("@", controller(blocks(definition.get())))
                    .where("H", blocks(CASING_TITANIUM_STABLE.get())
                            .or(abilities(MAINTENANCE).setExactLimit(1))
                            .or(abilities(IMPORT_FLUIDS).setMaxGlobalLimited(4).setPreviewCount(1))
                            .or(abilities(MUFFLER).setExactLimit(1)))
                    .where("E", blocks(CASING_ENGINE_INTAKE.get()))
                    .where("K", blocks(KINETIC_OUTPUT_HATCH.get()))
                    .where("X", blocks(CASING_TITANIUM_STABLE.get()))
                    .where("G", blocks(CASING_TITANIUM_GEARBOX.get()))
                    .build())
            .workableCasingModel(GTCEu.id("block/casings/solid/machine_casing_stable_titanium"),
                    GTCEu.id("block/multiblock/generator/large_combustion_engine"))
            .tooltips(Component.translatable("astrogreg.machine.kinetic_combustion_engine.tooltip_0"),
                    Component.translatable("astrogreg.machine.kinetic_combustion_engine.tooltip_1"),
                    Component.translatable("gtceu.universal.tooltip.uses_per_hour_lubricant",
                            KineticCombustionEngineMachine.LUBRICANT_MB_PER_HOUR),
                    Component.translatable("astrogreg.machine.kinetic_combustion_engine.tooltip_2"))
            .register();

    // Pre-Industrial Multiblocks
    public static final MultiblockMachineDefinition CONCRETE_PLANT = REGISTRATE
            .multiblock("concrete_plant", WorkableElectricMultiblockMachine::new)
            .langValue("Concrete Plant")
            .rotationState(RotationState.ALL)
            .recipeType(AstroRecipeTypes.CONCRETE_PLANT)
            .appearanceBlock(CASING_STEEL_SOLID)
            .recipeModifiers(OC_NON_PERFECT_SUBTICK, PARALLEL_HATCH, BATCH_MODE)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle(" XXX ", "XXXXX", "XXXXX", "XXXXX", " XXX ")
                    .aisle(" XXX ", "X###X", "X#P#X", "X###X", " XXX ")
                    .aisle(" XXX ", "X#G#X", "XGPGX", "X#G#X", " XXX ")
                    .aisle(" XXX ", "X###X", "X#P#X", "X###X", " XXX ")
                    .aisle(" XXX ", "XX#XX", "XXXXX", "XXXXX", " XXX ")
                    .aisle(" XXX ", " X@X ", "  X  ", "     ", "     ")
                    .where("@", controller(blocks(definition.get())))
                    .where("X", blocks(CASING_STEEL_SOLID.get()).setMinGlobalLimited(50)
                            .or(abilities(IMPORT_FLUIDS).setMaxGlobalLimited(4).setPreviewCount(1))
                            .or(abilities(IMPORT_ITEMS).setMaxGlobalLimited(4).setPreviewCount(1))
                            .or(abilities(EXPORT_FLUIDS).setMaxGlobalLimited(4).setPreviewCount(1))
                            .or(abilities(EXPORT_ITEMS).setMaxGlobalLimited(4).setPreviewCount(1))
                            .or(abilities(INPUT_ENERGY).setMinGlobalLimited(1).setMaxGlobalLimited(2)
                                    .setPreviewCount(2))
                            .or(abilities(MAINTENANCE).setExactLimit(1))
                            .or(abilities(PartAbility.PARALLEL_HATCH).setMaxGlobalLimited(1).setPreviewCount(0)))
                    .where("G", blocks(CASING_STEEL_GEARBOX.get()))
                    .where("P", blocks(CASING_STEEL_PIPE.get()))
                    .where("#", air())
                    .where(" ", any())
                    .build())
            .tooltips(Component.translatable("gtceu.multiblock.parallelizable.tooltip"))
            .workableCasingModel(GTCEu.id("block/casings/solid/machine_casing_solid_steel"),
                    AstroCore.id("block/multiblock/concrete_mixer"))
            .register();

    // Industrial Processing Machines
    public static final MultiblockMachineDefinition INDUSTRIAL_AUTOCLAVE = REGISTRATE
            .multiblock("industrial_autoclave", ProcessingCoreMachine::new)
            .langValue("Industrial Autoclave")
            .rotationState(RotationState.ALL)
            .recipeType(GTRecipeTypes.AUTOCLAVE_RECIPES)
            .recipeModifiers(ProcessingCoreMachine::processingCoreOverclock, BATCH_MODE)
            .appearanceBlock(MACHINE_CASING_STYRENE_BUTADIENE)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("XXX", "XXX", "XXX")
                    .aisle("XXX", "XCX", "XXX")
                    .aisle("XXX", "X@X", "XXX")
                    .where('@', controller(blocks(definition.get())))
                    .where('X', blocks(MACHINE_CASING_STYRENE_BUTADIENE.get()).setMinGlobalLimited(15)
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
            .tooltips(Component.translatable("astrogreg.machine.industrial_core.tooltip"),
                    Component.translatable("gtceu.machine.available_recipe_map_1.tooltip",
                            Component.translatable("gtceu.autoclave")),
                    Component.translatable("gtceu.multiblock.exact_hatch_1.tooltip"))
            .workableCasingModel(
                    AstroCore.id("block/casings/industrial_casings/machine_casing_styrene_butadiene_rubber"),
                    AstroCore.id("block/multiblock/autoclave"))
            .register();

    public static final MultiblockMachineDefinition INDUSTRIAL_BENDER = REGISTRATE
            .multiblock("industrial_bender", ProcessingCoreMachine::new)
            .langValue("Industrial Material Press")
            .rotationState(RotationState.ALL)
            .recipeTypes(GTRecipeTypes.BENDER_RECIPES, GTRecipeTypes.FORGE_HAMMER_RECIPES)
            .recipeModifiers(ProcessingCoreMachine::processingCoreOverclock, BATCH_MODE)
            .appearanceBlock(MACHINE_CASING_ULTIMET)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("XXX", "XXX", "XXX")
                    .aisle("XXX", "XCX", "XXX")
                    .aisle("XXX", "X@X", "XXX")
                    .where('@', controller(blocks(definition.get())))
                    .where('X', blocks(MACHINE_CASING_ULTIMET.get()).setMinGlobalLimited(15)
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
            .tooltips(Component.translatable("astrogreg.machine.industrial_core.tooltip"),
                    Component.translatable("gtceu.machine.available_recipe_map_2.tooltip",
                            Component.translatable("gtceu.bender"),
                            Component.translatable("gtceu.forge_hammer")),
                    Component.translatable("gtceu.multiblock.exact_hatch_1.tooltip"))
            .workableCasingModel(AstroCore.id("block/casings/industrial_casings/machine_casing_ultimet"),
                    AstroCore.id("block/multiblock/bender"))
            .register();

    public static final MultiblockMachineDefinition INDUSTRIAL_CENTRIFUGE = REGISTRATE
            .multiblock("industrial_centrifuge", ProcessingCoreMachine::new)
            .langValue("Industrial Centrifugal Unit")
            .rotationState(RotationState.ALL)
            .recipeTypes(GTRecipeTypes.CENTRIFUGE_RECIPES, GTRecipeTypes.THERMAL_CENTRIFUGE_RECIPES)
            .recipeModifiers(ProcessingCoreMachine::processingCoreOverclock, BATCH_MODE)
            .appearanceBlock(MACHINE_CASING_RED_STEEL)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("XXX", "XXX", "XXX")
                    .aisle("XXX", "XCX", "XXX")
                    .aisle("XXX", "X@X", "XXX")
                    .where('@', controller(blocks(definition.get())))
                    .where('X', blocks(MACHINE_CASING_RED_STEEL.get()).setMinGlobalLimited(15)
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
            .tooltips(Component.translatable("astrogreg.machine.industrial_core.tooltip"),
                    Component.translatable("gtceu.machine.available_recipe_map_2.tooltip",
                            Component.translatable("gtceu.centrifuge"),
                            Component.translatable("gtceu.thermal_centrifuge")),
                    Component.translatable("gtceu.multiblock.exact_hatch_1.tooltip"))
            .workableCasingModel(AstroCore.id("block/casings/industrial_casings/machine_casing_red_steel"),
                    AstroCore.id("block/multiblock/centrifuge"))
            .register();

    public static final MultiblockMachineDefinition INDUSTRIAL_CHEMICAL_BATH = REGISTRATE
            .multiblock("industrial_chemical_bath", ProcessingCoreMachine::new)
            .langValue("Industrial Chemical Bath")
            .rotationState(RotationState.ALL)
            .recipeTypes(GTRecipeTypes.ORE_WASHER_RECIPES, GTRecipeTypes.CHEMICAL_BATH_RECIPES)
            .recipeModifiers(ProcessingCoreMachine::processingCoreOverclock, BATCH_MODE)
            .appearanceBlock(MACHINE_CASING_POLYVINYL_CHLORIDE)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("XXX", "XXX", "XXX")
                    .aisle("XXX", "XCX", "XXX")
                    .aisle("XXX", "X@X", "XXX")
                    .where('@', controller(blocks(definition.get())))
                    .where('X', blocks(MACHINE_CASING_POLYVINYL_CHLORIDE.get()).setMinGlobalLimited(15)
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
            .tooltips(Component.translatable("astrogreg.machine.industrial_core.tooltip"),
                    Component.translatable("gtceu.machine.available_recipe_map_2.tooltip",
                            Component.translatable("gtceu.ore_washer"),
                            Component.translatable("gtceu.chemical_bath")),
                    Component.translatable("gtceu.multiblock.exact_hatch_1.tooltip"))
            .workableCasingModel(AstroCore.id("block/casings/industrial_casings/machine_casing_polyvinyl_chloride"),
                    AstroCore.id("block/multiblock/ore_washer"))
            .register();

    public static final MultiblockMachineDefinition INDUSTRIAL_ELECTROLYZER = REGISTRATE
            .multiblock("industrial_electrolyzer", ProcessingCoreMachine::new)
            .langValue("Industrial Electrolyzer")
            .rotationState(RotationState.ALL)
            .recipeType(GTRecipeTypes.ELECTROLYZER_RECIPES)
            .recipeModifiers(ProcessingCoreMachine::processingCoreOverclock, BATCH_MODE)
            .appearanceBlock(MACHINE_CASING_BLUE_STEEL)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("XXX", "XXX", "XXX")
                    .aisle("XXX", "XCX", "XXX")
                    .aisle("XXX", "X@X", "XXX")
                    .where('@', controller(blocks(definition.get())))
                    .where('X', blocks(MACHINE_CASING_BLUE_STEEL.get()).setMinGlobalLimited(15)
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
            .tooltips(Component.translatable("astrogreg.machine.industrial_core.tooltip"),
                    Component.translatable("gtceu.machine.available_recipe_map_1.tooltip",
                            Component.translatable("gtceu.electrolyzer")),
                    Component.translatable("gtceu.multiblock.exact_hatch_1.tooltip"))
            .workableCasingModel(AstroCore.id("block/casings/industrial_casings/machine_casing_blue_steel"),
                    AstroCore.id("block/multiblock/electrolyzer"))
            .register();

    public static final MultiblockMachineDefinition INDUSTRIAL_EXTRUDER = REGISTRATE
            .multiblock("industrial_extruder", ProcessingCoreMachine::new)
            .langValue("Industrial Extruder")
            .rotationState(RotationState.ALL)
            .recipeType(GTRecipeTypes.EXTRUDER_RECIPES)
            .recipeModifiers(ProcessingCoreMachine::processingCoreOverclock, BATCH_MODE)
            .appearanceBlock(MACHINE_CASING_BLACK_STEEL)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("XXX", "XXX", "XXX")
                    .aisle("XXX", "XCX", "XXX")
                    .aisle("XXX", "X@X", "XXX")
                    .where('@', controller(blocks(definition.get())))
                    .where('X', blocks(MACHINE_CASING_BLACK_STEEL.get()).setMinGlobalLimited(15)
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
            .tooltips(Component.translatable("astrogreg.machine.industrial_core.tooltip"),
                    Component.translatable("gtceu.machine.available_recipe_map_1.tooltip",
                            Component.translatable("gtceu.extruder")),
                    Component.translatable("gtceu.multiblock.exact_hatch_1.tooltip"))
            .workableCasingModel(AstroCore.id("block/casings/industrial_casings/machine_casing_black_steel"),
                    AstroCore.id("block/multiblock/extruder"))
            .register();

    public static final MultiblockMachineDefinition INDUSTRIAL_FLUID_SOLIDIFIER = REGISTRATE
            .multiblock("industrial_fluid_solidifier", ProcessingCoreMachine::new)
            .langValue("Industrial Fluid Solidifier")
            .rotationState(RotationState.ALL)
            .recipeType(GTRecipeTypes.FLUID_SOLIDFICATION_RECIPES)
            .recipeModifiers(ProcessingCoreMachine::processingCoreOverclock, BATCH_MODE)
            .appearanceBlock(MACHINE_CASING_SILICONE_RUBBER)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("XXX", "XXX", "XXX")
                    .aisle("XXX", "XCX", "XXX")
                    .aisle("XXX", "X@X", "XXX")
                    .where('@', controller(blocks(definition.get())))
                    .where('X', blocks(MACHINE_CASING_SILICONE_RUBBER.get()).setMinGlobalLimited(15)
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
            .tooltips(Component.translatable("astrogreg.machine.industrial_core.tooltip"),
                    Component.translatable("gtceu.machine.available_recipe_map_1.tooltip",
                            Component.translatable("gtceu.fluid_solidifier")),
                    Component.translatable("gtceu.multiblock.exact_hatch_1.tooltip"))
            .workableCasingModel(AstroCore.id("block/casings/industrial_casings/machine_casing_silicone_rubber"),
                    AstroCore.id("block/multiblock/fluid_solidifier"))
            .register();

    public static final MultiblockMachineDefinition INDUSTRIAL_LATHE = REGISTRATE
            .multiblock("industrial_lathe", ProcessingCoreMachine::new)
            .langValue("Industrial Lathe")
            .rotationState(RotationState.ALL)
            .recipeType(GTRecipeTypes.LATHE_RECIPES)
            .recipeModifiers(ProcessingCoreMachine::processingCoreOverclock, BATCH_MODE)
            .appearanceBlock(MACHINE_CASING_ROSE_GOLD)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("XXX", "XXX", "XXX")
                    .aisle("XXX", "XCX", "XXX")
                    .aisle("XXX", "X@X", "XXX")
                    .where('@', controller(blocks(definition.get())))
                    .where('X', blocks(MACHINE_CASING_ROSE_GOLD.get()).setMinGlobalLimited(15)
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
            .tooltips(Component.translatable("astrogreg.machine.industrial_core.tooltip"),
                    Component.translatable("gtceu.machine.available_recipe_map_1.tooltip",
                            Component.translatable("gtceu.lathe")),
                    Component.translatable("gtceu.multiblock.exact_hatch_1.tooltip"))
            .workableCasingModel(AstroCore.id("block/casings/industrial_casings/machine_casing_rose_gold"),
                    AstroCore.id("block/multiblock/lathe"))
            .register();

    public static final MultiblockMachineDefinition INDUSTRIAL_MACERATOR = REGISTRATE
            .multiblock("industrial_macerator", ProcessingCoreMachine::new)
            .langValue("Industrial Macerator")
            .rotationState(RotationState.ALL)
            .recipeType(GTRecipeTypes.MACERATOR_RECIPES)
            .recipeModifiers(ProcessingCoreMachine::processingCoreOverclock, BATCH_MODE)
            .appearanceBlock(MACHINE_CASING_CARBON_FIBER_MESH)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("XXX", "XXX", "XXX")
                    .aisle("XXX", "XCX", "XXX")
                    .aisle("XXX", "X@X", "XXX")
                    .where('@', controller(blocks(definition.get())))
                    .where('X', blocks(MACHINE_CASING_CARBON_FIBER_MESH.get()).setMinGlobalLimited(15)
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
            .tooltips(Component.translatable("astrogreg.machine.industrial_core.tooltip"),
                    Component.translatable("gtceu.machine.available_recipe_map_1.tooltip",
                            Component.translatable("gtceu.macerator")),
                    Component.translatable("gtceu.multiblock.exact_hatch_1.tooltip"))
            .workableCasingModel(AstroCore.id("block/casings/industrial_casings/machine_casing_carbon_fiber_mesh"),
                    AstroCore.id("block/multiblock/macerator"))
            .register();

    public static final MultiblockMachineDefinition INDUSTRIAL_MIXER = REGISTRATE
            .multiblock("industrial_mixer", ProcessingCoreMachine::new)
            .langValue("Industrial Mixer")
            .rotationState(RotationState.ALL)
            .recipeType(GTRecipeTypes.MIXER_RECIPES)
            .recipeModifiers(ProcessingCoreMachine::processingCoreOverclock, BATCH_MODE)
            .appearanceBlock(MACHINE_CASING_VANADIUM_STEEL)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("XXX", "XXX", "XXX")
                    .aisle("XXX", "XCX", "XXX")
                    .aisle("XXX", "X@X", "XXX")
                    .where('@', controller(blocks(definition.get())))
                    .where('X', blocks(MACHINE_CASING_VANADIUM_STEEL.get()).setMinGlobalLimited(15)
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
            .tooltips(Component.translatable("astrogreg.machine.industrial_core.tooltip"),
                    Component.translatable("gtceu.machine.available_recipe_map_1.tooltip",
                            Component.translatable("gtceu.mixer")),
                    Component.translatable("gtceu.multiblock.exact_hatch_1.tooltip"))
            .workableCasingModel(AstroCore.id("block/casings/industrial_casings/machine_casing_vanadium_steel"),
                    AstroCore.id("block/multiblock/mixer"))
            .register();

    public static final MultiblockMachineDefinition INDUSTRIAL_SIFTER = REGISTRATE
            .multiblock("industrial_sifter", ProcessingCoreMachine::new)
            .langValue("Industrial Sifter")
            .rotationState(RotationState.ALL)
            .recipeType(GTRecipeTypes.SIFTER_RECIPES)
            .recipeModifiers(ProcessingCoreMachine::processingCoreOverclock, BATCH_MODE)
            .appearanceBlock(MACHINE_CASING_BISMUTH_BRONZE)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("XXX", "XXX", "XXX")
                    .aisle("XXX", "XCX", "XXX")
                    .aisle("XXX", "X@X", "XXX")
                    .where('@', controller(blocks(definition.get())))
                    .where('X', blocks(MACHINE_CASING_BISMUTH_BRONZE.get()).setMinGlobalLimited(15)
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
            .tooltips(Component.translatable("astrogreg.machine.industrial_core.tooltip"),
                    Component.translatable("gtceu.machine.available_recipe_map_1.tooltip",
                            Component.translatable("gtceu.sifter")),
                    Component.translatable("gtceu.multiblock.exact_hatch_1.tooltip"))
            .workableCasingModel(AstroCore.id("block/casings/industrial_casings/machine_casing_bismuth_bronze"),
                    AstroCore.id("block/multiblock/sifter"))
            .register();

    public static final MultiblockMachineDefinition INDUSTRIAL_WIREMILL = REGISTRATE
            .multiblock("industrial_wiremill", ProcessingCoreMachine::new)
            .langValue("Industrial Wiremill")
            .rotationState(RotationState.ALL)
            .recipeType(GTRecipeTypes.WIREMILL_RECIPES)
            .recipeModifiers(ProcessingCoreMachine::processingCoreOverclock, BATCH_MODE)
            .appearanceBlock(MACHINE_CASING_COBALT_BRASS)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("XXX", "XXX", "XXX")
                    .aisle("XXX", "XCX", "XXX")
                    .aisle("XXX", "X@X", "XXX")
                    .where('@', controller(blocks(definition.get())))
                    .where('X', blocks(MACHINE_CASING_COBALT_BRASS.get()).setMinGlobalLimited(15)
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
            .tooltips(Component.translatable("astrogreg.machine.industrial_core.tooltip"),
                    Component.translatable("gtceu.machine.available_recipe_map_1.tooltip",
                            Component.translatable("gtceu.wiremill")),
                    Component.translatable("gtceu.multiblock.exact_hatch_1.tooltip"))
            .workableCasingModel(AstroCore.id("block/casings/industrial_casings/machine_casing_cobalt_brass"),
                    AstroCore.id("block/multiblock/wiremill"))
            .register();

    // Industrial Space Travel Multiblocks
    public static final MultiblockMachineDefinition INDUSTRIAL_OBSERVATORY = REGISTRATE
            .multiblock("industrial_observatory", IndustrialObservatoryMachine::new)
            .langValue("Industrial Observatory")
            .rotationState(RotationState.NON_Y_AXIS)
            .allowFlip(false)
            .recipeModifier(IndustrialObservatoryMachine::recipeModifier, true)
            .appearanceBlock(CASING_STEEL_SOLID)
            .recipeType(OBSERVATORY_RECIPES)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle(" XXXXXXX ", " SSSSSSS ", " XXXXXXX ", "  XXXXX  ", "         ", "         ", "         ",
                            "         ", "         ", "   TST   ", "  TTSTT  ", "         ", "         ")
                    .aisle("XXXXXXXXX", "SSX   XSS", "XYX   XYX", " YXXXXXY ", "  YYYYY  ", "         ", "         ",
                            "         ", "   TST   ", "  TTSTT  ", " T     T ", "         ", "         ")
                    .aisle("XXXYYYXXX", "SX     XS", "XX     XX", "XX     XX", " YYTXTYY ", "    X    ", "    X    ",
                            "   XXX   ", "  TSSST  ", " T     T ", "T       T", "         ", "         ")
                    .aisle("XXYYXYYXX", "S       S", "X       X", "XX     XX", " YTTXTTY ", "         ", "         ",
                            "  XTXTX  ", " TSISIST ", "TT     TT", "T       T", "         ", "         ")
                    .aisle("XXYXXXYXX", "S   O   S", "X   @   X", "XX  X  XX", " YXXXXXY ", "  X   X  ", "  X   X  ",
                            "  XXXXX  ", " SSSFSSS ", "SS  F  SS", "S   F   S", "    F    ", "    E    ")
                    .aisle("XXYYXYYXX", "S       S", "X       X", "XX     XX", " YTTXTTY ", "         ", "         ",
                            "  XTXTX  ", " TSISIST ", "TT     TT", "T       T", "         ", "         ")
                    .aisle("XXXYYYXXX", "SX     XS", "XX     XX", "XX     XX", " YYTXTYY ", "    X    ", "    X    ",
                            "   XXX   ", "  TSSST  ", " T     T ", "T       T", "         ", "         ")
                    .aisle("XXXXXXXXX", "SSX   XSS", "XYX   XYX", " YXXXXXY ", "  YYYYY  ", "         ", "         ",
                            "         ", "   TST   ", "  TTSTT  ", " T     T ", "         ", "         ")
                    .aisle(" XXXXXXX ", " SSX XSS ", " XXX XXX ", "  XXXXX  ", "         ", "         ", "         ",
                            "         ", "         ", "   TST   ", "  TTSTT  ", "         ", "         ")
                    .where("@", controller(blocks(definition.get())))
                    .where("X", blocks(CASING_STEEL_SOLID.get())
                            .or(abilities(MAINTENANCE).setExactLimit(1))
                            .or(abilities(INPUT_ENERGY).setPreviewCount(2).setMinGlobalLimited(1)
                                    .setMaxGlobalLimited(2))
                            .or(abilities(AstroPartAbility.IMPORT_CWU).setExactLimit(1)))
                    .where("Y", blocks(CASING_STAINLESS_CLEAN.get()))
                    .where("E", blocks(Blocks.EMERALD_BLOCK))
                    .where("F", frames(GTMaterials.Steel))
                    .where("T", blocks(CASING_TEMPERED_GLASS.get()))
                    .where("I", blocks(INDUSTRIAL_PROCESSING_CORE_MK1.get())
                            .or(blocks(INDUSTRIAL_PROCESSING_CORE_MK2.get()))
                            .or(blocks(INDUSTRIAL_PROCESSING_CORE_MK3.get())))
                    .where("S", blocks(STEEL_CONTROL_CASING.get()))
                    .where("O", blocks(OBSERVATORY_DATA_HOLDER.get()))
                    .where(" ", any())
                    .build())
            .tooltips(Component.translatable("astrogreg.machine.industrial_core.tooltip"))
            .workableCasingModel(GTCEu.id("block/casings/solid/machine_casing_solid_steel"),
                    AstroCore.id("block/multiblock/observatory"))
            .register();

    public static final MultiblockMachineDefinition INDUSTRIAL_ASTROPORT = REGISTRATE
            .multiblock("industrial_astroport", IndustrialAstroPortMachine::new)
            .langValue("Industrial Astroport")
            .rotationState(RotationState.NON_Y_AXIS)
            .allowFlip(false)
            .recipeModifier(IndustrialAstroPortMachine::recipeModifier, true)
            .partSorter(IndustrialAstroPortMachine::partSorter)
            .appearanceBlock(CASING_STEEL_SOLID)
            .recipeType(ASTROPORT_RECIPES)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("     XXXXXXXXXXX     ", "     XAAXXXXXAAX     ", "     XXAXT@TXAXX     ",
                            "      XXXTHTXXX      ",
                            "       XXTHTXX       ", "       XXTHTXX       ", "        XTHTX        ",
                            "        XXXXX        ",
                            "         X X         ", "                     ", "                     ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ")
                    .aisle("   XXXXXXXXXXXXXXX   ", "   XX           XX   ", "    X           X    ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ",
                            "         X X         ", "         X X         ", "                     ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ")
                    .aisle("  XXXXXXXGXGXXXXXXX  ", "  X               X  ", "                     ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ",
                            "                     ", "         X X         ", "         XXX         ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ")
                    .aisle(" XXXXTTTXGXGXTTTXXXX ", " X                 X ", "                     ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ",
                            "                     ", "                     ", "         XTX         ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ")
                    .aisle(" XXXXXFTXGXGXTFXXXXX ", " X    F       F    X ", " X    F       F    X ",
                            "      F       F      ",
                            "      F       F      ", "                     ", "                     ",
                            "                     ",
                            "                     ", "                     ", "         XTX         ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ")
                    .aisle("XXXTXXXTXXXXXTXXXTXXX", "X                   X", "X                   X",
                            "                     ",
                            "     F         F     ", "     F         F     ", "     F         F     ",
                            "     F         F     ",
                            "     F         F     ", "                     ", "        XXXXX        ",
                            "         XXX         ",
                            "         XTX         ", "         XXX         ", "          X          ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ")
                    .aisle("XXXTFXXXXAXAXXXXFTXXX", "A   F           F   A", "X   F           F   X",
                            "X   F           F   X",
                            "    F           F    ", "                     ", "                     ",
                            "                     ",
                            "                     ", "      F       F      ", "       XFFFFFX       ",
                            "       X     X       ",
                            "                     ", "                     ", "         XXX         ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ")
                    .aisle("XXXTTTXXXAXAXXXTTTXXX", "A                   A", "A                   A",
                            "X                   X",
                            "X                   X", "X                   X", "                     ",
                            "                     ",
                            "                     ", "                     ", "      XF     FX      ",
                            "      XF     FX      ",
                            "       F     F       ", "       F     F       ", "       FXX XXF       ",
                            "        FFFFF        ",
                            "                     ", "                     ", "                     ",
                            "                     ")
                    .aisle("XXXXXXXXFAXAFXXXXXXXX", "X       F   F       X", "X       F   F       X",
                            "X       F   F       X",
                            "X       F   F       X", "X       F   F       X", "X       F   F       X",
                            "X       F   F       X",
                            "        F   F        ", "        F   F        ", "     XF F   F FX     ",
                            "        F   F        ",
                            "        F   F        ", "        F   F        ", "       XF   FX       ",
                            "       FF   FF       ",
                            "        F   F        ", "        F   F        ", "        F   F        ",
                            "         FFF         ")
                    .aisle("XXGGGXAAACXCAAAXGGGXX", "X                   X", "G        ###        G",
                            "G        ###        G",
                            "G        ###        G", "G        ###        G", "G        ###        G",
                            "X        ###        X",
                            "XX       ###       XX", " XX      ###      XX ", "  XXXXF  ###  FXXXX  ",
                            "         ###         ",
                            "         ###         ", "         ###         ", "       X ### X       ",
                            "       F ### F       ",
                            "                     ", "                     ", "                     ",
                            "        F###F        ")
                    .aisle("XXXXXXXXXXXXXXXXXXXXX", "X         P         X", "I        ###        I",
                            "I        ###        I",
                            "I        ###        I", "I        ###        I", "I        ###        I",
                            "X        ###        X",
                            "         ###         ", "         ###         ", "  XTTXF       FXTTX  ",
                            "         ###         ",
                            "         ###         ", "         ###         ", "       X ### X       ",
                            "       F ### F       ",
                            "         ###         ", "         ###         ", "         ###         ",
                            "        F###F        ")
                    .aisle("XXGGGXAAACXCAAAXGGGXX", "X                   X", "G        ###        G",
                            "G        ###        G",
                            "G        ###        G", "G        ###        G", "G        ###        G",
                            "X        ###        X",
                            "XX       ###       XX", " XX      ###      XX ", "  XXXXF  ###  FXXXX  ",
                            "         ###         ",
                            "         ###         ", "         ###         ", "       X ### X       ",
                            "       F ### F       ",
                            "                     ", "                     ", "                     ",
                            "        F###F        ")
                    .aisle("XXXXXXXXFAXAFXXXXXXXX", "X       F   F       X", "X       F   F       X",
                            "X       F   F       X",
                            "X       F   F       X", "X       F   F       X", "X       F   F       X",
                            "X       F   F       X",
                            "        F   F        ", "        F   F        ", "     XF F   F FX     ",
                            "        F   F        ",
                            "        F   F        ", "        F   F        ", "       XF   FX       ",
                            "       FF   FF       ",
                            "        F   F        ", "        F   F        ", "        F   F        ",
                            "         FFF         ")
                    .aisle("XXXTTTXXXAXAXXXTTTXXX", "A                   A", "A                   A",
                            "X                   X",
                            "X                   X", "X                   X", "                     ",
                            "                     ",
                            "                     ", "                     ", "      XF     FX      ",
                            "      XF     FX      ",
                            "       F     F       ", "       F     F       ", "       FXX XXF       ",
                            "        FFFFF        ",
                            "                     ", "                     ", "                     ",
                            "                     ")
                    .aisle("XXXTFXXXXAXAXXXXFTXXX", "A   F           F   A", "X   F           F   X",
                            "X   F           F   X",
                            "    F           F    ", "                     ", "                     ",
                            "                     ",
                            "                     ", "      F       F      ", "       XFFFFFX       ",
                            "       X     X       ",
                            "                     ", "                     ", "         XXX         ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ")
                    .aisle("XXXTXXXTXXXXXTXXXTXXX", "X                   X", "X                   X",
                            "                     ",
                            "     F         F     ", "     F         F     ", "     F         F     ",
                            "     F         F     ",
                            "     F         F     ", "                     ", "        XXXXX        ",
                            "         XXX         ",
                            "         XTX         ", "         XXX         ", "          X          ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ")
                    .aisle(" XXXXXFTXGXGXTFXXXXX ", " X    F       F    X ", " X    F       F    X ",
                            "      F       F      ",
                            "      F       F      ", "                     ", "                     ",
                            "                     ",
                            "                     ", "                     ", "         XTX         ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ")
                    .aisle(" XXXXTTTXGXGXTTTXXXX ", " X                 X ", "                     ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ",
                            "                     ", "                     ", "         XTX         ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ")
                    .aisle("  XXXXXXXGXGXXXXXXX  ", "  X               X  ", "                     ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ",
                            "                     ", "         X X         ", "         XXX         ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ")
                    .aisle("   XXXXXXXXXXXXXXX   ", "   XX           XX   ", "    X           X    ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ",
                            "         X X         ", "         X X         ", "                     ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ")
                    .aisle("     XXXXXXXXXXX     ", "     XAAX   XAAX     ", "     XXAX   XAXX     ",
                            "      XXX   XXX      ",
                            "       XX   XX       ", "       XXXXXXX       ", "        XTTTX        ",
                            "        XXXXX        ",
                            "         X X         ", "                     ", "                     ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ")
                    .where("@", controller(blocks(definition.get())))
                    .where("X", blocks(CASING_STEEL_SOLID.get())
                            .or(abilities(INPUT_ENERGY).setPreviewCount(2).setMinGlobalLimited(1)
                                    .setMaxGlobalLimited(2)))
                    .where("H", blocks(CASING_STEEL_SOLID.get())
                            .or(abilities(DATA_ACCESS).setExactLimit(1))
                            .or(abilities(MAINTENANCE).setExactLimit(1))
                            .or(abilities(IMPORT_FLUIDS).setExactLimit(1))
                            .or(abilities(EXPORT_ITEMS).setExactLimit(1)))
                    .where("G", blocks(CASING_STEEL_GEARBOX.get()))
                    .where("A", blocks(CASING_GRATE.get()))
                    .where("I", blocks(ITEM_IMPORT_BUS[0].getBlock()))
                    .where("T", blocks(STEEL_CONTROL_CASING.get()))
                    .where("F", frames(GTMaterials.Steel))
                    .where("C", blocks(INDUSTRIAL_PROCESSING_CORE_MK1.get())
                            .or(blocks(INDUSTRIAL_PROCESSING_CORE_MK2.get()))
                            .or(blocks(INDUSTRIAL_PROCESSING_CORE_MK3.get())))
                    .where("P", blocks(ModBlocks.LAUNCH_PAD.get()))
                    .where(" ", any())
                    .where("#", air())
                    .build())
            .tooltips(Component.translatable("astrogreg.machine.industrial_core.tooltip"))
            .workableCasingModel(GTCEu.id("block/casings/solid/machine_casing_solid_steel"),
                    AstroCore.id("block/multiblock/astroport"))
            .shapeInfos(definition -> List.of(MultiblockShapeInfo.builder()
                    .aisle("     XXXXXXXXXXX     ", "     XAAX   XAAX     ", "     XXAX   XAXX     ",
                            "      XXX   XXX      ",
                            "       XX   XX       ", "       XXXXXXX       ", "        XTTTX        ",
                            "        XXXXX        ",
                            "         X X         ", "                     ", "                     ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ")
                    .aisle("   XXXXXXXXXXXXXXX   ", "   XX           XX   ", "    X           X    ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ",
                            "         X X         ", "         X X         ", "                     ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ")
                    .aisle("  XXXXXXXGXGXXXXXXX  ", "  X               X  ", "                     ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ",
                            "                     ", "         X X         ", "         XXX         ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ")
                    .aisle(" XXXXTTTXGXGXTTTXXXX ", " X                 X ", "                     ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ",
                            "                     ", "                     ", "         XTX         ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ")
                    .aisle(" XXXXXFTXGXGXTFXXXXX ", " X    F       F    X ", " X    F       F    X ",
                            "      F       F      ",
                            "      F       F      ", "                     ", "                     ",
                            "                     ",
                            "                     ", "                     ", "         XTX         ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ")
                    .aisle("XXXTXXXTXXXXXTXXXTXXX", "X                   X", "X                   X",
                            "                     ",
                            "     F         F     ", "     F         F     ", "     F         F     ",
                            "     F         F     ",
                            "     F         F     ", "                     ", "        XXXXX        ",
                            "         XXX         ",
                            "         XTX         ", "         XXX         ", "          X          ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ")
                    .aisle("XXXTFXXXXAXAXXXXFTXXX", "A   F           F   A", "X   F           F   X",
                            "X   F           F   X",
                            "    F           F    ", "                     ", "                     ",
                            "                     ",
                            "                     ", "      F       F      ", "       XFFFFFX       ",
                            "       X     X       ",
                            "                     ", "                     ", "         XXX         ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ")
                    .aisle("XXXTTTXXXAXAXXXTTTXXX", "A                   A", "A                   A",
                            "X                   X",
                            "X                   X", "X                   X", "                     ",
                            "                     ",
                            "                     ", "                     ", "      XF     FX      ",
                            "      XF     FX      ",
                            "       F     F       ", "       F     F       ", "       FXX XXF       ",
                            "        FFFFF        ",
                            "                     ", "                     ", "                     ",
                            "                     ")
                    .aisle("XXXXXXXXFAXAFXXXXXXXX", "X       F   F       X", "X       F   F       X",
                            "X       F   F       X",
                            "X       F   F       X", "X       F   F       X", "X       F   F       X",
                            "X       F   F       X",
                            "        F   F        ", "        F   F        ", "     XF F   F FX     ",
                            "        F   F        ",
                            "        F   F        ", "        F   F        ", "       XF   FX       ",
                            "       FF   FF       ",
                            "        F   F        ", "        F   F        ", "        F   F        ",
                            "         FFF         ")
                    .aisle("XXGGGXAAACXCAAAXGGGXX", "X                   X", "G        ###        G",
                            "G        ###        G",
                            "G        ###        G", "G        ###        G", "G        ###        G",
                            "X        ###        X",
                            "XX       ###       XX", " XX      ###      XX ", "  XXXXF  ###  FXXXX  ",
                            "         ###         ",
                            "         ###         ", "         ###         ", "       X ### X       ",
                            "       F ### F       ",
                            "                     ", "                     ", "                     ",
                            "        F###F        ")
                    .aisle("XXXXXXXXXXXXXXXXXXXXX", "X         P         X", "E        ###        I",
                            "E        ###        I",
                            "E        ###        I", "E        ###        I", "E        ###        I",
                            "X        ###        X",
                            "         ###         ", "         ###         ", "  XTTXF       FXTTX  ",
                            "         ###         ",
                            "         ###         ", "         ###         ", "       X ### X       ",
                            "       F ### F       ",
                            "         ###         ", "         ###         ", "         ###         ",
                            "        F###F        ")
                    .aisle("XXGGGXAAACXCAAAXGGGXX", "X                   X", "G        ###        G",
                            "G        ###        G",
                            "G        ###        G", "G        ###        G", "G        ###        G",
                            "X        ###        X",
                            "XX       ###       XX", " XX      ###      XX ", "  XXXXF  ###  FXXXX  ",
                            "         ###         ",
                            "         ###         ", "         ###         ", "       X ### X       ",
                            "       F ### F       ",
                            "                     ", "                     ", "                     ",
                            "        F###F        ")
                    .aisle("XXXXXXXXFAXAFXXXXXXXX", "X       F   F       X", "X       F   F       X",
                            "X       F   F       X",
                            "X       F   F       X", "X       F   F       X", "X       F   F       X",
                            "X       F   F       X",
                            "        F   F        ", "        F   F        ", "     XF F   F FX     ",
                            "        F   F        ",
                            "        F   F        ", "        F   F        ", "       XF   FX       ",
                            "       FF   FF       ",
                            "        F   F        ", "        F   F        ", "        F   F        ",
                            "         FFF         ")
                    .aisle("XXXTTTXXXAXAXXXTTTXXX", "A                   A", "A                   A",
                            "X                   X",
                            "X                   X", "X                   X", "                     ",
                            "                     ",
                            "                     ", "                     ", "      XF     FX      ",
                            "      XF     FX      ",
                            "       F     F       ", "       F     F       ", "       FXX XXF       ",
                            "        FFFFF        ",
                            "                     ", "                     ", "                     ",
                            "                     ")
                    .aisle("XXXTFXXXXAXAXXXXFTXXX", "A   F           F   A", "X   F           F   X",
                            "X   F           F   X",
                            "    F           F    ", "                     ", "                     ",
                            "                     ",
                            "                     ", "      F       F      ", "       XFFFFFX       ",
                            "       X     X       ",
                            "                     ", "                     ", "         XXX         ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ")
                    .aisle("XXXTXXXTXXXXXTXXXTXXX", "X                   X", "X                   X",
                            "                     ",
                            "     F         F     ", "     F         F     ", "     F         F     ",
                            "     F         F     ",
                            "     F         F     ", "                     ", "        XXXXX        ",
                            "         XXX         ",
                            "         XTX         ", "         XXX         ", "          X          ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ")
                    .aisle(" XXXXXFTXGXGXTFXXXXX ", " X    F       F    X ", " X    F       F    X ",
                            "      F       F      ",
                            "      F       F      ", "                     ", "                     ",
                            "                     ",
                            "                     ", "                     ", "         XTX         ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ")
                    .aisle(" XXXXTTTXGXGXTTTXXXX ", " X                 X ", "                     ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ",
                            "                     ", "                     ", "         XTX         ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ")
                    .aisle("  XXXXXXXGXGXXXXXXX  ", "  X               X  ", "                     ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ",
                            "                     ", "         X X         ", "         XXX         ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ")
                    .aisle("   XXXXXXXXXXXXXXX   ", "   XX           XX   ", "    X           X    ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ",
                            "         X X         ", "         X X         ", "                     ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ")
                    .aisle("     XXXXHXHXXXX     ", "     XAAXXXXXAAX     ", "     XXAXT@TXAXX     ",
                            "      XXXTDTXXX      ",
                            "       XXTNTXX       ", "       XXTUTXX       ", "        XTVTX        ",
                            "        XXXXX        ",
                            "         X X         ", "                     ", "                     ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ",
                            "                     ", "                     ", "                     ",
                            "                     ")
                    .where('@', definition, Direction.NORTH)
                    .where('X', CASING_STEEL_SOLID.get())
                    .where('A', CASING_GRATE.get())
                    .where('G', CASING_STEEL_GEARBOX.get())
                    .where('T', STEEL_CONTROL_CASING.get())
                    .where('F', GTMaterialBlocks.MATERIAL_BLOCKS.get(TagPrefix.frameGt, GTMaterials.Steel).get())
                    .where('C', INDUSTRIAL_PROCESSING_CORE_MK1.get())
                    .where('P', ModBlocks.LAUNCH_PAD.get())
                    .where('I', ITEM_IMPORT_BUS[0], Direction.WEST)
                    .where('E', ITEM_IMPORT_BUS[0], Direction.EAST)
                    .where('D', DATA_ACCESS_HATCH, Direction.NORTH)
                    .where('N', GTMachines.MAINTENANCE_HATCH, Direction.NORTH)
                    .where('U', GTMachines.FLUID_IMPORT_HATCH[GTValues.LV], Direction.NORTH)
                    .where('V', GTMachines.ITEM_EXPORT_BUS[GTValues.LV], Direction.NORTH)
                    .where('H', GTMachines.ENERGY_INPUT_HATCH[GTValues.MV], Direction.SOUTH)
                    .where(' ', Blocks.AIR.defaultBlockState())
                    .where('#', Blocks.AIR.defaultBlockState())
                    .build()))
            .register();

    // Post-Industrial Multiblocks
    public static final MultiblockMachineDefinition INSCRIPTION_MATRIX = REGISTRATE
            .multiblock("inscription_matrix", WorkableElectricMultiblockMachine::new)
            .langValue("ME Inscription Matrix")
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeType(AstroRecipeTypes.INSCRIPTION)
            .recipeModifiers(OC_PERFECT_SUBTICK, BATCH_MODE)
            .appearanceBlock(FUTURA_COMPUTER_CASING)
            .hasBER(true)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("F###F", "AAAAA", "AAAAA", "#AAA#")
                    .aisle("#####", "AAAAA", "A#P#A", "AAAAA")
                    .aisle("#####", "AAAAA", "AP#PA", "AAAAA")
                    .aisle("#####", "AAAAA", "A#P#A", "AAAAA")
                    .aisle("F###F", "AAAAA", "AA@AA", "#AAA#")
                    .where('@', controller(blocks(definition.get())))
                    .where('A', blocks(FUTURA_COMPUTER_CASING.get()).setMinGlobalLimited(40)
                            .or(abilities(IMPORT_ITEMS).setPreviewCount(1))
                            .or(abilities(EXPORT_ITEMS).setPreviewCount(1))
                            .or(abilities(IMPORT_FLUIDS).setPreviewCount(1))
                            .or(abilities(INPUT_ENERGY).setPreviewCount(1).setExactLimit(1))
                            .or(abilities(MAINTENANCE).setExactLimit(1)))
                    .where('F', frames(GTMaterials.StainlessSteel))
                    .where('P', blocks(CASING_STEEL_PIPE.get()))
                    .where('#', any())
                    .build())
            .modelProperty(GTMachineModelProperties.RECIPE_LOGIC_STATUS, RecipeLogic.Status.IDLE)
            .tooltips(Component.translatable("gtceu.multiblock.exact_hatch_1.tooltip"))
            .model(AstroMachineModels.createActiveCasingMachineModel(
                    AstroCore.id("block/casings/functional_casings/futura_computer_housing"),
                    AstroCore.id("block/casings/functional_casings/futura_computer_housing_active"),
                    GTCEu.id("block/machines/laser_engraver"))
                    .andThen(b -> b.addDynamicRenderer(() -> new AEMultiPartRender(FUTURA_COMPUTER_CASING))))
            .register();

    public static final MultiblockMachineDefinition LARGE_GAS_COLLECTOR = REGISTRATE
            .multiblock("large_gas_collector", WorkableElectricMultiblockMachine::new)
            .rotationState(RotationState.ALL)
            .langValue("Large Gas Collection Unit")
            .recipeTypes(GTRecipeTypes.GAS_COLLECTOR_RECIPES, GTRecipeTypes.AIR_SCRUBBER_RECIPES)
            .recipeModifier(BATCH_MODE)
            .tooltips(Component.translatable("gtceu.multiblock.parallelizable.tooltip"))
            .tooltips(Component.translatable("gtceu.machine.available_recipe_map_2.tooltip",
                    Component.translatable("gtceu.gas_collector"), Component.translatable("gtceu.air_scrubber")))
            .tooltips(Component.translatable("gtceu.multiblock.exact_hatch_1.tooltip"))
            .appearanceBlock(CASING_HSSE_STURDY)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle(" F F ", " XXX ", " XCX ", " XCX ", " XXX ", "  X  ")
                    .aisle("F   F", "XXXXX", "X###X", "X###X", "X###X", " XXX ")
                    .aisle("     ", "XXXXX", "C#P#C", "C#P#C", "X#P#X", "XX@XX")
                    .aisle("F   F", "XXXXX", "X###X", "X###X", "X###X", " XXX ")
                    .aisle(" F F ", " XXX ", " XCX ", " XCX ", " XXX ", "  X  ")
                    .where("@", controller(blocks(definition.get())))
                    .where("X", blocks(CASING_HSSE_STURDY.get()).setMinGlobalLimited(45)
                            .or(abilities(INPUT_ENERGY).setExactLimit(1))
                            .or(abilities(IMPORT_ITEMS))
                            .or(abilities(IMPORT_FLUIDS))
                            .or(abilities(EXPORT_FLUIDS))
                            .or(abilities(PartAbility.PARALLEL_HATCH).setMaxGlobalLimited(1))
                            .or(abilities(MAINTENANCE).setExactLimit(1)))
                    .where("P", blocks(CASING_TUNGSTENSTEEL_PIPE.get()))
                    .where("C", blocks(FILTER_CASING.get()))
                    .where("F", frames(GTMaterials.HSSG))
                    .where(" ", any())
                    .where("#", air())
                    .build())
            .workableCasingModel(GTCEu.id("block/casings/solid/machine_casing_sturdy_hsse"),
                    GTCEu.id("block/multiblock/cleanroom"))
            .register();

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
                    .where('S', controller(blocks(definition.get())))
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

    public static void init() {}
}
