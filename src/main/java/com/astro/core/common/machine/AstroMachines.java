package com.astro.core.common.machine;

import com.astro.core.AstroCore;

import static com.astro.core.common.registry.AstroRegistry.REGISTRATE;

@SuppressWarnings("all")
public class AstroMachines {

    static {
        REGISTRATE.creativeModeTab(() -> AstroCore.ASTRO_CREATIVE_TAB);
    }

    public static void init() {}
}
