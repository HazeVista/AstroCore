package com.astro.core.common.data.block.flower;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.api.block_entity.SpecialFlowerBlockEntity;
import vazkii.botania.api.recipe.PureDaisyRecipe;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.xplat.BotaniaConfig;

import com.astro.core.common.data.recipe.AstroRecipeTypes;

import java.util.Arrays;

public class CorruptDaisyBlockEntity extends SpecialFlowerBlockEntity {

    private static final String TAG_POSITION = "position";
    private static final String TAG_TICKS_REMAINING = "ticksRemaining";
    private static final int RECIPE_COMPLETE_EVENT = 0;

    private static final BlockPos[] POSITIONS = {
            new BlockPos(-1, 0, -1),
            new BlockPos(-1, 0,  0),
            new BlockPos(-1, 0,  1),
            new BlockPos( 0, 0,  1),
            new BlockPos( 1, 0,  1),
            new BlockPos( 1, 0,  0),
            new BlockPos( 1, 0, -1),
            new BlockPos( 0, 0, -1),
    };

    private int positionAt = 0;
    private final int[] prevTicksRemaining = new int[POSITIONS.length];
    private final int[] ticksRemaining = new int[POSITIONS.length];

    public CorruptDaisyBlockEntity(BlockPos pos, BlockState state) {
        super(AstroFlowerBlocks.CORRUPT_DAISY.get(), pos, state);
        Arrays.fill(prevTicksRemaining, -1);
        Arrays.fill(ticksRemaining, -1);
    }

    @Override
    public void tickFlower() {
        super.tickFlower();

        Level world = getLevel();
        if (world == null) return;

        if (world.isClientSide) {
            for (int i = 0; i < POSITIONS.length; i++) {
                if (ticksRemaining[i] > 0) {
                    BlockPos coords = getEffectivePos().offset(POSITIONS[i]);
                    SparkleParticleData data = SparkleParticleData.sparkle(
                            (float) Math.random(), 0.5F, 0F, 1F, 5);
                    world.addParticle(data,
                            coords.getX() + Math.random(),
                            coords.getY() + Math.random(),
                            coords.getZ() + Math.random(),
                            0, 0, 0);
                }
            }
            return;
        }

        positionAt++;
        if (positionAt == POSITIONS.length) positionAt = 0;

        BlockPos coords = getEffectivePos().offset(POSITIONS[positionAt]);

        if (!world.isEmptyBlock(coords)) {
            world.getProfiler().push("findCorruptDaisyRecipe");
            PureDaisyRecipe recipe = findRecipe(world, coords);
            world.getProfiler().pop();

            if (recipe != null) {
                if (ticksRemaining[positionAt] == -1) {
                    ticksRemaining[positionAt] = recipe.getTime();
                }
                ticksRemaining[positionAt]--;

                if (ticksRemaining[positionAt] <= 0) {
                    ticksRemaining[positionAt] = -1;
                    if (recipe.set(world, coords, this)) {
                        if (BotaniaConfig.common().blockBreakParticles()) {
                            world.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK,
                                    coords, Block.getId(recipe.getOutputState()));
                        }
                        world.gameEvent(null, GameEvent.BLOCK_CHANGE, coords);
                        world.blockEvent(getBlockPos(), getBlockState().getBlock(),
                                RECIPE_COMPLETE_EVENT, positionAt);
                    }
                }
            } else {
                ticksRemaining[positionAt] = -1;
            }
        } else {
            ticksRemaining[positionAt] = -1;
        }

        if (!Arrays.equals(ticksRemaining, prevTicksRemaining)) {
            setChanged();
            sync();
            System.arraycopy(ticksRemaining, 0, prevTicksRemaining, 0, POSITIONS.length);
        }
    }

    @Nullable
    private PureDaisyRecipe findRecipe(Level world, BlockPos coords) {
        BlockState state = world.getBlockState(coords);
        for (Recipe<?> recipe : world.getRecipeManager()
                .getAllRecipesFor(AstroRecipeTypes.CORRUPT_DAISY_TYPE.get())) {
            if (recipe instanceof PureDaisyRecipe daisyRecipe
                    && daisyRecipe.matches(world, coords, this, state)) {
                return daisyRecipe;
            }
        }
        return null;
    }

    @Override
    public boolean triggerEvent(int type, int param) {
        if (type == RECIPE_COMPLETE_EVENT) {
            if (getLevel() != null && getLevel().isClientSide) {
                BlockPos coords = getEffectivePos().offset(POSITIONS[param]);
                for (int i = 0; i < 25; i++) {
                    double x = coords.getX() + Math.random();
                    double y = coords.getY() + Math.random() + 0.5;
                    double z = coords.getZ() + Math.random();
                    WispParticleData data = WispParticleData.wisp(
                            (float) Math.random() / 2F, 0.5F, 0F, 1F);
                    getLevel().addParticle(data, x, y, z, 0, 0, 0);
                }
            }
            return true;
        }
        return super.triggerEvent(type, param);
    }

    @Override
    public RadiusDescriptor getRadius() {
        return RadiusDescriptor.Rectangle.square(getEffectivePos(), 1);
    }

    @Override
    public void readFromPacketNBT(CompoundTag cmp) {
        super.readFromPacketNBT(cmp);
        positionAt = cmp.getInt(TAG_POSITION);
        for (int i = 0; i < ticksRemaining.length; i++) {
            ticksRemaining[i] = cmp.getInt(TAG_TICKS_REMAINING + i);
        }
    }

    @Override
    public void writeToPacketNBT(CompoundTag cmp) {
        super.writeToPacketNBT(cmp);
        cmp.putInt(TAG_POSITION, positionAt);
        for (int i = 0; i < ticksRemaining.length; i++) {
            cmp.putInt(TAG_TICKS_REMAINING + i, ticksRemaining[i]);
        }
    }
}