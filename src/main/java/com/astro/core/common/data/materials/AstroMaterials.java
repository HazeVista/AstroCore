package com.astro.core.common.data.materials;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import com.astro.core.AstroCore;
import com.drd.ad_extendra.common.registry.*;
import earth.terrarium.adastra.common.registry.ModItems;

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

    public static void register() {
        DESH = new Material.Builder(
                AstroCore.id("desh"))
                .langValue("Desh")
                .ingot()
                .ore()
                .dust()
                .liquid()
                .element(AstroElements.DE).formula("De")
                .color(0xd35400).iconSet(MaterialIconSet.METALLIC)
                .buildAndRegister();

        OSTRUM = new Material.Builder(
                AstroCore.id("ostrum"))
                .langValue("Ostrum")
                .ingot()
                .dust()
                .liquid()
                .ore()
                .element(AstroElements.OT).formula("Ot")
                .color(0xa76b72).iconSet(MaterialIconSet.METALLIC)
                .buildAndRegister();

        CALORITE = new Material.Builder(
                AstroCore.id("calorite"))
                .langValue("Calorite")
                .ingot()
                .dust()
                .element(AstroElements.CT).formula("Ct")
                .color(0xc94d4e).iconSet(MaterialIconSet.METALLIC)
                .buildAndRegister();

        ETRIUM = new Material.Builder(
                AstroCore.id("etrium"))
                .langValue("Etrium")
                .ingot()
                .dust()
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
                .dust()
                .flags(MaterialFlags.GENERATE_PLATE)
                .element(AstroElements.SY).formula("Sy")
                .color(0x9a32e7).iconSet(MaterialIconSet.SHINY)
                .buildAndRegister();

        JUPERIUM = new Material.Builder(
                AstroCore.id("juperium"))
                .langValue("Juperium")
                .ingot()
                .liquid()
                .dust()
                .flags(MaterialFlags.GENERATE_PLATE)
                .element(AstroElements.JP).formula("Jp")
                .color(0x69bbee).iconSet(MaterialIconSet.BRIGHT)
                .buildAndRegister();

        KRONALIUM = new Material.Builder(
                AstroCore.id("kronalium"))
                .langValue("Kronalium")
                .ore()
                .dust()
                .components(AstroMaterials.SATURLYTE, 3, AstroMaterials.CALORITE, 4, GTMaterials.Oxygen, 7)
                .formula("Sy3Ct4O7")
                .color(0x6a1233).iconSet(MaterialIconSet.METALLIC)
                .buildAndRegister();

        JOVITE = new Material.Builder(
                AstroCore.id("jovite"))
                .langValue("Jovite")
                .ore()
                .dust()
                .components(AstroMaterials.JUPERIUM, 1, AstroMaterials.CALORITE, 1).formula("JpCt")
                .color(0x196b9e).iconSet(MaterialIconSet.DULL)
                .buildAndRegister();

        ELECTROLYTE = new Material.Builder(
                AstroCore.id("electrolyte"))
                .langValue("Electrolyte")
                .ingot()
                .plasma()
                .liquid()
                .dust()
                .color(0x6fd422).iconSet(MaterialIconSet.RADIOACTIVE)
                .element(AstroElements.E).formula("⚡")
                .buildAndRegister();
    }

    public static void init() {
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
    }
}
