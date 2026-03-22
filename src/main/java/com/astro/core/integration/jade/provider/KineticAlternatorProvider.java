package com.astro.core.integration.jade.provider;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import com.astro.core.AstroCore;
import com.astro.core.common.machine.multiblock.kinetic.KineticAlternatorMachine;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class KineticAlternatorProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    public static final ResourceLocation UID = AstroCore.id("kinetic_alternator_info");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (!(accessor.getBlockEntity() instanceof MetaMachineBlockEntity metaMachineBE &&
                metaMachineBE.getMetaMachine() instanceof KineticAlternatorMachine)) {
            return;
        }

        CompoundTag data = accessor.getServerData();
        if (!data.contains("formed") || !data.getBoolean("formed")) return;

        int parallels = data.getInt("parallels");
        long currentEU = data.getLong("currentEU");
        long outputVoltage = data.getLong("outputVoltage");

        if (currentEU > 0 && outputVoltage > 0) {
            int tier = GTUtil.getTierByVoltage(outputVoltage);
            float amperage = (float) currentEU / outputVoltage;

            MutableComponent text = Component
                    .translatable("gtceu.jade.amperage_use", FormattingUtil.formatNumber2Places(amperage))
                    .withStyle(ChatFormatting.RED)
                    .append(Component.translatable("gtceu.jade.at").withStyle(ChatFormatting.GREEN))
                    .append(Component.literal(GTValues.VNF[tier])
                            .withStyle(style -> style.withColor(GTValues.VC[tier])))
                    .append(Component.translatable("gtceu.universal.padded_parentheses",
                            Component.translatable("gtceu.recipe.eu.total",
                                    FormattingUtil.formatNumbers(currentEU)))
                            .withStyle(ChatFormatting.WHITE));

            tooltip.add(Component.translatable("gtceu.top.energy_production").append(" ").append(text));
        }

        tooltip.add(Component.translatable("gtceu.multiblock.total_runs",
                Component.literal(FormattingUtil.formatNumbers(parallels))
                        .withStyle(ChatFormatting.DARK_PURPLE))
                .withStyle(ChatFormatting.GRAY));

        tooltip.add(Component.translatable("gtceu.multiblock.parallel.exact",
                Component.literal(FormattingUtil.formatNumbers(parallels))
                        .withStyle(ChatFormatting.DARK_PURPLE))
                .withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        if (blockAccessor.getBlockEntity() instanceof MetaMachineBlockEntity metaMachineBE &&
                metaMachineBE.getMetaMachine() instanceof KineticAlternatorMachine machine) {

            compoundTag.putBoolean("formed", machine.isFormed());

            if (machine.isFormed()) {
                int parallels = machine.getAvailableParallels();
                long outputVoltage = machine.getOutputVoltage();
                compoundTag.putInt("parallels", parallels);
                compoundTag.putLong("currentEU",
                        machine.getRecipeLogic().isWorking() ?
                                (long) parallels * KineticAlternatorMachine.EU_PER_PARALLEL : 0L);
                compoundTag.putLong("outputVoltage", outputVoltage);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}
