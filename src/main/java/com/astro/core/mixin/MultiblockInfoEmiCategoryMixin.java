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
            GTCEu.id("mv_bedrock_ore_miner"),
            GTCEu.id("hv_bedrock_ore_miner"),
            GTCEu.id("ev_bedrock_ore_miner"),
            GTCEu.id("steam_grinder"));

    @Inject(method = "registerDisplays", at = @At(value = "HEAD"), remap = false, cancellable = true)
    private static void setAstro$excludedMultis$registerDisplays(EmiRegistry registry, CallbackInfo ci) {
        GTRegistries.MACHINES.values().stream()
                .filter(MultiblockMachineDefinition.class::isInstance)
                .map(MultiblockMachineDefinition.class::cast)
                .filter(MultiblockMachineDefinition::isRenderXEIPreview)
                .map(MultiblockInfoEmiRecipe::new)
                .filter(multi -> !astro$excludedMultis.contains(multi.getId()))
                .forEach(registry::addRecipe);

        ci.cancel();
    }
}
