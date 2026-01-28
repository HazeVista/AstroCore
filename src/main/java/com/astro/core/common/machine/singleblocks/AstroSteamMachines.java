package com.astro.core.common.machine.singleblocks;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.property.GTMachineModelProperties;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import com.astro.core.common.machine.trait.AstroSimpleSteamMachine;
import it.unimi.dsi.fastutil.Pair;

import static com.astro.core.common.data.machine.AstroMachineUtils.registerSteamMachines;

public class AstroSteamMachines {

    public static final Pair<MachineDefinition, MachineDefinition> STEAM_CENTRIFUGE = registerSteamMachines(
            "steam_centrifuge",
            AstroSimpleSteamMachine::new,
            (pressure, builder) -> builder
                    .addOutputLimit(ItemRecipeCapability.CAP, 1)
                    .rotationState(RotationState.NON_Y_AXIS)
                    .recipeType(GTRecipeTypes.CENTRIFUGE_RECIPES)
                    .recipeModifier(AstroSimpleSteamMachine::recipeModifier)
                    .modelProperty(GTMachineModelProperties.VENT_DIRECTION, RelativeDirection.BACK)
                    .workableSteamHullModel(pressure, GTCEu.id("block/machines/centrifuge"))
                    .register());

    public static final Pair<MachineDefinition, MachineDefinition> STEAM_MIXER = registerSteamMachines(
            "steam_mixer",
            AstroSimpleSteamMachine::new,
            (pressure, builder) -> builder
                    .addOutputLimit(ItemRecipeCapability.CAP, 1)
                    .rotationState(RotationState.NON_Y_AXIS)
                    .recipeType(GTRecipeTypes.MIXER_RECIPES)
                    .recipeModifier(AstroSimpleSteamMachine::recipeModifier)
                    .modelProperty(GTMachineModelProperties.VENT_DIRECTION, RelativeDirection.BACK)
                    .workableSteamHullModel(pressure, GTCEu.id("block/machines/mixer"))
                    .register());

    public static void init() {}
}
