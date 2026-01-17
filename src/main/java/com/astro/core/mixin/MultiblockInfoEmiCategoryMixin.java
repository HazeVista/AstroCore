package com.astro.core.mixin;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.integration.emi.multipage.MultiblockInfoEmiCategory;
import com.gregtechceu.gtceu.integration.emi.multipage.MultiblockInfoEmiRecipe;

import net.minecraft.resources.ResourceLocation;

import dev.emi.emi.api.EmiRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = MultiblockInfoEmiCategory.class)
public class MultiblockInfoEmiCategoryMixin {

    @Unique
    private final static List<ResourceLocation> astro$excludedMultis = List.of(
            GTCEu.id("coke_oven"),
            GTCEu.id("bedrock_miner"),
            GTCEu.id("advanced_bedrock_miner"));

    @Inject(method = "registerDisplays", at = @At(value = "HEAD"), remap = false, cancellable = true)
    private static void astro$registerDisplays(EmiRegistry registry, CallbackInfo ci) {
        GTRegistries.MACHINES.values().stream()
                .filter(MultiblockMachineDefinition.class::isInstance)
                .map(MultiblockMachineDefinition.class::cast)
                .filter(MultiblockMachineDefinition::isRenderXEIPreview)
                // NEW: Filter by the Definition ID before creating the EMI recipe object
                .filter(definition -> !astro$excludedMultis.contains(definition.getId()))
                .map(MultiblockInfoEmiRecipe::new)
                .forEach(registry::addRecipe);

        ci.cancel();
    }
}
