package com.astro.core.common.machine.multiblock.primitive;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.UITemplate;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.gui.widget.TankWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IUIMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;
import com.gregtechceu.gtceu.common.machine.multiblock.primitive.PrimitiveWorkableMachine;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.ProgressTexture;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.ProgressWidget;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fluids.FluidUtil;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.gregtechceu.gtceu.common.data.GTBlocks.CASING_COKE_BRICKS;
import static com.gregtechceu.gtceu.common.data.GTMachines.COKE_OVEN_HATCH;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CokeOvenMachine extends PrimitiveWorkableMachine implements IUIMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            CokeOvenMachine.class, PrimitiveWorkableMachine.MANAGED_FIELD_HOLDER);

    private static final int BASE_SIZE = 2;
    private static final int MAX_PARALLELS = 16;

    @Persisted
    private int structureDepth = BASE_SIZE;

    public CokeOvenMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    protected @NotNull RecipeLogic createRecipeLogic(Object... args) {
        return new RecipeLogic(this);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();

        BlockPos controllerPos = getPos();
        Direction facing = getFrontFacing();
        Direction back = facing.getOpposite();
        Direction.Axis axis = facing.getAxis();
        int direction = back.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 1 : -1;

        int layerCount = 0;

        for (int offset = 1; offset <= 20; offset++) {
            int actualOffset = offset * direction;
            BlockPos checkPos;

            if (axis == Direction.Axis.X) {
                checkPos = controllerPos.offset(actualOffset, 0, 0);
            } else if (axis == Direction.Axis.Z) {
                checkPos = controllerPos.offset(0, 0, actualOffset);
            } else {
                checkPos = controllerPos.offset(0, actualOffset, 0);
            }

            int centerBlocks = 0;
            int edgeBlocks = 0;

            for (int dy = -1; dy <= 1; dy++) {
                for (int cross = -1; cross <= 1; cross++) {
                    BlockPos scanPos;
                    if (axis == Direction.Axis.X) {
                        scanPos = checkPos.offset(0, dy, cross);
                    } else if (axis == Direction.Axis.Z) {
                        scanPos = checkPos.offset(cross, dy, 0);
                    } else {
                        scanPos = checkPos.offset(cross, 0, dy);
                    }

                    var state = getLevel().getBlockState(scanPos);
                    boolean isCokeBrick = state.is(CASING_COKE_BRICKS.get()) || state.is(COKE_OVEN_HATCH.getBlock());

                    if (dy == 0 && cross == 0) {
                        if (isCokeBrick) centerBlocks++;
                    } else {
                        if (isCokeBrick) edgeBlocks++;
                    }
                }
            }

            if (!isRemote() && (edgeBlocks > 0 || centerBlocks > 0)) {
            }

            if (edgeBlocks == 8 && centerBlocks == 1) {
                layerCount++;
                break;
            } else if (edgeBlocks == 8 && centerBlocks == 0) {
                layerCount++;
            } else {
                break;
            }
        }

        structureDepth = layerCount;

        if (!isRemote()) {
        }
    }

    public int getCurrentParallels() {
        if (!isFormed()) {
            return 1;
        }
        if (structureDepth <= 2) {
            return 1;
        }
        int parallels = Math.min(MAX_PARALLELS, structureDepth - 1);
        return parallels;
    }

    @Nonnull
    public static ModifierFunction recipeModifier(MetaMachine machine, @Nonnull GTRecipe recipe) {
        if (machine instanceof CokeOvenMachine cokeOven) {
            int maxParallel = cokeOven.getCurrentParallels();

            int actualParallel = ParallelLogic.getParallelAmount(machine, recipe, maxParallel);

            if (actualParallel <= 1) {
                return ModifierFunction.IDENTITY;
            }

            return ModifierFunction.builder()
                    .inputModifier(ContentModifier.multiplier(actualParallel))
                    .outputModifier(ContentModifier.multiplier(actualParallel))
                    .parallels(actualParallel)
                    .build();
        }
        return ModifierFunction.IDENTITY;
    }

    @Override
    public ModularUI createUI(Player entityPlayer) {
        var ui = new ModularUI(176, 166, this, entityPlayer)
                .background(GuiTextures.PRIMITIVE_BACKGROUND)
                .widget(new LabelWidget(5, 5, getBlockState().getBlock().getDescriptionId()))
                .widget(new SlotWidget(importItems.storage, 0, 52, 30, true, true)
                        .setBackgroundTexture(
                                new GuiTextureGroup(GuiTextures.PRIMITIVE_SLOT, GuiTextures.PRIMITIVE_FURNACE_OVERLAY)))
                .widget(new ProgressWidget(recipeLogic::getProgressPercent, 76, 32, 20, 15,
                        GuiTextures.PRIMITIVE_BLAST_FURNACE_PROGRESS_BAR))
                .widget(new SlotWidget(exportItems.storage, 0, 103, 30, true, false)
                        .setBackgroundTexture(
                                new GuiTextureGroup(GuiTextures.PRIMITIVE_SLOT, GuiTextures.PRIMITIVE_FURNACE_OVERLAY)))
                .widget(new TankWidget(exportFluids.getStorages()[0], 134, 13, 20, 58, true, false)
                        .setBackground(GuiTextures.PRIMITIVE_LARGE_FLUID_TANK)
                        .setFillDirection(ProgressTexture.FillDirection.DOWN_TO_UP)
                        .setShowAmountOverlay(false)
                        .setOverlay(GuiTextures.PRIMITIVE_LARGE_FLUID_TANK_OVERLAY))
                .widget(UITemplate.bindPlayerInventory(entityPlayer.getInventory(), GuiTextures.PRIMITIVE_SLOT, 7, 84,
                        true));

        int parallels = getCurrentParallels();
        if (parallels > 1) {
            ui.widget(new LabelWidget(5, 73, "Parallels: " + parallels));
        }

        return ui;
    }

    @Override
    public void animateTick(RandomSource random) {
        if (this.isActive()) {
            final BlockPos pos = getPos();
            float x = pos.getX() + 0.5F;
            float z = pos.getZ() + 0.5F;

            final var facing = getFrontFacing();
            final float horizontalOffset = GTValues.RNG.nextFloat() * 0.6F - 0.3F;
            final float y = pos.getY() + GTValues.RNG.nextFloat() * 0.375F + 0.3F;

            if (facing.getAxis() == Direction.Axis.X) {
                if (facing.getAxisDirection() == Direction.AxisDirection.POSITIVE) x += 0.52F;
                else x -= 0.52F;
                z += horizontalOffset;
            } else if (facing.getAxis() == Direction.Axis.Z) {
                if (facing.getAxisDirection() == Direction.AxisDirection.POSITIVE) z += 0.52F;
                else z -= 0.52F;
                x += horizontalOffset;
            }
            if (ConfigHolder.INSTANCE.machines.machineSounds && GTValues.RNG.nextDouble() < 0.1) {
                getLevel().playLocalSound(x, y, z, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F,
                        false);
            }
            getLevel().addParticle(ParticleTypes.LARGE_SMOKE, x, y, z, 0, 0, 0);
            getLevel().addParticle(ParticleTypes.FLAME, x, y, z, 0, 0, 0);
        }
    }

    @Override
    public InteractionResult onUse(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand,
                                   BlockHitResult hit) {
        if (!isRemote()) {
            if (super.onUse(state, world, pos, player, hand, hit) == InteractionResult.SUCCESS) {
                return InteractionResult.SUCCESS;
            }
            if (FluidUtil.interactWithFluidHandler(player, hand, exportFluids)) {
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }
}