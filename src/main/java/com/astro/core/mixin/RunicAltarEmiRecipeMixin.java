package com.astro.core.mixin;

import com.astro.core.common.data.AstroItems;
import com.astro.core.mixin.accessor.BotaniaEmiRecipeAccessor;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import vazkii.botania.client.integration.emi.RunicAltarEmiRecipe;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;

import java.util.List;

@Mixin(value = RunicAltarEmiRecipe.class, remap = false)
public class RunicAltarEmiRecipeMixin {

    @Shadow
    @Final
    private List<EmiIngredient> ingredients;

    @Shadow
    @Final
    private int mana;

    @Unique
    private static final EmiStack astro$RUNE_TABLET = EmiStack.of(AstroItems.RUNE_TABLET.get());

    @Unique
    private static final EmiStack astro$ALTAR = EmiStack.of(BotaniaBlocks.runeAltar);

    @Overwrite
    public void addWidgets(WidgetHolder widgets) {
        BotaniaEmiRecipeAccessor accessor = (BotaniaEmiRecipeAccessor) this;

        widgets.add(new vazkii.botania.client.integration.emi.ManaWidget(
                2, 100, mana, ManaPoolBlockEntity.MAX_MANA / 10));
        RunicAltarEmiRecipe.addRunicAltarWidgets(
                widgets,
                (RunicAltarEmiRecipe) (Object) this,
                ingredients,
                astro$ALTAR,
                accessor.astro$getOutput().get(0),
                astro$RUNE_TABLET);
    }
}
