package com.astrogreg.astrogreg;

import com.astrogreg.astrogreg.block.ModBlocks;
import com.astrogreg.astrogreg.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AstroGregExsilium.MOD_ID);

    public static final RegistryObject<CreativeModeTab> ASTROGREG_TAB = CREATIVE_MODE_TABS.register("astrogreg_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.IVUNICIRC.get()))
                    .title(Component.translatable("creativetab.astrogreg_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.ULVUNICIRC.get());
                        pOutput.accept(ModItems.LVUNICIRC.get());
                        pOutput.accept(ModItems.MVUNICIRC.get());
                        pOutput.accept(ModItems.HVUNICIRC.get());
                        pOutput.accept(ModItems.EVUNICIRC.get());
                        pOutput.accept(ModItems.IVUNICIRC.get());
                        pOutput.accept(ModItems.LUVUNICIRC.get());
                        pOutput.accept(ModItems.ZPMUNICIRC.get());
                        pOutput.accept(ModItems.UVUNICIRC.get());
                        pOutput.accept(ModItems.UHVUNICIRC.get());

                        /*pOutput.accept(ModBlocks.FUTURA_STEEL_CASING.get());*/
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
