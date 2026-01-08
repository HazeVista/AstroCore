package com.astro.core.common.machine.multiblock;

import com.astro.core.common.machine.multiblock.steam.LargeSteamBlastFurnace;
import com.astro.core.common.registry.AstroRegistry;
import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.utils.GTUtil;

@SuppressWarnings("all")
public class AGEMultiMachines {

    // public static final MultiblockMachineDefinition FILTRATION_PLANT = REGISTRATE
    // .multiblock("filtration_plant", WorkableElectricMultiblockMachine::new)
    // .langValue("Filtration Plant")
    // .rotationState(RotationState.NON_Y_AXIS)
    // .recipeTypes(AstroRecipeTypes.DEIONIZATION_RECIPES, GTRecipeTypes.DISTILLERY_RECIPES)
    // .recipeModifiers(GTRecipeModifiers.OC_NON_PERFECT_SUBTICK, GTRecipeModifiers.BATCH_MODE,
    // GTRecipeModifiers.PARALLEL_HATCH)
    // .appearanceBlock(CASING_PTFE_INERT)
    // .partAppearance((controller, part, side) -> {
    // if(part instanceof ItemBusPartMachine itemBus && itemBus.getInventory().canCapOutput()) {
    // return CASING_STAINLESS_CLEAN.getDefaultState();
    // }
    // if(part instanceof FluidHatchPartMachine fluidHatch && fluidHatch.getIO().supports(IO.IN)) {
    // return CASING_STAINLESS_CLEAN.getDefaultState();
    // }
    // return CASING_PTFE_INERT.getDefaultState();
    // })
    // .pattern(definition -> {
    // var pcasing = blocks(CASING_PTFE_INERT.get());
    // var scasing = blocks(CASING_STAINLESS_CLEAN.get());
    // return FactoryBlockPattern.start()
    // .aisle("SSSFFF", "SGSCCC", "SGSCCC", "SSSCCC", "###CCC")
    // .aisle("SSSFFF", "GHGPPC", "GHGCPC", "SSSCPC", "###CCC")
    // .aisle("SSSFFF", "SGSC@C", "SGSCCC", "SSSCCC", "###CCC")
    // .where('@', Predicates.controller(blocks(definition.getBlock())))
    // .where('#', Predicates.any())
    // .where('S', scasing
    // .or(Predicates.abilities(PartAbility.IMPORT_FLUIDS).setExactLimit(1))
    // .or(Predicates.abilities(PartAbility.IMPORT_ITEMS).setMinGlobalLimited(1).setMaxGlobalLimited(2)))
    // .where('C', pcasing
    // .or(Predicates.abilities(PartAbility.EXPORT_FLUIDS)).setMinGlobalLimited(1).setMaxGlobalLimited(2)
    // .or(Predicates.abilities(PartAbility.EXPORT_ITEMS)).setMaxGlobalLimited(1)
    // .or(Predicates.abilities(PartAbility.MAINTENANCE)).setExactLimit(1)
    // .or(Predicates.abilities(PartAbility.INPUT_ENERGY)).setExactLimit(1))
    // .where('P', blocks(GTBlocks.CASING_TUNGSTENSTEEL_PIPE.get()))
    // .where('F', blocks(GTBlocks.FIREBOX_TUNGSTENSTEEL.get()))
    // .where('G', blocks(GTBlocks.CASING_LAMINATED_GLASS.get()))
    // .where('H', blocks(GTBlocks.HERMETIC_CASING_IV.get()))
    // .build();
    // })
    // .model(GTMachineModels.createWorkableCasingMachineModel(
    // GTCEu.id("block/casings/solid/machine_casing_inert_ptfe"),
    // GTCEu.id("block/multiblock/central_monitor")))
    // .register();

    public static final MultiblockMachineDefinition LARGE_STEAM_BLAST_FURNACE = AstroRegistry.REGISTRATE.multiblock("large_steam_blast_furnace", LargeSteamBlastFurnace::new)
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeType(GTRecipeTypes.PRIMITIVE_BLAST_FURNACE_RECIPES)
            .recipeModifier(LargeSteamBlastFurnace::recipeModifier)
            .appearanceBlock(GTBlocks.CASING_BRONZE_BRICKS)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("FFF", "XXX", "XXX", "XXX")
                    .aisle("FFF", "X&X", "X#X", "XMX")
                    .aisle("FFF", "X@X", "XXX", "XXX")
                    .where('@', Predicates.controller(Predicates.blocks(definition.getBlock())))
                    .where('X', Predicates.blocks(GTBlocks.CASING_BRONZE_BRICKS.get()).setMinGlobalLimited(6)
                            .or(Predicates.abilities(PartAbility.STEAM_IMPORT_ITEMS).setPreviewCount(1))
                            .or(Predicates.abilities(PartAbility.STEAM_EXPORT_ITEMS).setPreviewCount(1)))
                    .where('F', Predicates.blocks(GTBlocks.FIREBOX_BRONZE.get())
                            .or(Predicates.abilities(PartAbility.STEAM).setExactLimit(1)))
                    .where('M', Predicates.abilities(PartAbility.MUFFLER).setExactLimit(1))
                    .where('#', Predicates.air())
                    .where('&', Predicates.air()
                            .or(Predicates.custom(bws -> GTUtil.isBlockSnow(bws.getBlockState()), null)))
                    .build())
            .workableCasingModel(
                    GTCEu.id("block/casings/solid/machine_casing_bronze_plated_bricks"),
                    GTCEu.id("block/multiblock/primitive_blast_furnace"))
            .register();

    public static void init() {}
}
