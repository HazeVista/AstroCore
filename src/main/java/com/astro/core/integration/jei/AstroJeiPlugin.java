package com.astro.core.integration.jei;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import com.astro.core.common.data.AstroItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import vazkii.botania.client.integration.jei.RunicAltarRecipeCategory;

@SuppressWarnings("all")
@JeiPlugin
public class AstroJeiPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation("astrocore", "jei_plugin");
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        registry.addRecipeCatalyst(
                new ItemStack(AstroItems.RUNE_TABLET.get()),
                RunicAltarRecipeCategory.TYPE);
    }
}
