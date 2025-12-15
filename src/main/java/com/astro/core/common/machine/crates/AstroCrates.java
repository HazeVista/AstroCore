package com.astro.core.common.machine.crates;

import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.astro.core.common.registry.AstroRegistry.REGISTRATE;
import static com.gregtechceu.gtceu.common.data.machines.GTMachineUtils.registerCrate;

public class AstroCrates {

    public static MachineDefinition RHODIUM_PLATED_PALLADIUM_CRATE;
    public static MachineDefinition NAQUADAH_ALLOY_CRATE;
    public static MachineDefinition DARMSTADTIUM_CRATE;
    public static MachineDefinition NEUTRONIUM_CRATE;

    public static void register() {
        RHODIUM_PLATED_PALLADIUM_CRATE = registerCrate(REGISTRATE, GTMaterials.RhodiumPlatedPalladium, 162,
                "Rhodium Plated Palladium Crate");
        NAQUADAH_ALLOY_CRATE = registerCrate(REGISTRATE, GTMaterials.NaquadahAlloy, 180, "Naquadah Alloy Crate");
        DARMSTADTIUM_CRATE = registerCrate(REGISTRATE, GTMaterials.Darmstadtium, 198, "Darmstadtium Crate");
        NEUTRONIUM_CRATE = registerCrate(REGISTRATE, GTMaterials.Neutronium, 206, "Neutronium Crate");
    }
}
