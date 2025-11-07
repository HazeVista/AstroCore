package com.astrogreg.astrogreg.item;

import com.astrogreg.astrogreg.AstroGregExsilium;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, AstroGregExsilium.MOD_ID);

    public static final RegistryObject<Item> ULVUNICIRC = ITEMS.register("ulvunicircuit",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> LVUNICIRC = ITEMS.register("lvunicircuit",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MVUNICIRC = ITEMS.register("mvunicircuit",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HVUNICIRC = ITEMS.register("hvunicircuit",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> EVUNICIRC = ITEMS.register("evunicircuit",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> IVUNICIRC = ITEMS.register("ivunicircuit",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> LUVUNICIRC = ITEMS.register("luvunicircuit",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ZPMUNICIRC = ITEMS.register("zpmunicircuit",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> UVUNICIRC = ITEMS.register("uvunicircuit",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> UHVUNICIRC = ITEMS.register("uhvunicircuit",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
