package com.astro.core.common.data.materials;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.BlastProperty;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import com.astro.core.AstroCore;
import com.astro.core.common.GTVoltage;
import com.drd.ad_extendra.common.registry.ModBlocks;
import earth.terrarium.adastra.common.registry.ModItems;
import org.zeith.botanicadds.init.BlocksBA;
import org.zeith.botanicadds.init.ItemsBA;
import owmii.powah.block.Blcks;
import owmii.powah.item.Itms;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;

import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;

public class AstroMaterials {

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
                .flags(MaterialFlags.GENERATE_FOIL, MaterialFlags.GENERATE_FINE_WIRE, MaterialFlags.GENERATE_BOLT_SCREW)
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
                .cableProperties(32, 4, 0, true).rotorStats(150, 130, 3, 12000)
                .components(AstroMaterials.DESH, 1, GTMaterials.RedAlloy, 1, GTMaterials.Iron, 1)
                .formula("DeFeCu(Si(FeS2)5(CrAl2O3)Hg3)4")
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
    }

    public static void init() {
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
}
