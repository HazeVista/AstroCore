package com.astro.core.common.data.materials;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlag;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.BlastProperty;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import com.astro.core.AstroCore;
import com.astro.core.common.GTVoltage;
import com.drd.ad_extendra.common.registry.ModBlocks;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import earth.terrarium.adastra.common.registry.ModItems;
import org.zeith.botanicadds.init.BlocksBA;
import org.zeith.botanicadds.init.ItemsBA;
import owmii.powah.block.Blcks;
import owmii.powah.item.Itms;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;

import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.*;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.GENERATE_FINE_WIRE;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.GENERATE_FOIL;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.GENERATE_FRAME;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.GENERATE_GEAR;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.GENERATE_LONG_ROD;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.GENERATE_RING;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.GENERATE_ROTOR;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.GENERATE_SMALL_GEAR;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;

@SuppressWarnings("all")
public class AstroMaterials {

    private static final long[] V = GTValues.V;
    private static final int[] VA = GTValues.VA;

    public static Material UNKNOWN;
    public static Material ANDESITE_ALLOY;
    public static Material BLAZING_ETRIUM;
    public static Material NIOTIC_CALORITE;
    public static Material POLYAMIDE_IMIDE;
    public static Material DESH;
    public static Material CALORITE;
    public static Material OSTRUM;
    public static Material ETRIUM;
    public static Material JUPERIUM;
    public static Material SATURLYTE;
    public static Material ELECTROLYTE;
    public static Material JOVITE;
    public static Material KRONALIUM;
    public static Material ENERGIZED_STEEL;
    public static Material DIELECTRIC;
    public static Material SKY_STONE;
    public static Material FLUIX;
    public static Material FLUIX_PEARL;
    public static Material FUTURA_ALLOY;
    public static Material MANA;
    public static Material MANASTEEL;
    public static Material MANA_DIAMOND;
    public static Material DRAGONSTONE;
    public static Material TERRASTEEL;
    public static Material DORMANT_TERRASTEEL;
    public static Material ELEMENTIUM;
    public static Material GAIASTEEL;
    public static Material AETHER;

    public static void register() {
        // Unknown
        UNKNOWN = new Material.Builder(
                AstroCore.id("unknown"))
                .langValue("Unknown")
                .element(AstroElements.UK)
                .color(0xffffff)
                .buildAndRegister();

        // Create
        ANDESITE_ALLOY = new Material.Builder(
                AstroCore.id("andesite_alloy"))
                .langValue("Andesite Alloy")
                .ingot()
                .components(GTMaterials.Iron, 1, AstroMaterials.UNKNOWN, 1).formula("Fe?")
                .color(0xa6a08f).iconSet(MaterialIconSet.ROUGH)
                .flags(MaterialFlags.DISABLE_DECOMPOSITION)
                .buildAndRegister();

        // Ad Astra/Extendra Materials
        DESH = new Material.Builder(
                AstroCore.id("desh"))
                .langValue("Desh")
                .ingot()
                .ore()
                .liquid()
                .element(AstroElements.DE).formula("De")
                .color(0xD68D4D).secondaryColor(0xba5143).iconSet(MaterialIconSet.DULL)
                .buildAndRegister();

        OSTRUM = new Material.Builder(
                AstroCore.id("ostrum"))
                .langValue("Ostrum")
                .ingot()
                .liquid()
                .ore()
                .element(AstroElements.OT).formula("Ot")
                .color(0xa76b72).iconSet(MaterialIconSet.METALLIC)
                .buildAndRegister();

        CALORITE = new Material.Builder(
                AstroCore.id("calorite"))
                .langValue("Calorite")
                .ingot()
                .element(AstroElements.CT).formula("Ct")
                .color(0xc94d4e).iconSet(MaterialIconSet.METALLIC)
                .buildAndRegister();

        ETRIUM = new Material.Builder(
                AstroCore.id("etrium"))
                .langValue("Etrium")
                .ingot()
                .liquid()
                .flags(MaterialFlags.GENERATE_FOIL, MaterialFlags.GENERATE_FINE_WIRE)
                .components(AstroMaterials.OSTRUM, 3, GTMaterials.Electrum, 2).formula("AgAuOt3")
                .color(0x82dbbb).iconSet(MaterialIconSet.BRIGHT)
                .buildAndRegister();

        SATURLYTE = new Material.Builder(
                AstroCore.id("saturlyte"))
                .langValue("Saturlyte")
                .ingot()
                .liquid()
                .flags(MaterialFlags.GENERATE_PLATE)
                .element(AstroElements.SY).formula("Sy")
                .color(0x9a32e7).iconSet(MaterialIconSet.SHINY)
                .buildAndRegister();

        JUPERIUM = new Material.Builder(
                AstroCore.id("juperium"))
                .langValue("Juperium")
                .ingot()
                .liquid()
                .flags(MaterialFlags.GENERATE_PLATE)
                .element(AstroElements.JP).formula("Jp")
                .color(0x69bbee).iconSet(MaterialIconSet.BRIGHT)
                .buildAndRegister();

        KRONALIUM = new Material.Builder(
                AstroCore.id("kronalium"))
                .langValue("Kronalium")
                .ore()
                .components(AstroMaterials.SATURLYTE, 3, AstroMaterials.CALORITE, 4, GTMaterials.Oxygen, 7)
                .formula("Sy3Ct4O7")
                .color(0x6a1233).iconSet(MaterialIconSet.METALLIC)
                .buildAndRegister();

        JOVITE = new Material.Builder(
                AstroCore.id("jovite"))
                .langValue("Jovite")
                .ore()
                .components(AstroMaterials.JUPERIUM, 1, AstroMaterials.CALORITE, 1).formula("JpCt")
                .color(0x196b9e).iconSet(MaterialIconSet.DULL)
                .buildAndRegister();

        ELECTROLYTE = new Material.Builder(
                AstroCore.id("electrolyte"))
                .langValue("Electrolyte")
                .ingot()
                .plasma()
                .liquid()
                .color(0x6fd422).iconSet(MaterialIconSet.RADIOACTIVE)
                .element(AstroElements.E).formula("⚡")
                .buildAndRegister();

        // Powah materials
        ENERGIZED_STEEL = new Material.Builder(
                AstroCore.id("energized_steel"))
                .langValue("Energized Steel")
                .ingot()
                .liquid()
                .color(0xbaa172).iconSet(MaterialIconSet.SHINY)
                .flags(MaterialFlags.GENERATE_FOIL, MaterialFlags.GENERATE_GEAR, MaterialFlags.GENERATE_LONG_ROD,
                        MaterialFlags.GENERATE_PLATE, MaterialFlags.GENERATE_SMALL_GEAR, MaterialFlags.GENERATE_ROD,
                        MaterialFlags.GENERATE_SMALL_GEAR, MaterialFlags.GENERATE_ROTOR,
                        MaterialFlags.GENERATE_FINE_WIRE, MaterialFlags.GENERATE_RING, MaterialFlags.GENERATE_FRAME)
                .cableProperties(32, 4, 0, true)
                .rotorStats(150, 130, 3, 12000)
                .components(AstroMaterials.DESH, 1, GTMaterials.RedAlloy, 1, GTMaterials.Iron, 1)
                .formula("DeFeCu(Si(FeS2)5(CrAl2O3)Hg3)4")
                .buildAndRegister();

        DIELECTRIC = new Material.Builder(
                AstroCore.id("dielectric"))
                .langValue("Dielectric")
                .color(0x000000)
                .flags(MaterialFlags.DISABLE_MATERIAL_RECIPES, MaterialFlags.DISABLE_DECOMPOSITION)
                .dust()
                .components(GTMaterials.Carbon, 3, GTMaterials.Blaze, 1, GTMaterials.Clay, 2)
                .buildAndRegister();

        // Applied Energistics
        SKY_STONE = new Material.Builder(
                AstroCore.id("sky_stone"))
                .langValue("Sky Stone")
                .dust()
                .element(AstroElements.SS).formula("✨")
                .color(0xffffff).iconSet(MaterialIconSet.ROUGH)
                .buildAndRegister();

        FLUIX = new Material.Builder(
                AstroCore.id("fluix"))
                .langValue("Fluix")
                .gem()
                .flags(MaterialFlags.GENERATE_PLATE, MaterialFlags.GENERATE_LENS, MaterialFlags.CRYSTALLIZABLE,
                        MaterialFlags.DISABLE_DECOMPOSITION)
                .components(GTMaterials.CertusQuartz, 1, GTMaterials.Redstone, 1, GTMaterials.Obsidian, 1)
                .formula("(SiO2)(Si(FeS2)5(CrAl2O3)Hg3)(MgFeSi2O4)")
                .color(0xC090F7).secondaryColor(0x2A1E5A).iconSet(MaterialIconSet.CERTUS)
                .buildAndRegister();

        FLUIX_PEARL = new Material.Builder(
                AstroCore.id("fluix_pearl"))
                .langValue("Fluix Pearl")
                .gem()
                .components(AstroMaterials.FLUIX, 8, GTMaterials.EnderEye, 1)
                .formula("((SiO2)(Si(FeS2)5(CrAl2O3)Hg3)(MgFeSi2O4))8((BeK4N5)(CS))")
                .flags(MaterialFlags.CRYSTALLIZABLE, MaterialFlags.DISABLE_DECOMPOSITION)
                .color(0x4E3C95).secondaryColor(0x181F3C).iconSet(MaterialIconSet.OPAL)
                .buildAndRegister();

        FUTURA_ALLOY = new Material.Builder(
                AstroCore.id("futura_alloy"))
                .langValue("Futura Steel")
                .ingot()
                .fluid()
                .blastTemp(1700, BlastProperty.GasTier.LOW, 400, 1200)
                .flags(MaterialFlags.GENERATE_FRAME, MaterialFlags.GENERATE_DENSE, MaterialFlags.DISABLE_ALLOY_BLAST,
                        MaterialFlags.GENERATE_PLATE, MaterialFlags.GENERATE_ROD, MaterialFlags.MORTAR_GRINDABLE,
                        MaterialFlags.DISABLE_ALLOY_PROPERTY)
                .color(0xebb7ea).secondaryColor(0x000000).iconSet(MaterialIconSet.SHINY)
                .components(GTMaterials.StainlessSteel, 4, AstroMaterials.SKY_STONE, 1)
                .formula("(Fe6CrMnNi)4✨")
                .buildAndRegister();

        // Botania
        MANA = new Material.Builder(
                AstroCore.id("mana"))
                .langValue("Mana")
                .dust()
                .flags(MaterialFlags.DISABLE_MATERIAL_RECIPES)
                .element(AstroElements.MN).formula("✨")
                .color(0x00fbff)
                .buildAndRegister();

        AETHER = new Material.Builder(
                AstroCore.id("aether"))
                .langValue("§3Æther§r")
                .gas()
                .element(AstroElements.AE).formula("✨")
                .color(0x26a33f)
                .buildAndRegister();

        MANA_DIAMOND = new Material.Builder(
                AstroCore.id("mana_diamond"))
                .langValue("Mana Diamond")
                .gem()
                .flags(MaterialFlags.GENERATE_LENS, MaterialFlags.GENERATE_PLATE, MaterialFlags.CRYSTALLIZABLE,
                        MaterialFlags.DISABLE_DECOMPOSITION)
                .components(GTMaterials.Carbon, 1, AstroMaterials.MANA, 1).formula("C✨")
                .color(0x47eaed).iconSet(MaterialIconSet.DIAMOND)
                .buildAndRegister();

        DRAGONSTONE = new Material.Builder(
                AstroCore.id("dragonstone"))
                .langValue("Dragonstone")
                .gem()
                .flags(MaterialFlags.GENERATE_LENS, MaterialFlags.GENERATE_PLATE, MaterialFlags.CRYSTALLIZABLE,
                        MaterialFlags.DISABLE_DECOMPOSITION)
                .components(GTMaterials.Carbon, 1, AstroMaterials.AETHER, 1).formula("C✨")
                .color(0xed64d4).iconSet(MaterialIconSet.DIAMOND)
                .buildAndRegister();

        MANASTEEL = new Material.Builder(
                AstroCore.id("manasteel"))
                .langValue("§9Manasteel")
                .ingot()
                .fluid()
                .blastTemp(1000, BlastProperty.GasTier.LOW, 120, 400)
                .flags(MaterialFlags.GENERATE_FRAME, MaterialFlags.GENERATE_GEAR, MaterialFlags.DISABLE_DECOMPOSITION,
                        MaterialFlags.GENERATE_PLATE, MaterialFlags.GENERATE_ROD, MaterialFlags.MORTAR_GRINDABLE,
                        MaterialFlags.DISABLE_ALLOY_BLAST, MaterialFlags.DISABLE_ALLOY_PROPERTY)
                .fluidPipeProperties(1855, 150, true, false, false, false)
                .color(0x228cc9).iconSet(MaterialIconSet.SHINY)
                .components(GTMaterials.Steel, 1, AstroMaterials.MANA, 1).formula("Fe✨")
                .buildAndRegister();

        DORMANT_TERRASTEEL = new Material.Builder(
                AstroCore.id("dormant_terrasteel"))
                .langValue("Dormant §2Terrasteel§r")
                .dust()
                .components(GTMaterials.Steel, 1, GTMaterials.Beryllium, 1, GTMaterials.Aluminium, 1).formula("FeBeAl")
                .color(0x128719)
                .buildAndRegister();

        TERRASTEEL = new Material.Builder(
                AstroCore.id("terrasteel"))
                .langValue("§2Terrasteel")
                .ingot()
                .fluid()
                .blastTemp(1700, BlastProperty.GasTier.LOW, (int) GTVoltage.VA.MV, 800)
                .flags(MaterialFlags.GENERATE_FRAME, MaterialFlags.GENERATE_GEAR, MaterialFlags.DISABLE_DECOMPOSITION,
                        MaterialFlags.GENERATE_PLATE, MaterialFlags.GENERATE_ROD, MaterialFlags.DISABLE_ALLOY_BLAST,
                        MaterialFlags.DISABLE_ALLOY_PROPERTY)
                .fluidPipeProperties(2142, 225, true, false, false, false)
                .color(0x159e1e).iconSet(MaterialIconSet.BRIGHT)
                .components(GTMaterials.Steel, 1, GTMaterials.Beryllium, 1, GTMaterials.Aluminium, 1,
                        AstroMaterials.MANA, 1)
                .formula("FeBeAl✨")
                .buildAndRegister();

        ELEMENTIUM = new Material.Builder(
                AstroCore.id("elementium"))
                .langValue("§dAlfsteel")
                .ingot()
                .fluid()
                .blastTemp(3500, BlastProperty.GasTier.MID, (int) GTVoltage.VA.IV, 1600)
                .flags(MaterialFlags.GENERATE_FRAME, MaterialFlags.GENERATE_GEAR, MaterialFlags.DISABLE_DECOMPOSITION,
                        MaterialFlags.GENERATE_PLATE, MaterialFlags.GENERATE_ROD)
                .fluidPipeProperties(2426, 300, true, false, false, false)
                .color(0xed64d4).iconSet(MaterialIconSet.SHINY)
                .components(GTMaterials.Titanium, 3, GTMaterials.Rhodium, 2, AstroMaterials.DRAGONSTONE, 1)
                .formula("Ti3Rh2C✨")
                .buildAndRegister();

        GAIASTEEL = new Material.Builder(
                AstroCore.id("gaiasteel"))
                .langValue("§cGaiasteel")
                .ingot()
                .fluid()
                .blastTemp(7100, BlastProperty.GasTier.HIGH, (int) GTVoltage.VA.ZPM, 2400)
                .flags(MaterialFlags.GENERATE_FRAME, MaterialFlags.GENERATE_GEAR, MaterialFlags.DISABLE_DECOMPOSITION,
                        MaterialFlags.GENERATE_PLATE, MaterialFlags.GENERATE_ROD, MaterialFlags.DISABLE_ALLOY_BLAST)
                .fluidPipeProperties(3776, 400, true, true, true, true)
                .color(0x8c2929).iconSet(MaterialIconSet.BRIGHT)
                // .components().formula()
                .buildAndRegister();

        // GregTech
        POLYAMIDE_IMIDE = new Material.Builder(
                AstroCore.id("polyamide_imide"))
                .langValue("Polyamide-Imide")
                .polymer()
                .liquid(1600)
                .dust()
                .ingot()
                .flags(MaterialFlags.GENERATE_PLATE, MaterialFlags.GENERATE_ROD, MaterialFlags.GENERATE_FRAME,
                        MaterialFlags.GENERATE_FOIL)
                // .toolStats(new ToolProperty(10.0F, 10.0F, 4096, 5,
                // new GTToolType[] {GTToolType.SOFT_MALLET, GTToolType.PLUNGER}))
                .fluidPipeProperties(1400, 1000, true, true, true, true)
                .color(0xd9ac37).secondaryColor(0x54301a).iconSet(MaterialIconSet.DULL)
                // .components().formula()
                .buildAndRegister();

        BLAZING_ETRIUM = new Material.Builder(
                AstroCore.id("blazing_etrium"))
                .langValue("Blazing Etrium")
                .ingot()
                .liquid()
                .color(0x8ee8ed).secondaryColor(0x00b0ba).iconSet(MaterialIconSet.METALLIC)
                .blastTemp(1700, BlastProperty.GasTier.LOW, VA[GTValues.HV], 800)
                .flags(MaterialFlags.GENERATE_FOIL, MaterialFlags.GENERATE_GEAR, MaterialFlags.GENERATE_LONG_ROD,
                        MaterialFlags.GENERATE_PLATE, MaterialFlags.GENERATE_SMALL_GEAR, MaterialFlags.GENERATE_ROD,
                        MaterialFlags.GENERATE_SMALL_GEAR, MaterialFlags.GENERATE_ROTOR,
                        MaterialFlags.DISABLE_ALLOY_PROPERTY,
                        MaterialFlags.GENERATE_FINE_WIRE, MaterialFlags.GENERATE_RING, MaterialFlags.GENERATE_FRAME,
                        MaterialFlags.DISABLE_ALLOY_BLAST)
                .cableProperties(V[GTValues.MV], 8, 0, true)
                .rotorStats(150, 130, 3, 14000)
                .components(AstroMaterials.ETRIUM, 2, GTMaterials.Blaze, 1)
                .formula("(Ot3(AgAu))CS")
                .buildAndRegister();

        NIOTIC_CALORITE = new Material.Builder(
                AstroCore.id("niotic_calorite"))
                .langValue("Niotic Calorite")
                .ingot()
                .liquid()
                .color(0xe4eb60).secondaryColor(0x9ea334).iconSet(MaterialIconSet.BRIGHT)
                .blastTemp(1700, BlastProperty.GasTier.LOW, VA[GTValues.EV], 1000)
                .flags(MaterialFlags.GENERATE_FOIL, MaterialFlags.GENERATE_GEAR, MaterialFlags.GENERATE_LONG_ROD,
                        MaterialFlags.GENERATE_PLATE, MaterialFlags.GENERATE_SMALL_GEAR, MaterialFlags.GENERATE_ROD,
                        MaterialFlags.GENERATE_SMALL_GEAR, MaterialFlags.GENERATE_ROTOR,
                        MaterialFlags.DISABLE_ALLOY_PROPERTY,
                        MaterialFlags.GENERATE_FINE_WIRE, MaterialFlags.GENERATE_RING, MaterialFlags.GENERATE_FRAME,
                        MaterialFlags.DISABLE_ALLOY_BLAST)
                .cableProperties(V[GTValues.HV], 16, 0, true)
                .rotorStats(220, 170, 3, 16000)
                // .components()
                // .formula("")
                .buildAndRegister();

        createComponentDusts();
        createSuperConductors();
    }

    public static void init() {
        // create
        ingot.setIgnored(AstroMaterials.ANDESITE_ALLOY, () -> AllItems.ANDESITE_ALLOY.get());
        block.setIgnored(AstroMaterials.ANDESITE_ALLOY, () -> AllBlocks.ANDESITE_ALLOY_BLOCK.get());

        // ad astra/extendra
        rawOre.setIgnored(AstroMaterials.DESH, ModItems.RAW_DESH);
        rawOreBlock.setIgnored(AstroMaterials.DESH, ModItems.RAW_DESH_BLOCK);
        block.setIgnored(AstroMaterials.DESH, ModItems.DESH_BLOCK);
        ingot.setIgnored(AstroMaterials.DESH, ModItems.DESH_INGOT);
        nugget.setIgnored(AstroMaterials.DESH, ModItems.DESH_NUGGET);

        rawOre.setIgnored(AstroMaterials.OSTRUM, ModItems.RAW_OSTRUM);
        rawOreBlock.setIgnored(AstroMaterials.OSTRUM, ModItems.RAW_OSTRUM_BLOCK);
        block.setIgnored(AstroMaterials.OSTRUM, ModItems.OSTRUM_BLOCK);
        ingot.setIgnored(AstroMaterials.OSTRUM, ModItems.OSTRUM_INGOT);
        nugget.setIgnored(AstroMaterials.OSTRUM, ModItems.OSTRUM_NUGGET);

        rawOre.setIgnored(AstroMaterials.CALORITE, ModItems.RAW_CALORITE);
        rawOreBlock.setIgnored(AstroMaterials.CALORITE, ModItems.RAW_CALORITE_BLOCK);
        block.setIgnored(AstroMaterials.CALORITE, ModItems.CALORITE_BLOCK);
        ingot.setIgnored(AstroMaterials.CALORITE, ModItems.CALORITE_INGOT);
        nugget.setIgnored(AstroMaterials.CALORITE, ModItems.CALORITE_NUGGET);

        block.setIgnored(AstroMaterials.ETRIUM, ModItems.ETRIUM_BLOCK);
        ingot.setIgnored(AstroMaterials.ETRIUM, ModItems.ETRIUM_INGOT);
        nugget.setIgnored(AstroMaterials.ETRIUM, ModItems.ETRIUM_NUGGET);

        block.setIgnored(AstroMaterials.JUPERIUM, ModBlocks.JUPERIUM_BLOCK);
        nugget.setIgnored(AstroMaterials.JUPERIUM, com.drd.ad_extendra.common.registry.ModItems.JUPERIUM_NUGGET);

        block.setIgnored(AstroMaterials.SATURLYTE, ModBlocks.SATURLYTE_BLOCK);
        nugget.setIgnored(AstroMaterials.SATURLYTE, com.drd.ad_extendra.common.registry.ModItems.SATURLYTE_NUGGET);

        // powah
        ingot.setIgnored(AstroMaterials.ENERGIZED_STEEL, Itms.ENERGIZED_STEEL);
        block.setIgnored(AstroMaterials.ENERGIZED_STEEL, Blcks.ENERGIZED_STEEL);

        dust.setIgnored(AstroMaterials.DIELECTRIC, () -> Itms.DIELECTRIC_PASTE.get());

        // ae2
        dust.setIgnored(AstroMaterials.SKY_STONE, AEItems.SKY_DUST);

        block.setIgnored(AstroMaterials.FLUIX, AEBlocks.FLUIX_BLOCK);
        dust.setIgnored(AstroMaterials.FLUIX, AEItems.FLUIX_DUST);
        gem.setIgnored(AstroMaterials.FLUIX, AEItems.FLUIX_CRYSTAL);

        gem.setIgnored(AstroMaterials.FLUIX_PEARL, AEItems.FLUIX_PEARL);

        // botania/additions
        dust.setIgnored(AstroMaterials.MANA, () -> BotaniaItems.manaPowder);

        gem.setIgnored(AstroMaterials.MANA_DIAMOND, () -> BotaniaItems.manaDiamond);
        block.setIgnored(AstroMaterials.MANA_DIAMOND, () -> BotaniaBlocks.manaDiamondBlock);

        gem.setIgnored(AstroMaterials.DRAGONSTONE, () -> BotaniaItems.dragonstone);
        block.setIgnored(AstroMaterials.DRAGONSTONE, () -> BotaniaBlocks.dragonstoneBlock);

        ingot.setIgnored(AstroMaterials.MANASTEEL, () -> BotaniaItems.manaSteel);
        nugget.setIgnored(AstroMaterials.MANASTEEL, () -> BotaniaItems.manasteelNugget);
        block.setIgnored(AstroMaterials.MANASTEEL, () -> BotaniaBlocks.manasteelBlock);

        ingot.setIgnored(AstroMaterials.TERRASTEEL, () -> BotaniaItems.terrasteel);
        block.setIgnored(AstroMaterials.TERRASTEEL, () -> BotaniaBlocks.terrasteelBlock);
        nugget.setIgnored(AstroMaterials.TERRASTEEL, () -> BotaniaItems.terrasteelNugget);

        ingot.setIgnored(AstroMaterials.ELEMENTIUM, () -> BotaniaItems.elementium);
        block.setIgnored(AstroMaterials.ELEMENTIUM, () -> BotaniaBlocks.elementiumBlock);
        nugget.setIgnored(AstroMaterials.ELEMENTIUM, () -> BotaniaItems.elementium);

        ingot.setIgnored(AstroMaterials.GAIASTEEL, ItemsBA.GAIASTEEL_INGOT);
        nugget.setIgnored(AstroMaterials.GAIASTEEL, ItemsBA.GAIASTEEL_NUGGET);
        block.setIgnored(AstroMaterials.GAIASTEEL, BlocksBA.GAIASTEEL_BLOCK);
    }

    // ID, Color 1, Color 2, Icon Set, Material Flags
    private static final Object[][] COMPONENT_DUSTS = {
            { "asteroid_stone", 0x964491, 0x70276b, MaterialIconSet.ROUGH,
                    new MaterialFlag[] { DISABLE_DECOMPOSITION } },
            { "mercury_stone", 0x844751, 0x4a263b, MaterialIconSet.ROUGH,
                    new MaterialFlag[] { DISABLE_DECOMPOSITION } },
            { "venus_stone", 0xdfb271, 0xb3763e, MaterialIconSet.ROUGH,
                    new MaterialFlag[] { DISABLE_DECOMPOSITION } },
            { "moon_stone", 0x506869, 0x3f474c, MaterialIconSet.ROUGH,
                    new MaterialFlag[] { DISABLE_DECOMPOSITION } },
            { "mars_stone", 0xcd9360, 0xb76f53, MaterialIconSet.ROUGH,
                    new MaterialFlag[] { DISABLE_DECOMPOSITION } },
            { "ceres_stone", 0x7b7b7b, 0x4d4d4d, MaterialIconSet.ROUGH,
                    new MaterialFlag[] { DISABLE_DECOMPOSITION } },
            { "jupiter_stone", 0xd5af97, 0x9f7961, MaterialIconSet.ROUGH,
                    new MaterialFlag[] { DISABLE_DECOMPOSITION } },
            { "saturn_stone", 0xfeeeb8, 0xd9b975, MaterialIconSet.ROUGH,
                    new MaterialFlag[] { DISABLE_DECOMPOSITION } },
            { "uranus_stone", 0x79adda, 0xaff4ff, MaterialIconSet.ROUGH,
                    new MaterialFlag[] { DISABLE_DECOMPOSITION } },
            { "neptune_stone", 0x8092d2, 0x343ea5, MaterialIconSet.ROUGH,
                    new MaterialFlag[] { DISABLE_DECOMPOSITION } },
            { "pluto_stone", 0xf0b467, 0xffd682, MaterialIconSet.ROUGH,
                    new MaterialFlag[] { DISABLE_DECOMPOSITION } },
            { "livingrock", 0xc9c2b1, 0x948e7f, MaterialIconSet.ROUGH,
                    new MaterialFlag[] { DISABLE_DECOMPOSITION } },
            { "livingclay", 0xc9c2e7, 0x4eaeb5, MaterialIconSet.FINE,
                    new MaterialFlag[] { DISABLE_DECOMPOSITION } },
            { "acorn", 0xe0b677, 0x734d15, MaterialIconSet.FINE,
                    new MaterialFlag[] { DISABLE_DECOMPOSITION } }
    };

    // SuperCons: ID, Name, Color 1, Color 2, Icon Set, Blast Properties, Cable Properties, Rotor Properties
    private static final Object[][] SUPERCONDUCTORS = {
            { "spirited_uranium", "Spirited Uranium", 0xcb74cc, 0xffebff, MaterialIconSet.RADIOACTIVE,
                    new Object[] { 3500, BlastProperty.GasTier.LOW, 7680, 1200 },
                    new Object[] { V[GTValues.EV], 24, 0, true },
                    new Object[] { 300, 190, 3, 18000 } },
            { "nitro_flux", "Nitro-Flux", 0x332f94, 0x110c9c, MaterialIconSet.SHINY,
                    new Object[] { 4400, BlastProperty.GasTier.MID, 30720, 1400 },
                    new Object[] { V[GTValues.IV], 32, 0, true },
                    new Object[] { 450, 220, 3, 20000 } },
            { "juperiosaturlytide", "Juperio-Saturlytide", 0xf66999, 0xfa3779, MaterialIconSet.BRIGHT,
                    new Object[] { 5300, BlastProperty.GasTier.MID, 122880, 1600 },
                    new Object[] { V[GTValues.LuV], 48, 0, true },
                    new Object[] { 700, 260, 3, 24000 } },
            { "gaiaforged_naquadah", "Gaia-Forged Naquadah", 0x7a1d29, 0x000000, MaterialIconSet.SHINY,
                    new Object[] { 7100, BlastProperty.GasTier.HIGH, 491520, 1800 },
                    new Object[] { V[GTValues.ZPM], 64, 0, true },
                    new Object[] { 1100, 380, 3, 32000 } },
            { "neptunium_molybdenum_selenide", "Neptunium Molybdenum Selenide", 0x088a5c, 0x65f4fc,
                    MaterialIconSet.RADIOACTIVE,
                    new Object[] { 10000, BlastProperty.GasTier.HIGHER, 1966080, 2000 },
                    new Object[] { V[GTValues.UV], 96, 0, true },
                    new Object[] { 2000, 550, 3, 48000 } }
    };

    private static void createComponentDusts() {
        for (Object[] dust : COMPONENT_DUSTS) {
            String name = (String) dust[0];
            int color = (int) dust[1];
            int color2 = (int) dust[2];
            MaterialIconSet icon = (MaterialIconSet) dust[3];
            MaterialFlag[] flags = (MaterialFlag[]) dust[4];

            // Create materials without components for now
            new Material.Builder(AstroCore.id(name))
                    .dust()
                    .color(color)
                    .secondaryColor(color2)
                    .iconSet(icon)
                    .flags(flags)
                    .buildAndRegister();
        }
    }

    private static void createSuperConductors() {
        MaterialFlag[] superConductorFlags = new MaterialFlag[] {
                GENERATE_PLATE, GENERATE_ROD, GENERATE_FRAME, DISABLE_DECOMPOSITION,
                GENERATE_GEAR, GENERATE_LONG_ROD, GENERATE_FOIL, GENERATE_RING,
                GENERATE_SMALL_GEAR, GENERATE_FINE_WIRE, GENERATE_ROTOR
        };

        for (Object[] sc : SUPERCONDUCTORS) {
            String id = (String) sc[0];
            String name = (String) sc[1];
            int color = (int) sc[2];
            int secColor = (int) sc[3];
            MaterialIconSet icon = (MaterialIconSet) sc[4];
            Object[] blast = (Object[]) sc[5];
            Object[] cable = (Object[]) sc[6];
            Object[] rotor = (Object[]) sc[7];

            Material material = new Material.Builder(AstroCore.id(id))
                    .langValue(name)
                    .ingot()
                    .fluid()
                    .color(color)
                    .secondaryColor(secColor)
                    .iconSet(icon)
                    .blastTemp((int) blast[0], (BlastProperty.GasTier) blast[1], (int) blast[2], (int) blast[3])
                    .cableProperties((long) cable[0], (int) cable[1], (int) cable[2], (boolean) cable[3])
                    .rotorStats(
                            ((Number) rotor[0]).intValue(),
                            ((Number) rotor[1]).intValue(),
                            ((Number) rotor[2]).intValue(),
                            ((Number) rotor[3]).intValue())
                    .flags(superConductorFlags)
                    .buildAndRegister();
        }
    }
}
