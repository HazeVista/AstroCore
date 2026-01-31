package com.astro.core.common.machine.hatches;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.machine.property.GTMachineModelProperties;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.common.machine.multiblock.part.ParallelHatchPartMachine;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.network.chat.Component;

import com.astro.core.AstroCore;
import com.astro.core.common.machine.trait.AstroPartAbility;

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

    public static final MachineDefinition[] MANA_INPUT_HATCH = registerTieredMachines("mana_input_hatch",
            (holder, tier) -> new AstroManaHatches(holder, tier),
            (tier, builder) -> {
                long capacity = AstroManaHatches.INITIAL_TANK_CAPACITY * (1L << (tier - 1));
                return builder
                        .langValue(VNF[tier] + " Arcane Sink Hatch")
                        .rotationState(RotationState.ALL)
                        .abilities(AstroPartAbility.IMPORT_EXOTIC_MATTER)
                        .modelProperty(IS_FORMED, false)
                        .colorOverlayTieredHullModel(
                                AstroCore.id("block/machines/hatches/mana_input_hatch/overlay_mana_hatch_input"),
                                null,
                                AstroCore.id(
                                        "block/machines/hatches/mana_input_hatch/overlay_mana_hatch_input_emissive"))
                        .tooltips(Component.translatable("astrogreg.machine.mana_input_hatch.tooltip"),
                                Component.translatable("gtceu.universal.tooltip.fluid_storage_capacity",
                                        FormattingUtil.formatNumbers(capacity)))
                        .allowCoverOnFront(true)
                        .register();
            },
            LV, MV, HV, EV, IV, LuV, ZPM, UV, UHV);

    public static final MachineDefinition[] MANA_OUTPUT_HATCH = registerTieredMachines("mana_output_hatch",
            (holder, tier) -> new AstroManaHatches(holder, tier, IO.OUT),
            (tier, builder) -> {
                long capacity = AstroManaHatches.INITIAL_TANK_CAPACITY * (1L << (tier - 1));
                return builder
                        .langValue(VNF[tier] + " Arcane Source Hatch")
                        .rotationState(RotationState.ALL)
                        .abilities(AstroPartAbility.EXPORT_EXOTIC_MATTER)
                        .modelProperty(IS_FORMED, false)
                        .colorOverlayTieredHullModel(
                                AstroCore.id("block/machines/hatches/mana_output_hatch/overlay_mana_hatch_output"),
                                null,
                                AstroCore.id(
                                        "block/machines/hatches/mana_output_hatch/overlay_mana_hatch_output_emissive"))
                        .tooltips(Component.translatable("astrogreg.machine.mana_output_hatch.tooltip"),
                                Component.translatable("gtceu.universal.tooltip.fluid_storage_capacity",
                                        FormattingUtil.formatNumbers(capacity * 2)))
                        .allowCoverOnFront(true)
                        .register();
            },
            LV, MV, HV, EV, IV, LuV, ZPM, UV, UHV);

    // public static final MachineDefinition[] CWU_INPUT_HATCH = AstroMachineUtils.registerTieredMachines(
    // "cwu_input_hatch",
    // CWUInputHatch::new,
    // (tier, builder) -> builder
    // .langValue("Simple Data Reception Hatch")
    // .abilities(AstroPartAbility.IMPORT_CWU)
    // .tooltips(Component.translatable("astrogreg.machine.cwu_input_hatch.tooltip"),
    // Component.translatable("gtceu.part_sharing.disabled"))
    // .rotationState(RotationState.ALL)
    // .modelProperty(IS_FORMED, false)
    // .overlayTieredHullModel("optical_data_hatch")
    // .register(),
    // HV);

    public static void init() {}
}
