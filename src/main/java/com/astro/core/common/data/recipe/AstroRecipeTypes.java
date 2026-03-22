package com.astro.core.common.data.recipe;

import com.astro.core.common.data.item.AstroPlanetaryDataItem;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.data.GTSoundEntries;
import com.gregtechceu.gtceu.utils.ResearchManager;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import com.astro.core.client.AstroGUITextures;
import com.astro.core.client.AstroSoundEntries;
import com.astro.core.common.data.machine.conditions.PlanetaryResearchCondition;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;
import static com.lowdragmc.lowdraglib.gui.texture.ProgressTexture.FillDirection.DOWN_TO_UP;
import static com.lowdragmc.lowdraglib.gui.texture.ProgressTexture.FillDirection.LEFT_TO_RIGHT;

@SuppressWarnings("all")
public class AstroRecipeTypes {

    public static GTRecipeType AETHER_ENGINE_RECIPES;
    public static GTRecipeType MANA_BOILER_RECIPES;
    public static GTRecipeType DEIONIZATION_RECIPES;
    public static GTRecipeType RUNE_INSCRIPTION_RECIPES;
    public static GTRecipeType STEAM_BLAST_FURNACE_RECIPES;
    public static GTRecipeType FARADAY_GENERATOR_RECIPES;
    public static GTRecipeType KINETIC_COMBUSTION_RECIPES;
    public static GTRecipeType CONCRETE_PLANT;
    public static GTRecipeType INSCRIPTION;
    public static GTRecipeType ASTROPORT_RECIPES;
    public static GTRecipeType OBSERVATORY_RECIPES;

    public static final String OBSERVATORY_SCAN_ITEM_KEY = "observatory_scan_item";
    public static final String OBSERVATORY_CWUT_KEY = "observatory_cwut";
    public static final String OBSERVATORY_TOTAL_CWU_KEY = "observatory_total_cwu";
    public static final String OBSERVATORY_EUT_KEY = "observatory_eut";
    public static final String OBSERVATORY_PLANET_NAME_KEY = "observatory_planet_name";
    public static final String OBSERVATORY_PLANET_ITEM_KEY = "observatory_planet_item";

    public static void init() {
        AETHER_ENGINE_RECIPES = register("aether_engine", ELECTRIC)
                .setMaxIOSize(0, 0, 1, 1)
                .setSlotOverlay(false, false, GuiTextures.BOX_OVERLAY)
                .setProgressBar(GuiTextures.PROGRESS_BAR_GAS_COLLECTOR, LEFT_TO_RIGHT)
                .setSound(GTSoundEntries.TURBINE)
                .setEUIO(IO.OUT);

        MANA_BOILER_RECIPES = register("mana_boiler", MULTIBLOCK)
                .setMaxIOSize(1, 0, 1, 1)
                .setProgressBar(AstroGUITextures.PROGRESS_BAR_BOILER_FUEL_MANA, DOWN_TO_UP)
                .setMaxTooltips(1)
                .setSound(GTSoundEntries.FURNACE);

        DEIONIZATION_RECIPES = register("deionization", ELECTRIC)
                .setMaxIOSize(2, 0, 1, 1)
                .setProgressBar(GuiTextures.PROGRESS_BAR_BATH, LEFT_TO_RIGHT)
                .setSound(GTSoundEntries.BATH)
                .setEUIO(IO.IN)
                .setIconSupplier(() -> {
                    try {
                        return BuiltInRegistries.ITEM
                                .getOptional(ResourceLocation.fromNamespaceAndPath("astrogreg", "filter_cartridge"))
                                .map(ItemStack::new)
                                .orElse(new ItemStack(Items.PAPER));
                    } catch (Exception e) {
                        return new ItemStack(Items.PAPER);
                    }
                });

        RUNE_INSCRIPTION_RECIPES = register("rune_inscription", ELECTRIC)
                .setEUIO(IO.IN)
                .setMaxIOSize(9, 1, 3, 0)
                .setProgressBar(AstroGUITextures.PROGRESS_BAR_RUNE, LEFT_TO_RIGHT)
                .setSound(AstroSoundEntries.RUNE_ENGRAVER)
                .setIconSupplier(() -> {
                    try {
                        return BuiltInRegistries.ITEM
                                .getOptional(ResourceLocation.fromNamespaceAndPath("botania", "gregorious_rune"))
                                .map(ItemStack::new)
                                .orElse(new ItemStack(Items.PAPER));
                    } catch (Exception e) {
                        return new ItemStack(Items.PAPER);
                    }
                });

        STEAM_BLAST_FURNACE_RECIPES = register("steam_blast_furnace", MULTIBLOCK)
                .setMaxIOSize(1, 1, 0, 0)
                .setProgressBar(GuiTextures.PRIMITIVE_BLAST_FURNACE_PROGRESS_BAR, LEFT_TO_RIGHT)
                .setSound(GTSoundEntries.FURNACE)
                .setEUIO(IO.IN)
                .setIconSupplier(() -> {
                    try {
                        return BuiltInRegistries.ITEM
                                .getOptional(ResourceLocation.fromNamespaceAndPath("astrogreg", "steam_blast_furnace"))
                                .map(ItemStack::new)
                                .orElse(new ItemStack(Items.PAPER));
                    } catch (Exception e) {
                        return new ItemStack(Items.PAPER);
                    }
                });

        FARADAY_GENERATOR_RECIPES = register("faraday_generator", ELECTRIC)
                .setMaxIOSize(1, 1, 0, 0)
                .setProgressBar(GuiTextures.PROGRESS_BAR_MAGNET, LEFT_TO_RIGHT)
                .setSound(GTSoundEntries.REPLICATOR)
                .setEUIO(IO.OUT);

        KINETIC_COMBUSTION_RECIPES = register("kinetic_combustion_generator", MULTIBLOCK)
                .setMaxIOSize(0, 0, 1, 0)
                .setSlotOverlay(false, true, true, GuiTextures.FURNACE_OVERLAY_2)
                .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, LEFT_TO_RIGHT)
                .setSound(GTSoundEntries.COMBUSTION)
                .setEUIO(IO.OUT);

        CONCRETE_PLANT = register("concrete_plant", ELECTRIC)
                .setMaxIOSize(6, 1, 2, 1)
                .setProgressBar(GuiTextures.PROGRESS_BAR_MIXER, LEFT_TO_RIGHT)
                .setSound(GTSoundEntries.MIXER)
                .setEUIO(IO.IN);

        INSCRIPTION = register("inscription", ELECTRIC)
                .setEUIO(IO.IN)
                .setMaxIOSize(6, 1, 3, 0)
                .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
                .setSound(GTSoundEntries.REPLICATOR);

        OBSERVATORY_RECIPES = register("observatory", ELECTRIC)
                .setMaxIOSize(2, 2, 0, 0)
                .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
                .setSlotOverlay(false, false, GuiTextures.SCANNER_OVERLAY)
                .setSlotOverlay(true, false, GuiTextures.RESEARCH_STATION_OVERLAY)
                .setScanner(true)
                .setMaxTooltips(4)
                .setSound(GTSoundEntries.COMPUTATION);

        ASTROPORT_RECIPES = register("astroport", MULTIBLOCK)
                .setEUIO(IO.IN)
                .setMaxIOSize(16, 1, 4, 0)
                .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
                .setSound(GTSoundEntries.ASSEMBLER)
                .setHasResearchSlot(true)
                .setUiBuilder(buildPlanetaryResearchSlot())
                .onRecipeBuild((builder, provider) -> {
                    ResearchManager.createDefaultResearchRecipe(builder, provider);
                    AstroRecipeTypes.createObservatoryResearchRecipe(builder, provider);
                });
    }

    private static void createObservatoryResearchRecipe(GTRecipeBuilder builder,
                                                        Consumer<FinishedRecipe> provider) {
        String planetId = builder.data.getString(PlanetaryResearchCondition.RECIPE_DATA_KEY);
        if (planetId.isEmpty()) return;

        String scanItemId = builder.data.getString(OBSERVATORY_SCAN_ITEM_KEY);
        int cwut = builder.data.getInt(OBSERVATORY_CWUT_KEY);
        int totalCwu = builder.data.getInt(OBSERVATORY_TOTAL_CWU_KEY);
        long eut = builder.data.getLong(OBSERVATORY_EUT_KEY);

        if (cwut <= 0) cwut = 16;
        if (totalCwu <= 0) totalCwu = 1200;
        if (eut <= 0) eut = VA[HV];

        ItemStack blankOrb = new ItemStack(ResearchManager.getDefaultResearchStationItem(cwut).getItem());

        AstroPlanetaryDataItem planetItem = AstroPlanetaryDataItem.getForPlanet(planetId);
        ItemStack recipeOutput;
        if (planetItem != null) {
            recipeOutput = new ItemStack(planetItem);
            CompoundTag outputTag = recipeOutput.getOrCreateTag();
            ResearchManager.writeResearchToNBT(outputTag, planetId, OBSERVATORY_RECIPES);
        } else {
            recipeOutput = ResearchManager.getDefaultResearchStationItem(cwut);
            ResearchManager.writeResearchToNBT(recipeOutput.getOrCreateTag(), planetId, OBSERVATORY_RECIPES);
        }

        ItemStack astroportOutput = ItemStack.EMPTY;
        var astroportOutputs = builder.output.getOrDefault(
                com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability.CAP,
                java.util.Collections.emptyList());
        if (!astroportOutputs.isEmpty()) {
            var items = com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability.CAP
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

        recipeBuilder.save(provider);
    }

    private static BiConsumer<GTRecipe, WidgetGroup> buildPlanetaryResearchSlot() {
        return (recipe, group) -> {
            String planetId = recipe.data.getString(PlanetaryResearchCondition.RECIPE_DATA_KEY);
            if (planetId.isEmpty()) return;

            String planetName = recipe.data.getString(OBSERVATORY_PLANET_NAME_KEY);

            ItemStack displayStack = ResearchManager.getDefaultResearchStationItem(16);
            ResearchManager.writeResearchToNBT(displayStack.getOrCreateTag(), planetId, OBSERVATORY_RECIPES);

            String planetItemId = recipe.data.getString(OBSERVATORY_PLANET_ITEM_KEY);
            final ItemStack orbStack = displayStack;
            final ItemStack planetStack = !planetItemId.isEmpty()
                    ? BuiltInRegistries.ITEM.getOptional(ResourceLocation.tryParse(planetItemId))
                    .map(ItemStack::new).orElse(ItemStack.EMPTY)
                    : ItemStack.EMPTY;

            final String finalPlanetName = planetName;
            final String finalPlanetId = planetId;

            final com.lowdragmc.lowdraglib.gui.texture.ResourceTexture planetTex = !planetStack.isEmpty()
                    ? new com.lowdragmc.lowdraglib.gui.texture.ResourceTexture(
                    "astrogreg:textures/item/" +
                            (planetItemId.contains(":") ? planetItemId.split(":")[1] : planetItemId) + ".png")
                    : null;

            net.minecraftforge.items.ItemStackHandler orbHandler = new net.minecraftforge.items.ItemStackHandler(1);
            orbHandler.setStackInSlot(0, orbStack);

            SlotWidget planetarySlot = new SlotWidget(orbHandler, 0, 130, 41, false, false);
            planetarySlot.setBackgroundTexture(new GuiTextureGroup(GuiTextures.SLOT, GuiTextures.DATA_ORB_OVERLAY));

            if (planetTex != null) {
                planetarySlot.setItemHook(stack ->
                        net.minecraft.client.gui.screens.Screen.hasShiftDown() ? planetStack : orbStack);
            }

            planetarySlot.setOverlay(GuiTextures.DATA_ORB_OVERLAY);

            planetarySlot.setOnAddedTooltips((w, tooltips) -> {
                tooltips.add(net.minecraft.network.chat.Component.translatable("astrogreg.item.planetary_data.title"));
                tooltips.add(net.minecraft.network.chat.Component.translatable("astrogreg.item.planetary_data.entry",
                        finalPlanetName.isEmpty() ? finalPlanetId : finalPlanetName));
            });
            group.addWidget(planetarySlot);
        };
    }
}