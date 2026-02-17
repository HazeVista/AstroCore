package com.astro.core.integration.jade.provider;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import com.astro.core.AstroCore;
import com.astro.core.common.data.configs.AstroConfigs;
import com.astro.core.common.machine.multiblock.generator.AstroSolarBoilers;
import org.lwjgl.glfw.GLFW;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class SolarBoilerProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    public static final ResourceLocation UID = AstroCore.id("solar_boiler_info");

    private static final int MAX_TEMP = 1000;
    private static final int EXPLOSION_THRESHOLD = 600;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (!(accessor.getBlockEntity() instanceof MetaMachineBlockEntity metaMachineBE &&
                metaMachineBE.getMetaMachine() instanceof AstroSolarBoilers)) {
            return;
        }

        CompoundTag data = accessor.getServerData();

        if (!data.contains("formed")) return;

        // Check if shift is held
        boolean shiftHeld = net.minecraft.client.Minecraft.getInstance().screen == null &&
                GLFW.glfwGetKey(net.minecraft.client.Minecraft.getInstance().getWindow().getWindow(),
                        GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS ||
                GLFW.glfwGetKey(net.minecraft.client.Minecraft.getInstance().getWindow().getWindow(),
                        GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS;

        if (!shiftHeld) {
            tooltip.add(Component.translatable("astrogreg.machine.solar_boiler_array.hold_shift")
                    .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            return;
        }

        double intensity = data.getDouble("intensity");
        int temperature = data.getInt("temperature");
        double efficiency = data.getDouble("efficiency");
        double cellMultiplier = data.getDouble("cellMultiplier");
        int sunlit = data.getInt("sunlit");
        long steamOutput = data.getLong("steamOutput");

        // Solar Intensity: 100.0%
        tooltip.add(Component.translatable("astrogreg.machine.solar_boiler_array.solar_intensity",
                String.format("%.1f", intensity))
                .withStyle(ChatFormatting.GOLD));

        // Temperature: 1000°C
        ChatFormatting tempColor = temperature >= EXPLOSION_THRESHOLD ? ChatFormatting.DARK_RED :
                temperature > 400 ? ChatFormatting.GOLD : ChatFormatting.YELLOW;
        tooltip.add(Component.translatable("astrogreg.machine.solar_boiler_array.temperature", temperature)
                .withStyle(tempColor));

        // Thermal Efficiency: 100.0%
        tooltip.add(Component.translatable("astrogreg.machine.solar_boiler_array.thermal_efficiency",
                String.format("%.1f", efficiency))
                .withStyle(ChatFormatting.AQUA));

        // Cell Quality: 15.00x
        tooltip.add(Component.translatable("astrogreg.machine.solar_boiler_array.cell_quality",
                String.format("%.2f", cellMultiplier))
                .withStyle(ChatFormatting.LIGHT_PURPLE));

        // Sunlit Cells: 49
        tooltip.add(Component.translatable("astrogreg.machine.solar_boiler_array.sunlit_cells", sunlit)
                .withStyle(ChatFormatting.YELLOW));

        // Steam Output: 58800 mB/t
        tooltip.add(Component.translatable("astrogreg.machine.solar_boiler_array.steam_output", steamOutput)
                .withStyle(ChatFormatting.AQUA));

        // Danger warnings
        if (temperature >= EXPLOSION_THRESHOLD && steamOutput == 0) {
            tooltip.add(Component.translatable("astrogreg.machine.solar_boiler_array.danger_explosive")
                    .withStyle(ChatFormatting.DARK_RED, ChatFormatting.UNDERLINE));
            tooltip.add(Component.translatable("astrogreg.machine.solar_boiler_array.danger_no_water")
                    .withStyle(ChatFormatting.DARK_RED));
            tooltip.add(Component.translatable("astrogreg.machine.solar_boiler_array.danger_cool_first")
                    .withStyle(ChatFormatting.DARK_RED));
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        if (blockAccessor.getBlockEntity() instanceof MetaMachineBlockEntity metaMachineBE &&
                metaMachineBE.getMetaMachine() instanceof AstroSolarBoilers machine) {

            compoundTag.putBoolean("formed", machine.isFormed());

            if (machine.isFormed()) {
                // Calculate values from the machine
                double dimensionMultiplier = getDimensionMultiplier(machine);
                double intensity = dimensionMultiplier * 100;

                int temperature = machine.temperature;

                int startTemp = AstroConfigs.INSTANCE.Steam.boilingPoint;
                double efficiency = temperature <= startTemp ? 0 :
                        (double) (temperature - startTemp) / (MAX_TEMP - startTemp) * 100;

                compoundTag.putDouble("intensity", intensity);
                compoundTag.putInt("temperature", temperature);
                compoundTag.putDouble("efficiency", efficiency);
                compoundTag.putDouble("cellMultiplier", machine.cellMultiplier);
                compoundTag.putInt("sunlit", machine.sunlit);
                compoundTag.putLong("steamOutput", machine.lastSteamOutput);
            }
        }
    }

    private double getDimensionMultiplier(AstroSolarBoilers machine) {
        if (machine.getLevel() == null) return 1.0;
        String path = machine.getLevel().dimension().location().getPath();
        var cfg = AstroConfigs.INSTANCE.Steam;

        return switch (path) {
            case "moon" -> cfg.moonMultiplier;
            case "venus" -> cfg.venusMultiplier;
            case "mercury" -> cfg.mercuryMultiplier;
            case "mars" -> cfg.marsMultiplier;
            case "ceres" -> cfg.ceresMultiplier;
            case "jupiter" -> cfg.jupiterMultiplier;
            case "saturn" -> cfg.saturnMultiplier;
            case "uranus" -> cfg.uranusMultiplier;
            case "neptune" -> cfg.neptuneMultiplier;
            case "pluto" -> cfg.plutoMultiplier;
            case "kuiper_belt" -> cfg.kuiperBeltMultiplier;
            case "the_end" -> 0;
            default -> 1.0;
        };
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}
