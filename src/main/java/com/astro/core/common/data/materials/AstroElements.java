package com.astro.core.common.data.materials;

import com.gregtechceu.gtceu.api.data.chemical.Element;
import com.gregtechceu.gtceu.api.registry.GTRegistries;

public class AstroElements {

    public static final Element DE = createAndRegister(0, 60, -1, null, "desh", "De", false);
    public static final Element OT = createAndRegister(0, 80, -1, null, "ostrum", "Ot", false);
    public static final Element CT = createAndRegister(0, 90, -1, null, "calorite", "Ct", false);
    public static final Element JP = createAndRegister(0, 120, -1, null, "juperium", "Jp", false);
    public static final Element SY = createAndRegister(0, 110, -1, null, "saturlyte", "Sy", false);
    public static final Element E = createAndRegister(0, 1500, -1, null, "electrolyte", "⚡", false);
    public static final Element SS = createAndRegister(0, 98, -1, null, "sky_stone", "✨", false);
    public static final Element MN = createAndRegister(0, 1, -1, null, "mana", "✨", false);
    public static final Element AE = createAndRegister(0, 1, -1, null, "aether", "✨", false);
    public static final Element UK = createAndRegister(0, 98, -1, null, "unknown", "?", false);
    public static final Element AD = createAndRegister(0, 500, -1, null, "debris", "An*", false);
    public static final Element NH = createAndRegister(0, 800, -1, null, "netherite", "An", false);
    
    public static Element createAndRegister(long protons, long neutrons, long halfLifeSeconds, String decayTo,
                                            String name, String symbol, boolean isIsotope) {
        Element element = new Element(protons, neutrons, halfLifeSeconds, decayTo, name, symbol, isIsotope);
        GTRegistries.ELEMENTS.register(name, element);
        return element;
    }

    public static void init() {}
}
