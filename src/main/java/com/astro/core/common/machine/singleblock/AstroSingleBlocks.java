package com.astro.core.common.machine.singleblock;

import com.astro.core.AstroCore;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.property.GTMachineModelProperties;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import net.minecraft.network.chat.Component;

import static com.astro.core.common.data.machine.AstroMachineUtils.registerTieredMachines;
import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.GTValues.VNF;
import static com.gregtechceu.gtceu.api.machine.property.GTMachineModelProperties.IS_FORMED;
import static com.gregtechceu.gtceu.common.data.models.GTMachineModels.createWorkableTieredHullMachineModel;

@SuppressWarnings("all")
public class AstroSingleBlocks {

//    public static final MachineDefinition[] CWU_GENERATOR = registerTieredMachines(
//            "cwu_generator",
//            (holder, tier) -> new CWUGeneratorMachine(holder, tier),
//            (tier, builder) -> builder
//                    .langValue(VN[tier] + " Research Computer")
//                    .tooltips(
//                            Component.translatable("gtceu.universal.tooltip.voltage_in",
//                                    CWUGeneratorMachine.getEUtForTier(tier), VNF[tier]),
//                            Component.translatable("astrogreg.machine.cwu_generator.tooltip.0"),
//                            Component.translatable("astrogreg.machine.cwu_generator.tooltip.1",
//                                    CWUGeneratorMachine.getCWUForTier(tier)),
//                            Component.translatable("astrogreg.machine.cwu_generator.tooltip.2",
//                                    CWUGeneratorMachine.getLubricantForTier(tier)))
//                    .modelProperty(IS_FORMED, false)
//                    .rotationState(RotationState.NON_Y_AXIS)
//                    .modelProperty(GTMachineModelProperties.RECIPE_LOGIC_STATUS, RecipeLogic.Status.IDLE)
//                    .model(createWorkableTieredHullMachineModel(
//                            AstroCore.id("block/machines/cwu_generator"))
//                            .andThen((ctx, prov, model) -> {
//                                model.addReplaceableTextures("bottom", "top", "side");
//                            }))
//                    .register(),
//            MV, HV, EV);

    public static void init() {
    }

}
