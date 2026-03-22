package com.astro.core.common.data.recipe.generated;

import com.astro.core.common.data.recipe.AstroRecipeTypes;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;

import net.minecraft.world.item.ItemStack;

import java.util.function.UnaryOperator;

@SuppressWarnings("all")
public class ObservatoryResearchBuilder {

    private ItemStack researchStack = ItemStack.EMPTY;
    private int cwut = 1;
    private int totalCWU = 1200;
    private long eut = GTValues.VA[GTValues.HV];
    private String planetName = "";
    private String planetDisplayItem = "";

    public ObservatoryResearchBuilder researchStack(ItemStack stack) {
        this.researchStack = stack;
        return this;
    }

    public ObservatoryResearchBuilder CWUt(int cwut) {
        this.cwut = cwut;
        return this;
    }

    public ObservatoryResearchBuilder CWUt(int cwut, int totalCWU) {
        this.cwut = cwut;
        this.totalCWU = totalCWU;
        return this;
    }

    public ObservatoryResearchBuilder totalCWU(int totalCWU) {
        this.totalCWU = totalCWU;
        return this;
    }

    public ObservatoryResearchBuilder EUt(long eut) {
        this.eut = eut;
        return this;
    }

    public ObservatoryResearchBuilder planetName(String name) {
        this.planetName = name;
        return this;
    }

    public ObservatoryResearchBuilder planetDisplayItem(net.minecraft.resources.ResourceLocation itemId) {
        this.planetDisplayItem = itemId.toString();
        return this;
    }

    public void apply(GTRecipeBuilder builder) {
        if (!researchStack.isEmpty()) {
            builder.addData(AstroRecipeTypes.OBSERVATORY_SCAN_ITEM_KEY,
                    net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(researchStack.getItem()).toString());
        }
        builder.addData(AstroRecipeTypes.OBSERVATORY_CWUT_KEY, cwut);
        builder.addData(AstroRecipeTypes.OBSERVATORY_TOTAL_CWU_KEY, totalCWU);
        builder.addData(AstroRecipeTypes.OBSERVATORY_EUT_KEY, eut);
        if (!planetName.isEmpty()) {
            builder.addData(AstroRecipeTypes.OBSERVATORY_PLANET_NAME_KEY, planetName);
        }
        if (!planetDisplayItem.isEmpty()) {
            builder.addData(AstroRecipeTypes.OBSERVATORY_PLANET_ITEM_KEY, planetDisplayItem);
        }
    }

    public static GTRecipeBuilder applyTo(GTRecipeBuilder builder,
                                          UnaryOperator<ObservatoryResearchBuilder> config) {
        config.apply(new ObservatoryResearchBuilder()).apply(builder);
        return builder;
    }
}