package com.astro.core.common.machine.part;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.BlockableSlotWidget;
import com.gregtechceu.gtceu.api.item.IComponentItem;
import com.gregtechceu.gtceu.api.item.component.IDataItem;
import com.gregtechceu.gtceu.api.item.component.IItemComponent;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;

import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.utils.Position;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.ItemStack;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("all")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ObservatoryObjectHolderMachine extends MultiblockPartMachine {

    @Persisted
    private final ObservatoryHolderHandler heldItems;

    @Getter
    @Persisted
    @DescSynced
    private boolean isLocked;

    public ObservatoryObjectHolderMachine(IMachineBlockEntity holder) {
        super(holder);
        heldItems = new ObservatoryHolderHandler(this);
    }

    public void setLocked(boolean locked) {
        this.isLocked = locked;
    }

    /** Slot 0: the research subject (non-data-item) */
    @NotNull
    public ItemStack getHeldItem(boolean remove) {
        return getSlotItem(0, remove);
    }

    public void setHeldItem(@NotNull ItemStack item) {
        heldItems.setStackInSlot(0, item);
    }

    /** Slot 1: the data item (blank AstroPlanetaryDataItem to be written) */
    @NotNull
    public ItemStack getDataItem(boolean remove) {
        return getSlotItem(1, remove);
    }

    public void setDataItem(@NotNull ItemStack item) {
        heldItems.setStackInSlot(1, item);
    }

    public @NotNull NotifiableItemStackHandler getAsHandler() {
        return heldItems;
    }

    @NotNull
    private ItemStack getSlotItem(int slot, boolean remove) {
        ItemStack stack = heldItems.getStackInSlot(slot);
        if (remove && !stack.isEmpty()) {
            heldItems.setStackInSlot(slot, ItemStack.EMPTY);
        }
        return stack;
    }

    @Override
    public Widget createUIWidget() {
        return new WidgetGroup(new Position(0, 0))
                .addWidget(new ImageWidget(46, 15, 84, 60, GuiTextures.PROGRESS_BAR_RESEARCH_STATION_BASE))
                // Slot 0: research subject
                .addWidget(new BlockableSlotWidget(heldItems, 0, 79, 36)
                        .setIsBlocked(this::isLocked)
                        .setBackground(GuiTextures.SLOT, GuiTextures.RESEARCH_STATION_OVERLAY))
                // Slot 1: blank planetary data item
                .addWidget(new BlockableSlotWidget(heldItems, 1, 15, 36)
                        .setIsBlocked(this::isLocked)
                        .setBackground(GuiTextures.SLOT, GuiTextures.DATA_ORB_OVERLAY));
    }

    private static boolean isDataItem(ItemStack stack) {
        if (stack.getItem() instanceof IComponentItem metaItem) {
            for (IItemComponent component : metaItem.getComponents()) {
                if (component instanceof IDataItem) return true;
            }
        }
        return false;
    }

    private class ObservatoryHolderHandler extends NotifiableItemStackHandler {

        public ObservatoryHolderHandler(MetaMachine machine) {
            super(machine, 2, IO.IN, IO.BOTH, size -> new CustomItemStackHandler(size) {

                @Override
                public int getSlotLimit(int slot) {
                    return 1;
                }
            });
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @NotNull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (!isLocked()) {
                return super.extractItem(slot, amount, simulate);
            }
            return ItemStack.EMPTY;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if (stack.isEmpty()) return true;
            boolean dataItem = isDataItem(stack);
            if (slot == 0) return !dataItem;
            else return dataItem;
        }
    }
}
