package com.astro.core.mixin;

import com.gregtechceu.gtceu.client.util.RenderUtil;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import com.astro.core.common.data.recipe.AstroRecipeTypes;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings("all")
@Mixin(value = RenderUtil.class, remap = false)
public class DataItemRenderMixin {

    @WrapMethod(method = "renderResearchItemContent")
    private static boolean astro$renderPlanetOnShift(GuiGraphics graphics, Operation<Void> originalMethod,
                                                     @Nullable LivingEntity entity, @Nullable Level level,
                                                     ItemStack stack, int x, int y, int z, int seed,
                                                     Operation<Boolean> original) {
        if (Screen.hasShiftDown() && stack.hasTag()) {
            String planetItemId = stack.getTag().getString(AstroRecipeTypes.OBSERVATORY_PLANET_ITEM_KEY);
            if (!planetItemId.isEmpty()) {
                var opt = BuiltInRegistries.ITEM.getOptional(ResourceLocation.tryParse(planetItemId));
                if (opt.isPresent()) {
                    graphics.renderItem(new ItemStack(opt.get()), x, y);
                    return true;
                }
            }
        }
        return original.call(graphics, originalMethod, entity, level, stack, x, y, z, seed);
    }
}
