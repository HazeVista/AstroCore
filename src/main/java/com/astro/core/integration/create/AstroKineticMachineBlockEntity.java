package com.astro.core.integration.create;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.client.model.machine.MachineRenderState;

import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.syncdata.IManaged;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.annotation.RequireRerender;
import com.lowdragmc.lowdraglib.syncdata.field.FieldManagedStorage;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import com.lowdragmc.lowdraglib.syncdata.managed.MultiManagedStorage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import com.astro.core.common.machine.hatches.AstroHatches;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

@SuppressWarnings("all")
public class AstroKineticMachineBlockEntity extends GeneratingKineticBlockEntity
                                            implements IMachineBlockEntity, IManaged {

    public final MultiManagedStorage managedStorage = new MultiManagedStorage();
    @Getter
    public final MetaMachine metaMachine;
    @Getter
    private final FieldManagedStorage syncStorage = new FieldManagedStorage(this);
    @Getter
    @Persisted
    @DescSynced
    @RequireRerender
    private MachineRenderState renderState;
    private final long offset = GTValues.RNG.nextInt(20);
    private float workingSpeed;

    public AstroKineticMachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.renderState = getDefinition().defaultRenderState();
        this.metaMachine = getDefinition().createMetaMachine(this);
        this.getRootStorage().attach(getSyncStorage());
    }

    @Override
    public void onChanged() {
        var level = getLevel();
        if (level != null && !level.isClientSide && level.getServer() != null) {
            level.getServer().execute(this::setChanged);
        }
    }

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            AstroKineticMachineBlockEntity.class);

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    private float workingCapacityPerRPM = 0f;

    public void scheduleWorking(float su, float capacityPerRPM) {
        workingCapacityPerRPM = capacityPerRPM;
        workingSpeed = Math.min(256f, su / capacityPerRPM);
        updateGeneratedRotation();
    }

    @Override
    public AstroKineticMachineDefinition getDefinition() {
        return (AstroKineticMachineDefinition) IMachineBlockEntity.super.getDefinition();
    }

    @Override
    public AstroKineticMachineBlockEntity self() {
        return this;
    }

    @Override
    public MultiManagedStorage getRootStorage() {
        return managedStorage;
    }

    @Override
    public void setRenderState(MachineRenderState state) {
        this.renderState = state;
        scheduleRenderUpdate();
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        var result = MetaMachineBlockEntity.getCapability(getMetaMachine(), cap, side);
        return result.isPresent() ? result : super.getCapability(cap, side);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        metaMachine.onUnload();
    }

    @Override
    public void clearRemoved() {
        super.clearRemoved();
        metaMachine.onLoad();
    }

    @Override
    public boolean shouldRenderGrid(Player player, BlockPos pos, BlockState state,
                                    ItemStack held, Set<GTToolType> toolTypes) {
        return metaMachine.shouldRenderGrid(player, pos, state, held, toolTypes);
    }

    @Override
    public @Nullable ResourceTexture sideTips(Player player, BlockPos pos, BlockState state,
                                              Set<GTToolType> toolTypes, Direction side) {
        return metaMachine.sideTips(player, pos, state, toolTypes, side);
    }

    @Override
    public boolean triggerEvent(int id, int para) {
        if (id == 1) {
            if (level != null && level.isClientSide) {
                scheduleRenderUpdate();
            }
            return true;
        }
        return false;
    }

    // ======== Create Source Logic ========

    public float scheduleWorking(float su, boolean simulate) {
        float speed = Math.min(256f, su / 1562.5f);
        float actualSU = speed * 1562.5f;
        if (!simulate) {
            workingSpeed = speed;
            updateGeneratedRotation();
        }
        return actualSU;
    }

    @Override
    public float calculateAddedStressCapacity() {
        var block = getBlockState().getBlock();
        if (block != AstroHatches.KINETIC_OUTPUT_HATCH.getBlock()) return 0;
        if (workingCapacityPerRPM == 0) return 0;
        this.lastCapacityProvided = workingCapacityPerRPM;
        return workingCapacityPerRPM;
    }

    public void scheduleWorking(float su) {
        workingSpeed = Math.min(256f, su / 1562.5f);
        updateGeneratedRotation();
    }

    public void stopWorking() {
        if (getDefinition().isSource() && getGeneratedSpeed() != 0) {
            workingSpeed = 0;
            updateGeneratedRotation();
        }
    }

    @Override
    public float getGeneratedSpeed() {
        return workingSpeed;
    }

    // ======== NBT ========

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putFloat("workingSpeed", workingSpeed);
        compound.putFloat("workingCapacityPerRPM", workingCapacityPerRPM);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        workingSpeed = compound.contains("workingSpeed") ? compound.getFloat("workingSpeed") : 0;
        workingCapacityPerRPM = compound.contains("workingCapacityPerRPM") ?
                compound.getFloat("workingCapacityPerRPM") : 0f;
    }
}
