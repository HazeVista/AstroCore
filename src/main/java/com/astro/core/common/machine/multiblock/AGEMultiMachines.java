package com.astro.core.common.machine.multiblock;

import com.astro.core.common.data.block.AstroBlocks;
import com.astro.core.common.data.configs.AstroConfigs;
import com.astro.core.common.machine.multiblock.generator.AstroSolarBoilers;
import com.astro.core.common.machine.multiblock.steam.SteamBlastFurnace;
import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.MultiblockShapeInfo;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.client.renderer.machine.DynamicRenderHelper;
import com.gregtechceu.gtceu.common.block.BoilerFireboxType;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.utils.GTUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;

import java.util.List;

import static com.astro.core.common.registry.AstroRegistry.REGISTRATE;
import static com.gregtechceu.gtceu.api.machine.multiblock.PartAbility.EXPORT_FLUIDS;
import static com.gregtechceu.gtceu.api.machine.multiblock.PartAbility.IMPORT_FLUIDS;
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

    public static final MultiblockMachineDefinition STEAM_BLAST_FURNACE = REGISTRATE.multiblock("steam_blast_furnace", SteamBlastFurnace::new)
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeType(GTRecipeTypes.PRIMITIVE_BLAST_FURNACE_RECIPES)
            .recipeModifier(SteamBlastFurnace::recipeModifier)
            .appearanceBlock(CASING_BRONZE_BRICKS)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("FFF", "XXX", "XXX", "XXX")
                    .aisle("FFF", "X&X", "X#X", "XMX")
                    .aisle("FFF", "X@X", "XXX", "XXX")
                    .where('@', controller(blocks(definition.getBlock())))
                    .where('X', blocks(CASING_BRONZE_BRICKS.get()).setMinGlobalLimited(6)
                            .or(abilities(PartAbility.STEAM_IMPORT_ITEMS).setPreviewCount(1))
                            .or(abilities(PartAbility.STEAM_EXPORT_ITEMS).setPreviewCount(1)))
                    .where('F', blocks(GTBlocks.FIREBOX_BRONZE.get())
                            .or(abilities(PartAbility.STEAM).setExactLimit(1)))
                    .where('M', abilities(PartAbility.MUFFLER).setExactLimit(1))
                    .where('#', Predicates.air())
                    .where('&', Predicates.air()
                            .or(Predicates.custom(bws -> GTUtil.isBlockSnow(bws.getBlockState()), null)))
                    .build())
            .model(createWorkableCasingMachineModel(GTCEu.id("block/casings/solid/machine_casing_bronze_plated_bricks"),
                    GTCEu.id("block/multiblock/primitive_blast_furnace"))
                    .andThen(b -> b.addDynamicRenderer(
                            () -> DynamicRenderHelper.makeBoilerPartRender(
                                    BoilerFireboxType.BRONZE_FIREBOX, CASING_BRONZE_BRICKS))))
            .register();

    public static final MultiblockMachineDefinition SOLAR_BOILER_ARRAY = REGISTRATE.multiblock("solar_boiler_array", AstroSolarBoilers::new)
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeType(GTRecipeTypes.DUMMY_RECIPES)
            .appearanceBlock(GTBlocks.CASING_STEEL_SOLID)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("AAA")
                    .aisle("ABA")
                    .aisle("A@A")
                    .where('@', controller(blocks(definition.get())))
                    .where('A', blocks(GTBlocks.CASING_STEEL_SOLID.get())
                            .or(abilities(IMPORT_FLUIDS))
                            .or(abilities(EXPORT_FLUIDS)))
                    .where('B', blocks(AstroBlocks.SOLAR_CELL.get()))
                    .build())
            .shapeInfos(definition -> {
                var minShape = MultiblockShapeInfo.builder()
                        .aisle("AAAAA")
                        .aisle("ABBBA")
                        .aisle("ABBBA")
                        .aisle("ABBBA")
                        .aisle("AA@AA")
                        .where('@', definition, Direction.SOUTH)
                        .where('A', GTBlocks.CASING_STEEL_SOLID.get())
                        .where('B', AstroBlocks.SOLAR_CELL.get())
                        .build();
                return List.of(minShape);
            })
            .workableCasingModel(GTCEu.id("block/casings/solid/machine_casing_solid_steel"), GTCEu.id("block/multiblock/blast_furnace"))
            .tooltips(
                    Component.translatable("astrogreg.tooltip.mega_solar.desc", "An expandable array for solar-powered steam production.")
                            .withStyle(ChatFormatting.GRAY),
                    Component.translatable("astrogreg.tooltip.mega_solar.sunlight", "The entire structure must be exposed to direct sunlight to produce steam.")
                            .withStyle(ChatFormatting.WHITE),
                    Component.translatable("astrogreg.tooltip.mega_solar.production", "Production: " + AstroConfigs.INSTANCE.features.solarSpeed + " mb/t of Steam per active block")
                            .withStyle(ChatFormatting.WHITE),
                    Component.translatable("astrogreg.tooltip.mega_solar.max_size", "Max Size: 33 x 33")
                            .withStyle(ChatFormatting.GRAY)
            )
            .register();

    public static void init() {}
}
