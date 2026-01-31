package com.astro.core.common.machine.multiblock.generator;

import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.machine.property.GTMachineModelProperties;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import com.gregtechceu.gtceu.common.data.GTMaterialItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.machine.multiblock.steam.LargeBoilerMachine;
import com.gregtechceu.gtceu.config.ConfigHolder;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import com.astro.core.AstroCore;
import com.astro.core.client.renderer.machine.AstroBoilerMultiPartRender;
import com.astro.core.common.data.AstroBlocks;
import com.astro.core.common.data.AstroRecipeTypes;
import com.astro.core.common.registry.AstroRegistry;

import java.util.function.Supplier;

import static com.gregtechceu.gtceu.api.pattern.Predicates.blocks;
import static com.gregtechceu.gtceu.common.data.models.GTMachineModels.createWorkableCasingMachineModel;

public class AstroBoilers {

    public static MultiblockMachineDefinition registerAstroBoiler(String id, String lang,
                                                                  Supplier<? extends Block> casing,
                                                                  Supplier<? extends Block> pipe,
                                                                  Supplier<? extends Block> fireBox,
                                                                  ResourceLocation texture,
                                                                  AstroBlocks.FireboxInfo fireboxInfo,
                                                                  int maxTemperature, int heatSpeed) {
        return registerAstroBoiler(AstroRegistry.REGISTRATE, id, lang, casing, pipe, fireBox, texture, fireboxInfo,
                maxTemperature, heatSpeed);
    }

    public static MultiblockMachineDefinition registerAstroBoiler(GTRegistrate registrate, String id, String lang,
                                                                  Supplier<? extends Block> casing,
                                                                  Supplier<? extends Block> pipe,
                                                                  Supplier<? extends Block> fireBox,
                                                                  ResourceLocation texture,
                                                                  AstroBlocks.FireboxInfo fireboxInfo,
                                                                  int maxTemperature, int heatSpeed) {
        return registrate
                .multiblock("%s_large_boiler".formatted(id),
                        holder -> new LargeBoilerMachine(holder, maxTemperature, heatSpeed))
                .langValue(lang)
                .allowExtendedFacing(false)
                .rotationState(RotationState.NON_Y_AXIS)
                .recipeType(AstroRecipeTypes.MANA_BOILER_RECIPES)
                .recipeModifier(LargeBoilerMachine::recipeModifier, true)
                .appearanceBlock(casing)
                .partAppearance((controller, part,
                                 side) -> controller.self().getPos().below().getY() == part.self().getPos().getY() ?
                                         fireBox.get().defaultBlockState() : casing.get().defaultBlockState())
                .pattern((definition) -> {
                    TraceabilityPredicate fireboxPred = blocks(fireBox.get()).setMinGlobalLimited(3)
                            .or(Predicates.abilities(PartAbility.IMPORT_FLUIDS).setMinGlobalLimited(1)
                                    .setPreviewCount(1))
                            .or(Predicates.abilities(PartAbility.IMPORT_ITEMS).setMaxGlobalLimited(1)
                                    .setPreviewCount(1))
                            .or(Predicates.abilities(PartAbility.MUFFLER).setExactLimit(1));

                    if (ConfigHolder.INSTANCE.machines.enableMaintenance) {
                        fireboxPred = fireboxPred.or(Predicates.abilities(PartAbility.MAINTENANCE).setExactLimit(1));
                    }

                    return FactoryBlockPattern.start()
                            .aisle("XXX", "CCC", "CCC", "CCC")
                            .aisle("XXX", "CPC", "CPC", "CCC")
                            .aisle("XXX", "CSC", "CCC", "CCC")
                            .where('S', Predicates.controller(blocks(definition.getBlock())))
                            .where('P', blocks(pipe.get()))
                            .where('X', fireboxPred)
                            .where('C', blocks(casing.get()).setMinGlobalLimited(20)
                                    .or(Predicates.abilities(PartAbility.EXPORT_FLUIDS).setMinGlobalLimited(1)
                                            .setPreviewCount(1)))
                            .build();
                })
                .recoveryItems(
                        () -> new ItemLike[] {
                                GTMaterialItems.MATERIAL_ITEMS.get(TagPrefix.dustTiny, GTMaterials.Ash).get() })
                .modelProperty(GTMachineModelProperties.RECIPE_LOGIC_STATUS, RecipeLogic.Status.IDLE)
                .model(createWorkableCasingMachineModel(texture,
                        AstroCore.id("block/multiblock/large_%s_boiler".formatted(id)))
                        .andThen(b -> b.addDynamicRenderer(() -> new AstroBoilerMultiPartRender(fireBox, casing))))
                .tooltips(
                        Component.translatable("gtceu.multiblock.large_boiler.max_temperature", maxTemperature + 274,
                                maxTemperature),
                        Component.translatable("gtceu.multiblock.large_boiler.heat_time_tooltip",
                                maxTemperature / heatSpeed / 20),
                        Component.translatable("gtceu.multiblock.large_boiler.explosion_tooltip")
                                .withStyle(ChatFormatting.DARK_RED))
                .register();
    }
}
