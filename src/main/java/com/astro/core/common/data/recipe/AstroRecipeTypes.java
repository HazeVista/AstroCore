package com.astro.core.common.data.recipe;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.data.GTSoundEntries;

import com.lowdragmc.lowdraglib.gui.texture.ProgressTexture;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import com.astro.core.client.AstroGUITextures;
import com.astro.core.client.AstroSoundEntries;

import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;
import static com.lowdragmc.lowdraglib.gui.texture.ProgressTexture.FillDirection.DOWN_TO_UP;

public class AstroRecipeTypes {

    public static GTRecipeType AETHER_ENGINE_RECIPES;
    public static GTRecipeType MANA_BOILER_RECIPES;
    public static GTRecipeType DEIONIZATION_RECIPES;
    public static GTRecipeType RUNE_INSCRIPTION_RECIPES;
    public static GTRecipeType STEAM_BLAST_FURNACE_RECIPES;
    public static GTRecipeType FARADAY_GENERATOR_RECIPES;

    public static void init() {
        AETHER_ENGINE_RECIPES = register("aether_engine", MULTIBLOCK)
                .setMaxIOSize(0, 0, 1, 1)
                .setSlotOverlay(false, false, GuiTextures.BOX_OVERLAY)
                .setProgressBar(GuiTextures.PROGRESS_BAR_GAS_COLLECTOR, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
                .setSound(GTSoundEntries.TURBINE)
                .setEUIO(IO.OUT);

        MANA_BOILER_RECIPES = register("mana_boiler", MULTIBLOCK)
                .setMaxIOSize(1, 0, 1, 1)
                .setProgressBar(AstroGUITextures.PROGRESS_BAR_BOILER_FUEL_MANA, DOWN_TO_UP)
                .setMaxTooltips(1)
                .setSound(GTSoundEntries.FURNACE);

        DEIONIZATION_RECIPES = register("deionization", MULTIBLOCK)
                .setMaxIOSize(2, 0, 1, 1)
                .setProgressBar(GuiTextures.PROGRESS_BAR_BATH, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
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

        RUNE_INSCRIPTION_RECIPES = register("rune_inscription", MULTIBLOCK) // Bgame was Here
                .setEUIO(IO.IN)
                .setMaxIOSize(9, 1, 3, 0)
                .setProgressBar(AstroGUITextures.PROGRESS_BAR_RUNE, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
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
                .setProgressBar(GuiTextures.PRIMITIVE_BLAST_FURNACE_PROGRESS_BAR,
                        ProgressTexture.FillDirection.LEFT_TO_RIGHT)
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

        FARADAY_GENERATOR_RECIPES = register("faraday_generator", MULTIBLOCK)
                .setMaxIOSize(2, 1, 2, 0)
                .setProgressBar(GuiTextures.PROGRESS_BAR_MAGNET, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
                .setSound(GTSoundEntries.REPLICATOR)
                .setEUIO(IO.OUT);
    }
}
