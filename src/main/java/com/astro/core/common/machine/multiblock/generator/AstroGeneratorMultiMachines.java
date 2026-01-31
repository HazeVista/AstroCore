package com.astro.core.common.machine.multiblock.generator;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import com.astro.core.AstroCore;
import com.astro.core.common.data.AstroRecipeTypes;

import static com.astro.core.common.data.AstroBlocks.*;
import static com.astro.core.common.machine.multiblock.generator.AstroGeneratorRegistry.*;

@SuppressWarnings("unused")

public class AstroGeneratorMultiMachines {

    public static MultiblockMachineDefinition AETHER_ENGINE;
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
