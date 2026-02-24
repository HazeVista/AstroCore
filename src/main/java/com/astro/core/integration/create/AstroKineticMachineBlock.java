package com.astro.core.integration.create;

import com.gregtechceu.gtceu.api.block.MetaMachineBlock;
import com.gregtechceu.gtceu.api.machine.MetaMachine;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("all")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AstroKineticMachineBlock extends MetaMachineBlock implements IRotate {

    public AstroKineticMachineBlock(Properties properties, AstroKineticMachineDefinition definition) {
        super(properties, definition);
    }

    @Override
    public AstroKineticMachineDefinition getDefinition() {
        return (AstroKineticMachineDefinition) super.getDefinition();
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        if (MetaMachine.getMachine(world, pos) instanceof IKineticMachine kineticMachine) {
            return kineticMachine.hasShaftTowards(face);
        }
        return false;
    }

    public Direction getRotationFacing(BlockState state) {
        var frontFacing = getFrontFacing(state);
        return getDefinition().isFrontRotation() ? frontFacing :
                (frontFacing.getAxis() == Direction.Axis.Y ? Direction.NORTH : frontFacing.getClockWise());
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return getRotationFacing(state).getAxis();
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        BlockEntity tileEntity = level.getBlockEntity(pos);
        if (tileEntity instanceof KineticBlockEntity kineticTileEntity) {
            kineticTileEntity.preventSpeedUpdate = 0;
            if (oldState.getBlock() != state.getBlock()) return;
            if (state.hasBlockEntity() != oldState.hasBlockEntity()) return;
            if (!areStatesKineticallyEquivalent(oldState, state)) return;
            kineticTileEntity.preventSpeedUpdate = 2;
        }
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(getRotationState().property,
                pRotation.rotate(pState.getValue(getRotationState().property)));
    }

    public boolean areStatesKineticallyEquivalent(BlockState oldState, BlockState newState) {
        if (oldState.getBlock() != newState.getBlock()) return false;
        return getRotationAxis(newState) == getRotationAxis(oldState);
    }

    @Override
    public void updateIndirectNeighbourShapes(BlockState stateIn, LevelAccessor worldIn, BlockPos pos,
                                              int flags, int count) {
        if (worldIn.isClientSide()) return;
        BlockEntity tileEntity = worldIn.getBlockEntity(pos);
        if (!(tileEntity instanceof KineticBlockEntity kte)) return;
        if (kte.preventSpeedUpdate > 0) return;
        kte.warnOfMovement();
        kte.clearKineticInformation();
        kte.updateSpeed = true;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
                                                                            BlockEntityType<T> blockEntityType) {
        if (blockEntityType == getDefinition().getBlockEntityType()) {
            if (!level.isClientSide) {
                return (pLevel, pPos, pState, pTile) -> {
                    if (pTile instanceof AstroKineticMachineBlockEntity kbe) {
                        kbe.getMetaMachine().serverTick();
                        kbe.tick();
                    }
                };
            } else {
                return (pLevel, pPos, pState, pTile) -> {
                    if (pTile instanceof AstroKineticMachineBlockEntity kbe) {
                        kbe.getMetaMachine().clientTick();
                        kbe.tick();
                    }
                };
            }
        }
        return null;
    }
}
