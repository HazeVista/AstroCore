package com.astro.core.common.machine.multiblock.generator;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IRotorHolderMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate;
import com.gregtechceu.gtceu.api.pattern.predicates.SimplePredicate;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import com.gregtechceu.gtceu.common.data.GTMaterialItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.common.machine.multiblock.generator.LargeTurbineMachine;

import com.lowdragmc.lowdraglib.utils.BlockInfo;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fluids.FluidType;

import java.util.function.Supplier;
import java.util.stream.IntStream;

import static com.astro.core.common.registry.AstroRegistry.REGISTRATE;
import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.pattern.Predicates.*;

@SuppressWarnings("all")
public class AstroGeneratorRegistry {

    public static MultiblockMachineDefinition registerAstroTurbine(String id, String lang, int tier,
                                                                   GTRecipeType recipeType,
                                                                   Supplier<? extends Block> casing,
                                                                   Supplier<? extends Block> gear,
                                                                   ResourceLocation casingTexture,
                                                                   ResourceLocation overlayModel) {
        return registerAstroTurbine(REGISTRATE, id, lang, tier, recipeType, casing, gear, casingTexture,
                overlayModel);
    }

    public static MultiblockMachineDefinition registerAstroTurbine(GTRegistrate registrate,
                                                                   String id, String lang, int tier,
                                                                   GTRecipeType recipeType,
                                                                   Supplier<? extends Block> casing,
                                                                   Supplier<? extends Block> gear,
                                                                   ResourceLocation casingTexture,
                                                                   ResourceLocation overlayModel) {
        return registerAstroTurbine(registrate, id, lang, tier, recipeType, casing, gear, casingTexture, overlayModel,
                true);
    }

    public static MultiblockMachineDefinition registerAstroTurbine(String id, String lang, int tier,
                                                                   GTRecipeType recipeType,
                                                                   Supplier<? extends Block> casing,
                                                                   Supplier<? extends Block> gear,
                                                                   ResourceLocation casingTexture,
                                                                   ResourceLocation overlayModel,
                                                                   boolean needsMuffler) {
        return registerAstroTurbine(REGISTRATE, id, lang, tier, recipeType, casing, gear, casingTexture,
                overlayModel,
                needsMuffler);
    }

    public static MultiblockMachineDefinition registerAstroTurbine(GTRegistrate registrate,
                                                                   String id, String lang, int tier,
                                                                   GTRecipeType recipeType,
                                                                   Supplier<? extends Block> casing,
                                                                   Supplier<? extends Block> gear,
                                                                   ResourceLocation casingTexture,
                                                                   ResourceLocation overlayModel,
                                                                   boolean needsMuffler) {
        return registrate.multiblock(id, holder -> new LargeTurbineMachine(holder, tier))
                .langValue(lang)
                .rotationState(RotationState.ALL)
                .recipeType(recipeType)
                .generator(true)
                .recipeModifier(LargeTurbineMachine::recipeModifier, true)
                .appearanceBlock(casing)
                .pattern(definition -> FactoryBlockPattern.start()
                        .aisle("CCCC", "CHHC", "CCCC")
                        .aisle("CHHC", "RGGR", "CHHC")
                        .aisle("CCCC", "CSHC", "CCCC")
                        .where('S', controller(blocks(definition.getBlock())))
                        .where('G', blocks(gear.get()))
                        .where('C', blocks(casing.get()))
                        .where('R',
                                new TraceabilityPredicate(
                                        new SimplePredicate(
                                                state -> MetaMachine.getMachine(state.getWorld(),
                                                        state.getPos()) instanceof IRotorHolderMachine rotorHolder &&
                                                        state.getWorld()
                                                                .getBlockState(state.getPos()
                                                                        .relative(rotorHolder.self().getFrontFacing()))
                                                                .isAir(),
                                                () -> PartAbility.ROTOR_HOLDER.getAllBlocks().stream()
                                                        .map(BlockInfo::fromBlock).toArray(BlockInfo[]::new)))
                                        .addTooltips(Component.translatable("gtceu.multiblock.pattern.clear_amount_3"))
                                        .addTooltips(Component.translatable("gtceu.multiblock.pattern.error.limited.1",
                                                VN[tier]))
                                        .setExactLimit(1)
                                        .or(abilities(PartAbility.OUTPUT_ENERGY)).setExactLimit(1))
                        .where('H', blocks(casing.get())
                                .or(autoAbilities(definition.getRecipeTypes(), false, false, true, true, true, true))
                                .or(autoAbilities(true, needsMuffler, false)))
                        .build())
                .recoveryItems(
                        () -> new ItemLike[] {
                                GTMaterialItems.MATERIAL_ITEMS.get(TagPrefix.dustTiny, GTMaterials.Ash).get() })
                .workableCasingModel(casingTexture, overlayModel)
                .tooltips(
                        Component.translatable("gtceu.universal.tooltip.base_production_eut", V[tier] * 2),
                        Component.translatable("gtceu.multiblock.turbine.efficiency_tooltip", VNF[tier]))
                .register();
    }

    public static MultiblockMachineDefinition registerAstroCombustionEngine(String name, int tier,
                                                                            Supplier<? extends Block> casing,
                                                                            Supplier<? extends Block> gear,
                                                                            Supplier<? extends Block> intake,
                                                                            ResourceLocation casingTexture,
                                                                            ResourceLocation overlayModel) {
        return registerAstroCombustionEngine(REGISTRATE, name, tier, casing, gear, intake, casingTexture, overlayModel);
    }

    public static MultiblockMachineDefinition registerAstroCombustionEngine(GTRegistrate registrate,
                                                                            String name, int tier,
                                                                            Supplier<? extends Block> casing,
                                                                            Supplier<? extends Block> gear,
                                                                            Supplier<? extends Block> intake,
                                                                            ResourceLocation casingTexture,
                                                                            ResourceLocation overlayModel) {
        return registrate.multiblock(name, holder -> new AstroOverdriveCombustionEngine(holder, tier))
                .rotationState(RotationState.ALL)
                .recipeType(GTRecipeTypes.COMBUSTION_GENERATOR_FUELS)
                .generator(true)
                .recipeModifier(AstroOverdriveCombustionEngine::recipeModifier, true)
                .appearanceBlock(casing)
                .pattern(definition -> FactoryBlockPattern.start()
                        .aisle("  X  ", "     ", "X   X", "     ", "  X  ")
                        .aisle("  X  ", " FFF ", "XFDFX", " FFF ", "  X  ")
                        .aisle("  X  ", " XXX ", "XXGXX", " XXX ", "  X  ")
                        .aisle("     ", " XXX ", " XGX ", " XXX ", "     ")
                        .aisle("     ", " XCX ", " CGC ", " XCX ", "     ")
                        .aisle("     ", " XCX ", " CGC ", " XCX ", "     ")
                        .aisle(" A A ", " XXX ", " X@X ", " XXX ", "     ")
                        .where('X', blocks(casing.get()))
                        .where('G', blocks(gear.get()))
                        .where('C', blocks(casing.get()).setMinGlobalLimited(3)
                                .or(autoAbilities(definition.getRecipeTypes(), false, false, true, true, true, true))
                                .or(autoAbilities(true, true, false)))
                        .where('D',
                                ability(PartAbility.OUTPUT_ENERGY,
                                        IntStream.of(ULV, LV, MV, HV, EV, IV, LuV, ZPM, UV, UHV)
                                                .filter(t -> t >= tier)
                                                .toArray())
                                        .addTooltips(Component.translatable("gtceu.multiblock.pattern.error.limited.1",
                                                GTValues.VN[tier])))
                        .where('F',
                                blocks(intake.get())
                                        .addTooltips(Component.translatable("gtceu.multiblock.pattern.clear_amount_1")))
                        .where('@', controller(blocks(definition.getBlock())))
                        .where('A', frames(GTMaterials.NaquadahAlloy))
                        .where(' ', any())
                        .build())
                .recoveryItems(
                        () -> new ItemLike[] {
                                GTMaterialItems.MATERIAL_ITEMS.get(TagPrefix.dustTiny, GTMaterials.Ash).get() })
                .workableCasingModel(casingTexture, overlayModel)
                .tooltips(
                        Component.translatable("gtceu.universal.tooltip.base_production_eut", V[tier]),
                        Component.translatable("gtceu.universal.tooltip.uses_per_hour_lubricant",
                                FluidType.BUCKET_VOLUME * 100),
                        tier > ZPM ?
                                Component.translatable("gtceu.machine.large_combustion_engine.tooltip.boost_extreme",
                                        V[tier] * 16) :
                                Component.translatable("gtceu.machine.large_combustion_engine.tooltip.boost_regular",
                                        V[tier] * 12))
                .register();
    }

    public static MultiblockMachineDefinition registerAstroOverdriveTurbine(String id, String lang, int tier,
                                                                            GTRecipeType recipeType,
                                                                            Supplier<Block> casing,
                                                                            Supplier<Block> gear,
                                                                            Material frameMaterial,
                                                                            ResourceLocation casingTexture,
                                                                            ResourceLocation overlayModel) {
        return registerAstroOverdriveTurbine(REGISTRATE, id, lang, tier, recipeType, casing, gear, frameMaterial,
                casingTexture, overlayModel);
    }

    public static MultiblockMachineDefinition registerAstroOverdriveTurbine(GTRegistrate registrate, String id,
                                                                            String lang, int tier,
                                                                            GTRecipeType recipeType,
                                                                            Supplier<Block> casing,
                                                                            Supplier<Block> gear,
                                                                            Material frameMaterial,
                                                                            ResourceLocation casingTexture,
                                                                            ResourceLocation overlayModel) {
        return registerAstroOverdriveTurbine(registrate, id, lang, tier, recipeType, casing, gear, frameMaterial,
                casingTexture, overlayModel, true);
    }

    public static MultiblockMachineDefinition registerAstroOverdriveTurbine(String id, String lang, int tier,
                                                                            GTRecipeType recipeType,
                                                                            Supplier<Block> casing,
                                                                            Supplier<Block> gear,
                                                                            Material frameMaterial,
                                                                            ResourceLocation casingTexture,
                                                                            ResourceLocation overlayModel,
                                                                            boolean needsMuffler) {
        return registerAstroOverdriveTurbine(REGISTRATE, id, lang, tier, recipeType, casing, gear, frameMaterial,
                casingTexture, overlayModel, needsMuffler);
    }

    public static MultiblockMachineDefinition registerAstroOverdriveTurbine(GTRegistrate registrate, String id,
                                                                            String lang, int tier,
                                                                            GTRecipeType recipeType,
                                                                            Supplier<Block> casing,
                                                                            Supplier<Block> gear,
                                                                            Material frameMaterial,
                                                                            ResourceLocation casingTexture,
                                                                            ResourceLocation overlayModel,
                                                                            boolean needsMuffler) {
        return registrate.multiblock(id, holder -> new AstroOverdriveTurbines(holder, tier))
                .langValue(lang)
                .rotationState(RotationState.ALL)
                .recipeType(recipeType)
                .generator(true)
                .recipeModifier(AstroOverdriveTurbines::recipeModifier, true)
                .appearanceBlock(casing)
                .pattern(definition -> FactoryBlockPattern.start()
                        .aisle("  X  ", "     ", "X   X", "     ", "  X  ")
                        .aisle("  X  ", " XXX ", "XXRXX", " XXX ", "  X  ")
                        .aisle("  X  ", " XXX ", "XXGXX", " XXX ", "  X  ")
                        .aisle("     ", " XXX ", " XGX ", " XXX ", "     ")
                        .aisle("     ", " XCX ", " CGC ", " XCX ", "     ")
                        .aisle("     ", " XCX ", " CGC ", " XCX ", "     ")
                        .aisle(" F F ", " XXX ", " X@X ", " XXX ", "     ")
                        .where('@', controller(blocks(definition.getBlock())))
                        .where('G', blocks(gear.get()))
                        .where('X', blocks(casing.get()))
                        .where('R', new TraceabilityPredicate(
                                new SimplePredicate(
                                        state -> MetaMachine.getMachine(state.getWorld(),
                                                state.getPos()) instanceof IRotorHolderMachine rotorHolder &&
                                                state.getWorld()
                                                        .getBlockState(state.getPos()
                                                                .relative(rotorHolder.self().getFrontFacing()))
                                                        .isAir(),
                                        () -> PartAbility.ROTOR_HOLDER.getAllBlocks().stream()
                                                .map(BlockInfo::fromBlock).toArray(BlockInfo[]::new)))
                                .addTooltips(Component.translatable("gtceu.multiblock.pattern.clear_amount_3"))
                                .addTooltips(
                                        Component.translatable("gtceu.multiblock.pattern.error.limited.1", VN[tier]))
                                .setExactLimit(1))
                        .where('C', blocks(casing.get())
                                .or(autoAbilities(definition.getRecipeTypes(), false, true, true, true, true, true))
                                .or(autoAbilities(true, needsMuffler, false))
                        /* .or(abilities(PartAbility.OUTPUT_ENERGY)).setExactLimit(1) */)
                        .where('F', frames(frameMaterial))
                        .where(' ', any())
                        .build())
                .recoveryItems(
                        () -> new ItemLike[] {
                                GTMaterialItems.MATERIAL_ITEMS.get(TagPrefix.dustTiny, GTMaterials.Ash).get() })
                .workableCasingModel(casingTexture, overlayModel)
                .tooltips(
                        Component.translatable("gtceu.universal.tooltip.base_production_eut", V[tier] * 8),
                        Component.translatable("gtceu.multiblock.turbine.efficiency_tooltip", VNF[tier]))
                .register();
    }
}
