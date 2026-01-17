package com.astro.core.common.machine.integration;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;

import net.minecraft.network.chat.Component;

import com.astro.core.AstroCore;
import com.astro.core.common.data.configs.AstroConfigs;
import com.astro.core.integration.ae2.METagInputBusPartMachine;
import com.astro.core.integration.ae2.METagInputHatchPartMachine;
import com.astro.core.integration.ae2.machine.ExpandedPatternBufferPartMachine;
import com.astro.core.integration.ae2.machine.ExpandedPatternBufferProxyPartMachine;

import static com.astro.core.common.registry.AstroRegistry.REGISTRATE;
import static com.gregtechceu.gtceu.api.GTValues.UHV;
import static com.gregtechceu.gtceu.api.GTValues.ZPM;

public class AstroAEMachines {

    public static MachineDefinition EXPANDED_ME_PATTERN_BUFFER = null;
    public static MachineDefinition EXPANDED_ME_PATTERN_BUFFER_PROXY = null;
    public static MachineDefinition ME_TAG_INPUT_BUS = null;
    public static MachineDefinition ME_TAG_INPUT_HATCH = null;

    static {
        if (AstroConfigs.INSTANCE.Steam.aeMachines && GTCEu.Mods.isAE2Loaded() || GTCEu.isDataGen()) {
            EXPANDED_ME_PATTERN_BUFFER = REGISTRATE
                    .machine("expanded_me_pattern_buffer", ExpandedPatternBufferPartMachine::new)
                    .tier(ZPM)
                    .rotationState(RotationState.ALL)
                    .abilities(PartAbility.IMPORT_ITEMS, PartAbility.IMPORT_FLUIDS, PartAbility.EXPORT_FLUIDS,
                            PartAbility.EXPORT_ITEMS)
                    .colorOverlayTieredHullModel(GTCEu.id("block/overlay/appeng/me_buffer_hatch"))
                    .langValue("Expanded ME Pattern Buffer")
                    .tooltips(
                            Component.literal(
                                    "§fAllows expanded direct §6AE2 pattern storage §ffor GregTech Multiblocks."),
                            Component.translatable("block.gtceu.pattern_buffer.desc.1"),
                            Component.literal(
                                    "§fLink §6Expanded Pattern Buffer Proxies §fwith a §bdatastick §fto link machines together!"),
                            Component.translatable("gtceu.part_sharing.enabled"))
                    .register();

            EXPANDED_ME_PATTERN_BUFFER_PROXY = REGISTRATE
                    .machine("expanded_me_pattern_buffer_proxy", ExpandedPatternBufferProxyPartMachine::new)
                    .tier(ZPM)
                    .rotationState(RotationState.ALL)
                    .abilities(PartAbility.IMPORT_ITEMS, PartAbility.IMPORT_FLUIDS, PartAbility.EXPORT_FLUIDS,
                            PartAbility.EXPORT_ITEMS)
                    .colorOverlayTieredHullModel(GTCEu.id("block/overlay/appeng/me_buffer_hatch_proxy"))
                    .langValue("Expanded ME Pattern Buffer Proxy")
                    .tooltips(
                            Component.literal(
                                    "§fAllows linking many machines to a singular §6Expanded ME Pattern Buffer§f."),
                            Component.translatable("block.gtceu.pattern_buffer_proxy.desc.2"),
                            Component.translatable("gtceu.part_sharing.enabled"))
                    .register();
            ME_TAG_INPUT_BUS = REGISTRATE
                    .machine("me_tag_input_bus", METagInputBusPartMachine::new)
                    .tier(UHV)
                    .rotationState(RotationState.ALL)
                    .abilities(
                            PartAbility.IMPORT_ITEMS,
                            PartAbility.EXPORT_ITEMS)
                    .colorOverlayTieredHullModel(AstroCore.id("block/machines/tag_inputs/me_tag_input_bus"))
                    .langValue("ME Tag Input Bus")
                    .tooltips(
                            Component.literal("§6Tag‑based AE2 Item Import"),
                            Component.literal("§7Matches items via tag expressions"),
                            Component.translatable("gtceu.part_sharing.enabled"))
                    .register();

            ME_TAG_INPUT_HATCH = REGISTRATE
                    .machine("me_tag_input_hatch", METagInputHatchPartMachine::new)
                    .tier(UHV)
                    .rotationState(RotationState.ALL)
                    .abilities(
                            PartAbility.IMPORT_FLUIDS,
                            PartAbility.EXPORT_FLUIDS)
                    .colorOverlayTieredHullModel(AstroCore.id("block/machines/tag_inputs/me_tag_input_hatch"))
                    .langValue("ME Tag Input Hatch")
                    .tooltips(
                            Component.literal("§6Tag‑based AE2 Fluid Import"),
                            Component.literal("§7Matches fluids via tag expressions"),
                            Component.translatable("gtceu.part_sharing.enabled"))
                    .register();
        }
    }

    public static void init() {}
}
