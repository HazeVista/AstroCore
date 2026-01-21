package com.astro.core.common.data.machine;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import com.gregtechceu.gtceu.api.registry.registrate.MachineBuilder;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import it.unimi.dsi.fastutil.Pair;

import java.util.Locale;
import java.util.function.BiFunction;

import static com.astro.core.common.registry.AstroRegistry.REGISTRATE;

public class AstroMachineUtils {

    /**
     * Register a pair of steam machines (low pressure and high pressure)
     * in the AstroCore namespace with proper datagen support.
     *
     * @param name The base name for the machine (e.g., "steam_centrifuge")
     * @param factory Factory function to create the machine instance
     * @param builder Builder function to configure the machine
     * @return Pair of low pressure and high pressure machine definitions
     */
    public static Pair<MachineDefinition, MachineDefinition> registerSteamMachines(
            String name,
            BiFunction<IMachineBlockEntity, Boolean, MetaMachine> factory,
            BiFunction<Boolean, MachineBuilder<MachineDefinition>, MachineDefinition> builder) {
        return registerSteamMachines(REGISTRATE, name, factory, builder);
    }

    public static Pair<MachineDefinition, MachineDefinition> registerSteamMachines(
            GTRegistrate registrate,
            String name,
            BiFunction<IMachineBlockEntity, Boolean, MetaMachine> factory,
            BiFunction<Boolean, MachineBuilder<MachineDefinition>, MachineDefinition> builder) {

        MachineDefinition lowTier = builder.apply(false,
                registrate.machine("lp_%s".formatted(name), holder -> factory.apply(holder, false))
                        .langValue("Low Pressure " + FormattingUtil.toEnglishName(name))
                        .tier(0));

        MachineDefinition highTier = builder.apply(true,
                registrate.machine("hp_%s".formatted(name), holder -> factory.apply(holder, true))
                        .langValue("High Pressure " + FormattingUtil.toEnglishName(name))
                        .tier(1));

        return Pair.of(lowTier, highTier);
    }

    public static MachineDefinition[] registerTieredMachines(
            String name,
            BiFunction<IMachineBlockEntity, Integer, MetaMachine> factory,
            BiFunction<Integer, MachineBuilder<MachineDefinition>, MachineDefinition> builder,
            int... tiers) {
        return registerTieredMachines(REGISTRATE, name, factory, builder, tiers);
    }

    public static MachineDefinition[] registerTieredMachines(
            GTRegistrate registrate,
            String name,
            BiFunction<IMachineBlockEntity, Integer, MetaMachine> factory,
            BiFunction<Integer, MachineBuilder<MachineDefinition>, MachineDefinition> builder,
            int... tiers) {

        MachineDefinition[] definitions = new MachineDefinition[GTValues.TIER_COUNT];

        for (int tier : tiers) {
            var register = registrate
                    .machine(GTValues.VN[tier].toLowerCase(Locale.ROOT) + "_" + name,
                            holder -> factory.apply(holder, tier))
                    .tier(tier);
            definitions[tier] = builder.apply(tier, register);
        }

        return definitions;
    }

    public static void init() {
    }
}