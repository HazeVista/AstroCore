package com.astro.core.common.machine.multiblock.kinetic;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IFluidRenderMulti;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.RequireRerender;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("all")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class KineticMixerMachine extends KineticParallelMultiblockMachine implements IFluidRenderMulti {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            KineticMixerMachine.class, KineticParallelMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Getter
    @Setter
    @DescSynced
    @RequireRerender
    private @NotNull Set<BlockPos> fluidBlockOffsets = new HashSet<>();

    public KineticMixerMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        IFluidRenderMulti.super.onStructureFormed();
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        IFluidRenderMulti.super.onStructureInvalid();
    }

    @Override
    public @NotNull Set<BlockPos> saveOffsets() {
        Direction up = RelativeDirection.UP.getRelative(getFrontFacing(), getUpwardsFacing(), isFlipped());
        Direction back = getFrontFacing().getOpposite();
        Direction clockWise = RelativeDirection.RIGHT.getRelative(getFrontFacing(), getUpwardsFacing(), isFlipped());
        Direction counterClockWise = RelativeDirection.LEFT.getRelative(getFrontFacing(), getUpwardsFacing(),
                isFlipped());

        BlockPos pos = getPos();
        BlockPos center = pos.relative(up, 3);
        Set<BlockPos> offsets = new HashSet<>();

        for (int i = 0; i < 3; i++) {
            center = center.relative(back);
            if (i % 2 == 0) {
                offsets.add(center.subtract(pos));
            }
            offsets.add(center.relative(clockWise).subtract(pos));
            offsets.add(center.relative(counterClockWise).subtract(pos));
        }
        return offsets;
    }
}
