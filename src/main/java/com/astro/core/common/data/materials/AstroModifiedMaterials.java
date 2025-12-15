package com.astro.core.common.data.materials;


import com.gregtechceu.gtceu.api.data.chemical.material.properties.FluidPipeProperties;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.common.data.GTMaterials;


public class AstroModifiedMaterials {
    public static void init(){
        GTMaterials.RhodiumPlatedPalladium.setProperty(PropertyKey.FLUID_PIPE,
                new FluidPipeProperties(5000,
                        300,
                        true,
                        true,
                        true,
                        false)
        );

        GTMaterials.NaquadahAlloy.setProperty(PropertyKey.FLUID_PIPE,
                new FluidPipeProperties(10000,
                        500,
                        true,
                        true,
                        true,
                        true)
        );
        GTMaterials.Darmstadtium.setProperty(PropertyKey.FLUID_PIPE,
                new FluidPipeProperties(50000,
                        1000,
                        true,
                        true,
                        true,
                        true)
        );


    }

}
