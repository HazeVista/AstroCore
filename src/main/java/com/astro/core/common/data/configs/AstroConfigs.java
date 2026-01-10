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
        @Configurable.Comment({ "The max temperature of the Large Manasteel Mana Boiler. Default:2000" })
        public int manasteelBoilerMaxTemperature = 2000;
        @Configurable
        @Configurable.Comment({ "The heat speed of the Large Manasteel Mana Boiler. Default:1" })
        public int manasteelBoilerHeatSpeed = 1;
        @Configurable
        @Configurable.Comment({ "The max temperature of the Large Terrasteel Mana Boiler. Default:3000" })
        public int terrasteelBoilerMaxTemperature = 3000;
        @Configurable
        @Configurable.Comment({ "The heat speed of the Large Terrasteel Mana Boiler. Default:2" })
        public int terrasteelBoilerHeatSpeed = 2;
        @Configurable
        @Configurable.Comment({ "The max temperature of the Large Alfsteel Mana Boiler. Default:4000" })
        public int alfsteelBoilerMaxTemperature = 4000;
        @Configurable
        @Configurable.Comment({ "The heat speed of the Large Alfsteel Mana Boiler. Default:3" })
        public int alfsteelBoilerHeatSpeed = 3;

        // Solar Boiler Array
        @Configurable
        @Configurable.Comment({ "The base heat capacity (seconds per degree drop) when the boiler is smaller than the capacity point. Default: 1" })
        public int solarBaseCapacity = 1;
        @Configurable
        @Configurable.Comment({ "The amount of solar cells required to increase the heat capacity by 1 second. Default: 40" })
        public int cellsPerCapacityPoint = 40;
        @Configurable
        @Configurable.Comment({ "The multiplier added to heat speed for every sunlit cell. (e.g. 0.01 = 1% faster per cell) Default: 0.01" })
        public double heatSpeedPerCell = 0.01;
        @Configurable
        @Configurable.Comment({ "The base steam per tick per cell production rate of the Solar Boiler Array in mB. Default: 1" })
        public int solarSpeed = 1;
        @Configurable
        @Configurable.Comment({ "The amount of Steam produced per 1mB of Water. Default: 1.0" })
        public double steamRatio = 1.0;
        @Configurable
        @Configurable.Comment({ "The temperature at which the Solar Boiler Array starts producing steam. Default: 50" })
        public int boilingPoint = 50;
        @Configurable
        @Configurable.Comment({ "The base temperature in Kelvin gained per second when sunlit. Default: 2" })
        public int baseHeatRate = 2;
        @Configurable
        @Configurable.Comment({ "The multiplier applied to the Moon dimension. Default: 1.2" })
        public double moonMultiplier = 1.2;
        @Configurable
        @Configurable.Comment({ "The multiplier applied to the Venus dimension. Default: 0.5" })
        public double venusMultiplier = 0.5;
        @Configurable
        @Configurable.Comment({ "The multiplier applied to the Mercury dimension. Default: 2.5" })
        public double mercuryMultiplier = 2.5;
        @Configurable
        @Configurable.Comment({ "The multiplier applied to the Mars dimension. Default: 0.8" })
        public double marsMultiplier = 0.8;
        @Configurable
        @Configurable.Comment({ "The multiplier applied to the Ceres dimension. Default: 0.7" })
        public double ceresMultiplier = 0.7;
        @Configurable
        @Configurable.Comment({ "The multiplier applied to the Jupiter dimension. Default: 0.5" })
        public double jupiterMultiplier = 0.5;
        @Configurable
        @Configurable.Comment({ "The multiplier applied to the Saturn dimension. Default: 0.4" })
        public double saturnMultiplier = 0.4;
        @Configurable
        @Configurable.Comment({ "The multiplier applied to the Uranus dimension. Default: 0.25" })
        public double uranusMultiplier = 0.25;
        @Configurable
        @Configurable.Comment({ "The multiplier applied to the Neptune dimension. Default: 0.2" })
        public double neptuneMultiplier = 0.2;
        @Configurable
        @Configurable.Comment({ "The multiplier applied to the Pluto dimension. Default: 0.15" })
        public double plutoMultiplier = 0.15;
        @Configurable
        @Configurable.Comment({ "The multiplier applied to the Kuiper Belt dimension. Default: 0.1" })
        public double kuiperBeltMultiplier = 0.1;

        // Other Steam Configs
        @Configurable
        @Configurable.Comment({
                "The base speed multiplier for the Steam Blast Furnace. (works inversely, e.g. 0.5 = 2x speed) Default: 1.0" })
        public double SBFRecipeSpeed = 1.0;
        @Configurable
        @Configurable.Comment({
                "The base steam consumed per tick by custom AstroGreg steam machines. Default: 1.0" })
        public double steamConsumption = 1.0;
    }
}
