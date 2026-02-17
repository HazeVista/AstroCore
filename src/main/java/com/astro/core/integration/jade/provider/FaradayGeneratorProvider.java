package com.astro.core.integration.jade.provider;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import com.astro.core.AstroCore;
import com.astro.core.common.machine.multiblock.generator.FaradayGeneratorMachine;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

@SuppressWarnings("all")
public class FaradayGeneratorProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    public static final ResourceLocation UID = AstroCore.id("faraday_generator_info");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (!(accessor.getBlockEntity() instanceof MetaMachineBlockEntity metaMachineBE &&
                metaMachineBE.getMetaMachine() instanceof FaradayGeneratorMachine)) {
            return;
        }

        CompoundTag data = accessor.getServerData();

        if (!data.contains("formed")) return;

        int magnetRows = data.getInt("magnetRows");
        int coolantUsage = data.getInt("coolantUsage");
        String coolantType = data.getString("coolantType");
        boolean isActive = data.getBoolean("isActive");

        tooltip.add(Component.translatable("astrogreg.machine.faraday_generator.magnet_rows", magnetRows)
                .withStyle(ChatFormatting.YELLOW));

        if (isActive && magnetRows > 0) {
            tooltip.add(Component.translatable("astrogreg.machine.faraday_generator.lubricant_usage", magnetRows)
                    .withStyle(ChatFormatting.GOLD));

            tooltip.add(Component.translatable("astrogreg.machine.faraday_generator.coolant_usage", coolantUsage, coolantType)
                    .withStyle(ChatFormatting.AQUA));
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        if (blockAccessor.getBlockEntity() instanceof MetaMachineBlockEntity metaMachineBE &&
                metaMachineBE.getMetaMachine() instanceof FaradayGeneratorMachine machine) {

            compoundTag.putBoolean("formed", machine.isFormed());

            if (machine.isFormed()) {
                compoundTag.putInt("magnetRows", machine.getMagnetRows());
                compoundTag.putBoolean("isActive", machine.isActive());

                boolean usingHelium = RecipeHelper.matchRecipe(machine,
                        GTRecipeBuilder.ofRaw().inputFluids(GTMaterials.Helium.getFluid(1)).buildRawRecipe()).isSuccess();

                int coolantAmount = usingHelium ? machine.getMagnetRows() * 25 : machine.getMagnetRows() * 100;
                String coolantName = usingHelium ? "Liquid Helium" : "Liquid Oxygen";

                compoundTag.putInt("coolantUsage", coolantAmount);
                compoundTag.putString("coolantType", coolantName);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}