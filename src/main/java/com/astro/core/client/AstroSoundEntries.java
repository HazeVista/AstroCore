package com.astro.core.client;

import com.gregtechceu.gtceu.api.sound.SoundEntry;

import com.astro.core.AstroCore;

import static com.astro.core.common.registry.AstroRegistry.REGISTRATE;

public class AstroSoundEntries {

    public static final SoundEntry RUNE_ENGRAVER = REGISTRATE.sound(AstroCore.id("rune_engraver")).build();
    public static final SoundEntry BEVERAGE_PROCESSOR = REGISTRATE.sound(AstroCore.id("beverage_processor")).build();
    public static final SoundEntry CULINARY_FABRICATOR = REGISTRATE.sound(AstroCore.id("culinary_fabricator")).build();
    public static final SoundEntry MANAFIELD_SIMULATOR = REGISTRATE.sound(AstroCore.id("manafield_simulator")).build();

    public static void init() {};
}
