package com.astro.core.datagen.lang;

import com.tterrag.registrate.providers.RegistrateLangProvider;

public class AstroLangHandler {

    public static void init(RegistrateLangProvider provider) {
        provider.add("astrogreg.machine.water_hatch.tooltip", "§eAccepted Fluid:§r Water");
        provider.add("astrogreg.machine.large_miner.zpm.tooltip", "Planetary Depletion Apparatus");
        provider.add("astrogreg.machine.fluid_drilling_rig.iv.tooltip", "Crust Sucker");

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
