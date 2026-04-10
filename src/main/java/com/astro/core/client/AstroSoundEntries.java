package com.astro.core.client;

import com.gregtechceu.gtceu.api.sound.SoundEntry;
import com.astro.core.AstroCore;

import static com.astro.core.common.registry.AstroRegistry.REGISTRATE;

public class AstroSoundEntries {

    // ======== Machine Sounds ========

    public static final SoundEntry RUNE_ENGRAVER       = REGISTRATE.sound(AstroCore.id("rune_engraver")).build();
    public static final SoundEntry BEVERAGE_PROCESSOR  = REGISTRATE.sound(AstroCore.id("beverage_processor")).build();
    public static final SoundEntry CULINARY_FABRICATOR = REGISTRATE.sound(AstroCore.id("culinary_fabricator")).build();
    public static final SoundEntry MANAFIELD_SIMULATOR = REGISTRATE.sound(AstroCore.id("manafield_simulator")).build();

    // ======== Glaciodillo Sounds ========

    public static final SoundEntry GLACIODILLO_AMBIENT       = REGISTRATE.sound(AstroCore.id("entity.glaciodillo.ambient")).build();
    public static final SoundEntry GLACIODILLO_DEATH         = REGISTRATE.sound(AstroCore.id("entity.glaciodillo.death")).build();
    public static final SoundEntry GLACIODILLO_HURT          = REGISTRATE.sound(AstroCore.id("entity.glaciodillo.hurt")).build();
    public static final SoundEntry GLACIODILLO_HURT_REDUCED  = REGISTRATE.sound(AstroCore.id("entity.glaciodillo.hurt_reduced")).build();
    public static final SoundEntry GLACIODILLO_STEP          = REGISTRATE.sound(AstroCore.id("entity.glaciodillo.step")).build();
    public static final SoundEntry GLACIODILLO_EAT           = REGISTRATE.sound(AstroCore.id("entity.glaciodillo.eat")).build();
    public static final SoundEntry GLACIODILLO_ROLL          = REGISTRATE.sound(AstroCore.id("entity.glaciodillo.roll")).build();
    public static final SoundEntry GLACIODILLO_UNROLL_FINISH = REGISTRATE.sound(AstroCore.id("entity.glaciodillo.unroll_finish")).build();
    public static final SoundEntry GLACIODILLO_UNROLL_START  = REGISTRATE.sound(AstroCore.id("entity.glaciodillo.unroll_start")).build();
    public static final SoundEntry GLACIODILLO_PEEK          = REGISTRATE.sound(AstroCore.id("entity.glaciodillo.peek")).build();

    public static void init() {}
}