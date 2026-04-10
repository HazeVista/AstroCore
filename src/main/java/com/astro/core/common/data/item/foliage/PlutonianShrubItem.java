package com.astro.core.common.data.item.foliage;

import com.astro.core.common.data.AstroBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.tags.BlockTags;

public class PlutonianShrubItem extends ItemNameBlockItem {

    public PlutonianShrubItem(Properties properties) {
        super(AstroBlocks.PLUTONIAN_SHRUB.get(), properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockPos above = pos.above();
        BlockState clicked = level.getBlockState(pos);

        if (clicked.is(BlockTags.SAND) && level.getBlockState(above).isAir()) {
            if (!level.isClientSide) {
                level.setBlock(above, AstroBlocks.PLUTONIAN_SHRUB.get()
                        .getStateForAge(2), 3);
                context.getItemInHand().shrink(1);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }
}