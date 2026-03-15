package com.astro.core.common.data.item;

import com.gregtechceu.gtceu.api.item.IComponentItem;
import com.gregtechceu.gtceu.api.item.component.IDataItem;
import com.gregtechceu.gtceu.api.item.component.IItemComponent;

import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AstroPlanetaryDataItem extends Item implements IComponentItem {

    public static final String NBT_PLANET_ID = "PlanetResearchId";

    private final String planetId;
    private final String planetName;
    private final List<IItemComponent> components = new ArrayList<>();

    public AstroPlanetaryDataItem(String planetId, String planetName, Properties properties) {
        super(properties.stacksTo(1));
        this.planetId = planetId;
        this.planetName = planetName;
        attachComponents(new PlanetDataItemComponent());
    }

    @Override
    public List<IItemComponent> getComponents() {
        return components;
    }

    @Override
    public void attachComponents(IItemComponent... newComponents) {
        for (IItemComponent component : newComponents) {
            components.add(component);
        }
    }

    public ItemStack createResearched() {
        ItemStack stack = new ItemStack(this);
        CompoundTag tag = stack.getOrCreateTag();
        tag.putString(NBT_PLANET_ID, planetId);
        return stack;
    }

    @Nullable
    public static String getPlanetId(ItemStack stack) {
        if (stack.hasTag()) {
            CompoundTag tag = stack.getTag();
            if (tag != null && tag.contains(NBT_PLANET_ID)) {
                return tag.getString(NBT_PLANET_ID);
            }
        }
        return null;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        if (net.minecraft.client.gui.screens.Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("astrogreg.item.planetary_data.shift_header")
                    .withStyle(ChatFormatting.AQUA));
            tooltip.add(Component.literal(" - ")
                    .append(Component.literal(planetName)
                            .withStyle(ChatFormatting.WHITE)));
        } else {
            tooltip.add(Component.translatable("astrogreg.item.planetary_data.hold_shift")
                    .withStyle(ChatFormatting.GRAY));
        }
    }

    public static class PlanetDataItemComponent implements IDataItem, IItemComponent {

        @Override
        public void onAttached(Item item) {}

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
