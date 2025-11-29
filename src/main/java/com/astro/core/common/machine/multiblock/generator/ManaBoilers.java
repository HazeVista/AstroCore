package com.astro.core.common.machine.multiblock.generator;

import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;

import com.astro.core.AstroCore;
import com.astro.core.common.data.block.AstroBlocks;
import com.astro.core.common.data.configs.AstroConfigs;

import static com.astro.core.common.machine.multiblock.generator.AstroBoilers.registerAstroBoiler;

public class ManaBoilers {

    public static final MultiblockMachineDefinition MANASTEEL_MANA_BOILER = registerAstroBoiler(
            "manasteel_mana",
            "Large §9Manasteel§r Mana Boiler",
            AstroBlocks.MANASTEEL_MACHINE_CASING,
            AstroBlocks.MANASTEEL_PIPE_CASING,
            AstroBlocks.FIREBOX_MANASTEEL,
            AstroCore.id("block/generators/machine_casing_manasteel_plated_bricks"),
            AstroBlocks.MANASTEEL_FIREBOX,
            AstroConfigs.INSTANCE.features.manasteelBoilerMaxTemperature,
            AstroConfigs.INSTANCE.features.manasteelBoilerHeatSpeed);

    public static final MultiblockMachineDefinition TERRASTEEL_MANA_BOILER = registerAstroBoiler(
            "terrasteel_mana",
            "Large §2Terrasteel§r Mana Boiler",
            AstroBlocks.TERRASTEEL_MACHINE_CASING,
            AstroBlocks.TERRASTEEL_PIPE_CASING,
            AstroBlocks.FIREBOX_TERRASTEEL,
            AstroCore.id("block/generators/terrasteel_casing"),
            AstroBlocks.TERRASTEEL_FIREBOX,
            AstroConfigs.INSTANCE.features.terrasteelBoilerMaxTemperature,
            AstroConfigs.INSTANCE.features.terrasteelBoilerHeatSpeed);

    public static final MultiblockMachineDefinition ALFSTEEL_MANA_BOILER = registerAstroBoiler(
            "alfsteel_mana",
            "Large §dAlfsteel§r Mana Boiler",
            AstroBlocks.ALFSTEEL_MACHINE_CASING,
            AstroBlocks.ALFSTEEL_PIPE_CASING,
            AstroBlocks.FIREBOX_ALFSTEEL,
            AstroCore.id("block/generators/machine_casing_turbine_alfsteel"),
            AstroBlocks.ALFSTEEL_FIREBOX,
            AstroConfigs.INSTANCE.features.alfsteelBoilerMaxTemperature,
            AstroConfigs.INSTANCE.features.alfsteelBoilerHeatSpeed);

    public static void init() {}
}
