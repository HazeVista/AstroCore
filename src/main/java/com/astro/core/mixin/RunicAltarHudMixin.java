package com.astro.core.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.ItemStack;

import com.astro.core.common.data.AstroItems;
import com.astro.core.mixin.accessor.RunicAltarBlockEntityAccessor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.gui.HUDHandler;
import vazkii.botania.common.block.block_entity.RunicAltarBlockEntity;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.botania.common.helper.PlayerHelper;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.WandOfTheForestItem;

@Mixin(value = RunicAltarBlockEntity.Hud.class, remap = false)
public class RunicAltarHudMixin {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private static void astro$render(RunicAltarBlockEntity altar, GuiGraphics gui, Minecraft mc, CallbackInfo ci) {
        RunicAltarBlockEntityAccessor accessor = (RunicAltarBlockEntityAccessor) altar;

        PoseStack ms = gui.pose();
        int xc = mc.getWindow().getGuiScaledWidth() / 2;
        int yc = mc.getWindow().getGuiScaledHeight() / 2;

        float angle = -90;
        int radius = 24;
        int amt = 0;
        for (int i = 0; i < altar.inventorySize(); i++) {
            if (altar.getItemHandler().getItem(i).isEmpty()) {
                break;
            }
            amt++;
        }

        if (amt > 0 && altar.manaToGet > 0) {
            float anglePer = 360F / amt;
            altar.getLevel().getRecipeManager()
                    .getRecipeFor(BotaniaRecipeTypes.RUNE_TYPE, altar.getItemHandler(), altar.getLevel())
                    .ifPresent(recipe -> {
                        RenderSystem.enableBlend();
                        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

                        float progress = (float) accessor.astro$getMana() / (float) altar.manaToGet;

                        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
                        RenderHelper.drawTexturedModalRect(gui, HUDHandler.manaBar,
                                xc + radius + 9, yc - 8, progress == 1F ? 0 : 22, 8, 22, 15);

                        if (progress == 1F) {
                            gui.renderFakeItem(new ItemStack(AstroItems.RUNE_TABLET.get()), xc + radius + 16, yc + 8);
                            ms.pushPose();
                            ms.translate(0, 0, 100);
                            ItemStack playerWand = PlayerHelper.getFirstHeldItemClass(mc.player,
                                    WandOfTheForestItem.class);
                            if (playerWand.isEmpty()) {
                                playerWand = PlayerHelper.getItemClassFromInventory(mc.player,
                                        WandOfTheForestItem.class);
                            }
                            ItemStack wandToRender = playerWand.isEmpty() ? new ItemStack(BotaniaItems.twigWand) :
                                    playerWand;
                            gui.renderFakeItem(wandToRender, xc + radius + 24, yc + 8);
                            ms.popPose();
                        }

                        RenderHelper.renderProgressPie(gui, xc + radius + 32, yc - 8, progress,
                                recipe.assemble(altar.getItemHandler(), altar.getLevel().registryAccess()));

                        if (progress == 1F) {
                            gui.drawString(mc.font, "+", xc + radius + 14, yc + 12, 0xFFFFFF, false);
                        }
                    });

            for (int i = 0; i < amt; i++) {
                double xPos = xc + Math.cos(angle * Math.PI / 180D) * radius - 8;
                double yPos = yc + Math.sin(angle * Math.PI / 180D) * radius - 8;
                ms.pushPose();
                ms.translate(xPos, yPos, 0);
                gui.renderFakeItem(altar.getItemHandler().getItem(i), 0, 0);
                ms.popPose();
                angle += anglePer;
            }
        }

        if (accessor.astro$getRecipeKeepTicks() > 0 && altar.canAddLastRecipe()) {
            String s = I18n.get("botaniamisc.altarRefill0");
            gui.drawString(mc.font, s, xc - mc.font.width(s) / 2, yc + 10, 0xFFFFFF);
            s = I18n.get("botaniamisc.altarRefill1");
            gui.drawString(mc.font, s, xc - mc.font.width(s) / 2, yc + 20, 0xFFFFFF);
        }

        ci.cancel();
    }
}
