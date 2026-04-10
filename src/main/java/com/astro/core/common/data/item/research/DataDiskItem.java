package com.astro.core.common.data.item.research;

import com.gregtechceu.gtceu.api.item.IComponentItem;
import com.gregtechceu.gtceu.api.item.component.IDataItem;
import com.gregtechceu.gtceu.api.item.component.IItemComponent;
import com.gregtechceu.gtceu.utils.ResearchManager;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import com.astro.core.common.data.recipe.AstroRecipeTypes;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class DataDiskItem extends Item implements IComponentItem {

    private final List<IItemComponent> components = new ArrayList<>();

    public DataDiskItem(Properties properties) {
        super(properties.stacksTo(64));
        components.add(new DataDiskComponent());
    }

    @Override
    public List<IItemComponent> getComponents() {
        return components;
    }

    @Override
    public void attachComponents(IItemComponent... newComponents) {
        components.addAll(List.of(newComponents));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        String key = stack.getItem().getDescriptionId() + ".tooltip";
        if (I18n.exists(key)) {
            tooltip.add(Component.translatable(key));
        }
        if (!stack.hasTag()) return;

        if (stack.hasTag()) {
            var researchData = ResearchManager.readResearchId(stack);
            if (researchData != null && researchData.recipeType() == AstroRecipeTypes.OBSERVATORY_RECIPES) {
                var recipes = researchData.recipeType().getDataStickEntry(researchData.researchId());
                if (recipes == null || recipes.isEmpty()) return;
                String planetName = researchData.researchId();
                String planetItemId = "";
                for (var recipe : recipes) {
                    String name = recipe.data.getString(AstroRecipeTypes.OBSERVATORY_PLANET_NAME_KEY);
                    if (!name.isEmpty()) {
                        planetName = name;
                        break;
                    }
                }
                for (var recipe : recipes) {
                    String itemId = recipe.data.getString(AstroRecipeTypes.OBSERVATORY_PLANET_ITEM_KEY);
                    if (!itemId.isEmpty()) {
                        planetItemId = itemId;
                        break;
                    }
                }
                tooltip.add(Component.translatable("astrogreg.item.planetary_data.title"));
                tooltip.add(Component.translatable("astrogreg.item.planetary_data.entry", planetName));
            }
        }
    }

    public static class DataDiskComponent implements IDataItem, IItemComponent {

        @Override
        public boolean requireDataBank() {
            return false;
        }

        @Override
        public int getCapacity() {
            return 1;
        }
    }
}
