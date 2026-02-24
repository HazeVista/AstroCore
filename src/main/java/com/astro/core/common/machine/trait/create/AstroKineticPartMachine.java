package com.astro.core.common.machine.trait.create;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IWorkableMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredIOPartMachine;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;

import com.astro.core.integration.create.AstroNotifiableStressTrait;
import com.astro.core.integration.create.IKineticMachine;
import lombok.Getter;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class AstroKineticPartMachine extends TieredIOPartMachine implements IKineticMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            AstroKineticPartMachine.class, TieredIOPartMachine.MANAGED_FIELD_HOLDER);

    @Getter
    @Persisted
    protected final AstroNotifiableStressTrait stressTrait;

    public AstroKineticPartMachine(IMachineBlockEntity holder, int tier, IO io, Object... args) {
        super(holder, tier, io);
        this.stressTrait = createStressTrait(args);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    protected AstroNotifiableStressTrait createStressTrait(Object... args) {
        return new AstroNotifiableStressTrait(this, this.io, this.io);
    }

    @Override
    public void onRotated(Direction oldFacing, Direction newFacing) {
        super.onRotated(oldFacing, newFacing);
        if (!isRemote()) {
            if (oldFacing.getAxis() != newFacing.getAxis()) {
                var holder = getKineticHolder();
                if (holder.hasNetwork()) {
                    holder.getOrCreateNetwork().remove(holder);
                }
                holder.detachKinetics();
                holder.removeSource();
            }
        }
    }

    @Override
    public boolean onWaiting(IWorkableMultiController controller) {
        getKineticHolder().stopWorking();
        return super.onWaiting(controller);
    }

    @Override
    public boolean onPaused(IWorkableMultiController controller) {
        getKineticHolder().stopWorking();
        return super.onPaused(controller);
    }

    @Override
    public void removedFromController(IMultiController controller) {
        super.removedFromController(controller);
        getKineticHolder().stopWorking();
    }

    @Override
    public void setWorkingEnabled(boolean workingEnabled) {
        if (!workingEnabled) {
            getKineticHolder().stopWorking();
        }
        super.setWorkingEnabled(workingEnabled);
    }

    @Override
    public boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
        return false;
    }
}
