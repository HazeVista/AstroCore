package com.astro.core.common.machine;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import net.minecraft.data.recipes.FinishedRecipe;

import com.astro.core.common.data.materials.AstroMaterials;

import java.util.Locale;
import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.VA;
import static com.gregtechceu.gtceu.api.GTValues.VN;
import static com.gregtechceu.gtceu.data.recipe.GTCraftingComponents.*;
import static com.gregtechceu.gtceu.data.recipe.GTCraftingComponents.FRAME;

public class AstroHatchesAndBuses {

    public static void init(Consumer<FinishedRecipe> provider) {
        Material[] fluidMap = new Material[] { GTMaterials.Glue, GTMaterials.Polyethylene,
                GTMaterials.Polytetrafluoroethylene, GTMaterials.Polybenzimidazole, AstroMaterials.POLYAMIDE_IMIDE };

        for (var machine : GTMachines.FLUID_IMPORT_HATCH) {
            if (machine == null) continue;
            int tier = machine.getTier();
            int j = Math.min(fluidMap.length - 1, tier / 2);
            for (; j < fluidMap.length; j++) {
                int fluidAmount = GTValues.L * 2 * (tier + 1);
                GTRecipeTypes.ASSEMBLER_RECIPES
                        .recipeBuilder("fluid_hatch_" + VN[tier].toLowerCase(Locale.ROOT) + "_" + fluidMap[j].getName())
                        .inputItems(HULL.get(tier))
                        .inputItems(DRUM.get(tier))
                        .circuitMeta(1)
                        .inputFluids(fluidMap[j].getFluid(fluidAmount >> j))
                        .outputItems(machine)
                        .duration(300)
                        .EUt(VA[tier])
                        .save(provider);
            }
        }
        for (var machine : GTMachines.FLUID_EXPORT_HATCH) {
            if (machine == null) continue;
            int tier = machine.getTier();
            int j = Math.min(fluidMap.length - 1, tier / 2);
            for (; j < fluidMap.length; j++) {
                int fluidAmount = GTValues.L * 2 * (tier + 1);
                GTRecipeTypes.ASSEMBLER_RECIPES
                        .recipeBuilder(
                                "fluid_export_hatch_" + VN[tier].toLowerCase(Locale.ROOT) + "_" + fluidMap[j].getName())
                        .inputItems(HULL.get(tier))
                        .inputItems(DRUM.get(tier))
                        .circuitMeta(2)
                        .inputFluids(fluidMap[j].getFluid(fluidAmount >> j))
                        .outputItems(machine)
                        .duration(300)
                        .EUt(VA[tier])
                        .save(provider);
            }
        }

        for (var machine : GTMachines.ITEM_IMPORT_BUS) {
            if (machine == null) continue;
            int tier = machine.getTier();
            int j = Math.min(fluidMap.length - 1, tier / 2);
            for (; j < fluidMap.length; j++) {
                int fluidAmount = GTValues.L * 2 * (tier + 1);
                GTRecipeTypes.ASSEMBLER_RECIPES
                        .recipeBuilder(
                                "item_import_bus_" + VN[tier].toLowerCase(Locale.ROOT) + "_" + fluidMap[j].getName())
                        .inputItems(HULL.get(tier))
                        .inputItems(CRATE.get(tier))
                        .circuitMeta(1)
                        .inputFluids(fluidMap[j].getFluid(fluidAmount >> j))
                        .outputItems(machine)
                        .duration(300)
                        .EUt(VA[tier])
                        .save(provider);
            }
        }

        for (var machine : GTMachines.ITEM_EXPORT_BUS) {
            if (machine == null) continue;
            int tier = machine.getTier();
            int j = Math.min(fluidMap.length - 1, tier / 2);
            for (; j < fluidMap.length; j++) {
                int fluidAmount = GTValues.L * 2 * (tier + 1);
                GTRecipeTypes.ASSEMBLER_RECIPES
                        .recipeBuilder(
                                "item_export_bus_" + VN[tier].toLowerCase(Locale.ROOT) + "_" + fluidMap[j].getName())
                        .inputItems(HULL.get(tier))
                        .inputItems(CRATE.get(tier))
                        .circuitMeta(2)
                        .inputFluids(fluidMap[j].getFluid(fluidAmount >> j))
                        .outputItems(machine)
                        .duration(300)
                        .EUt(VA[tier])
                        .save(provider);
            }
        }

        for (var machine : GTMachines.DUAL_IMPORT_HATCH) {
            if (machine == null) continue;
            int tier = machine.getTier();
            int j = Math.min(fluidMap.length - 1, tier / 2);
            for (; j < fluidMap.length; j++) {
                int fluidAmount = GTValues.L * 8 * (tier + 1);
                GTRecipeTypes.ASSEMBLER_RECIPES
                        .recipeBuilder(
                                "dual_import_bus_" + VN[tier].toLowerCase(Locale.ROOT) + "_" + fluidMap[j].getName())
                        .inputItems(GTMachines.ITEM_IMPORT_BUS[tier])
                        .inputItems(GTMachines.FLUID_IMPORT_HATCH[tier])
                        .inputItems(PIPE_NONUPLE.get(tier))
                        .inputItems(FRAME.get(tier), 3)
                        .circuitMeta(1)
                        .inputFluids(fluidMap[j].getFluid(fluidAmount >> j))
                        .outputItems(machine)
                        .duration(300)
                        .EUt(VA[tier])
                        .save(provider);
            }
        }

        for (var machine : GTMachines.DUAL_EXPORT_HATCH) {
            if (machine == null) continue;
            int tier = machine.getTier();
            int j = Math.min(fluidMap.length - 1, tier / 2);
            for (; j < fluidMap.length; j++) {
                int fluidAmount = GTValues.L * 8 * (tier + 1);
                GTRecipeTypes.ASSEMBLER_RECIPES
                        .recipeBuilder(
                                "dual_export_bus_" + VN[tier].toLowerCase(Locale.ROOT) + "_" + fluidMap[j].getName())
                        .inputItems(GTMachines.ITEM_IMPORT_BUS[tier])
                        .inputItems(GTMachines.FLUID_IMPORT_HATCH[tier])
                        .inputItems(PIPE_NONUPLE.get(tier))
                        .inputItems(FRAME.get(tier), 3)
                        .circuitMeta(2)
                        .inputFluids(fluidMap[j].getFluid(fluidAmount >> j))
                        .outputItems(machine)
                        .duration(300)
                        .EUt(VA[tier])
                        .save(provider);
            }
        }
    }
}
