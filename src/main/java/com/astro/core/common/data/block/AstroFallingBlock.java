package com.astro.core.common.data.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;

public class AstroFallingBlock extends FallingBlock {

    private final int dustColor;

    public AstroFallingBlock(Properties properties, int dustColor) {
        super(properties);
        this.dustColor = dustColor;
    }

    @Override
    public int getDustColor(BlockState state, BlockGetter level, BlockPos pos) {
        return dustColor;
    }
}
