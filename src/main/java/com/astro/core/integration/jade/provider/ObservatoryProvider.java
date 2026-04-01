package com.astro.core.integration.jade.provider;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.astro.core.AstroCore;
import com.astro.core.common.machine.multiblock.electric.planetary_research.ObservatoryMachine;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

@SuppressWarnings("all")
public class ObservatoryProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    public static final ResourceLocation UID = AstroCore.id("observatory_info");

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        BlockEntity be = accessor.getBlockEntity();
        if (!(be instanceof MetaMachineBlockEntity mmbe)) return;
        if (!(mmbe.getMetaMachine() instanceof ObservatoryMachine machine)) return;

        data.putBoolean("active", machine.getRecipeLogic().isActive());
        data.putInt("progress", machine.getRecipeLogic().getProgress());
        data.putInt("maxProgress", machine.getRecipeLogic().getMaxProgress());
    }

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {}
}
