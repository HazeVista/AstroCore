package com.astro.core.datagen;

import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;

import com.astro.core.common.data.block.flower.AstroFlowerBlocks;

public class AstroFlowerDatagen {

    public static void blockstates(RegistrateBlockstateProvider prov) {
        prov.simpleBlock(AstroFlowerBlocks.CORRUPT_DAISY_BLOCK.get(),
                prov.models().cross(
                        "block/flowers/corrupt_daisy",
                        prov.modLoc("block/flowers/corrupt_daisy")));
    }

    public static void itemModels(RegistrateItemModelProvider prov) {
        prov.generated(
                () -> AstroFlowerBlocks.CORRUPT_DAISY_ITEM.get(),
                prov.modLoc("block/flowers/corrupt_daisy"));
    }
}