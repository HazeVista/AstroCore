package com.astro.core.common.data.recipe.generated;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;

import com.astro.core.AstroCore;
import com.astro.core.common.data.recipe.AstroRecipeTypes;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;

@SuppressWarnings("all")
public class ComposterRecipes {

    private static final int DURATION_TICKS = 60;

    public static void init() {
        ComposterBlock.COMPOSTABLES.put(
                ChemicalHelper.get(TagPrefix.dust, GTMaterials.Ash).getItem(), 0.20f);
        ComposterBlock.COMPOSTABLES.put(
                ChemicalHelper.get(TagPrefix.dust, GTMaterials.DarkAsh).getItem(), 0.40f);

        AstroRecipeTypes.COMPOSTER_RECIPES.addCustomRecipeLogic(new GTRecipeType.ICustomRecipeLogic() {

            @Override
            public GTRecipe createCustomRecipe(IRecipeCapabilityHolder holder) {
                var handlers = holder.getCapabilitiesFlat(IO.IN, ItemRecipeCapability.CAP);
                for (var handler : handlers) {
                    for (var content : handler.getContents()) {
                        if (!(content instanceof ItemStack stack) || stack.isEmpty()) continue;
                        Float chance = ComposterBlock.COMPOSTABLES.get(stack.getItem());
                        if (chance == null) continue;
                        int outputCount = Math.max(1, (int) (chance / 0.2f));
                        var itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
                        if (itemId == null) continue;
                        return AstroRecipeTypes.COMPOSTER_RECIPES
                                .recipeBuilder(AstroCore.id("composter_" + itemId.getNamespace() + "_" +
                                        itemId.getPath().replace('/', '_')))
                                .inputItems(stack.getItem())
                                .outputItems(TagPrefix.dustTiny, GTMaterials.Bone, outputCount)
                                .duration(DURATION_TICKS)
                                .buildRawRecipe();
                    }
                }
                return null;
            }

            @Override
            public void buildRepresentativeRecipes() {
                for (Object2FloatMap.Entry<ItemLike> entry : ComposterBlock.COMPOSTABLES.object2FloatEntrySet()) {
                    float chance = entry.getFloatValue();
                    if (chance <= 0f) continue;
                    int outputCount = Math.max(1, (int) (chance / 0.2f));
                    var itemId = BuiltInRegistries.ITEM.getKey(entry.getKey().asItem());
                    if (itemId == null) continue;
                    GTRecipe recipe = AstroRecipeTypes.COMPOSTER_RECIPES
                            .recipeBuilder(AstroCore.id(
                                    "composter_" + itemId.getNamespace() + "_" + itemId.getPath().replace('/', '_')))
                            .inputItems(entry.getKey().asItem())
                            .outputItems(TagPrefix.dustTiny, GTMaterials.Bone, outputCount)
                            .duration(DURATION_TICKS)
                            .buildRawRecipe();
                    recipe.setId(recipe.getId().withPrefix("/"));
                    AstroRecipeTypes.COMPOSTER_RECIPES.addToMainCategory(recipe);
                }
            }
        });
    }
}
