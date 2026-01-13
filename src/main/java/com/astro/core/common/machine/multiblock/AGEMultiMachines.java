package com.astro.core.common.machine.multiblock;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.MultiblockShapeInfo;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.client.renderer.machine.DynamicRenderHelper;
import com.gregtechceu.gtceu.common.block.BoilerFireboxType;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;

import com.astro.core.common.data.block.AstroBlocks;
import com.astro.core.common.machine.multiblock.generator.AstroSolarBoilers;
import com.astro.core.common.machine.multiblock.steam.SteamBlastFurnace;

import static com.astro.core.common.registry.AstroRegistry.REGISTRATE;
import static com.gregtechceu.gtceu.api.machine.multiblock.PartAbility.*;
import static com.gregtechceu.gtceu.api.pattern.Predicates.*;
import static com.gregtechceu.gtceu.common.data.GTBlocks.CASING_BRONZE_BRICKS;
import static com.gregtechceu.gtceu.common.data.models.GTMachineModels.createWorkableCasingMachineModel;

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

    public static final MultiblockMachineDefinition STEAM_BLAST_FURNACE = REGISTRATE
            .multiblock("steam_blast_furnace", SteamBlastFurnace::new)
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeType(GTRecipeTypes.PRIMITIVE_BLAST_FURNACE_RECIPES)
            .recipeModifier(SteamBlastFurnace::recipeModifier)
            .addOutputLimit(ItemRecipeCapability.CAP, 1)
            .appearanceBlock(CASING_BRONZE_BRICKS)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("FFF", "XXX", "XXX", "XXX")
                    .aisle("FFF", "X&X", "X#X", "X#X")
                    .aisle("FFF", "X@X", "XXX", "XXX")
                    .where('@', controller(blocks(definition.getBlock())))
                    .where('X', blocks(CASING_BRONZE_BRICKS.get()).setMinGlobalLimited(6)
                            .or(abilities(PartAbility.STEAM_IMPORT_ITEMS).setPreviewCount(1))
                            .or(abilities(PartAbility.STEAM_EXPORT_ITEMS).setPreviewCount(1))
                            .or(abilities(PartAbility.MUFFLER).setExactLimit(1)))
                    .where('F', blocks(GTBlocks.FIREBOX_BRONZE.get())
                            .or(abilities(PartAbility.STEAM).setExactLimit(1)))
                    .where('#', Predicates.air())
                    .where('&', Predicates.air()
                            .or(Predicates.custom(bws -> GTUtil.isBlockSnow(bws.getBlockState()), null)))
                    .build())
            .model(createWorkableCasingMachineModel(GTCEu.id("block/casings/solid/machine_casing_bronze_plated_bricks"),
                    GTCEu.id("block/multiblock/primitive_blast_furnace"))
                    .andThen(b -> b.addDynamicRenderer(
                            () -> DynamicRenderHelper.makeBoilerPartRender(
                                    BoilerFireboxType.BRONZE_FIREBOX, CASING_BRONZE_BRICKS))))
            .hasBER(true)
            .register();

    public static final MultiblockMachineDefinition SOLAR_BOILER_ARRAY = REGISTRATE
            .multiblock("solar_boiler_array", AstroSolarBoilers::new)
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeType(GTRecipeTypes.DUMMY_RECIPES)
            .appearanceBlock(GTBlocks.CASING_STEEL_SOLID)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("AAA")
                    .aisle("ABA")
                    .aisle("A@A")
                    .where('@', controller(blocks(definition.get())))
                    .where('A', blocks(GTBlocks.CASING_STEEL_SOLID.get())
                            .or(abilities(IMPORT_FLUIDS)).setMaxGlobalLimited(2)
                            .or(abilities(EXPORT_FLUIDS)).setMaxGlobalLimited(2)
                            .or(abilities(MAINTENANCE)).setExactLimit(1))
                    .where('B', blocks(AstroBlocks.SOLAR_CELL.get())
                            .or(Predicates.blocks(AstroBlocks.SOLAR_CELL_ETRIUM.get()))
                            .or(Predicates.blocks(AstroBlocks.SOLAR_CELL_VESNIUM.get())))
                    .build())
            .shapeInfos(definition -> {
                var shapes = new java.util.ArrayList<MultiblockShapeInfo>();

                for (int size = 5; size <= 35; size += 2) {
                    shapes.add(createSolarBoilerShape(definition, size));
                }

                return shapes;
            })
            .allowFlip(false)
            .workableCasingModel(GTCEu.id("block/casings/solid/machine_casing_solid_steel"),
                    GTCEu.id("block/multiblock/blast_furnace"))
            .tooltipBuilder((stack, tooltip) -> {
                tooltip.add(Component
                        .literal("Cells must be exposed to direct sunlight to work properly.")
                        .withStyle(ChatFormatting.WHITE));
                tooltip.add(Component
                        .literal("Heating speed scales with distance from the Sun.")
                        .withStyle(ChatFormatting.WHITE));
                tooltip.add(Component
                        .literal(
                                "Heat scaling: §e−1 K/s per Cell below 40 or +1% heating speed per sunlit Cell above 40")
                        .withStyle(ChatFormatting.AQUA));
                tooltip.add(Component.literal("Max Cell Count: §e33 x 33 (1089 Cells)")
                        .withStyle(ChatFormatting.AQUA));
            })
            .register();

    private static MultiblockShapeInfo createSolarBoilerShape(MultiblockMachineDefinition definition, int size) {
        var builder = MultiblockShapeInfo.builder();
        int center = size / 2;

        for (int z = 0; z < size; z++) {
            var layer = new StringBuilder();
            for (int x = 0; x < size; x++) {
                if (z == 0) {
                    if (x == center - 1) layer.append('D');
                    else if (x == center) layer.append('@');
                    else if (x == center + 1) layer.append('C');
                    else if (x == 0) layer.append('E');
                    else layer.append('A');
                } else if (z == size - 1 || x == 0 || x == size - 1) {
                    layer.append('A');
                } else {
                    layer.append('B');
                }
            }
            builder.aisle(layer.toString());
        }

        return builder
                .where('@', definition, Direction.NORTH)
                .where('A', GTBlocks.CASING_STEEL_SOLID.getDefaultState())
                .where('B', AstroBlocks.SOLAR_CELL.get().defaultBlockState())
                .where('C', GTMachines.FLUID_EXPORT_HATCH[GTValues.LV], Direction.NORTH)
                .where('D', GTMachines.FLUID_IMPORT_HATCH[GTValues.LV], Direction.NORTH)
                .where('E', GTMachines.MAINTENANCE_HATCH, Direction.NORTH)
                .build();
    }

    public static void init() {}
}
