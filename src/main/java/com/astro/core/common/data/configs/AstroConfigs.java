package com.astro.core.common.data.configs;

import com.astro.core.AstroCore;
import dev.toma.configuration.Configuration;
import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.ConfigHolder;
import dev.toma.configuration.config.Configurable;
import dev.toma.configuration.config.format.ConfigFormats;

@Config(id = AstroCore.MOD_ID)
public class AstroConfigs {

    public static AstroConfigs INSTANCE;

    public static ConfigHolder<AstroConfigs> CONFIG_HOLDER;

    public static void init() {
        CONFIG_HOLDER = Configuration.registerConfig(AstroConfigs.class, ConfigFormats.yaml());
        INSTANCE = CONFIG_HOLDER.getConfigInstance();
    }

    @Configurable
    public FeatureConfigs features = new FeatureConfigs();

    public static class FeatureConfigs {

        // Mana Boiler configs
        @Configurable
        @Configurable.Comment({ "The max temperature of the Large Manasteel Mana Boiler" })
        public int manasteelBoilerMaxTemperature = 2000;
        @Configurable
        @Configurable.Comment({ "The heat speed of the Large Manasteel Mana Boiler" })
        public int manasteelBoilerHeatSpeed = 1;
        @Configurable
        @Configurable.Comment({ "The max temperature of the Large Terrasteel Mana Boiler" })
        public int terrasteelBoilerMaxTemperature = 3000;
        @Configurable
        @Configurable.Comment({ "The heat speed of the Large Terrasteel Mana Boiler" })
        public int terrasteelBoilerHeatSpeed = 2;
        @Configurable
        @Configurable.Comment({ "The max temperature of the Large Alfsteel Mana Boiler" })
        public int alfsteelBoilerMaxTemperature = 4000;
        @Configurable
        @Configurable.Comment({ "The heat speed of the Large Alfsteel Mana Boiler" })
        public int alfsteelBoilerHeatSpeed = 3;
        @Configurable
        @Configurable.Comment({
                "The base speed multiplier for the Steam Blast Furnace. (works inversely, 0.5 would be 2x as fast)" })
        public double SBFRecipeSpeed = 1.0;

        // Solar Boiler Array
        @Configurable
        @Configurable.Comment({ "The base heat capacity (seconds per degree drop) when the boiler is small." })
        public int solarBaseCapacity = 1;
        @Configurable
        @Configurable.Comment({ "How many solar cells are required to increase the heat capacity by 1 second." })
        public int cellsPerCapacityPoint = 40;
        @Configurable
        @Configurable.Comment({ "The multiplier added to heat speed for every sunlit cell. (e.g. 0.01 = 1% faster per cell)" })
        public double heatSpeedPerCell = 0.01;
        @Configurable
        @Configurable.Comment({ "The steam per tick per cell production rate of the Solar Boiler Array in mB" })
        public int solarSpeed = 90;
        @Configurable
        @Configurable.Comment({ "The amount of Steam produced per 1mB of Water. Default is 160." })
        public double steamRatio = 160.0;
        @Configurable
        @Configurable.Comment({ "The temperature at which the Solar Boiler Array starts producing steam." })
        public int boilingPoint = 50;
        @Configurable
        @Configurable.Comment({ "The base temperature in Kelvin gained per second when sunlit." })
        public int baseHeatRate = 2;
        @Configurable
        @Configurable.Comment({ "The multiplier applied to the Moon dimension" })
        public double moonMultiplier = 1.2;
        @Configurable
        @Configurable.Comment({ "The multiplier applied to the Venus dimension" })
        public double venusMultiplier = 0.5;
        @Configurable
        @Configurable.Comment({ "The multiplier applied to the Mercury dimension" })
        public double mercuryMultiplier = 2.5;
        @Configurable
        @Configurable.Comment({ "The multiplier applied to the Mars dimension" })
        public double marsMultiplier = 0.8;
        @Configurable
        @Configurable.Comment({ "The multiplier applied to the Ceres dimension" })
        public double ceresMultiplier = 0.7;
        @Configurable
        @Configurable.Comment({ "The multiplier applied to the Jupiter dimension" })
        public double jupiterMultiplier = 0.25;
        @Configurable
        @Configurable.Comment({ "The multiplier applied to the Saturn dimension" })
        public double saturnMultiplier = 0.2;
        @Configurable
        @Configurable.Comment({ "The multiplier applied to the Uranus dimension" })
        public double uranusMultiplier = 0.15;
        @Configurable
        @Configurable.Comment({ "The multiplier applied to the Neptune dimension" })
        public double neptuneMultiplier = 0.1;
        @Configurable
        @Configurable.Comment({ "The multiplier applied to the Pluto dimension" })
        public double plutoMultiplier = 0.05;
        @Configurable
        @Configurable.Comment({ "The multiplier applied to the Kuiper Belt dimension" })
        public double kuiperBeltMultiplier = 0.02;
    }
}
