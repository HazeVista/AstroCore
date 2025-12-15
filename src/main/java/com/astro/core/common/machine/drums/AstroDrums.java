package com.astro.core.common.machine.drums;

import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraftforge.fluids.FluidType;

import static com.astro.core.common.registry.AstroRegistry.REGISTRATE;
import static com.gregtechceu.gtceu.common.data.machines.GTMachineUtils.registerDrum;

public class AstroDrums {

    public static MachineDefinition RHODIUM_PLATED_PALLADIUM_DRUM;
    public static MachineDefinition NAQUADAH_ALLOY_DRUM;
    public static MachineDefinition DARMSTADTIUM_DRUM;
    public static MachineDefinition NEUTRONIUM_DRUM;

    public static void register() {
        RHODIUM_PLATED_PALLADIUM_DRUM = registerDrum(REGISTRATE, GTMaterials.RhodiumPlatedPalladium,
                2048 * FluidType.BUCKET_VOLUME, "Rhodium Plated Palladium Drum");
        NAQUADAH_ALLOY_DRUM = registerDrum(REGISTRATE, GTMaterials.NaquadahAlloy, 4096 * FluidType.BUCKET_VOLUME,
                "Naquadah Alloy Drum");
        DARMSTADTIUM_DRUM = registerDrum(REGISTRATE, GTMaterials.Darmstadtium, 8192 * FluidType.BUCKET_VOLUME,
                "Darmstadtium Drum");
        NEUTRONIUM_DRUM = registerDrum(REGISTRATE, GTMaterials.Neutronium, 16384 * FluidType.BUCKET_VOLUME,
                "Neutronium Drum");
    }
}
