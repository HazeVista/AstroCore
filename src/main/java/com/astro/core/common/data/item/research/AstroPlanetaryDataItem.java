package com.astro.core.common.data.item.research;

import com.gregtechceu.gtceu.api.item.IComponentItem;
import com.gregtechceu.gtceu.api.item.component.IDataItem;
import com.gregtechceu.gtceu.api.item.component.IItemComponent;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("all")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AstroPlanetaryDataItem extends Item implements IComponentItem {

    public static final String NBT_PLANET_ID = "PlanetResearchId";

    private static final Map<String, AstroPlanetaryDataItem> REGISTRY = new HashMap<>();

    private final String planetId;
    private final String planetName;
    private final List<IItemComponent> components = new ArrayList<>();

    public AstroPlanetaryDataItem(String planetId, String planetName, Properties properties) {
        super(properties.stacksTo(1));
        this.planetId = planetId;
        this.planetName = planetName;
        REGISTRY.put(planetId, this);
        attachComponents(new PlanetDataItemComponent(this));
    }

    @Nullable
    public static AstroPlanetaryDataItem getForPlanet(String planetId) {
        return REGISTRY.get(planetId);
    }

    @Override
    public List<IItemComponent> getComponents() {
        return components;
    }

    @Override
    public void attachComponents(IItemComponent... newComponents) {
        components.addAll(List.of(newComponents));
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

    public static class PlanetDataItemComponent implements IDataItem, IItemComponent {

        private final AstroPlanetaryDataItem owner;

        public PlanetDataItemComponent(AstroPlanetaryDataItem owner) {
            this.owner = owner;
        }

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
