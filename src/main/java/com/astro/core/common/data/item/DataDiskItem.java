package com.astro.core.common.data.item;

import com.gregtechceu.gtceu.api.item.IComponentItem;
import com.gregtechceu.gtceu.api.item.component.IDataItem;
import com.gregtechceu.gtceu.api.item.component.IItemComponent;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataDiskItem extends Item implements IComponentItem {

    private final List<IItemComponent> components = new ArrayList<>();

    public DataDiskItem(Properties properties) {
        super(properties.stacksTo(1));
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
