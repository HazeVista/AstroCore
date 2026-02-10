package com.astro.core.integration.jade.provider;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import com.astro.core.AstroCore;
import com.astro.core.common.machine.multiblock.electric.ProcessingCoreMachine;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class ProcessingCoreProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    public static final ResourceLocation UID = AstroCore.id("processing_core_info");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        CompoundTag data = accessor.getServerData();

        if (!data.contains("coreTier")) return;

        int coreTier = data.getInt("coreTier");
        if (coreTier == -1) return;

        String mkLevel;
        long maxEUt = GTValues.V[coreTier];
        String tierName;

        switch (coreTier) {
            case GTValues.HV -> {
                mkLevel = "MK I";
                tierName = "HV";
            }
            case GTValues.EV -> {
                mkLevel = "MK II";
                tierName = "EV";
            }
            case GTValues.IV -> {
                mkLevel = "MK III";
                tierName = "IV";
            }
            default -> {
                return;
            }
        }

        tooltip.add(Component.translatable("astrogreg.machine.processing_core.core", mkLevel));
        tooltip.add(Component.translatable("astrogreg.machine.processing_core.max_eut", maxEUt, tierName));
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        if (blockAccessor.getBlockEntity() instanceof MetaMachineBlockEntity metaMachineBE &&
                metaMachineBE.getMetaMachine() instanceof ProcessingCoreMachine machine &&
                machine.isFormed()) {

            compoundTag.putInt("coreTier", machine.getCoreTier());
        }
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}
