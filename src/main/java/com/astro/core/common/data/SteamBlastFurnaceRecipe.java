package com.astro.core.common.data;

import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.ingredient.EnergyStack;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.astro.core.AstroCore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = AstroCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class SteamBlastFurnaceRecipe {

    public static final int BASE_EUT = 32;

    private SteamBlastFurnaceRecipe() {}

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        rebuildFrom(event.getServer());
    }

    public static void rebuildFrom(MinecraftServer server) {
        if (server == null) return;
        rebuildFrom(server.getRecipeManager());
    }

    public static void rebuildFrom(RecipeManager recipeManager) {
        if (recipeManager == null) return;
        if (AstroRecipeTypes.STEAM_BLAST_FURNACE_RECIPES == null) return;

        // Avoid duplicates if called more than once
        AstroRecipeTypes.STEAM_BLAST_FURNACE_RECIPES.getLookup().removeAllRecipes();

        List<GTRecipe> primitiveRecipes = recipeManager.getAllRecipesFor(GTRecipeTypes.PRIMITIVE_BLAST_FURNACE_RECIPES);

        int added = 0;
        for (GTRecipe src : primitiveRecipes) {
            GTRecipe copied = copyPrimitiveToSteam(src, AstroRecipeTypes.STEAM_BLAST_FURNACE_RECIPES, BASE_EUT);
            if (copied != null && AstroRecipeTypes.STEAM_BLAST_FURNACE_RECIPES.getLookup().addRecipe(copied)) {
                added++;
            }
        }

        AstroCore.LOGGER.info(
                "SteamBlastFurnaceRecipeCopies: copied {} primitive blast furnace recipes into {}",
                added, AstroRecipeTypes.STEAM_BLAST_FURNACE_RECIPES);
    }

    private static GTRecipe copyPrimitiveToSteam(GTRecipe src, GTRecipeType destType, int baseEUt) {
        if (src == null || destType == null) return null;

        // Unique id (not registered in RecipeManager; used for conflict detection / debug)
        ResourceLocation srcId = src.getId();
        String srcNs = (srcId != null) ? srcId.getNamespace() : "unknown";
        String srcPath = (srcId != null) ? srcId.getPath() : ("generated_" + System.identityHashCode(src));
        ResourceLocation newId = AstroCore.id("steam_blast_furnace/" + srcNs + "/" + srcPath);

        // Deep-ish copy contents
        Map<RecipeCapability<?>, List<Content>> inputs = ContentModifier.IDENTITY.applyContents(src.inputs);
        Map<RecipeCapability<?>, List<Content>> outputs = ContentModifier.IDENTITY.applyContents(src.outputs);
        Map<RecipeCapability<?>, List<Content>> tickInputs = ContentModifier.IDENTITY.applyContents(src.tickInputs);
        Map<RecipeCapability<?>, List<Content>> tickOutputs = ContentModifier.IDENTITY.applyContents(src.tickOutputs);

        // Inject EU/t as a per-tick input (SteamEnergyRecipeHandler reads this)
        EURecipeCapability.putEUContent(tickInputs, new EnergyStack(Math.max(1, baseEUt)));

        Map<RecipeCapability<?>, ?> inputChance = new HashMap<>(src.inputChanceLogics);
        Map<RecipeCapability<?>, ?> outputChance = new HashMap<>(src.outputChanceLogics);
        Map<RecipeCapability<?>, ?> tickInputChance = new HashMap<>(src.tickInputChanceLogics);
        Map<RecipeCapability<?>, ?> tickOutputChance = new HashMap<>(src.tickOutputChanceLogics);

        // Construct the new recipe under the new type
        GTRecipe out = new GTRecipe(destType, newId,
                inputs, outputs,
                tickInputs, tickOutputs,
                (Map) inputChance, (Map) outputChance,
                (Map) tickInputChance, (Map) tickOutputChance,
                new ArrayList<>(src.conditions),
                new ArrayList<>(src.ingredientActions),
                src.data.copy(),
                src.duration,
                destType.getCategory());

        // Preserve a few flags
        out.ocLevel = src.ocLevel;
        out.batchParallels = src.batchParallels;
        out.subtickParallels = src.subtickParallels;

        return out;
    }
}
