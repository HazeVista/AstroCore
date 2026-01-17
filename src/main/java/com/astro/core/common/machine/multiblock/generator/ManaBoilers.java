package com.astro.core.common.machine.multiblock.generator;

import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;

import com.astro.core.AstroCore;
import com.astro.core.common.data.block.AstroBlocks;
import com.astro.core.common.data.configs.AstroConfigs;

import static com.astro.core.common.machine.multiblock.generator.AstroBoilers.registerAstroBoiler;

public class ManaBoilers {

    public static MultiblockMachineDefinition MANASTEEL_MANA_BOILER;
    public static MultiblockMachineDefinition TERRASTEEL_MANA_BOILER;
    public static MultiblockMachineDefinition ALFSTEEL_MANA_BOILER;

    public static void init() {
        // Manasteel Boiler
        MANASTEEL_MANA_BOILER = registerAstroBoiler(
                "manasteel_mana",
                "Large §9Manasteel§r Mana Boiler",
                AstroBlocks.MANASTEEL_MACHINE_CASING,
                AstroBlocks.MANASTEEL_PIPE_CASING,
                AstroBlocks.FIREBOX_MANASTEEL,
                AstroCore.id("block/generators/machine_casing_manasteel_plated_bricks"),
                // Change this line to match the name in AstroBlocks
                AstroBlocks.MANASTEEL_FIREBOX,
                AstroConfigs.INSTANCE.Steam.manasteelBoilerMaxTemperature,
                AstroConfigs.INSTANCE.Steam.manasteelBoilerHeatSpeed);

        // Terrasteel Boiler
        TERRASTEEL_MANA_BOILER = registerAstroBoiler(
                "terrasteel_mana",
                "Large §2Terrasteel§r Mana Boiler",
                AstroBlocks.TERRASTEEL_MACHINE_CASING,
                AstroBlocks.TERRASTEEL_PIPE_CASING, // Now available
                AstroBlocks.FIREBOX_TERRASTEEL,
                AstroCore.id("block/generators/terrasteel_casing"), // Updated path
                AstroBlocks.TERRASTEEL_FIREBOX, // Custom Record
                AstroConfigs.INSTANCE.Steam.terrasteelBoilerMaxTemperature,
                AstroConfigs.INSTANCE.Steam.terrasteelBoilerHeatSpeed);

        // Alfsteel Boiler
        ALFSTEEL_MANA_BOILER = registerAstroBoiler(
                "alfsteel_mana",
                "Large §dAlfsteel§r Mana Boiler",
                AstroBlocks.ALFSTEEL_MACHINE_CASING,
                AstroBlocks.ALFSTEEL_PIPE_CASING, // Now available
                AstroBlocks.FIREBOX_ALFSTEEL,
                AstroCore.id("block/generators/machine_casing_turbine_alfsteel"),
                AstroBlocks.ALFSTEEL_FIREBOX, // Custom Record
                AstroConfigs.INSTANCE.Steam.alfsteelBoilerMaxTemperature,
                AstroConfigs.INSTANCE.Steam.alfsteelBoilerHeatSpeed);
    }
}
