package com.astro.core.common.data.materials;

import com.astro.core.AstroCore;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet;

public class AstroPolymers {

    public static Material POLYAMIDE_IMIDE;
    public static void register() {
        // GT Progression
        POLYAMIDE_IMIDE = new Material.Builder(
                AstroCore.id("polyamide_imide"))
                .langValue("Polyamide-Imide")
                .polymer()
                .fluid()
                .dust()
                .ingot()
                .color(0xd9ac37).secondaryColor(0x54301a).iconSet(MaterialIconSet.DULL)
                // .components().formula()
                .buildAndRegister();
    }
    public static void init() {}
}
