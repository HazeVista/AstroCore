package com.astro.core.common.data;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.item.ComponentItem;
import com.gregtechceu.gtceu.api.item.component.ElectricStats;

import net.minecraft.world.item.Item;

import com.tterrag.registrate.util.entry.ItemEntry;

import static com.astro.core.AstroCore.ASTRO_CREATIVE_TAB;
import static com.astro.core.common.registry.AstroRegistry.REGISTRATE;

public class AstroItems {

    static {
        REGISTRATE.creativeModeTab(() -> ASTRO_CREATIVE_TAB);
    }
    public static ItemEntry<ComponentItem> POWER_UNIT_LUV = REGISTRATE.item("luv_power_unit", ComponentItem::create)
            .lang("LuV Power Unit")
            .properties(p -> p.stacksTo(8))
            .model((ctx, prov) -> prov.generated(ctx, prov.modLoc("item/tools/power_unit_luv")))
            .onRegister((c) -> c.attachComponents(ElectricStats.createElectricItem(25600000L, GTValues.LuV)))
            .register();
    public static ItemEntry<Item> basic_fuel_rod = REGISTRATE
            .item("basic_fuel_rod", Item::new)
            .model((ctx, prov) -> prov.withExistingParent(ctx.getName(), prov.mcLoc("item/generated"))
                    .texture("layer0", prov.modLoc("item/" + ctx.getName())))
            .register();

    public static void init() {}
}
