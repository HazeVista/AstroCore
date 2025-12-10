package com.astro.core.common.data.materials;

import com.gregtechceu.gtceu.api.data.chemical.Element;
import com.gregtechceu.gtceu.api.registry.GTRegistries;

public class AstroElements {

    public static final Element DE = createAndRegister(-1, -1, -1, null, "desh", "De", false);
    public static final Element OT = createAndRegister(-1, -1, -1, null, "ostrum", "Ot", false);
    public static final Element CT = createAndRegister(-1, -1, -1, null, "calorite", "Ct", false);
    public static final Element JP = createAndRegister(-1, -1, -1, null, "juperium", "Jp", false);
    public static final Element SY = createAndRegister(-1, -1, -1, null, "saturlyte", "Sy", false);
    public static final Element E = createAndRegister(-1, -1, -1, null, "electrolyte", "⚡", false);

    public static Element createAndRegister(long protons, long neutrons, long halfLifeSeconds, String decayTo,
                                            String name, String symbol, boolean isIsotope) {
        Element element = new Element(protons, neutrons, halfLifeSeconds, decayTo, name, symbol, isIsotope);
        GTRegistries.ELEMENTS.register(name, element);
        return element;
    }

    public static void init() {}
}
