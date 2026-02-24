package com.astro.core.integration.jade.provider;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import com.astro.core.AstroCore;
import com.astro.core.common.machine.multiblock.kinetic.KineticSteamEngineMachine;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class KineticSteamEngineProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    public static final ResourceLocation UID = AstroCore.id("kinetic_steam_engine_info");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (!(accessor.getBlockEntity() instanceof MetaMachineBlockEntity metaMachineBE &&
                metaMachineBE.getMetaMachine() instanceof KineticSteamEngineMachine)) {
            return;
        }

        CompoundTag data = accessor.getServerData();
        if (!data.contains("formed") || !data.getBoolean("formed")) return;

        int steamDemand = data.getInt("steamDemand");
        int suOutput = data.getInt("suOutput");

        tooltip.add(Component.translatable("astrogreg.machine.kinetic_steam_engine.steam_usage", steamDemand)
                .withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.translatable("astrogreg.machine.kinetic_steam_engine.su_output", suOutput)
                .withStyle(ChatFormatting.AQUA));
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        if (blockAccessor.getBlockEntity() instanceof MetaMachineBlockEntity metaMachineBE &&
                metaMachineBE.getMetaMachine() instanceof KineticSteamEngineMachine machine) {

            compoundTag.putBoolean("formed", machine.isFormed());

            if (machine.isFormed()) {
                compoundTag.putInt("steamDemand", machine.calculateSteamPerTick());
                compoundTag.putInt("suOutput",
                        machine.getRecipeLogic().isActive() ? (int) machine.calculateTotalSU() : 0);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}
