package com.astro.core.common.data.recipe;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTSoundEntries;
import com.gregtechceu.gtceu.utils.ResearchManager;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import com.astro.core.AstroCore;
import com.astro.core.client.AstroGUITextures;
import com.astro.core.client.AstroSoundEntries;
import com.astro.core.common.data.AstroItems;
import com.astro.core.common.data.recipe.botania.CorruptDaisyRecipe;

import static com.astro.core.common.data.recipe.planetary_research.ObservatoryRecipeBuilder.buildPlanetaryResearchSlot;
import static com.astro.core.common.data.recipe.planetary_research.ObservatoryRecipeBuilder.createObservatoryResearchRecipe;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;
import static com.lowdragmc.lowdraglib.gui.texture.ProgressTexture.FillDirection.DOWN_TO_UP;
import static com.lowdragmc.lowdraglib.gui.texture.ProgressTexture.FillDirection.LEFT_TO_RIGHT;

@SuppressWarnings("all")
public class AstroRecipeTypes {

    public static GTRecipeType COMPOSTER_RECIPES;
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
    public static GTRecipeType CORRUPT_DAISY_RECIPES;

    public static final String OBSERVATORY_SCAN_ITEM_KEY = "observatory_scan_item";
    public static final String OBSERVATORY_CWUT_KEY = "observatory_cwut";
    public static final String OBSERVATORY_TOTAL_CWU_KEY = "observatory_total_cwu";
    public static final String OBSERVATORY_EUT_KEY = "observatory_eut";
    public static final String OBSERVATORY_PLANET_NAME_KEY = "observatory_planet_name";
    public static final String OBSERVATORY_PLANET_ITEM_KEY = "observatory_planet_item";
    public static final String OBSERVATORY_RESEARCH_ITEM_KEY = "observatory_research_item";

    // Valid values: "orb", "stick", "disk", "module"
    public static final String OBSERVATORY_RESEARCH_ITEM_TYPE_KEY = "observatory_research_item_type";

    public static void init() {
        COMPOSTER_RECIPES = register("composter", MULTIBLOCK)
                .setMaxIOSize(1, 1, 0, 0)
                .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT);

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
                .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
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
                .setSound(GTSoundEntries.COMPUTATION)
                .setIconSupplier(() -> {
                    try {
                        return BuiltInRegistries.ITEM
                                .getOptional(ResourceLocation.fromNamespaceAndPath("gtceu", "lv_sensor"))
                                .map(ItemStack::new)
                                .orElse(new ItemStack(Items.PAPER));
                    } catch (Exception e) {
                        return new ItemStack(Items.PAPER);
                    }
                });

        ASTROPORT_RECIPES = register("astroport", MULTIBLOCK)
                .setEUIO(IO.IN)
                .setMaxIOSize(16, 1, 4, 0)
                .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
                .setSound(GTSoundEntries.ASSEMBLER)
                .setHasResearchSlot(true)
                .setUiBuilder(buildPlanetaryResearchSlot())
                .onRecipeBuild((builder, provider) -> {
                    ResearchManager.createDefaultResearchRecipe(builder, provider);
                    createObservatoryResearchRecipe(builder, provider);
                })
                .setIconSupplier(() -> {
                    try {
                        return BuiltInRegistries.ITEM
                                .getOptional(ResourceLocation.fromNamespaceAndPath("ad_astra", "tier_1_rocket"))
                                .map(ItemStack::new)
                                .orElse(new ItemStack(Items.PAPER));
                    } catch (Exception e) {
                        return new ItemStack(Items.PAPER);
                    }
                });
    }

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPE_REGISTER = DeferredRegister
            .create(ForgeRegistries.RECIPE_TYPES, AstroCore.MOD_ID);

    public static final RegistryObject<RecipeType<CorruptDaisyRecipe>> CORRUPT_DAISY_TYPE = RECIPE_TYPE_REGISTER
            .register("corrupt_daisy", () -> new RecipeType<>() {

                @Override
                public String toString() {
                    return AstroCore.MOD_ID + ":corrupt_daisy";
                }
            });

    public static ItemStack getResearchItemForType(String type, int cwut) {
        return switch (type) {
            case "disk" -> AstroItems.DATA_DISK.asStack();
            case "stick" -> GTItems.TOOL_DATA_STICK.asStack();
            case "module" -> GTItems.TOOL_DATA_MODULE.asStack();
            default -> ResearchManager.getDefaultResearchStationItem(cwut);
        };
    }
}
