package com.astro.core.common.machine.multiblock.generator;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.MultiblockShapeInfo;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.common.data.GCYMBlocks;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Blocks;

import com.astro.core.AstroCore;
import com.astro.core.common.data.recipe.AstroRecipeTypes;

import java.util.ArrayList;
import java.util.List;

import static com.astro.core.common.data.AstroBlocks.*;
import static com.astro.core.common.machine.multiblock.generator.AstroGeneratorRegistry.*;
import static com.astro.core.common.registry.AstroRegistry.REGISTRATE;
import static com.gregtechceu.gtceu.api.data.RotationState.ALL;
import static com.gregtechceu.gtceu.api.pattern.Predicates.blocks;
import static com.gregtechceu.gtceu.api.pattern.Predicates.controller;
import static com.gregtechceu.gtceu.api.pattern.util.RelativeDirection.*;
import static com.gregtechceu.gtceu.common.data.GTMachines.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeModifiers.BATCH_MODE;

@SuppressWarnings("unused")
public class AstroGeneratorMultiMachines {

    public static MultiblockMachineDefinition AETHER_ENGINE;
    public static MultiblockMachineDefinition FARADAY_GENERATOR;
    public static MultiblockMachineDefinition OVERDRIVE_COMBUSTION_ENGINE;
    public static MultiblockMachineDefinition OVERDRIVE_STEAM_TURBINE;
    public static MultiblockMachineDefinition OVERDRIVE_GAS_TURBINE;

    public static void init() {
        AETHER_ENGINE = registerAstroTurbine(
                "aether_turbine", "§3Æther§r Engine",
                GTValues.EV, AstroRecipeTypes.AETHER_ENGINE_RECIPES,
                ALFSTEEL_ENGINE_CASING, ALFSTEEL_GEARBOX_CASING,
                AstroCore.id("block/generators/machine_casing_turbine_alfsteel"),
                AstroCore.id("block/multiblock/aether_engine"), false);

        FARADAY_GENERATOR = REGISTRATE
                .multiblock("faraday_electromagnetic_generator", FaradayGeneratorMachine::new)
                .rotationState(ALL)
                .allowExtendedFacing(true)
                .recipeType(AstroRecipeTypes.FARADAY_GENERATOR_RECIPES)
                .recipeModifiers(FaradayGeneratorMachine::recipeModifier, BATCH_MODE)
                .appearanceBlock(GCYMBlocks.CASING_NONCONDUCTING)
                .pattern(definition -> FactoryBlockPattern.start(LEFT, UP, BACK)

                        .aisle("#XXXXX#", "XXXXXXX", "XXGXGXX", "XXG@GXX", "XXGXGXX", "XXXXXXX", "#XXXXX#")

                        .aisle("#XXXXX#", "X MMM X", "XM   MX", "XM C MX", "XM   MX", "X MMM X", "#XXXXX#")
                        .aisle("#XXXXX#", "X MMM X", "XM   MX", "XM C MX", "XM   MX", "X MMM X", "#XXXXX#")
                        .aisle("#XXXXX#", "X MMM X", "XM   MX", "XM C MX", "XM   MX", "X MMM X", "#XXXXX#")
                        .aisle("#XXXXX#", "X MMM X", "XM   MX", "XM C MX", "XM   MX", "X MMM X", "#XXXXX#")
                        .setRepeatable(1, 61)
                        .aisle("#XXXXX#", "XXXXXXX", "XXGXGXX", "XXGDGXX", "XXGXGXX", "XXXXXXX", "#XXXXX#")


                        .where('@', controller(blocks(definition.get())))
                        .where('X', Predicates.blocks(GCYMBlocks.CASING_NONCONDUCTING.get()).setMinGlobalLimited(140)
                                .or(Predicates.abilities(PartAbility.IMPORT_FLUIDS).setMaxGlobalLimited(2))
                                .or(Predicates.abilities(PartAbility.IMPORT_ITEMS).setMaxGlobalLimited(1))
                                .or(Predicates.abilities(PartAbility.EXPORT_ITEMS).setMaxGlobalLimited(1))
                                .or(Predicates.abilities(PartAbility.EXPORT_FLUIDS).setMaxGlobalLimited(1))
                                .or(Predicates.abilities(PartAbility.MAINTENANCE).setExactLimit(1)))
                        .where('M', Predicates.blocks(ELECTROMAGNET_MK1.get())
                                .or(Predicates.blocks(ELECTROMAGNET_MK2.get()))
                                .or(Predicates.blocks(ELECTROMAGNET_MK3.get())))
                        .where('C', Predicates.blocks(FARADAY_GENERATOR_COIL.get()))
                        .where('D', Predicates.abilities(PartAbility.OUTPUT_ENERGY))
                        .where('G', Predicates.blocks(GTBlocks.CASING_LAMINATED_GLASS.get()))
                        .where('#', Predicates.any())
                        .where(' ', Predicates.air())
                        .build())
                .workableCasingModel(GTCEu.id("block/casings/gcym/nonconducting_casing"),
                        AstroCore.id("block/multiblock/faraday_electromagnetic_generator"))
                .tooltipBuilder((stack, tooltip) -> {
                    tooltip.add(Component.translatable("astrogreg.machine.faraday_generator_description.tooltip"));
                    tooltip.add(Component.translatable("astrogreg.machine.faraday_generator_expanding.tooltip"));
                    tooltip.add(Component.translatable("astrogreg.machine.faraday_generator_magnets.tooltip"));
                    tooltip.add(Component.translatable("astrogreg.machine.faraday_generator_fluids.tooltip"));
                    tooltip.add(Component.translatable("astrogreg.machine.faraday_generator_returns.tooltip"));
                    tooltip.add(Component.translatable("astrogreg.machine.faraday_generator_max_length.tooltip"));
                })
                .shapeInfos(definition -> {
                    List<MultiblockShapeInfo> shapeInfos = new ArrayList<>();

                    for (int length = 1; length <= 61; length++) {
                        MultiblockShapeInfo.ShapeInfoBuilder builder = MultiblockShapeInfo.builder()
                                .where('@', definition, Direction.NORTH)
                                .where('X', GCYMBlocks.CASING_NONCONDUCTING.getDefaultState())
                                .where('M', ELECTROMAGNET_MK1.getDefaultState())
                                .where('C', FARADAY_GENERATOR_COIL.getDefaultState())
                                .where('D', ENERGY_OUTPUT_HATCH[GTValues.LV], Direction.SOUTH)
                                .where('G', GTBlocks.CASING_LAMINATED_GLASS.getDefaultState())
                                .where('I', ITEM_IMPORT_BUS[GTValues.LV], Direction.NORTH)
                                .where('O', ITEM_EXPORT_BUS[GTValues.LV], Direction.NORTH)
                                .where('F', FLUID_IMPORT_HATCH[GTValues.LV], Direction.NORTH)
                                .where('E', FLUID_EXPORT_HATCH[GTValues.LV], Direction.NORTH)
                                .where('A', AUTO_MAINTENANCE_HATCH, Direction.NORTH)
                                .where(' ', Blocks.AIR.defaultBlockState())
                                .aisle(" IOAFE ", "XXXXXXX", "XXGXGXX", "XXG@GXX", "XXGXGXX", "XXXXXXX", " XXXXX ")
                                .aisle(" XXXXX ", "X MMM X", "XM   MX", "XM C MX", "XM   MX", "X MMM X", " XXXXX ")
                                .aisle(" XXXXX ", "X MMM X", "XM   MX", "XM C MX", "XM   MX", "X MMM X", " XXXXX ")
                                .aisle(" XXXXX ", "X MMM X", "XM   MX", "XM C MX", "XM   MX", "X MMM X", " XXXXX ")
                                .aisle(" XXXXX ", "X MMM X", "XM   MX", "XM C MX", "XM   MX", "X MMM X", " XXXXX ");

                        for (int i = 0; i < length - 1; i++) {
                            builder.aisle(" XXXXX ", "X MMM X", "XM   MX", "XM C MX", "XM   MX", "X MMM X", " XXXXX ");
                        }

                        builder.aisle(" XXXXX ", "XXXXXXX", "XXGXGXX", "XXGDGXX", "XXGXGXX", "XXXXXXX", " XXXXX ");
                        shapeInfos.add(builder.build());
                    }
                    return shapeInfos;
                })
                .register();

        OVERDRIVE_COMBUSTION_ENGINE = registerAstroCombustionEngine(
                "overdrive_combustion_engine",
                GTValues.ZPM, MACHINE_CASING_NAQUADAH_ALLOY,
                GEARBOX_CASING_NAQUADAH_ALLOY, ULTIMATE_INTAKE_CASING,
                AstroCore.id("block/casings/machine_casing_invariant_naquadah_alloy"),
                GTCEu.id("block/multiblock/generator/extreme_combustion_engine"));

        OVERDRIVE_STEAM_TURBINE = registerAstroOverdriveTurbine(
                "overdrive_steam_turbine", "Overdrive Steam Turbine",
                GTValues.ZPM, GTRecipeTypes.STEAM_TURBINE_FUELS,
                TURBINE_CASING_RHODIUM_PLATED_PALLADIUM, GEARBOX_CASING_RHODIUM_PLATED_PALLADIUM,
                GTMaterials.RhodiumPlatedPalladium,
                AstroCore.id("block/generators/machine_casing_turbine_rhodium_plated_palladium"),
                GTCEu.id("block/multiblock/generator/large_plasma_turbine"), false);

        OVERDRIVE_GAS_TURBINE = registerAstroOverdriveTurbine(
                "overdrive_gas_turbine", "Overdrive Gas Turbine",
                GTValues.ZPM, GTRecipeTypes.GAS_TURBINE_FUELS,
                TURBINE_CASING_NAQUADAH_ALLOY, GEARBOX_CASING_NAQUADAH_ALLOY, GTMaterials.NaquadahAlloy,
                AstroCore.id("block/generators/machine_casing_turbine_naquadah_alloy"),
                GTCEu.id("block/multiblock/generator/large_plasma_turbine"), true);
    }
}
