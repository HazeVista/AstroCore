package com.astro.core.common.data;

import com.astro.core.common.data.item.conduit_router.ConduitRouterItem;
import com.tterrag.registrate.util.entry.ItemEntry;

import static com.astro.core.AstroCore.ASTRO_CREATIVE_TAB;
import static com.astro.core.common.registry.AstroRegistry.REGISTRATE;

@SuppressWarnings("all")
public class AstroItems {

    static {
        REGISTRATE.creativeModeTab(() -> ASTRO_CREATIVE_TAB);
    }

//    public static ItemEntry<ConduitRouterItem> CONDUIT_ROUTER = REGISTRATE
//            .item("conduit_router", ConduitRouterItem::new)
//            .properties(p -> p.stacksTo(1))
//            .lang("Conduit Router")
//            .register();

    public static void init() {}
}
