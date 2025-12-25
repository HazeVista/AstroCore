package com.astro.core.common.data;

import net.minecraft.world.item.Item;

import com.tterrag.registrate.util.entry.ItemEntry;

import static com.astro.core.AstroCore.ASTRO_CREATIVE_TAB;
import static com.gregtechceu.gtceu.common.registry.GTRegistration.REGISTRATE;

@SuppressWarnings("all")
public class AstroItems {

    static {
        REGISTRATE.creativeModeTab(() -> ASTRO_CREATIVE_TAB);
    }

    public static ItemEntry<Item> createBasicItem = REGISTRATE
            .item("manasteel_firebox_casing", Item::new)
            .model((ctx, prov) -> {
                prov.withExistingParent(ctx.getName(), prov.mcLoc("item/generated"))
                        .texture("layer0", prov.modLoc("item/" + ctx.getName()));
            })
            .register();

    public static void init() {}
}
