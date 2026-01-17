package com.astro.core.datagen;

import com.astro.core.datagen.lang.AstroLangHandler;
import com.tterrag.registrate.providers.ProviderType;

import static com.astro.core.common.registry.AstroRegistry.REGISTRATE;

@SuppressWarnings("all")
public class AstroDatagen {

    public static void init() {
        REGISTRATE.addDataGenerator(ProviderType.LANG, AstroLangHandler::init);
    }
}
