package com.astro.core.common.data;

import net.minecraft.world.item.Item;

import com.tterrag.registrate.util.entry.ItemEntry;

import static com.astro.core.AstroCore.ASTRO_CREATIVE_TAB;
import static com.astro.core.common.registry.AstroRegistry.REGISTRATE;

@SuppressWarnings("all")
public class AstroItems {

    static {
        REGISTRATE.creativeModeTab(() -> ASTRO_CREATIVE_TAB);
    }

    public static ItemEntry<Item> RUNE_TABLET = REGISTRATE.item("rune_tablet", Item::new).lang("Rune Tablet")
            .register();
    public static ItemEntry<Item> UNFIRED_RUNE_TABLET = REGISTRATE.item("unfired_rune_tablet", Item::new)
            .lang("Unfired Rune Tablet")
            .register();

    public static void init() {}
}
