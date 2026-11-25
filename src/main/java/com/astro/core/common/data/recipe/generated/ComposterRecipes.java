package com.astro.core.common.data.recipe.generated;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;

import com.astro.core.AstroCore;
import com.astro.core.common.data.recipe.AstroRecipeTypes;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;

@SuppressWarnings("all")
public class ComposterRecipes {

    private static final int PRIMITIVE_DURATION_TICKS = 60;
    private static final int INDUSTRIAL_DURATION_TICKS = 100;
    private static final int INDUSTRIAL_EU_PER_TICK = 64;
    private static final int MAX_CHANCE = 10000;

    private static final ResourceLocation BONE_MEAL_ID = new ResourceLocation("minecraft", "bone_meal");
    private static final ResourceLocation FERTILIZER_ID = new ResourceLocation("gtceu", "fertilizer");

    public static void init() {
        ComposterBlock.COMPOSTABLES.put(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Ash).getItem(), 0.20f);
        ComposterBlock.COMPOSTABLES.put(ChemicalHelper.get(TagPrefix.dust, GTMaterials.DarkAsh).getItem(), 0.40f);

        initPrimitive();
        initIndustrial();
    }

    private static void initPrimitive() {
        AstroRecipeTypes.COMPOSTER_RECIPES.addCustomRecipeLogic(new GTRecipeType.ICustomRecipeLogic() {

            @Override
            public GTRecipe createCustomRecipe(IRecipeCapabilityHolder holder) {
                for (var handler : holder.getCapabilitiesFlat(IO.IN, ItemRecipeCapability.CAP)) {
                    for (var content : handler.getContents()) {
                        if (!(content instanceof ItemStack stack) || stack.isEmpty()) continue;
                        Float chance = ComposterBlock.COMPOSTABLES.get(stack.getItem());
                        if (chance == null) continue;

                        var itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
                        if (itemId == null) continue;

                        int outputCount = Math.max(1, (int) (chance / 0.2f));
                        return AstroRecipeTypes.COMPOSTER_RECIPES
                                .recipeBuilder(AstroCore.id("composter_" + itemId.getNamespace() + "_" +
                                        itemId.getPath().replace('/', '_')))
                                .inputItems(stack.getItem())
                                .outputItems(TagPrefix.dustTiny, GTMaterials.Bone, outputCount)
                                .duration(PRIMITIVE_DURATION_TICKS)
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

                    var itemId = BuiltInRegistries.ITEM.getKey(entry.getKey().asItem());
                    if (itemId == null) continue;

                    int outputCount = Math.max(1, (int) (chance / 0.2f));
                    GTRecipe recipe = AstroRecipeTypes.COMPOSTER_RECIPES
                            .recipeBuilder(AstroCore.id("composter_" + itemId.getNamespace() + "_" +
                                    itemId.getPath().replace('/', '_')))
                            .inputItems(entry.getKey().asItem())
                            .outputItems(TagPrefix.dustTiny, GTMaterials.Bone, outputCount)
                            .duration(PRIMITIVE_DURATION_TICKS)
                            .buildRawRecipe();
                    recipe.setId(recipe.getId().withPrefix("/"));
                    AstroRecipeTypes.COMPOSTER_RECIPES.addToMainCategory(recipe);
                }
            }
        });
    }

    private static void initIndustrial() {
        AstroRecipeTypes.INDUSTRIAL_COMPOSTER_RECIPES.addCustomRecipeLogic(new GTRecipeType.ICustomRecipeLogic() {

            @Override
            public GTRecipe createCustomRecipe(IRecipeCapabilityHolder holder) {
                ItemStack compostable = null;
                float compostChance = 0f;
                int circuitConfig = -1;

                for (var handler : holder.getCapabilitiesFlat(IO.IN, ItemRecipeCapability.CAP)) {
                    for (var content : handler.getContents()) {
                        if (!(content instanceof ItemStack stack) || stack.isEmpty()) continue;

                        if (stack.is(GTItems.PROGRAMMED_CIRCUIT.get())) {
                            circuitConfig = IntCircuitBehaviour.getCircuitConfiguration(stack);
                            continue;
                        }

                        if (compostable == null) {
                            Float chance = ComposterBlock.COMPOSTABLES.get(stack.getItem());
                            if (chance != null) {
                                compostable = stack;
                                compostChance = chance;
                            }
                        }
                    }
                }

                if (compostable == null) return null;

                var itemId = BuiltInRegistries.ITEM.getKey(compostable.getItem());
                if (itemId == null) return null;

                return buildIndustrialRecipe(itemId, compostable.getItem(), compostChance, circuitConfig == 1, false);
            }

            @Override
            public void buildRepresentativeRecipes() {
                for (Object2FloatMap.Entry<ItemLike> entry : ComposterBlock.COMPOSTABLES.object2FloatEntrySet()) {
                    float chance = entry.getFloatValue();
                    if (chance <= 0f) continue;

                    var itemId = BuiltInRegistries.ITEM.getKey(entry.getKey().asItem());
                    if (itemId == null) continue;

                    buildIndustrialRecipe(itemId, entry.getKey().asItem(), chance, true, true);
                    buildIndustrialRecipe(itemId, entry.getKey().asItem(), chance, false, true);
                }
            }

            private GTRecipe buildIndustrialRecipe(ResourceLocation itemId, ItemLike input, float baseChance,
                                                   boolean useBoneMeal, boolean isRepresentative) {
                float appliedChance = useBoneMeal ? baseChance : baseChance / 2f;
                int chanceValue = Math.round(appliedChance * MAX_CHANCE);
                if (chanceValue <= 0) return null;

                ResourceLocation outputId = useBoneMeal ? BONE_MEAL_ID : FERTILIZER_ID;
                ItemStack outputStack = new ItemStack(BuiltInRegistries.ITEM.get(outputId));

                String recipeName = "industrial_composter_" + itemId.getNamespace() + "_" +
                        itemId.getPath().replace('/', '_') + (useBoneMeal ? "_circuit1" : "_default");

                GTRecipe recipe = AstroRecipeTypes.INDUSTRIAL_COMPOSTER_RECIPES
                        .recipeBuilder(AstroCore.id(recipeName))
                        .inputItems(input.asItem())
                        .circuitMeta(useBoneMeal ? 1 : 0)
                        .chancedOutput(outputStack, chanceValue, 0)
                        .duration(INDUSTRIAL_DURATION_TICKS)
                        .EUt(INDUSTRIAL_EU_PER_TICK)
                        .buildRawRecipe();

                if (isRepresentative && recipe != null) {
                    recipe.setId(recipe.getId().withPrefix("/"));
                    AstroRecipeTypes.INDUSTRIAL_COMPOSTER_RECIPES.addToMainCategory(recipe);
                }

                return recipe;
            }
        });
    }
}