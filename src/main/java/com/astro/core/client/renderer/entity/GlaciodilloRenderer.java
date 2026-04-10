package com.astro.core.client.renderer.entity;

import com.astro.core.AstroCore;
import com.astro.core.client.model.GlaciodilloModel;
import com.astro.core.common.entity.GlaciodilloEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GlaciodilloRenderer extends MobRenderer<GlaciodilloEntity, GlaciodilloModel> {

    private static final ResourceLocation TEXTURE =
            AstroCore.id("textures/mob/glaciodillo.png");

    public GlaciodilloRenderer(EntityRendererProvider.Context context) {
        super(context, new GlaciodilloModel(
                context.bakeLayer(GlaciodilloModelLayers.GLACIODILLO)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(GlaciodilloEntity entity) {
        return TEXTURE;
    }

    @Override
    protected void scale(GlaciodilloEntity entity, PoseStack poseStack, float partialTick) {
        if (entity.isBaby()) {
            poseStack.scale(0.5F, 0.5F, 0.5F);
        }
    }
}