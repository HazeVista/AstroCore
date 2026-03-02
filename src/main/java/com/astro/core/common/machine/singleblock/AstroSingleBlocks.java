package com.astro.core.common.machine.singleblock;

import com.astro.core.AstroCore;

import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.property.GTMachineModelProperties;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;

import net.minecraft.network.chat.Component;

import static com.astro.core.common.AstroMachineUtils.registerTieredMachines;
import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.machine.property.GTMachineModelProperties.IS_FORMED;
import static com.gregtechceu.gtceu.common.data.models.GTMachineModels.createWorkableTieredHullMachineModel;

@SuppressWarnings("all")
public class AstroSingleBlocks {

    private static final String[] TIER_COLOR = { "§8", "§7", "§b", "§6", "§5", "§9", "§d", "§c", "§3", "§4" };

    private static String tieredName(int tier) {
        return TIER_COLOR[tier] + VN[tier] + " Research Computer§r";
    }

    public static final MachineDefinition[] CWU_GENERATOR = registerTieredMachines(
            "cwu_generator",
            (holder, tier) -> new CWUGeneratorMachine(holder, tier),
            (tier, builder) -> builder
                    .langValue(tieredName(tier))
                    .tooltips(
                            Component.translatable("gtceu.universal.tooltip.voltage_in",
                                    CWUGeneratorMachine.getEUtForTier(tier), VNF[tier]),
                            Component.translatable("astrogreg.machine.cwu_generator.tooltip.1",
                                    CWUGeneratorMachine.getCWUForTier(tier)),
                            Component.translatable("astrogreg.machine.cwu_generator.tooltip.2",
                                    CWUGeneratorMachine.getLubricantForTier(tier)),
                            Component.translatable("astrogreg.machine.cwu_generator.tooltip.3",
                                    CWUGeneratorMachine.getTankCapacityForTier(tier)))
                    .rotationState(RotationState.NON_Y_AXIS)
                    .modelProperty(GTMachineModelProperties.RECIPE_LOGIC_STATUS, RecipeLogic.Status.IDLE)
                    .model(createWorkableTieredHullMachineModel(
                            AstroCore.id("block/machines/cwu_generator"))
                            .andThen((ctx, prov, model) -> {
                                model.addReplaceableTextures("bottom", "top", "side");
                            }))
                    .register(),
            MV, HV, EV);

    public static void init() {}
}