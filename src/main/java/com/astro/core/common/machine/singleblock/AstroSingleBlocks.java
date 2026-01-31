package com.astro.core.common.machine.singleblock;

import static com.gregtechceu.gtceu.api.GTValues.*;

@SuppressWarnings("all")
public class AstroSingleBlocks {

    // public static final MachineDefinition[] CWU_GENERATOR = registerTieredMachines(
    // "cwu_generator",
    // (holder, tier) -> new CWUGeneratorMachine(holder, tier),
    // (tier, builder) -> builder
    // .langValue(VN[tier] + " Research Computer")
    // .tooltips(
    // Component.translatable("gtceu.universal.tooltip.voltage_in",
    // CWUGeneratorMachine.getEUtForTier(tier), VNF[tier]),
    // Component.translatable("astrogreg.machine.cwu_generator.tooltip.0"),
    // Component.translatable("astrogreg.machine.cwu_generator.tooltip.1",
    // CWUGeneratorMachine.getCWUForTier(tier)),
    // Component.translatable("astrogreg.machine.cwu_generator.tooltip.2",
    // CWUGeneratorMachine.getLubricantForTier(tier)))
    // .modelProperty(IS_FORMED, false)
    // .rotationState(RotationState.NON_Y_AXIS)
    // .modelProperty(GTMachineModelProperties.RECIPE_LOGIC_STATUS, RecipeLogic.Status.IDLE)
    // .model(createWorkableTieredHullMachineModel(
    // AstroCore.id("block/machines/cwu_generator"))
    // .andThen((ctx, prov, model) -> {
    // model.addReplaceableTextures("bottom", "top", "side");
    // }))
    // .register(),
    // MV, HV, EV);

    public static void init() {}
}
