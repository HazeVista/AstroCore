package com.astro.core.common.data.materials;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlag;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.*;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.astro.core.common.data.materials.AstroMaterialFlags.*;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;

public class AstroModifiedMaterials {

    private static final Object[][] MATERIAL_MODIFIERS = {
            { "titanium", new MaterialFlag[] { GENERATE_DENSE } },
            { "neutronium", new MaterialFlag[] { GENERATE_DENSE, GENERATE_SLEEVE, GENERATE_SMALL_GEAR } },
            { "iron", new MaterialFlag[] { GENERATE_FOIL } },
            { "potin", new MaterialFlag[] { GENERATE_FOIL, GENERATE_RING } },
            { "brass", new MaterialFlag[] { GENERATE_FOIL, GENERATE_RING } },
            { "invar", new MaterialFlag[] { GENERATE_RING, GENERATE_FOIL } },
            { "red_alloy", new MaterialFlag[] { GENERATE_RING } },
            { "zinc", new MaterialFlag[] { GENERATE_BOLT_SCREW } },
            { "nickel", new MaterialFlag[] { GENERATE_FOIL, GENERATE_RING, GENERATE_ROD, GENERATE_BOLT_SCREW } },
            { "netherite", new MaterialFlag[] { GENERATE_BOLT_SCREW, GENERATE_ROUND,
                    GENERATE_DENSE, GENERATE_ROD, GENERATE_PLATE, GENERATE_GEAR,
                    GENERATE_LONG_ROD, DISABLE_DECOMPOSITION, GENERATE_SLEEVE } },
            { "tin", new MaterialFlag[] { GENERATE_SLEEVE } },
            { "steel", new MaterialFlag[] { GENERATE_SLEEVE } },
            { "stainless_steel", new MaterialFlag[] { GENERATE_SLEEVE } },
            { "tungsten_carbide", new MaterialFlag[] { GENERATE_SLEEVE, GENERATE_ROUND } },
            { "duranium", new MaterialFlag[] { GENERATE_SLEEVE } },
            { "tritanium", new MaterialFlag[] { GENERATE_SLEEVE, GENERATE_ROTOR } },
            { "graphene", new MaterialFlag[] { GENERATE_FINE_WIRE } },
            { "ultimet", new MaterialFlag[] { GENERATE_SLEEVE, GENERATE_ROUND } },
            { "rtan", new MaterialFlag[] { GENERATE_FINE_WIRE } },
            { "kanthal", new MaterialFlag[] { GENERATE_FINE_WIRE } },
            { "uranium_triplatinum", new MaterialFlag[] { GENERATE_FINE_WIRE } },
            { "siao", new MaterialFlag[] { GENERATE_FINE_WIRE } },
            { "rpp", new MaterialFlag[] { GENERATE_FRAME, GENERATE_GEAR } },
            { "rose_gold", new MaterialFlag[] { GENERATE_FRAME, DISABLE_ALLOY_PROPERTY } },
            { "cobalt_brass", new MaterialFlag[] { GENERATE_FRAME } },
            { "red_steel", new MaterialFlag[] { GENERATE_FRAME } },
            { "vanadium_steel", new MaterialFlag[] { GENERATE_FRAME, DISABLE_ALLOY_PROPERTY } },
            { "bismuth_bronze", new MaterialFlag[] { GENERATE_FRAME, DISABLE_ALLOY_PROPERTY } },
            { "sbr", new MaterialFlag[] { GENERATE_FRAME } },
            { "silicone", new MaterialFlag[] { GENERATE_FRAME } },
            { "pvc", new MaterialFlag[] { GENERATE_FRAME } }

    };

    private static void applyFluidPipeProperties() {
        setFluidPipe(
                GTMaterials.RhodiumPlatedPalladium,
                5000, 300,
                true, true, true, false);

        setFluidPipe(
                GTMaterials.NaquadahAlloy,
                10000, 500,
                true, true, true, true);

        setFluidPipe(
                GTMaterials.Darmstadtium,
                50000, 1_000,
                true, true, true, true);
    }

    private static void setFluidPipe(
                                     Material material,
                                     int capacity,
                                     int temperature,
                                     boolean gas,
                                     boolean acid,
                                     boolean plasma,
                                     boolean cryogenic) {
        material.setProperty(
                PropertyKey.FLUID_PIPE,
                new FluidPipeProperties(
                        capacity,
                        temperature,
                        gas,
                        acid,
                        plasma,
                        cryogenic));
    }

    private static Material getMaterialByName(String name) {
        return switch (name.toLowerCase()) {
            case "titanium" -> Titanium;
            case "neutronium" -> Neutronium;
            case "iron" -> Iron;
            case "potin" -> Potin;
            case "brass" -> Brass;
            case "invar" -> Invar;
            case "red_alloy" -> RedAlloy;
            case "zinc" -> Zinc;
            case "nickel" -> Nickel;
            case "neptunium" -> Neptunium;
            case "netherite" -> Netherite;
            case "tin" -> Tin;
            case "steel" -> Steel;
            case "stainless_steel" -> StainlessSteel;
            case "tungsten_carbide" -> TungstenCarbide;
            case "hsss" -> HSSS;
            case "duranium" -> Duranium;
            case "tritanium" -> Tritanium;
            case "graphene" -> Graphene;
            case "ultimet" -> Ultimet;
            case "rtan" -> RutheniumTriniumAmericiumNeutronate;
            case "kanthal" -> Kanthal;
            case "uranium_triplatinum" -> UraniumTriplatinum;
            case "siao" -> SamariumIronArsenicOxide;
            case "rpp" -> RhodiumPlatedPalladium;
            case "red_steel" -> RedSteel;
            case "vanadium_steel" -> VanadiumSteel;
            case "rose_gold" -> RoseGold;
            case "cobalt_brass" -> Cobalt;
            case "bismuth_bronze" -> BismuthBronze;
            case "sbr" -> StyreneButadieneRubber;
            case "pvc" -> PolyvinylChloride;
            case "silicone" -> SiliconeRubber;
            default -> null;
        };
    }

    private static void addMaterialFlags() {
        for (Object[] modifier : MATERIAL_MODIFIERS) {
            String name = (String) modifier[0];
            MaterialFlag[] flags = (MaterialFlag[]) modifier[1];

            Material material = getMaterialByName(name);

            if (material != null) {
                material.addFlags(flags);
            }
        }
    }

    private static final Object[][] PERIODIC_ELEMENTS = {
            { "neptunium", new String[] { "ingot", "fluid" } },
            { "selenium", new String[] { "dust" } },
            { "netherite", new String[] { "fluid" } }
    };

    private static void modifyPeriodicElements() {
        for (Object[] element : PERIODIC_ELEMENTS) {
            String name = (String) element[0];
            String[] types = (String[]) element[1];

            Material material = getMaterialByName(name);

            if (material == null) {
                continue;
            }

            // Add properties
            for (String type : types) {
                switch (type) {
                    case "ingot":
                        if (!material.hasProperty(PropertyKey.INGOT)) {
                            material.setProperty(PropertyKey.INGOT, new IngotProperty());
                        }
                        break;
                    case "dust":
                        if (!material.hasProperty(PropertyKey.DUST)) {
                            material.setProperty(PropertyKey.DUST, new DustProperty());
                        }
                        break;
                    case "fluid":
                        if (!material.hasProperty(PropertyKey.FLUID)) {
                            FluidProperty fluidProperty = new FluidProperty();
                            fluidProperty.getStorage().enqueueRegistration(
                                    com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys.LIQUID,
                                    new com.gregtechceu.gtceu.api.fluids.FluidBuilder());
                            material.setProperty(PropertyKey.FLUID, fluidProperty);
                        }
                        break;
                }
            }
        }
    }

    public static void init() {
        modifyPeriodicElements();
        addMaterialFlags();
        applyFluidPipeProperties();
    }
}
