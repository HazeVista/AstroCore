package com.astro.core.mixin;

import com.astro.core.client.AstroGUITextures;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.lowdragmc.lowdraglib.gui.texture.ProgressTexture.FillDirection;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.gregtechceu.gtceu.api.recipe.GTRecipeType;

@Mixin(value = GTRecipeTypes.class, remap = false)
public class MixinGTRecipeTypes {

    @Shadow
    @Final
    public static GTRecipeType ARC_FURNACE_RECIPES;
    @Shadow
    @Final
    public static GTRecipeType CENTRIFUGE_RECIPES;
    @Shadow
    @Final
    public static GTRecipeType MIXER_RECIPES;

    @Inject(method = "init", at = @At(value = "TAIL"), remap = false)
    private static void astro$init(CallbackInfo ci) {

        ARC_FURNACE_RECIPES.setMaxIOSize(1, 9, 1, 0);

        CENTRIFUGE_RECIPES.setSteamProgressBar(GuiTextures.PROGRESS_BAR_EXTRACT_STEAM, FillDirection.LEFT_TO_RIGHT);
        CENTRIFUGE_RECIPES.setMaxIOSize(2, 6, 1, 3);

        MIXER_RECIPES.setSteamProgressBar(AstroGUITextures.PROGRESS_BAR_STEAM_MIXER, FillDirection.LEFT_TO_RIGHT);

    }

}
