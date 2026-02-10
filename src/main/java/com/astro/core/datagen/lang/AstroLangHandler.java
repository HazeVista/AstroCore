package com.astro.core.datagen.lang;

import com.tterrag.registrate.providers.RegistrateLangProvider;

@SuppressWarnings("all")
public class AstroLangHandler {

    public static void init(RegistrateLangProvider provider) {
        // multiblock tooltips
        provider.add("astrogreg.machine.coke_oven_description.tooltip",
                "Making better fuels for Steel and Power Generation");
        provider.add("astrogreg.machine.coke_oven_parallels.tooltip",
                "Gains Parallels for each layer in length added for up to 16 Parallels total");

        provider.add("astrogreg.machine.steam_blast_furnace.steam_usage", "Using %s mB/t Steam (%s mB/t x %s)");
        provider.add("astrogreg.machine.steam_blast_furnace.parallels", "Parallels: %s (active: %s)");

        provider.add("astrogreg.machine.solar_boiler_array_sunlit_info.tooltip",
                "Cells must be exposed to direct sunlight to work properly.");
        provider.add("astrogreg.machine.solar_boiler_array_heat_speed.tooltip",
                "Heating speed scales with distance from the Sun.");
        provider.add("astrogreg.machine.solar_boiler_array_heat_scaling.tooltip",
                "Heat scaling: §e−1 K/s per Cell below 40 or +1% heating speed per sunlit Cell above 40");
        provider.add("astrogreg.machine.solar_boiler_array_max_cells.tooltip",
                "Max Cell Count: §e33 x 33 (1089 Cells)");
        provider.add("astrogreg.machine.solar_boiler_array.solar_intensity", "Solar Intensity: %s%%");
        provider.add("astrogreg.machine.solar_boiler_array.temperature", "Temperature: %s°C");
        provider.add("astrogreg.machine.solar_boiler_array.thermal_efficiency", "Thermal Efficiency: %s%%");
        provider.add("astrogreg.machine.solar_boiler_array.cell_quality", "Cell Quality: %sx");
        provider.add("astrogreg.machine.solar_boiler_array.sunlit_cells", "Sunlit Cells: %s");
        provider.add("astrogreg.machine.solar_boiler_array.steam_output", "Steam Output: %s mB/t");
        provider.add("astrogreg.machine.solar_boiler_array.danger_explosive", "DANGER: EXPLOSIVE!");
        provider.add("astrogreg.machine.solar_boiler_array.danger_no_water", "DO NOT ADD WATER!");
        provider.add("astrogreg.machine.solar_boiler_array.danger_cool_first", "Wait for the array to cool first.");
        provider.add("astrogreg.machine.solar_boiler_array.hold_shift", "§o§7Hold Shift for details");

        provider.add("astrogreg.machine.industrial_core.tooltip",
                "Maximum EU/t for this machine is limited by the tier of its §bIndustrial Processing Core§r.");
        provider.add("astrogreg.machine.processing_core.core", "§aProcessing Core:§r §6%s");
        provider.add("astrogreg.machine.processing_core.max_eut", "§aMax EU/t:§r §f%s (%s§r)");
        provider.add("config.jade.plugin_astrogreg.processing_core_info", "§aIndustrial Processing Core Info§r");

        provider.add("astrogreg.machine.faraday_generator_description.tooltip", "§fConverts rotational force into EU");
        provider.add("astrogreg.machine.faraday_generator_expanding.tooltip", "§eBase Production:§r§f 4096 EU/t (2A §5EV§r) per Coil Row.");
        provider.add("astrogreg.machine.faraday_generator_magnets.tooltip", "Higher tiers of §bFaraday Generator Magnet§r rows multiply EU output by 4x for that row.");
        provider.add("astrogreg.machine.faraday_generator_max_length.tooltip", "§aMax Length:§r§e 64 Coil Rows");

        provider.add("astrogreg.machine.large_miner.zpm.tooltip", "Planetary Depletion Apparatus");
        provider.add("astrogreg.machine.fluid_drilling_rig.iv.tooltip", "Crust Sucker");

        // custom hatch tooltips
        provider.add("astrogreg.machine.water_hatch.tooltip", "§eAccepted Fluid:§r Water");

        provider.add("astrogreg.machine.mana_input_hatch.tooltip", "Exotic Matter Input for Multiblocks");
        provider.add("astrogreg.machine.mana_output_hatch.tooltip", "Exotic Matter Output for Multiblocks");

        provider.add("astrogreg.machine.cwu_input_hatch.tooltip", "Accepts CWU from adjacent CWU Generators");

        provider.add("astrogreg.machine.expanded_me_pattern_buffer.tooltip.0",
                "§fAllows expanded direct §6AE2 pattern storage §ffor GregTech Multiblocks.");
        provider.add("astrogreg.machine.expanded_me_pattern_buffer.tooltip.1",
                "§fLink §6Expanded Pattern Buffer Proxies §fwith a §bdatastick §fto link machines together!");
        provider.add("astrogreg.machine.expanded_me_pattern_buffer_proxy.tooltip.0",
                "§fAllows linking many machines to a singular §6Expanded ME Pattern Buffer§f.");
        provider.add("astrogreg.machine.me_tag_input_bus.tooltip.0", "§6Tag‑based AE2 Item Import");
        provider.add("astrogreg.machine.me_tag_input_bus.tooltip.1", "§7Matches items via tag expressions");
        provider.add("astrogreg.machine.me_tag_input_hatch.tooltip.0", "§6Tag‑based AE2 Fluid Import");
        provider.add("astrogreg.machine.me_tag_input_hatch.tooltip.1", "§7Matches fluids via tag expressions");
        provider.add("astrogreg.gui.me_tag.clear", "Clear");
        provider.add("astrogreg.gui.me_tag.whitelist_tags", "Whitelist Tags");
        provider.add("astrogreg.gui.me_tag.blacklist_tags", "Blacklist Tags");
        provider.add("astrogreg.gui.me_tag.placeholder", "...");
        provider.add("astrogreg.gui.me_tag.settings_copied", "Settings Copied: %s");
        provider.add("astrogreg.gui.me_tag.settings_pasted", "Settings Pasted successfully.");
        provider.add("astrogreg.gui.me_tag.item_preview", "Item Preview (Read Only)");
        provider.add("astrogreg.gui.me_tag.datastick_name", "ME Tag Input Bus Configuration Data");
        provider.add("astrogreg.gui.me_tag.fluid_preview", "Fluid Preview (Read Only)");
        provider.add("astrogreg.gui.me_tag.settings_copied_hatch", "Tag Fluid Hatch settings copied");
        provider.add("config.jade.plugin_astrogreg.solar_boiler_info", "Solar Boiler Array Info");

        // cwu generator tooltips
        provider.add("astrogreg.machine.cwu_generator.tooltip.0", "Generates Computational Work Units");
        provider.add("astrogreg.machine.cwu_generator.tooltip.1", "Produces %s CWU/t");
        provider.add("astrogreg.machine.cwu_generator.tooltip.2", "Consumes %s mB/t Lubricant");
        provider.add("astrogreg.machine.cwu_generator.producing", "Producing: %s CWU/t");
        provider.add("astrogreg.machine.cwu_generator.lubricant", "Lubricant: %s mB/t");
        provider.add("astrogreg.machine.cwu_generator.available", "Available: %s CWU");

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
