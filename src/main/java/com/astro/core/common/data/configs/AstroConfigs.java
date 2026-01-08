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
        @Configurable.Comment({ "The steam per tick per cell production rate of the Solar Boiler Array" })
        public int solarSpeed = 90;

        @Configurable
        @Configurable.Comment({ "The amount of Steam produced per 1mB of Water. Default is 160." })
        public double waterToSteamRatio = 160.0;

        @Configurable
        @Configurable.Comment({ "The temperature at which the boiler starts producing steam." })
        public int boilingPoint = 50;

        @Configurable
        @Configurable.Comment({ "Base heat gain per second when sunlit." })
        public int baseHeatRate = 2;
        @Configurable public double moonBoost = 1.2;
        @Configurable public double venusPenalty = 0.5;
        @Configurable public double mercuryBoost = 2.5;
        @Configurable public double marsPenalty = 0.8;
        @Configurable public double glacioPenalty = 0.3;

        // Ad Extendra / Addons
        @Configurable public double ceresPenalty = 0.7;
        @Configurable public double jupiterPenalty = 0.25;
        @Configurable public double saturnPenalty = 0.2;
        @Configurable public double uranusPenalty = 0.15;
        @Configurable public double neptunePenalty = 0.1;
        @Configurable public double plutoPenalty = 0.05;
        @Configurable public double kuiperBeltPenalty = 0.02;

    }
}
