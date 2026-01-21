package com.astro.core.common.machine.hatches;

import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.machine.property.GTMachineModelProperties;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.common.machine.multiblock.part.ParallelHatchPartMachine;

import net.minecraft.network.chat.Component;

import com.astro.core.AstroCore;

import static com.astro.core.common.AstroMachineUtils.registerTieredMachines;
import static com.astro.core.common.registry.AstroRegistry.REGISTRATE;
import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.machine.property.GTMachineModelProperties.IS_FORMED;
import static com.gregtechceu.gtceu.common.data.models.GTMachineModels.createWorkableTieredHullMachineModel;

@SuppressWarnings("all")
public class AstroHatches {

    public static final MachineDefinition[] PARALLEL_HATCH = registerTieredMachines("parallel_hatch",
            ParallelHatchPartMachine::new,
            (tier, builder) -> builder
                    .rotationState(RotationState.ALL)
                    .abilities(PartAbility.PARALLEL_HATCH)
                    .modelProperty(IS_FORMED, false)
                    .modelProperty(GTMachineModelProperties.RECIPE_LOGIC_STATUS, RecipeLogic.Status.IDLE)
                    .model(createWorkableTieredHullMachineModel(
                            AstroCore.id("block/machines/hatches/parallel_hatch_mk" + (tier - 4)))
                            .andThen((ctx, prov, model) -> {
                                model.addReplaceableTextures("bottom", "top", "side");
                            }))
                    .tooltips(Component.translatable("astrogreg.machine.parallel_hatch_mk" + tier + ".tooltip"),
                            Component.translatable("gtceu.part_sharing.disabled"))
                    .register(),
            UHV);

    public static final MachineDefinition WATER_HATCH = REGISTRATE
            .machine("water_input_hatch", AstroWaterHatch::new)
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.IMPORT_FLUIDS)
            .overlaySteamHullModel("water_hatch")
            .modelProperty(IS_FORMED, false)
            .tooltips(Component.translatable("gtceu.universal.tooltip.fluid_storage_capacity",
                    AstroWaterHatch.INITIAL_TANK_CAPACITY),
                    Component.translatable("astrogreg.machine.water_hatch.tooltip"))
            .allowCoverOnFront(true)
            .register();

    public static void init() {}
}
