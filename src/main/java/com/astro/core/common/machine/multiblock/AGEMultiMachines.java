package com.astro.core.common.machine.multiblock;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.property.GTMachineModelProperties;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.MultiblockShapeInfo;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.client.renderer.machine.DynamicRenderHelper;
import com.gregtechceu.gtceu.client.renderer.machine.impl.BoilerMultiPartRender;
import com.gregtechceu.gtceu.common.block.BoilerFireboxType;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.common.machine.multiblock.steam.SteamParallelMultiblockMachine;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.model.generators.BlockModelBuilder;

import com.astro.core.AstroCore;
import com.astro.core.common.data.AstroRecipeTypes;
import com.astro.core.common.data.block.AstroBlocks;
import com.astro.core.common.machine.multiblock.electric.FluidDrillMachine;
import com.astro.core.common.machine.multiblock.electric.LargeMinerMachine;
import com.astro.core.common.machine.multiblock.generator.AstroSolarBoilers;
import com.astro.core.common.machine.multiblock.primitive.CokeOvenMachine;
import com.astro.core.common.machine.multiblock.steam.SteamBlastFurnace;
import com.astro.core.common.machine.multiblock.steam.SteamGrinder;
import com.astro.core.common.machine.multiblock.steam.SteamWasher;

import java.util.ArrayList;
import java.util.List;

import static com.astro.core.common.machine.hatches.AstroHatches.WATER_HATCH;
import static com.astro.core.common.registry.AstroRegistry.REGISTRATE;
import static com.gregtechceu.gtceu.api.machine.multiblock.PartAbility.*;
import static com.gregtechceu.gtceu.api.pattern.Predicates.*;
import static com.gregtechceu.gtceu.common.data.GCYMBlocks.CASING_INDUSTRIAL_STEAM;
import static com.gregtechceu.gtceu.common.data.GTBlocks.*;
import static com.gregtechceu.gtceu.common.data.GTMachines.COKE_OVEN_HATCH;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.COKE_OVEN_RECIPES;
import static com.gregtechceu.gtceu.common.data.models.GTMachineModels.createWorkableCasingMachineModel;
import static com.gregtechceu.gtceu.utils.FormattingUtil.formatNumbers;

@SuppressWarnings("all")
public class AGEMultiMachines {

    public static final MultiblockMachineDefinition COKE_OVEN = REGISTRATE
            .multiblock("coke_oven", CokeOvenMachine::new)
            .rotationState(RotationState.ALL)
            .recipeType(COKE_OVEN_RECIPES)
            .appearanceBlock(CASING_COKE_BRICKS)
            .recipeModifier(CokeOvenMachine::recipeModifier)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("AAA", "AAA", "AAA")
                    .aisle("AAA", "A#A", "AAA").setRepeatable(1, 16)
                    .aisle("AAA", "A@A", "AAA")
                    .where("@", controller(blocks(definition.get())))
                    .where("A", blocks(CASING_COKE_BRICKS.get())
                            .or(blocks(COKE_OVEN_HATCH.get())))
                    .where("#", Predicates.air())
                    .build())
            .shapeInfos(definition -> {
                List<MultiblockShapeInfo> shapeInfos = new ArrayList<>();
                var builder = MultiblockShapeInfo.builder()
                        .where('C', definition, Direction.NORTH)
                        .where('B', CASING_COKE_BRICKS.getDefaultState())
                        .where('#', Blocks.AIR.defaultBlockState());
                for (int height = 3; height <= 18; height++) {
                    List<String[]> aisles = new ArrayList<>();
                    aisles.add(new String[] { "BBB", "BCB", "BBB" });
                    for (int i = 1; i < height - 1; i++) {
                        aisles.add(new String[] { "BBB", "B#B", "BBB" });
                    }
                    aisles.add(new String[] { "BBB", "BBB", "BBB" });
                    var copy = builder.shallowCopy();
                    for (String[] aisle : aisles) {
                        copy.aisle(aisle);
                    }
                    shapeInfos.add(copy.build());
                }
                return shapeInfos;
            })
            .tooltipBuilder((stack, tooltip) -> {
                tooltip.add(Component.literal("Making better fuels for Steel and Power"));
                tooltip.add(Component
                        .literal("Gains Parallels for each layer in length added for up to 16 Parallels total"));
            })
            .workableCasingModel(GTCEu.id("block/casings/solid/machine_coke_bricks"),
                    GTCEu.id("block/multiblock/coke_oven"))
            .register();

    public static final MultiblockMachineDefinition STEAM_BLAST_FURNACE = REGISTRATE
            .multiblock("steam_blast_furnace", SteamBlastFurnace::new)
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeType(AstroRecipeTypes.STEAM_BLAST_FURNACE_RECIPES)
            .recipeModifier(SteamBlastFurnace::recipeModifier)
            .addOutputLimit(ItemRecipeCapability.CAP, 1)
            .appearanceBlock(CASING_BRONZE_BRICKS)
            .hasBER(true)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("FFF", "XXX", "XXX", "XXX")
                    .aisle("FFF", "X#X", "X#X", "XMX")
                    .aisle("FFF", "X@X", "XXX", "XXX")
                    .where('@', controller(blocks(definition.getBlock())))
                    .where('X', blocks(CASING_BRONZE_BRICKS.get()).setMinGlobalLimited(6)
                            .or(abilities(STEAM_IMPORT_ITEMS).setPreviewCount(1))
                            .or(abilities(STEAM_EXPORT_ITEMS).setPreviewCount(1)))
                    .where('F', blocks(GTBlocks.FIREBOX_BRONZE.get())
                            .or(abilities(STEAM).setExactLimit(1)))
                    .where('#', air())
                    .where('M', abilities(MUFFLER))
                    .build())
            .modelProperty(GTMachineModelProperties.RECIPE_LOGIC_STATUS, RecipeLogic.Status.IDLE)
            .model(createWorkableCasingMachineModel(
                    GTCEu.id("block/casings/solid/machine_casing_bronze_plated_bricks"),
                    AstroCore.id("block/multiblock/steam_blast_furnace"))
                    .andThen(b -> b.addDynamicRenderer(
                            () -> new BoilerMultiPartRender(
                                    BoilerFireboxType.BRONZE_FIREBOX, CASING_BRONZE_BRICKS))))
            .register();

    public static final MultiblockMachineDefinition STEAM_MACERATOR = REGISTRATE
            .multiblock("large_steam_macerator", SteamGrinder::new)
            .langValue("Large Steam Grinder")
            .addOutputLimit(ItemRecipeCapability.CAP, 1)
            .rotationState(RotationState.ALL)
            .recipeType(GTRecipeTypes.MACERATOR_RECIPES)
            .recipeModifier(SteamGrinder::recipeModifier, true)
            .appearanceBlock(CASING_BRONZE_BRICKS)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("XXXXX", "XXXXX", "XXXXX")
                    .aisle("XXXXX", "XGGGX", "X   X")
                    .aisle("XXXXX", "XGGGX", "X   X")
                    .aisle("XXXXX", "XGGGX", "X   X")
                    .aisle("XXXXX", "XX@XX", "XXXXX")
                    .where("@", controller(blocks(definition.get())))
                    .where('X', blocks(CASING_BRONZE_BRICKS.get()).setMinGlobalLimited(14)
                            .or(abilities(STEAM).setExactLimit(1).setPreviewCount(1))
                            .or(abilities(STEAM_IMPORT_ITEMS).setPreviewCount(1).setMinGlobalLimited(1)
                                    .setMaxGlobalLimited(2))
                            .or(abilities(STEAM_EXPORT_ITEMS).setPreviewCount(1).setMinGlobalLimited(1)
                                    .setMaxGlobalLimited(2)))
                    .where(" ", air())
                    .where("G", blocks(AstroBlocks.BRONZE_CRUSHING_WHEELS.get()))
                    .build())
            .workableCasingModel(GTCEu.id("block/casings/solid/machine_casing_bronze_plated_bricks"),
                    GTCEu.id("block/multiblock/steam_grinder"))
            .register();

    public static final MultiblockMachineDefinition STEAM_COMPRESSOR = REGISTRATE
            .multiblock("large_steam_compressor", SteamParallelMultiblockMachine::new)
            .rotationState(RotationState.ALL)
            .recipeType(GTRecipeTypes.COMPRESSOR_RECIPES)
            .recipeModifier(SteamParallelMultiblockMachine::recipeModifier, true)
            .appearanceBlock(CASING_BRONZE_BRICKS)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle(" XXX ", " XXX ", "  X  ")
                    .aisle("XXXXX", "X###X", " XXX ")
                    .aisle("XXXXX", "X###X", "XXXXX")
                    .aisle("XXXXX", "X###X", " XXX ")
                    .aisle(" XXX ", " X@X ", "  X  ")
                    .where("@", controller(blocks(definition.get())))
                    .where('X', blocks(CASING_BRONZE_BRICKS.get()).setMinGlobalLimited(14)
                            .or(abilities(STEAM).setExactLimit(1).setPreviewCount(1))
                            .or(abilities(STEAM_IMPORT_ITEMS).setPreviewCount(1).setMinGlobalLimited(1)
                                    .setMaxGlobalLimited(2))
                            .or(abilities(STEAM_EXPORT_ITEMS).setPreviewCount(1).setMinGlobalLimited(1)
                                    .setMaxGlobalLimited(2)))
                    .where(" ", any())
                    .where("#", air())
                    .build())
            .workableCasingModel(GTCEu.id("block/casings/solid/machine_casing_bronze_plated_bricks"),
                    AstroCore.id("block/multiblock/compressor"))
            .register();

    public static final MultiblockMachineDefinition STEAM_SEPARATOR = REGISTRATE
            .multiblock("large_steam_centrifuge", SteamParallelMultiblockMachine::new)
            .langValue("Large Steam Separator")
            .rotationState(RotationState.ALL)
            .recipeType(GTRecipeTypes.CENTRIFUGE_RECIPES)
            .recipeModifier(SteamParallelMultiblockMachine::recipeModifier, true)
            .appearanceBlock(CASING_INDUSTRIAL_STEAM)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("#XXX#", "XXXXX", "#XXX#")
                    .aisle("XXXXX", "XAPAX", "XXXXX")
                    .aisle("XXXXX", "XPAPX", "XXXXX")
                    .aisle("XXXXX", "XAPAX", "XXXXX")
                    .aisle("#XXX#", "XX@XX", "#XXX#")
                    .where("@", controller(blocks(definition.get())))
                    .where("P", blocks(CASING_BRONZE_GEARBOX.get()))
                    .where("X", blocks(CASING_INDUSTRIAL_STEAM.get()).setMinGlobalLimited(40)
                            .or(abilities(STEAM).setExactLimit(1).setPreviewCount(1))
                            .or(abilities(STEAM_IMPORT_ITEMS).setPreviewCount(1).setMinGlobalLimited(1)
                                    .setMaxGlobalLimited(3))
                            .or(abilities(STEAM_EXPORT_ITEMS).setPreviewCount(1).setMinGlobalLimited(1)
                                    .setMaxGlobalLimited(3)))
                    .where("A", air())
                    .where("#", any())
                    .build())
            .workableCasingModel(GTCEu.id("block/casings/gcym/industrial_steam_casing"),
                    AstroCore.id("block/multiblock/centrifuge"))
            .register();

    public static final MultiblockMachineDefinition STEAM_WASHER = REGISTRATE
            .multiblock("large_steam_ore_washer", SteamWasher::new)
            .langValue("Large Steam Washer")
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeType(GTRecipeTypes.ORE_WASHER_RECIPES)
            .recipeModifier(SteamWasher::recipeModifier, true)
            .appearanceBlock(CASING_INDUSTRIAL_STEAM)
            .hasBER(true)
            .allowFlip(false)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("XXXXXXX", "XXXXXXX", "XXXXXXX")
                    .aisle("XXXXXXX", "XP###PX", "X#####X")
                    .aisle("XXXXXXX", "XP###PX", "X#####X")
                    .aisle("XXXXXXX", "XP###PX", "X#####X")
                    .aisle("XXXXXXX", "XXX@XXX", "XXXXXXX")
                    .where("@", controller(blocks(definition.get())))
                    .where("P", blocks(CASING_BRONZE_PIPE.get()))
                    .where("X", blocks(CASING_INDUSTRIAL_STEAM.get()).setMinGlobalLimited(55)
                            .or(abilities(STEAM).setExactLimit(1))
                            .or(abilities(STEAM_IMPORT_ITEMS).setPreviewCount(1).setMinGlobalLimited(1)
                                    .setMaxGlobalLimited(3))
                            .or(abilities(STEAM_EXPORT_ITEMS).setPreviewCount(1).setMinGlobalLimited(1)
                                    .setMaxGlobalLimited(3))
                            .or(blocks(WATER_HATCH.get()).setPreviewCount(1).setMaxGlobalLimited(2)))
                    .where("#", air())
                    .build())
            .modelProperty(GTMachineModelProperties.RECIPE_LOGIC_STATUS, RecipeLogic.Status.IDLE)
            .model(createWorkableCasingMachineModel(GTCEu.id("block/casings/gcym/industrial_steam_casing"),
                    AstroCore.id("block/multiblock/ore_washer"))
                    .andThen(b -> b.addDynamicRenderer(DynamicRenderHelper::makeRecipeFluidAreaRender)))
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
                            .or(blocks(AstroBlocks.SOLAR_CELL_ETRIUM.get()))
                            .or(blocks(AstroBlocks.SOLAR_CELL_VESNIUM.get())))
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
                    GTCEu.id("block/multiblock/generator/large_steel_boiler"))
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

    public static final MultiblockMachineDefinition FLUID_DRILLING_RIG_IV = REGISTRATE
            .multiblock("fluid_drilling_rig_iv", holder -> new FluidDrillMachine(holder, GTValues.IV))
            .rotationState(RotationState.ALL)
            .langValue("§9Elite Fluid Drilling Rig")
            .recipeType(GTRecipeTypes.DUMMY_RECIPES)
            .tooltips(
                    Component.translatable("astrogreg.machine.fluid_drilling_rig.iv.tooltip"),
                    Component.translatable("gtceu.machine.fluid_drilling_rig.description"),
                    Component.translatable("gtceu.machine.fluid_drilling_rig.depletion", formatNumbers(3)),
                    Component.translatable("gtceu.universal.tooltip.energy_tier_range", GTValues.VNF[GTValues.LuV],
                            GTValues.VNF[GTValues.ZPM]),
                    Component.translatable("gtceu.machine.fluid_drilling_rig.production", 256,
                            formatNumbers(384)))
            .appearanceBlock(() -> AstroBlocks.MACHINE_CASING_RHODIUM_PLATED_PALLADIUM.get())
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("#XXX#", "#####", "#####", "#####", "#####", "#####", "#####", "#####", "#####", "#####")
                    .aisle("XXXXX", "#FFF#", "#FFF#", "#FFF#", "##F##", "##F##", "##F##", "#####", "#####", "#####")
                    .aisle("XXXXX", "#FCF#", "#FCF#", "#FXF#", "#FCF#", "#FCF#", "#FCF#", "##F##", "##F##", "##F##")
                    .aisle("XXXXX", "#FFF#", "#FFF#", "#FFF#", "##F##", "##F##", "##F##", "#####", "#####", "#####")
                    .aisle("#XSX#", "#####", "#####", "#####", "#####", "#####", "#####", "#####", "#####", "#####")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(AstroBlocks.MACHINE_CASING_RHODIUM_PLATED_PALLADIUM.get()).setMinGlobalLimited(3)
                            .or(abilities(INPUT_ENERGY).setMinGlobalLimited(1).setMaxGlobalLimited(2))
                            .or(abilities(EXPORT_FLUIDS).setMaxGlobalLimited(1)))
                    .where('C', blocks(AstroBlocks.MACHINE_CASING_RHODIUM_PLATED_PALLADIUM.get()))
                    .where('F', frames(GTMaterials.RhodiumPlatedPalladium))
                    .where('#', any())
                    .build())
            .workableCasingModel(AstroCore.id("block/casings/machine_casing_pristine_rhodium_plated_palladium"),
                    GTCEu.id("block/multiblock/fluid_drilling_rig"))
            .register();

    public static final MultiblockMachineDefinition LARGE_MINER_ZPM = REGISTRATE
            .multiblock("large_miner_zpm", holder -> new LargeMinerMachine(holder, GTValues.ZPM, 64 / GTValues.ZPM,
                    2 * GTValues.ZPM - 5, GTValues.ZPM, 6))
            .rotationState(RotationState.NON_Y_AXIS)
            .langValue("§cElite Large Ore Miner III")
            .recipeType(GTRecipeTypes.MACERATOR_RECIPES)
            .appearanceBlock(() -> AstroBlocks.MACHINE_CASING_RHODIUM_PLATED_PALLADIUM.get())
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("#XXX#", "#####", "#####", "#####", "#####", "#####", "#####", "#####", "#####", "#####")
                    .aisle("XXXXX", "#FFF#", "#FFF#", "#FFF#", "##F##", "##F##", "##F##", "#####", "#####", "#####")
                    .aisle("XXXXX", "#FCF#", "#FCF#", "#FCF#", "#FCF#", "#FCF#", "#FCF#", "##F##", "##F##", "##F##")
                    .aisle("XXXXX", "#FFF#", "#FFF#", "#FFF#", "##F##", "##F##", "##F##", "#####", "#####", "#####")
                    .aisle("#XSX#", "#####", "#####", "#####", "#####", "#####", "#####", "#####", "#####", "#####")
                    .where('S', controller(blocks(definition.getBlock())))
                    .where('X', blocks(AstroBlocks.MACHINE_CASING_RHODIUM_PLATED_PALLADIUM.get())
                            .or(abilities(EXPORT_ITEMS).setExactLimit(1).setPreviewCount(1))
                            .or(abilities(IMPORT_FLUIDS).setExactLimit(1).setPreviewCount(1))
                            .or(abilities(INPUT_ENERGY).setMinGlobalLimited(1).setMaxGlobalLimited(2)
                                    .setPreviewCount(1)))
                    .where('C', blocks(AstroBlocks.MACHINE_CASING_RHODIUM_PLATED_PALLADIUM.get()))
                    .where('F', frames(GTMaterials.RhodiumPlatedPalladium))
                    .where('#', any())
                    .build())
            .allowExtendedFacing(true)
            .modelProperty(GTMachineModelProperties.RECIPE_LOGIC_STATUS, RecipeLogic.Status.IDLE)
            .model(createWorkableCasingMachineModel(
                    AstroCore.id("block/casings/machine_casing_pristine_rhodium_plated_palladium"),
                    GTCEu.id("block/multiblock/large_miner"))
                    .andThen((ctx, prov, modelBuilder) -> {
                        modelBuilder.replaceForAllStates((state, models) -> {
                            if (!state.getValue(GTMachineModelProperties.IS_FORMED)) {
                                return models;
                            }
                            var parentModel = prov.models()
                                    .getExistingFile(GTCEu.id("block/machine/large_miner_active"));
                            for (var model : models) {
                                ((BlockModelBuilder) model.model)
                                        .parent(parentModel);
                            }
                            return models;
                        });
                    }))
            .tooltips(Component.translatable("astrogreg.machine.large_miner.zpm.tooltip"),
                    Component.translatable("gtceu.machine.miner.multi.description"))
            .tooltipBuilder((stack, tooltip) -> {
                int workingAreaChunks = 2 * GTValues.ZPM - 5;
                tooltip.add(Component.translatable("gtceu.machine.miner.multi.modes"));
                tooltip.add(Component.translatable("gtceu.machine.miner.multi.production"));
                tooltip.add(Component.translatable("gtceu.machine.miner.fluid_usage", 6,
                        GTMaterials.DrillingFluid.getLocalizedName()));
                tooltip.add(Component.translatable("gtceu.universal.tooltip.working_area_chunks", workingAreaChunks,
                        workingAreaChunks));
                tooltip.add(Component.translatable("gtceu.universal.tooltip.energy_tier_range",
                        GTValues.VNF[GTValues.UV], GTValues.VNF[GTValues.UHV]));
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
