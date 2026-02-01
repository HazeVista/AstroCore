package com.astro.core.datagen.lang;

import com.tterrag.registrate.providers.RegistrateLangProvider;

@SuppressWarnings("all")
public class AstroLangHandler {

    public static void init(RegistrateLangProvider provider) {

        // multiblock tooltips
        provider.add("astrogreg.machine.coke_oven_machine.tooltip", "Making better fuels for Steel and Power Generation");
        provider.add("astrogreg.machine.coke_oven_parallels.tooltip", "Gains Parallels for each layer in length added for up to 16 Parallels total");

        provider.add("astrogreg.machine.solar_boiler_array_sunlit_info.tooltip", "Cells must be exposed to direct sunlight to work properly.");
        provider.add("astrogreg.machine.solar_boiler_array_heat_speed.tooltip", "Heating speed scales with distance from the Sun.");
        provider.add("astrogreg.machine.solar_boiler_array_heat_scaling.tooltip", "Heat scaling: §e−1 K/s per Cell below 40 or +1% heating speed per sunlit Cell above 40");
        provider.add("astrogreg.machine.solar_boiler_array_max_cells.tooltip", "Max Cell Count: §e33 x 33 (1089 Cells)");

        provider.add("astrogreg.machine.industrial_core.tooltip", "Maximum EU/t for this machine is limited by the tier of its §bIndustrial Processing Core§r.");

        provider.add("astrogreg.machine.large_miner.zpm.tooltip", "Planetary Depletion Apparatus");
        provider.add("astrogreg.machine.fluid_drilling_rig.iv.tooltip", "Crust Sucker");

        // custom hatch tooltips
        provider.add("astrogreg.machine.water_hatch.tooltip", "§eAccepted Fluid:§r Water");

        provider.add("astrogreg.machine.mana_input_hatch.tooltip", "Exotic Matter Input for Multiblocks");
        provider.add("astrogreg.machine.mana_output_hatch.tooltip", "Exotic Matter Output for Multiblocks");

        // cwu generator tooltips
        provider.add("astrogreg.machine.cwu_generator.tooltip.0", "Generates Computational Work Units");
        provider.add("astrogreg.machine.cwu_generator.tooltip.1", "Produces %s CWU/t");
        provider.add("astrogreg.machine.cwu_generator.tooltip.2", "Consumes %s mB/t Lubricant");
        provider.add("astrogreg.machine.cwu_generator.producing", "Producing: %s CWU/t");
        provider.add("astrogreg.machine.cwu_generator.lubricant", "Lubricant: %s mB/t");
        provider.add("astrogreg.machine.cwu_generator.available", "Available: %s CWU");
        provider.add("astrogreg.machine.cwu_input_hatch.tooltip", "Accepts CWU from adjacent CWU Generators");

        // miscellaneous
        multilineLang(provider, "astrogreg.gui.configurator_slot.tooltip",
                "§fConfigurator Slot§r\n§7Place a §6Programmed Circuit§7 in this slot to\n§7change its configured value.\n§aA Programmed Circuit in this slot is also valid for recipe inputs.§r");
    }

    protected static void multilineLang(RegistrateLangProvider provider, String key, String multiline) {
        var lines = multiline.split("\n");
        multiLang(provider, key, lines);
    }

    protected static void multiLang(RegistrateLangProvider provider, String key, String... values) {
        for (var i = 0; i < values.length; i++) {
            provider.add(getSubKey(key, i), values[i]);
        }
    }

    protected static String getSubKey(String key, int index) {
        return key + "." + index;
    }
}
