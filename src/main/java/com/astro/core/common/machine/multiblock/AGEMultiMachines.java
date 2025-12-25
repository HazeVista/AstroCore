package com.astro.core.common.machine.multiblock;

@SuppressWarnings("all")
public class AGEMultiMachines {

    // public static final MultiblockMachineDefinition FILTRATION_PLANT = REGISTRATE
    // .multiblock("filtration_plant", WorkableElectricMultiblockMachine::new)
    // .langValue("Filtration Plant")
    // .rotationState(RotationState.NON_Y_AXIS)
    // .recipeTypes(AstroRecipeTypes.DEIONIZATION_RECIPES, GTRecipeTypes.DISTILLERY_RECIPES)
    // .recipeModifiers(GTRecipeModifiers.OC_NON_PERFECT_SUBTICK, GTRecipeModifiers.BATCH_MODE,
    // GTRecipeModifiers.PARALLEL_HATCH)
    // .appearanceBlock(GTBlocks.CASING_PTFE_INERT)
    // .partAppearance((controller, part, side) -> {
    // if(part instanceof ItemBusPartMachine itemBus && itemBus.getInventory().canCapOutput()) {
    // return CASING_STAINLESS_CLEAN.getDefaultState();
    // }
    // if(part instanceof FluidHatchPartMachine check && cast.getIO.supports(IO.IN)) {
    // return CASING_STAINLESS_CLEAN.getDefaultState();
    // }
    // return CASING_PTFE_INERT.getDefaultState();
    // })
    // .pattern(definition -> {
    // var pcasing = blocks(GTBlocks.CASING_PTFE_INERT.get());
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
    // .where('P', Predicates.blocks(GTBlocks.CASING_TUNGSTENSTEEL_PIPE.get()))
    // .where('F', Predicates.blocks(GTBlocks.FIREBOX_TUNGSTENSTEEL.get()))
    // .where('G', Predicates.blocks(GTBlocks.CASING_LAMINATED_GLASS.get()))
    // .where('H', Predicates.blocks(GTBlocks.HERMETIC_CASING_IV.get()))
    // .build();
    // })
    // .model(GTMachineModels.createWorkableCasingMachineModel(
    // GTCEu.id("block/casings/solid/machine_casing_inert_ptfe"),
    // GTCEu.id("block/multiblock/central_monitor")))
    // .register();

    public static void init() {}
}
