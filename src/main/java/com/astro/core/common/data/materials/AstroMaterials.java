package com.astro.core.common.data.materials;

import com.astro.core.AstroCore;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet;
import earth.terrarium.adastra.common.registry.ModItems;

import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;

public class AstroMaterials {

    public static Material DESH;
    public static Material CALORITE;
    public static Material OSTRUM;

    public static void register() {
        DESH = new Material.Builder(
                AstroCore.id("desh"))
                .ingot()
                .ore()
                .dust()
                .color(0xd88c4e).iconSet(MaterialIconSet.METALLIC)
                .buildAndRegister();

        OSTRUM = new Material.Builder(
                AstroCore.id("ostrum"))
                .ingot()
                .dust()
                .ore()
                .color(0x8c2938).iconSet(MaterialIconSet.METALLIC)
                .buildAndRegister();

        CALORITE = new Material.Builder(
                AstroCore.id("calorite"))
                .ingot()
                .ore()
                .dust()
                .color(0xc21313).iconSet(MaterialIconSet.METALLIC)
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
    }
}
