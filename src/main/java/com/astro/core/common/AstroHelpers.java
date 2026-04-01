package com.astro.core.common;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.ItemMaterialData;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.ItemMaterialInfo;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import com.astro.core.AstroCore;
import it.unimi.dsi.fastutil.objects.Reference2LongOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Random;

@SuppressWarnings("all")
public class AstroHelpers {

    public static final Random RANDOM = new Random();

    public static boolean isMaterialRegistrationFinished;

    @Nullable
    public static Material getMaterial(@NotNull String materialName) {
        var material = GTCEuAPI.materialManager.getMaterial(materialName);
        if (material == null) {
            material = GTCEuAPI.materialManager.getMaterial(AstroCore.MOD_ID + ":" + materialName);
        }

        return material;
    }

    public static void registerMaterialInfo(ResourceLocation itemId, Map<String, Double> materialStacks) {
        Item item = ForgeRegistries.ITEMS.getValue(itemId);
        if (item == null) {
            AstroCore.LOGGER.error("Error in registerMaterialInfo - item not found: {}", itemId);
            return;
        }

        Reference2LongOpenHashMap<Material> matStacks = new Reference2LongOpenHashMap<>();
        for (var tuple : materialStacks.entrySet()) {
            var material = getMaterial(tuple.getKey());
            if (material == null) {
                AstroCore.LOGGER.error("Error in registerMaterialInfo - material not found: {}", tuple.getKey());
                return;
            }

            matStacks.addTo(material, Math.round(tuple.getValue() * GTValues.M));
        }

        ItemMaterialData.registerMaterialInfo(item, new ItemMaterialInfo(matStacks));
    }
}
