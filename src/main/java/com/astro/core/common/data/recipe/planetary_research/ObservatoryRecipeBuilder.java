package com.astro.core.common.data.recipe.planetary_research;

import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.gregtechceu.gtceu.utils.ResearchManager;

import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

import com.astro.core.common.data.item.research.AstroPlanetaryDataItem;
import com.astro.core.common.data.machine.conditions.PlanetaryResearchCondition;
import com.astro.core.common.data.recipe.AstroRecipeTypes;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.astro.core.common.data.recipe.AstroRecipeTypes.*;
import static com.gregtechceu.gtceu.api.GTValues.*;

@SuppressWarnings("all")
public class ObservatoryRecipeBuilder {

    public static void createObservatoryResearchRecipe(GTRecipeBuilder builder,
                                                       Consumer<FinishedRecipe> provider) {
        String planetId = builder.data.getString(PlanetaryResearchCondition.RECIPE_DATA_KEY);
        if (planetId.isEmpty()) return;

        String scanItemId = builder.data.getString(OBSERVATORY_SCAN_ITEM_KEY);
        int cwut = builder.data.getInt(OBSERVATORY_CWUT_KEY);
        int totalCwu = builder.data.getInt(OBSERVATORY_TOTAL_CWU_KEY);
        long eut = builder.data.getLong(OBSERVATORY_EUT_KEY);
        String researchItemType = builder.data.getString(OBSERVATORY_RESEARCH_ITEM_TYPE_KEY);

        if (cwut <= 0) cwut = 16;
        if (totalCwu <= 0) totalCwu = 1200;
        if (eut <= 0) eut = VA[HV];

        ItemStack researchItem = AstroRecipeTypes.getResearchItemForType(researchItemType, cwut);
        ItemStack blankOrb = new ItemStack(researchItem.getItem());

        AstroPlanetaryDataItem planetItem = AstroPlanetaryDataItem.getForPlanet(planetId);
        ItemStack recipeOutput;
        if (planetItem != null) {
            recipeOutput = new ItemStack(planetItem);
            CompoundTag outputTag = recipeOutput.getOrCreateTag();
            ResearchManager.writeResearchToNBT(outputTag, planetId, OBSERVATORY_RECIPES);
        } else {
            recipeOutput = researchItem.copy();
            ResearchManager.writeResearchToNBT(recipeOutput.getOrCreateTag(), planetId, OBSERVATORY_RECIPES);
        }

        ItemStack astroportOutput = ItemStack.EMPTY;
        var astroportOutputs = builder.output.getOrDefault(
                ItemRecipeCapability.CAP,
                java.util.Collections.emptyList());
        if (!astroportOutputs.isEmpty()) {
            var items = ItemRecipeCapability.CAP
                    .of(astroportOutputs.get(0).content).getItems();
            if (items.length > 0) astroportOutput = items[0];
        }

        var recipeBuilder = OBSERVATORY_RECIPES.recipeBuilder(planetId.replace(":", "_") + "_planetary_scan")
                .inputItems(blankOrb)
                .outputItems(astroportOutput.isEmpty() ? recipeOutput : astroportOutput.copy())
                .outputItems(recipeOutput)
                .CWUt(cwut)
                .totalCWU(totalCwu)
                .EUt(eut)
                .researchScan(true);

        if (!scanItemId.isEmpty()) {
            BuiltInRegistries.ITEM.getOptional(ResourceLocation.tryParse(scanItemId))
                    .ifPresent(item -> recipeBuilder.inputItems(new ItemStack(item)));
        }

        String planetItemId = builder.data.getString(OBSERVATORY_PLANET_ITEM_KEY);
        if (!planetItemId.isEmpty()) {
            recipeBuilder.addData(OBSERVATORY_PLANET_ITEM_KEY, planetItemId);
        }

        recipeBuilder.save(provider);

        OBSERVATORY_RECIPES.addDataStickEntry(planetId, recipeBuilder.buildRawRecipe());
    }

    public static BiConsumer<GTRecipe, WidgetGroup> buildPlanetaryResearchSlot() {
        return (recipe, group) -> {
            String planetId = recipe.data.getString(PlanetaryResearchCondition.RECIPE_DATA_KEY);
            if (planetId.isEmpty()) return;

            String researchItemType = recipe.data.getString(OBSERVATORY_RESEARCH_ITEM_TYPE_KEY);
            int cwut = recipe.data.getInt(OBSERVATORY_CWUT_KEY);
            if (cwut <= 0) cwut = 16;

            String planetItemId = recipe.data.getString(OBSERVATORY_PLANET_ITEM_KEY);

            ItemStack displayStack = AstroRecipeTypes.getResearchItemForType(researchItemType, cwut);
            ResearchManager.writeResearchToNBT(displayStack.getOrCreateTag(), planetId, OBSERVATORY_RECIPES);

            final ItemStack orbStack = displayStack;
            final ItemStack planetStack = !planetItemId.isEmpty() ?
                    BuiltInRegistries.ITEM.getOptional(ResourceLocation.tryParse(planetItemId))
                            .map(ItemStack::new).orElse(ItemStack.EMPTY) :
                    ItemStack.EMPTY;

            SimpleContainer container = new SimpleContainer(1);
            container.setItem(0, orbStack);

            SlotWidget planetarySlot = new SlotWidget(container, 0, 130, 41, false, false);
            planetarySlot.setBackgroundTexture(new GuiTextureGroup(
                    GuiTextures.SLOT,
                    GuiTextures.DATA_ORB_OVERLAY));
            planetarySlot.setOverlay(new ResourceTexture("astrogreg:textures/gui/overlay/planet_slot_overlay.png"));

            if (!planetStack.isEmpty()) {
                planetarySlot.setItemHook(stack -> Screen.hasShiftDown() ? planetStack : orbStack);
            }

            group.addWidget(planetarySlot);
        };
    }
}
