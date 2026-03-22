package com.astro.core.common.data.materials;

import com.gregtechceu.gtceu.api.data.chemical.material.properties.OreProperty;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;

import static com.astro.core.common.data.materials.AstroMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;

@SuppressWarnings("all")
public class AstroMaterialFlagAddition {

    public static void register() {
        OreProperty Desh = DESH.getProperty(PropertyKey.ORE);
        Desh.setOreByProducts(Iron, OSTRUM, Iron, OSTRUM);
        Desh.setWashedIn(SodiumPersulfate);
        Desh.setSeparatedInto(OSTRUM);

        OreProperty Debris = ANCIENT_DEBRIS.getProperty(PropertyKey.ORE);
        Debris.setOreByProducts(Magnetite, VanadiumMagnetite, Magnetite);
        Debris.setSeparatedInto(Gold);

        OreProperty Ostrum = OSTRUM.getProperty(PropertyKey.ORE);
        Ostrum.setOreByProducts(Manganese, CALORITE, Boron, Magnesium);
        Ostrum.setSeparatedInto(DESH);

        // OreProperty Calorite = CALORITE.getProperty(PropertyKey.ORE);
        // Calorite.setOreByProducts(Nickel, CALORITE, CALORITE, Platinum);
    }
}
