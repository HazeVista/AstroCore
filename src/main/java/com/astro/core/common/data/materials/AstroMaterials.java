package com.astro.core.common.data.materials;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.BlastProperty;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.ToolProperty;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.gregtechceu.gtceu.common.data.GTMaterials;

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
                        MaterialFlags.GENERATE_FINE_WIRE,
                        MaterialFlags.GENERATE_RING, MaterialFlags.GENERATE_FRAME)
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

        FUTURA_ALLOY = new Material.Builder(
                AstroCore.id("futura_alloy"))
                .langValue("Futura Steel")
                .ingot()
                .fluid()
                .blastTemp(1700, BlastProperty.GasTier.LOW, 400, 1200)
                .flags(MaterialFlags.GENERATE_FRAME, MaterialFlags.GENERATE_DENSE, MaterialFlags.DISABLE_ALLOY_BLAST,
                        MaterialFlags.GENERATE_PLATE, MaterialFlags.GENERATE_ROD, MaterialFlags.MORTAR_GRINDABLE)
                .color(0xebb7ea).secondaryColor(0x000000).iconSet(MaterialIconSet.SHINY)
                .components(AstroMaterials.SKY_STONE, 1, GTMaterials.StainlessSteel, 4)
                .formula("(Fe6CrMnNi)4✨")
                .buildAndRegister();

        // Botania
        MANA = new Material.Builder(
                AstroCore.id("mana"))
                .langValue("Mana")
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

        MANASTEEL = new Material.Builder(
                AstroCore.id("manasteel"))
                .langValue("§9Manasteel")
                .ingot()
                .fluid()
                .blastTemp(1000, BlastProperty.GasTier.LOW, 120, 400)
                .flags(MaterialFlags.GENERATE_FRAME, MaterialFlags.GENERATE_DENSE, MaterialFlags.DISABLE_DECOMPOSITION,
                        MaterialFlags.GENERATE_PLATE, MaterialFlags.GENERATE_ROD, MaterialFlags.MORTAR_GRINDABLE)
                .toolStats(new ToolProperty(8.0F, 7.0F, 768, 3,
                        new GTToolType[] { GTToolType.SHOVEL, GTToolType.PICKAXE, GTToolType.AXE, GTToolType.HOE,
                                GTToolType.WRENCH_IV, GTToolType.WIRE_CUTTER_LV,
                                GTToolType.MINING_HAMMER, GTToolType.SPADE, GTToolType.SAW, GTToolType.HARD_HAMMER,
                                GTToolType.SOFT_MALLET, GTToolType.WRENCH,
                                GTToolType.FILE, GTToolType.CROWBAR, GTToolType.SCREWDRIVER, GTToolType.MORTAR,
                                GTToolType.WIRE_CUTTER, GTToolType.SCYTHE, GTToolType.KNIFE,
                                GTToolType.BUTCHERY_KNIFE, GTToolType.PLUNGER, GTToolType.DRILL_LV, GTToolType.DRILL_MV,
                                GTToolType.DRILL_HV, GTToolType.DRILL_EV,
                                GTToolType.DRILL_IV, GTToolType.CHAINSAW_LV, GTToolType.BUZZSAW,
                                GTToolType.SCREWDRIVER_LV, GTToolType.WRENCH_LV, GTToolType.WRENCH_HV,
                                GTToolType.WIRE_CUTTER_HV, GTToolType.WIRE_CUTTER_IV }))
                .color(0x228cc9).iconSet(MaterialIconSet.SHINY)
                .components(GTMaterials.Steel, 1, AstroMaterials.MANA, 1).formula("Fe✨")
                .buildAndRegister();

        DORMANT_TERRASTEEL = new Material.Builder(
                AstroCore.id("dormant_terrasteel"))
                .langValue("Inactive §2Terrasteel§r")
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
                .flags(MaterialFlags.GENERATE_FRAME, MaterialFlags.GENERATE_DENSE, MaterialFlags.DISABLE_DECOMPOSITION,
                        MaterialFlags.GENERATE_PLATE, MaterialFlags.GENERATE_ROD)
                .toolStats(new ToolProperty(11.0F, 11.0F, 2048, 3,
                        new GTToolType[] { GTToolType.SHOVEL, GTToolType.PICKAXE, GTToolType.AXE, GTToolType.HOE,
                                GTToolType.WRENCH_IV, GTToolType.WIRE_CUTTER_LV,
                                GTToolType.MINING_HAMMER, GTToolType.SPADE, GTToolType.SAW, GTToolType.HARD_HAMMER,
                                GTToolType.SOFT_MALLET, GTToolType.WRENCH,
                                GTToolType.FILE, GTToolType.CROWBAR, GTToolType.SCREWDRIVER, GTToolType.MORTAR,
                                GTToolType.WIRE_CUTTER, GTToolType.SCYTHE, GTToolType.KNIFE,
                                GTToolType.BUTCHERY_KNIFE, GTToolType.PLUNGER, GTToolType.DRILL_LV, GTToolType.DRILL_MV,
                                GTToolType.DRILL_HV, GTToolType.DRILL_EV,
                                GTToolType.DRILL_IV, GTToolType.CHAINSAW_LV, GTToolType.BUZZSAW,
                                GTToolType.SCREWDRIVER_LV, GTToolType.WRENCH_LV, GTToolType.WRENCH_HV,
                                GTToolType.WIRE_CUTTER_HV, GTToolType.WIRE_CUTTER_IV }))
                .color(0x159e1e).iconSet(MaterialIconSet.METALLIC)
                .components(GTMaterials.Steel, 1, GTMaterials.Beryllium, 1, GTMaterials.Aluminium, 1,
                        AstroMaterials.MANA, 1)
                .formula("FeBeAl✨")
                .buildAndRegister();

        ELEMENTIUM = new Material.Builder(
                AstroCore.id("elementium"))
                .langValue("§dElementium")
                .ingot()
                .fluid()
                .blastTemp(3500, BlastProperty.GasTier.MID, (int) GTVoltage.VA.IV, 1600)
                .flags(MaterialFlags.GENERATE_FRAME, MaterialFlags.GENERATE_DENSE, MaterialFlags.DISABLE_DECOMPOSITION,
                        MaterialFlags.GENERATE_PLATE, MaterialFlags.GENERATE_ROD)
                .toolStats(new ToolProperty(16.0F, 13.0F, 3072, 4,
                        new GTToolType[] { GTToolType.SHOVEL, GTToolType.PICKAXE, GTToolType.AXE, GTToolType.HOE,
                                GTToolType.WRENCH_IV, GTToolType.WIRE_CUTTER_LV,
                                GTToolType.MINING_HAMMER, GTToolType.SPADE, GTToolType.SAW, GTToolType.HARD_HAMMER,
                                GTToolType.SOFT_MALLET, GTToolType.WRENCH,
                                GTToolType.FILE, GTToolType.CROWBAR, GTToolType.SCREWDRIVER, GTToolType.MORTAR,
                                GTToolType.WIRE_CUTTER, GTToolType.SCYTHE, GTToolType.KNIFE,
                                GTToolType.BUTCHERY_KNIFE, GTToolType.PLUNGER, GTToolType.DRILL_LV, GTToolType.DRILL_MV,
                                GTToolType.DRILL_HV, GTToolType.DRILL_EV,
                                GTToolType.DRILL_IV, GTToolType.CHAINSAW_LV, GTToolType.BUZZSAW,
                                GTToolType.SCREWDRIVER_LV, GTToolType.WRENCH_LV, GTToolType.WRENCH_HV,
                                GTToolType.WIRE_CUTTER_HV, GTToolType.WIRE_CUTTER_IV }))
                .color(0xed64d4).iconSet(MaterialIconSet.SHINY)
                .components(GTMaterials.Titanium, 3, GTMaterials.Rhodium, 2, GTMaterials.Carbon, 1, AstroMaterials.AETHER,
                        1)
                .formula("Ti3Rh2C✨")
                .buildAndRegister();

        GAIASTEEL = new Material.Builder(
                AstroCore.id("gaiasteel"))
                .langValue("§cGaiasteel")
                .ingot()
                .fluid()
                .blastTemp(7100, BlastProperty.GasTier.HIGH, (int) GTVoltage.VA.ZPM, 2400)
                .flags(MaterialFlags.GENERATE_FRAME, MaterialFlags.GENERATE_DENSE, MaterialFlags.DISABLE_DECOMPOSITION,
                        MaterialFlags.GENERATE_PLATE, MaterialFlags.GENERATE_ROD)
                .toolStats(ToolProperty.Builder.of(48.0F, 16.0F, 4096, 5)
                        .types(GTToolType.SHOVEL, GTToolType.PICKAXE, GTToolType.AXE, GTToolType.HOE,
                                GTToolType.WRENCH_IV, GTToolType.WIRE_CUTTER_LV, GTToolType.DRILL_MV,
                                GTToolType.MINING_HAMMER, GTToolType.SPADE, GTToolType.SAW,
                                GTToolType.SOFT_MALLET, GTToolType.WRENCH, GTToolType.HARD_HAMMER,
                                GTToolType.FILE, GTToolType.CROWBAR, GTToolType.SCREWDRIVER,
                                GTToolType.WIRE_CUTTER, GTToolType.SCYTHE, GTToolType.KNIFE,
                                GTToolType.BUTCHERY_KNIFE, GTToolType.PLUNGER, GTToolType.DRILL_LV,
                                GTToolType.DRILL_HV, GTToolType.DRILL_EV, GTToolType.MORTAR,
                                GTToolType.DRILL_IV, GTToolType.CHAINSAW_LV, GTToolType.BUZZSAW,
                                GTToolType.SCREWDRIVER_LV, GTToolType.WRENCH_LV, GTToolType.WRENCH_HV,
                                GTToolType.WIRE_CUTTER_HV, GTToolType.WIRE_CUTTER_IV)
                        .magnetic().build())
                .color(0x8c2929).iconSet(MaterialIconSet.BRIGHT)
                // .components().formula()
                .buildAndRegister();

        MANA_DIAMOND = new Material.Builder(
                AstroCore.id("mana_diamond"))
                .langValue("Mana Diamond")
                .flags(MaterialFlags.GENERATE_LENS, MaterialFlags.GENERATE_PLATE, MaterialFlags.CRYSTALLIZABLE)
                .components(GTMaterials.Carbon, 1, AstroMaterials.MANA, 1).formula("C✨")
                .color(0x47eaed).iconSet(MaterialIconSet.DIAMOND)
                .buildAndRegister();

        DRAGONSTONE = new Material.Builder(
                AstroCore.id("dragonstone"))
                .langValue("Dragonstone")
                .flags(MaterialFlags.GENERATE_LENS, MaterialFlags.GENERATE_PLATE, MaterialFlags.CRYSTALLIZABLE)
                .components(GTMaterials.Carbon, 1, AstroMaterials.AETHER, 1).formula("C✨")
                .color(0xed64d4).iconSet(MaterialIconSet.DIAMOND)
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

//         botania/additions
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
