package com.astro.core.common.data.materials;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlag;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.BlastProperty;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialStack;
import com.gregtechceu.gtceu.api.fluids.FluidBuilder;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import com.astro.core.AstroCore;
import com.drd.ad_extendra.common.registry.ModBlocks;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import earth.terrarium.adastra.common.registry.ModItems;
import owmii.powah.block.Blcks;
import owmii.powah.item.Itms;
import sonar.fluxnetworks.register.RegistryItems;

import static com.astro.core.common.data.materials.AstroMaterialFlags.GENERATE_COMPRESSED_SPRING;
import static com.astro.core.common.data.materials.AstroMaterialSet.DULL_MAGNETIC;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.*;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;

@SuppressWarnings("all")
public class AstroMaterials {

    private static final long[] V = GTValues.V;
    private static final int[] VA = GTValues.VA;

    public static Material UNKNOWN;
    public static Material NETHERITE;
    public static Material ANCIENT_DEBRIS;
    public static Material ANDESITE_ALLOY;
    public static Material DESH;
    public static Material CALORITE;
    public static Material OSTRUM;
    public static Material VESNIUM;
    public static Material ETRIUM;
    public static Material JUPERIUM;
    public static Material SATURLYTE;
    public static Material ELECTROLYTE;
    public static Material JOVITE;
    public static Material KRONALIUM;
    public static Material ENERGIZED_STEEL;
    public static Material DIELECTRIC;
    public static Material FLUX;
    public static Material SKY_STONE;
    public static Material FLUIX;
    public static Material FLUIX_PEARL;
    public static Material FUTURA_ALLOY;
    public static Material POLYAMIDE_IMIDE;
    public static Material BLAZING_ETRIUM;
    public static Material NIOTIC_CALORITE;
    public static Material SPIRITED_URANIUM;
    public static Material NITRO_FLUX;
    public static Material RADIANT_ZEPHYRON;
    public static Material THALASSIUM;
    public static Material DEIONIZED_WATER;
    public static Material DIVINYLBENZENE;
    public static Material DIETHYLBENZENE;
    public static Material MAGNETIC_NEUTRONIUM;
    public static Material ABYSSALLOY239;
    public static Material ASTEROID_STONE;
    public static Material MERCURY_STONE;
    public static Material VENUS_STONE;
    public static Material MOON_STONE;
    public static Material MARS_STONE;
    public static Material CERES_STONE;
    public static Material JUPITER_STONE;
    public static Material SATURN_STONE;
    public static Material URANUS_STONE;
    public static Material NEPTUNE_STONE;
    public static Material PLUTO_STONE;
    public static Material LIVINGROCK;
    public static Material LIVINGCLAY;
    public static Material ACORN;
    public static Material PLUTO_AIR;
    public static Material NEPTUNE_AIR;
    public static Material URANUS_AIR;
    public static Material SATURN_AIR;
    public static Material JUPITER_AIR;
    public static Material CERES_AIR;
    public static Material MARS_AIR;
    public static Material MOON_AIR;
    public static Material VENUS_AIR;
    public static Material MERCURY_AIR;
    public static Material LIQUID_PLUTO_AIR;
    public static Material LIQUID_NEPTUNE_AIR;
    public static Material LIQUID_URANUS_AIR;
    public static Material LIQUID_SATURN_AIR;
    public static Material LIQUID_JUPITER_AIR;
    public static Material LIQUID_CERES_AIR;
    public static Material LIQUID_MARS_AIR;
    public static Material LIQUID_MOON_AIR;
    public static Material LIQUID_VENUS_AIR;
    public static Material LIQUID_MERCURY_AIR;

    public static void register() {
        // Misc
        UNKNOWN = new Material.Builder(
                AstroCore.id("unknown"))
                .langValue("Unknown")
                .element(AstroElements.UK)
                .color(0xffffff)
                .buildAndRegister();

        ANCIENT_DEBRIS = new Material.Builder(
                AstroCore.id("ancient_debris"))
                .langValue("Ancient Debris")
                .element(AstroElements.AD)
                .color(0x7B5E57).secondaryColor(0x1F0F0A)
                .flags(NO_ORE_SMELTING, NO_SMELTING)
                .iconSet(ROUGH)
                .ore()
                .buildAndRegister();

        // Create
        ANDESITE_ALLOY = new Material.Builder(
                AstroCore.id("andesite_alloy"))
                .langValue("Andesite Alloy")
                .ingot()
                .components(Iron, 1, UNKNOWN, 1).formula("Fe?")
                .flags(GENERATE_PLATE)
                .color(0xa6a08f).iconSet(ROUGH)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        // Ad Astra/Extendra Materials
        DESH = new Material.Builder(
                AstroCore.id("desh"))
                .langValue("Desh")
                .ingot()
                .ore()
                .liquid(1650)
                .element(AstroElements.DE).formula("De")
                .color(0xD68D4D).secondaryColor(0xba5143).iconSet(DULL)
                .buildAndRegister();

        OSTRUM = new Material.Builder(
                AstroCore.id("ostrum"))
                .langValue("Ostrum")
                .ingot()
                .liquid(1811)
                .ore()
                .element(AstroElements.OT).formula("Ot")
                .color(0xa76b72).iconSet(METALLIC)
                .buildAndRegister();

        CALORITE = new Material.Builder(
                AstroCore.id("calorite"))
                .langValue("Calorite")
                .ingot()
                .element(AstroElements.CT).formula("Ct")
                .color(0xc94d4e).iconSet(METALLIC)
                .buildAndRegister();

        ETRIUM = new Material.Builder(
                AstroCore.id("etrium"))
                .langValue("Etrium")
                .ingot()
                .liquid(1337)
                .flags(GENERATE_FOIL, GENERATE_FINE_WIRE)
                .components(OSTRUM, 3, Electrum, 2).formula("AgAuOt3")
                .cableProperties(128, 2, 2, false)
                .color(0x82dbbb).iconSet(BRIGHT)
                .buildAndRegister();

        VESNIUM = new Material.Builder(
                AstroCore.id("vesnium"))
                .langValue("Vesnium")
                .ingot()
                .liquid(1565)
                .flags(GENERATE_FOIL, GENERATE_FINE_WIRE)
                .components(RedSteel, 4, MolybdenumDisilicide, 3, TungstenCarbide, 1)
                .cableProperties(8192, 4, 0, false)
                .color(0xf77b68).secondaryColor(0xcb4e4f).iconSet(BRIGHT)
                .blastTemp(3000, BlastProperty.GasTier.MID, VA[GTValues.IV])
                .buildAndRegister();

        SATURLYTE = new Material.Builder(
                AstroCore.id("saturlyte"))
                .langValue("Saturlyte")
                .ingot()
                .liquid(2133)
                .flags(GENERATE_PLATE)
                .element(AstroElements.SY).formula("Sy")
                .color(0x9a32e7).iconSet(SHINY)
                .buildAndRegister();

        JUPERIUM = new Material.Builder(
                AstroCore.id("juperium"))
                .langValue("Juperium")
                .ingot()
                .liquid(2221)
                .flags(GENERATE_PLATE)
                .element(AstroElements.JP).formula("Jp")
                .color(0x69bbee).iconSet(BRIGHT)
                .buildAndRegister();

        KRONALIUM = new Material.Builder(
                AstroCore.id("kronalium"))
                .langValue("Kronalium")
                .ore()
                .components(SATURLYTE, 3, CALORITE, 4, Oxygen, 7)
                .formula("Sy3Ct4O7")
                .color(0x6a1233).iconSet(METALLIC)
                .buildAndRegister();

        JOVITE = new Material.Builder(
                AstroCore.id("jovite"))
                .langValue("Jovite")
                .ore()
                .components(JUPERIUM, 1, CALORITE, 1).formula("JpCt")
                .color(0x196b9e).iconSet(DULL)
                .buildAndRegister();

        ELECTROLYTE = new Material.Builder(
                AstroCore.id("electrolyte"))
                .langValue("Electrolyte")
                .ingot()
                // .plasma(11656)
                .liquid(4556)
                .flags(GENERATE_FINE_WIRE, GENERATE_ROUND, GENERATE_LONG_ROD,
                        GENERATE_RING, GENERATE_SPRING, GENERATE_COMPRESSED_SPRING)
                .cableProperties(2097152, 128, 0, true)
                .rotorStats(3200, 720, 3, 100000)
                .color(0xfad64a).secondaryColor(0x752802).iconSet(SHINY)
                .element(AstroElements.E).formula("⚡")
                .buildAndRegister();

        // Powah materials
        ENERGIZED_STEEL = new Material.Builder(
                AstroCore.id("energized_steel"))
                .langValue("Energized Steel")
                .ingot()
                .liquid(1412)
                .color(0xbaa172).iconSet(SHINY)
                .flags(GENERATE_FOIL, GENERATE_GEAR, GENERATE_LONG_ROD,
                        GENERATE_PLATE, GENERATE_SMALL_GEAR, GENERATE_ROD,
                        GENERATE_SMALL_GEAR, GENERATE_ROTOR, GENERATE_RING,
                        GENERATE_SPRING, GENERATE_FINE_WIRE, GENERATE_FRAME,
                        GENERATE_COMPRESSED_SPRING)
                .cableProperties(32, 4, 0, true)
                .rotorStats(150, 130, 3, 12000)
                .components(DESH, 1, RedAlloy, 1, Iron, 1)
                .formula("DeFeCu(Si(FeS2)5(CrAl2O3)Hg3)4")
                .buildAndRegister();

        DIELECTRIC = new Material.Builder(
                AstroCore.id("dielectric"))
                .langValue("Dielectric")
                .color(0x000000)
                .flags(DISABLE_MATERIAL_RECIPES, DISABLE_DECOMPOSITION)
                .dust()
                .components(Carbon, 3, Blaze, 1, Clay, 2)
                .buildAndRegister();

        // Flux Networks
        FLUX = new Material.Builder(
                AstroCore.id("flux"))
                .langValue("Flux")
                .ingot()
                .color(0x222422).iconSet(ROUGH)
                .flags(GENERATE_ROD, GENERATE_FRAME, GENERATE_PLATE)
                .components(DIELECTRIC, 4, Obsidian, 3, Silicon, 3)
                .buildAndRegister();

        // Applied Energistics
        SKY_STONE = new Material.Builder(
                AstroCore.id("sky_stone"))
                .langValue("Sky Stone")
                .dust()
                .element(AstroElements.SS).formula("✨")
                .flags(DISABLE_MATERIAL_RECIPES)
                .color(0xffffff).iconSet(ROUGH)
                .buildAndRegister();

        FLUIX = new Material.Builder(
                AstroCore.id("fluix"))
                .langValue("Fluix")
                .gem()
                .flags(GENERATE_PLATE, GENERATE_LENS, CRYSTALLIZABLE,
                        DISABLE_DECOMPOSITION)
                .components(CertusQuartz, 1, Redstone, 1, Obsidian, 1)
                .formula("(SiO2)(Si(FeS2)5(CrAl2O3)Hg3)(MgFeSi2O4)")
                .color(0xC090F7).secondaryColor(0x2A1E5A).iconSet(CERTUS)
                .buildAndRegister();

        FLUIX_PEARL = new Material.Builder(
                AstroCore.id("fluix_pearl"))
                .langValue("Fluix Pearl")
                .gem()
                .components(FLUIX, 8, EnderEye, 1)
                .formula("((SiO2)(Si(FeS2)5(CrAl2O3)Hg3)(MgFeSi2O4))8((BeK4N5)(CS))")
                .flags(CRYSTALLIZABLE, DISABLE_DECOMPOSITION)
                .color(0x4E3C95).secondaryColor(0x181F3C).iconSet(OPAL)
                .buildAndRegister();

        FUTURA_ALLOY = new Material.Builder(
                AstroCore.id("futura_alloy"))
                .langValue("Futura Steel")
                .ingot()
                .fluid()
                .blastTemp(1700, BlastProperty.GasTier.LOW, 400, 1200)
                .flags(GENERATE_FRAME, GENERATE_DENSE, DISABLE_ALLOY_BLAST,
                        GENERATE_PLATE, GENERATE_ROD, MORTAR_GRINDABLE,
                        DISABLE_ALLOY_PROPERTY)
                .color(0xebb7ea).secondaryColor(0x000000).iconSet(SHINY)
                .components(StainlessSteel, 4, SKY_STONE, 1)
                .formula("(Fe6CrMnNi)4✨")
                .buildAndRegister();

        // GregTech
        POLYAMIDE_IMIDE = new Material.Builder(
                AstroCore.id("polyamide_imide"))
                .langValue("Polyamide-Imide")
                .polymer()
                .liquid(1600)
                .dust()
                .ingot()
                .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_FRAME,
                        GENERATE_FOIL, GENERATE_RING)
                // .toolStats(new ToolProperty(10.0F, 10.0F, 4096, 5,
                // new GTToolType[] {GTToolType.SOFT_MALLET, GTToolType.PLUNGER}))
                .components(Carbon, 22, Hydrogen, 14, Nitrogen, 2, Oxygen, 3)
                .fluidPipeProperties(1400, 1000, true, true, true, true)
                .color(0xd9ac37).secondaryColor(0x54301a).iconSet(DULL)
                .buildAndRegister();

        BLAZING_ETRIUM = new Material.Builder(
                AstroCore.id("blazing_etrium"))
                .langValue("Blazing Etrium")
                .ingot()
                .liquid(1410)
                .color(0x8ee8ed).secondaryColor(0x00b0ba).iconSet(METALLIC)
                .blastTemp(1700, BlastProperty.GasTier.LOW, VA[GTValues.HV], 800)
                .flags(GENERATE_FOIL, GENERATE_GEAR, GENERATE_LONG_ROD, GENERATE_PLATE,
                        GENERATE_SMALL_GEAR, GENERATE_ROD, GENERATE_SMALL_GEAR, GENERATE_ROTOR,
                        DISABLE_ALLOY_PROPERTY, GENERATE_FINE_WIRE, GENERATE_RING, GENERATE_FRAME,
                        GENERATE_SPRING, DISABLE_ALLOY_BLAST, GENERATE_COMPRESSED_SPRING)
                .cableProperties(V[GTValues.MV], 8, 0, true)
                .rotorStats(150, 130, 3, 14000)
                .components(ETRIUM, 2, Blaze, 1)
                .formula("(Ot3(AgAu))CS")
                .buildAndRegister();

        NIOTIC_CALORITE = new Material.Builder(
                AstroCore.id("niotic_calorite"))
                .langValue("Niotic Calorite")
                .ingot()
                .liquid(1780)
                .color(0xe4eb60).secondaryColor(0x9ea334).iconSet(BRIGHT)
                .blastTemp(1700, BlastProperty.GasTier.LOW, VA[GTValues.EV], 1000)
                .flags(GENERATE_FOIL, GENERATE_GEAR, GENERATE_LONG_ROD, GENERATE_SPRING,
                        GENERATE_PLATE, GENERATE_SMALL_GEAR, GENERATE_ROD, GENERATE_COMPRESSED_SPRING,
                        GENERATE_SMALL_GEAR, GENERATE_ROTOR, DISABLE_ALLOY_PROPERTY,
                        GENERATE_FINE_WIRE, GENERATE_RING, GENERATE_FRAME, DISABLE_ALLOY_BLAST)
                .cableProperties(V[GTValues.HV], 16, 0, true)
                .rotorStats(220, 170, 3, 16000)
                .components(CALORITE, 3, EnderPearl, 2)
                .buildAndRegister();

        SPIRITED_URANIUM = new Material.Builder(
                AstroCore.id("spirited_uranium"))
                .ingot()
                .liquid(2178)
                .cableProperties(V[GTValues.EV], 24, 0, true)
                .blastTemp(3500, BlastProperty.GasTier.LOW, 7680, 1200)
                .rotorStats(300, 190, 3, 18000)
                .color(0xcb74cc).secondaryColor(0xffebff).iconSet(RADIOACTIVE)
                .flags(GENERATE_FOIL, GENERATE_GEAR, GENERATE_LONG_ROD, GENERATE_SPRING,
                        GENERATE_PLATE, GENERATE_SMALL_GEAR, GENERATE_ROD, GENERATE_COMPRESSED_SPRING,
                        GENERATE_SMALL_GEAR, GENERATE_ROTOR, DISABLE_ALLOY_PROPERTY,
                        GENERATE_FINE_WIRE, GENERATE_RING, GENERATE_FRAME, DISABLE_ALLOY_BLAST)
                // .components(ENERGIZED_STEEL, , Uranium235)
                .buildAndRegister();

        NITRO_FLUX = new Material.Builder(
                AstroCore.id("nitro_flux"))
                .langValue("Nitro-Flux")
                .ingot()
                .liquid(2856)
                .cableProperties(V[GTValues.IV], 32, 0, true)
                .blastTemp(4400, BlastProperty.GasTier.MID, 30720, 1400)
                .rotorStats(450, 220, 3, 20000)
                .color(0x332f94).secondaryColor(0x110c9c).iconSet(SHINY)
                .flags(GENERATE_FOIL, GENERATE_GEAR, GENERATE_LONG_ROD, GENERATE_SPRING,
                        GENERATE_PLATE, GENERATE_SMALL_GEAR, GENERATE_ROD, GENERATE_COMPRESSED_SPRING,
                        GENERATE_SMALL_GEAR, GENERATE_ROTOR, DISABLE_ALLOY_PROPERTY,
                        GENERATE_FINE_WIRE, GENERATE_RING, GENERATE_FRAME, DISABLE_ALLOY_BLAST)
                .components(FLUX, 5, ManganesePhosphide, 2, Platinum, 2)
                .buildAndRegister();

        RADIANT_ZEPHYRON = new Material.Builder(
                AstroCore.id("radiant_zephyron"))
                .ingot()
                .liquid(3744)
                .cableProperties(V[GTValues.LuV], 48, 0, true)
                .blastTemp(5300, BlastProperty.GasTier.MID, 122880, 1600)
                .rotorStats(700, 260, 3, 24000)
                .color(0xf66999).secondaryColor(0xfa3779).iconSet(BRIGHT)
                .flags(GENERATE_FOIL, GENERATE_GEAR, GENERATE_LONG_ROD, GENERATE_COMPRESSED_SPRING,
                        GENERATE_PLATE, GENERATE_SMALL_GEAR, GENERATE_ROD, GENERATE_SPRING,
                        GENERATE_SMALL_GEAR, GENERATE_ROTOR, DISABLE_ALLOY_PROPERTY,
                        GENERATE_FINE_WIRE, GENERATE_RING, GENERATE_FRAME, DISABLE_ALLOY_BLAST)
                // .components(NIOTIC_CALORITE, )
                .buildAndRegister();

        THALASSIUM = new Material.Builder(
                AstroCore.id("neptunium_molybdenum_selenide"))
                .langValue("Thalassium")
                .ingot()
                .liquid(3920)
                .cableProperties(V[GTValues.UV], 96, 0, true)
                .blastTemp(10000, BlastProperty.GasTier.HIGHER, 1966080, 2000)
                .rotorStats(2000, 550, 3, 48000)
                .color(0x088a5c).secondaryColor(0x65f4fc).iconSet(RADIOACTIVE)
                .flags(GENERATE_FOIL, GENERATE_GEAR, GENERATE_LONG_ROD, GENERATE_SPRING,
                        GENERATE_PLATE, GENERATE_SMALL_GEAR, GENERATE_ROD, GENERATE_COMPRESSED_SPRING,
                        GENERATE_SMALL_GEAR, GENERATE_ROTOR, DISABLE_ALLOY_PROPERTY,
                        GENERATE_FINE_WIRE, GENERATE_RING, GENERATE_FRAME, DISABLE_ALLOY_BLAST)
                .components(Neptunium, 1, Molybdenum, 3, Selenium, 4)
                .buildAndRegister();

        DEIONIZED_WATER = new Material.Builder(
                AstroCore.id("deionized_water"))
                .flags(DISABLE_DECOMPOSITION)
                .liquid(new FluidBuilder().customStill().temperature(273))
                .components(Water, 1)
                .buildAndRegister();

        DIETHYLBENZENE = new Material.Builder(
                AstroCore.id("diethylbenzene"))
                .liquid(450)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xf0ee92)
                .components(Carbon, 10, Hydrogen, 14)
                .buildAndRegister();

        DIVINYLBENZENE = new Material.Builder(
                AstroCore.id("divinylbenzene"))
                .dust()
                .flags(DISABLE_DECOMPOSITION)
                .color(0xebe99d).secondaryColor(0x96943f)
                .components(Carbon, 10, Hydrogen, 10)
                .buildAndRegister();

        MAGNETIC_NEUTRONIUM = new Material.Builder(
                AstroCore.id("magnetic_neutronium"))
                .ingot()
                .color(0xFFFFFF).secondaryColor(0x000000)
                .flags(GENERATE_ROD, IS_MAGNETIC, GENERATE_LONG_ROD, GENERATE_PLATE)
                .components(Neutronium, 1)
                .ingotSmeltInto(Neutronium)
                .arcSmeltInto(Neutronium)
                .macerateInto(Neutronium)
                .iconSet(DULL_MAGNETIC)
                .buildAndRegister();
        Neutronium.getProperty(PropertyKey.INGOT).setMagneticMaterial(MAGNETIC_NEUTRONIUM);

        ABYSSALLOY239 = new Material.Builder(
                AstroCore.id("abyssalloy_239"))
                .langValue("Abyssalloy-239")
                .ingot()
                .liquid(5760)
                .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_GEAR, GENERATE_ROTOR,
                        GENERATE_SMALL_GEAR, GENERATE_FOIL, GENERATE_BOLT_SCREW)
                .components(Netherite, 6, Plutonium239, 5, NaquadahAlloy, 8)
                .blastTemp(10800, BlastProperty.GasTier.HIGHEST, 1966080, 2400)
                .color(0x61524d).secondaryColor(0xfc6f03)
                .iconSet(RADIOACTIVE)
                .buildAndRegister();

        NETHERITE = new Material.Builder(
                AstroCore.id("netherite"))
                .langValue("Netherite")
                .element(AstroElements.NH)
                .color(0x4b4042).secondaryColor(0x474447)
                .buildAndRegister();

        Netherite.setComponents(new MaterialStack(AstroMaterials.NETHERITE, 1));

        Glowstone.setComponents(new MaterialStack(Gold, 1), new MaterialStack(Redstone, 1));

        // Planet Gases
        PLUTO_AIR = new Material.Builder(
                AstroCore.id("pluto_air"))
                .langValue("Plutonian Air")
                .gas(new FluidBuilder().customStill())
                .flags(DISABLE_DECOMPOSITION)
                // .components()
                .buildAndRegister();

        LIQUID_PLUTO_AIR = new Material.Builder(
                AstroCore.id("liquid_pluto_air"))
                .langValue("Liquid Plutonian Air")
                .liquid(new FluidBuilder().customStill().temperature(36))
                .flags(DISABLE_DECOMPOSITION)
                // .components()
                .buildAndRegister();

        NEPTUNE_AIR = new Material.Builder(
                AstroCore.id("neptune_air"))
                .langValue("Neptunian Air")
                .gas(new FluidBuilder().customStill())
                .flags(DISABLE_DECOMPOSITION)
                // .components()
                .buildAndRegister();

        LIQUID_NEPTUNE_AIR = new Material.Builder(
                AstroCore.id("liquid_neptune_air"))
                .langValue("Liquid Neptunian Air")
                .liquid(new FluidBuilder().customStill().temperature(36))
                .flags(DISABLE_DECOMPOSITION)
                // .components()
                .buildAndRegister();

        URANUS_AIR = new Material.Builder(
                AstroCore.id("uranus_air"))
                .langValue("Uranian Air")
                .gas(new FluidBuilder().customStill())
                .flags(DISABLE_DECOMPOSITION)
                // .components()
                .buildAndRegister();

        LIQUID_URANUS_AIR = new Material.Builder(
                AstroCore.id("liquid_uranus_air"))
                .langValue("Liquid Uranian Air")
                .liquid(new FluidBuilder().customStill().temperature(36))
                .flags(DISABLE_DECOMPOSITION)
                // .components()
                .buildAndRegister();

        SATURN_AIR = new Material.Builder(
                AstroCore.id("saturn_air"))
                .langValue("Kronian Air")
                .gas(new FluidBuilder().customStill())
                .flags(DISABLE_DECOMPOSITION)
                // .components()
                .buildAndRegister();

        LIQUID_SATURN_AIR = new Material.Builder(
                AstroCore.id("liquid_saturn_air"))
                .langValue("Liquid Kronian Air")
                .liquid(new FluidBuilder().customStill().temperature(36))
                .flags(DISABLE_DECOMPOSITION)
                // .components()
                .buildAndRegister();

        JUPITER_AIR = new Material.Builder(
                AstroCore.id("jupiter_air"))
                .langValue("Jovian Air")
                .gas(new FluidBuilder().customStill())
                .flags(DISABLE_DECOMPOSITION)
                // .components()
                .buildAndRegister();

        LIQUID_JUPITER_AIR = new Material.Builder(
                AstroCore.id("liquid_jupiter_air"))
                .langValue("Liquid Jovian Air")
                .liquid(new FluidBuilder().customStill().temperature(36))
                .flags(DISABLE_DECOMPOSITION)
                // .components()
                .buildAndRegister();

        CERES_AIR = new Material.Builder(
                AstroCore.id("ceres_air"))
                .langValue("Cererian Air")
                .gas(new FluidBuilder().customStill())
                .flags(DISABLE_DECOMPOSITION)
                // .components()
                .buildAndRegister();

        LIQUID_CERES_AIR = new Material.Builder(
                AstroCore.id("liquid_ceres_air"))
                .langValue("Liquid Cererian Air")
                .liquid(new FluidBuilder().customStill().temperature(36))
                .flags(DISABLE_DECOMPOSITION)
                // .components()
                .buildAndRegister();

        MARS_AIR = new Material.Builder(
                AstroCore.id("mars_air"))
                .langValue("Martian Air")
                .gas(new FluidBuilder().customStill())
                .flags(DISABLE_DECOMPOSITION)
                // .components()
                .buildAndRegister();

        LIQUID_MARS_AIR = new Material.Builder(
                AstroCore.id("liquid_mars_air"))
                .langValue("Liquid Martian Air")
                .liquid(new FluidBuilder().customStill().temperature(36))
                .flags(DISABLE_DECOMPOSITION)
                // .components()
                .buildAndRegister();

        MOON_AIR = new Material.Builder(
                AstroCore.id("moon_air"))
                .langValue("Lunar Air")
                .gas(new FluidBuilder().customStill())
                .flags(DISABLE_DECOMPOSITION)
                // .components()
                .buildAndRegister();

        LIQUID_MOON_AIR = new Material.Builder(
                AstroCore.id("liquid_moon_air"))
                .langValue("Liquid Lunar Air")
                .liquid(new FluidBuilder().customStill().temperature(36))
                .flags(DISABLE_DECOMPOSITION)
                // .components()
                .buildAndRegister();

        VENUS_AIR = new Material.Builder(
                AstroCore.id("venus_air"))
                .langValue("Venusian Air")
                .gas(new FluidBuilder().customStill())
                .flags(DISABLE_DECOMPOSITION)
                // .components()
                .buildAndRegister();

        LIQUID_VENUS_AIR = new Material.Builder(
                AstroCore.id("liquid_venus_air"))
                .langValue("Venusian Air")
                .liquid(new FluidBuilder().customStill().temperature(36))
                .flags(DISABLE_DECOMPOSITION)
                // .components()
                .buildAndRegister();

        MERCURY_AIR = new Material.Builder(
                AstroCore.id("mercury_air"))
                .langValue("Mercurian Air")
                .gas(new FluidBuilder().customStill())
                .flags(DISABLE_DECOMPOSITION)
                // .components()
                .buildAndRegister();

        LIQUID_MERCURY_AIR = new Material.Builder(
                AstroCore.id("liquid_mercury_air"))
                .langValue("Liquid Mercurian Air")
                .liquid(new FluidBuilder().customStill().temperature(36))
                .flags(DISABLE_DECOMPOSITION)
                // .components()
                .buildAndRegister();

        createComponentDusts();
    }

    public static void init() {
        // vanilla
        block.setIgnored(Netherite, Blocks.NETHERITE_BLOCK);

        rawOreBlock.setIgnored(ANCIENT_DEBRIS, Blocks.ANCIENT_DEBRIS);
        rawOre.setIgnored(ANCIENT_DEBRIS, Items.NETHERITE_SCRAP);

        // create
        ingot.setIgnored(ANDESITE_ALLOY, () -> AllItems.ANDESITE_ALLOY.get());
        block.setIgnored(ANDESITE_ALLOY, () -> AllBlocks.ANDESITE_ALLOY_BLOCK.get());

        // ad astra/extendra
        rock.setIgnored(PLUTO_STONE, ModBlocks.PLUTO_STONE);
        rock.setIgnored(JUPITER_STONE, ModBlocks.JUPITER_STONE);
        rock.setIgnored(CERES_STONE, ModBlocks.CERES_STONE);
        rock.setIgnored(SATURN_STONE, ModBlocks.SATURN_STONE);
        rock.setIgnored(NEPTUNE_STONE, ModBlocks.NEPTUNE_STONE);
        rock.setIgnored(URANUS_STONE, ModBlocks.URANUS_STONE);

        rawOre.setIgnored(DESH, ModItems.RAW_DESH);
        rawOreBlock.setIgnored(DESH, ModItems.RAW_DESH_BLOCK);
        block.setIgnored(DESH, ModItems.DESH_BLOCK);
        ingot.setIgnored(DESH, ModItems.DESH_INGOT);
        nugget.setIgnored(DESH, ModItems.DESH_NUGGET);

        rawOre.setIgnored(OSTRUM, ModItems.RAW_OSTRUM);
        rawOreBlock.setIgnored(OSTRUM, ModItems.RAW_OSTRUM_BLOCK);
        block.setIgnored(OSTRUM, ModItems.OSTRUM_BLOCK);
        ingot.setIgnored(OSTRUM, ModItems.OSTRUM_INGOT);
        nugget.setIgnored(OSTRUM, ModItems.OSTRUM_NUGGET);

        rawOre.setIgnored(CALORITE, ModItems.RAW_CALORITE);
        rawOreBlock.setIgnored(CALORITE, ModItems.RAW_CALORITE_BLOCK);
        block.setIgnored(CALORITE, ModItems.CALORITE_BLOCK);
        ingot.setIgnored(CALORITE, ModItems.CALORITE_INGOT);
        nugget.setIgnored(CALORITE, ModItems.CALORITE_NUGGET);

        block.setIgnored(ETRIUM, ModItems.ETRIUM_BLOCK);
        ingot.setIgnored(ETRIUM, ModItems.ETRIUM_INGOT);
        nugget.setIgnored(ETRIUM, ModItems.ETRIUM_NUGGET);

        block.setIgnored(JUPERIUM, ModBlocks.JUPERIUM_BLOCK);
        nugget.setIgnored(JUPERIUM, com.drd.ad_extendra.common.registry.ModItems.JUPERIUM_NUGGET);

        block.setIgnored(SATURLYTE, ModBlocks.SATURLYTE_BLOCK);
        nugget.setIgnored(SATURLYTE, com.drd.ad_extendra.common.registry.ModItems.SATURLYTE_NUGGET);

        // powah
        ingot.setIgnored(ENERGIZED_STEEL, Itms.ENERGIZED_STEEL);
        block.setIgnored(ENERGIZED_STEEL, Blcks.ENERGIZED_STEEL);

        dust.setIgnored(DIELECTRIC, () -> Itms.DIELECTRIC_PASTE.get());

        // flux
        dust.setIgnored(FLUX, () -> RegistryItems.FLUX_DUST.get());

        // ae2
        dust.setIgnored(SKY_STONE, AEItems.SKY_DUST);

        block.setIgnored(FLUIX, AEBlocks.FLUIX_BLOCK);
        dust.setIgnored(FLUIX, AEItems.FLUIX_DUST);
        gem.setIgnored(FLUIX, AEItems.FLUIX_CRYSTAL);

        gem.setIgnored(FLUIX_PEARL, AEItems.FLUIX_PEARL);

        gem.setIgnored(CertusQuartz, AEItems.CERTUS_QUARTZ_CRYSTAL);
        block.setIgnored(CertusQuartz, AEBlocks.QUARTZ_BLOCK);
    }

    // ID, Color 1, Color 2, Icon Set, Material Flags
    private static final Object[][] COMPONENT_DUSTS = {
            { "asteroid_stone", 0x964491, 0x70276b, ROUGH,
                    new MaterialFlag[] { DISABLE_DECOMPOSITION } },
            { "mercury_stone", 0x844751, 0x4a263b, ROUGH,
                    new MaterialFlag[] { DISABLE_DECOMPOSITION } },
            { "venus_stone", 0xdfb271, 0xb3763e, ROUGH,
                    new MaterialFlag[] { DISABLE_DECOMPOSITION } },
            { "moon_stone", 0x506869, 0x3f474c, ROUGH,
                    new MaterialFlag[] { DISABLE_DECOMPOSITION } },
            { "mars_stone", 0xcd9360, 0xb76f53, ROUGH,
                    new MaterialFlag[] { DISABLE_DECOMPOSITION } },
            { "ceres_stone", 0x7b7b7b, 0x4d4d4d, ROUGH,
                    new MaterialFlag[] { DISABLE_DECOMPOSITION } },
            { "jupiter_stone", 0xd5af97, 0x9f7961, ROUGH,
                    new MaterialFlag[] { DISABLE_DECOMPOSITION } },
            { "saturn_stone", 0xfeeeb8, 0xd9b975, ROUGH,
                    new MaterialFlag[] { DISABLE_DECOMPOSITION } },
            { "uranus_stone", 0x79adda, 0xaff4ff, ROUGH,
                    new MaterialFlag[] { DISABLE_DECOMPOSITION } },
            { "neptune_stone", 0x8092d2, 0x343ea5, ROUGH,
                    new MaterialFlag[] { DISABLE_DECOMPOSITION } },
            { "pluto_stone", 0xf0b467, 0xffd682, ROUGH,
                    new MaterialFlag[] { DISABLE_DECOMPOSITION } },
            { "livingrock", 0xc9c2b1, 0x948e7f, ROUGH,
                    new MaterialFlag[] { DISABLE_DECOMPOSITION } },
            { "livingclay", 0xc9c2e7, 0x4eaeb5, FINE,
                    new MaterialFlag[] { DISABLE_DECOMPOSITION } },
            { "acorn", 0xe0b677, 0x734d15, FINE,
                    new MaterialFlag[] { DISABLE_DECOMPOSITION } },
    };

    private static void createComponentDusts() {
        for (Object[] dust : COMPONENT_DUSTS) {
            String name = (String) dust[0];
            int color = (int) dust[1];
            int color2 = (int) dust[2];
            MaterialIconSet icon = (MaterialIconSet) dust[3];
            MaterialFlag[] flags = (MaterialFlag[]) dust[4];

            Material material = new Material.Builder(AstroCore.id(name))
                    .dust()
                    .color(color)
                    .secondaryColor(color2)
                    .iconSet(icon)
                    .flags(flags)
                    .buildAndRegister();

            switch (name) {
                case "asteroid_stone" -> ASTEROID_STONE = material;
                case "mercury_stone" -> MERCURY_STONE = material;
                case "venus_stone" -> VENUS_STONE = material;
                case "moon_stone" -> MOON_STONE = material;
                case "mars_stone" -> MARS_STONE = material;
                case "ceres_stone" -> CERES_STONE = material;
                case "jupiter_stone" -> JUPITER_STONE = material;
                case "saturn_stone" -> SATURN_STONE = material;
                case "uranus_stone" -> URANUS_STONE = material;
                case "neptune_stone" -> NEPTUNE_STONE = material;
                case "pluto_stone" -> PLUTO_STONE = material;
                case "livingrock" -> LIVINGROCK = material;
                case "livingclay" -> LIVINGCLAY = material;
                case "acorn" -> ACORN = material;
            }
        }
    }
}
