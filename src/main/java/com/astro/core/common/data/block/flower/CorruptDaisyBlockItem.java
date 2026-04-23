package com.astro.core.common.data.block.flower;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.common.item.block.SpecialFlowerBlockItem;

import java.util.List;

@SuppressWarnings("all")
public class CorruptDaisyBlockItem extends SpecialFlowerBlockItem {

    public CorruptDaisyBlockItem(net.minecraft.world.level.block.Block block, Properties props) {
        super(block, props);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(
                Component.translatable("item.astrogreg.corrupt_daisy.tooltip")
                        .withStyle(ChatFormatting.ITALIC)
                        .withStyle(ChatFormatting.GRAY)
        );
    }
}