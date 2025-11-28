package com.astro.core.common.machine.multiblock.generator;

import com.astro.core.AstroCore;
import com.astro.core.common.data.AstroRecipeTypes;
import com.astro.core.common.data.block.AstroBlocks;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;

import static com.gregtechceu.gtceu.common.data.machines.GTMachineUtils.registerLargeTurbine;

@SuppressWarnings("unused")

public class AetherEngine {

    static{

    }

    public static final MultiblockMachineDefinition AETHER_ENGINE = registerLargeTurbine(
            "aether_turbine",
            GTValues.EV,
            AstroRecipeTypes.AETHER_ENGINE_RECIPES,
            AstroBlocks.ALFSTEEL_MACHINE_CASING,
            AstroBlocks.ALFSTEEL_GEARBOX_CASING,
            AstroCore.id("block/generators/machine_casing_turbine_alfsteel"),
            AstroCore.id("block/multiblock/aether_engine"), false);

    public static void init() {
    }
}


