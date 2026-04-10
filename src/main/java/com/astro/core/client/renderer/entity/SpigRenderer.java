package com.astro.core.client.renderer.entity;

import com.astro.core.AstroCore;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HoglinRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SpigRenderer extends HoglinRenderer {

    private static final ResourceLocation TEXTURE =
            AstroCore.id("textures/mob/spig.png");

    public SpigRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(Hoglin entity) {
        return TEXTURE;
    }
}