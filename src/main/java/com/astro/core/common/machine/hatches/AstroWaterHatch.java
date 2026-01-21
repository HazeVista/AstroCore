package com.astro.core.common.machine.hatches;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.UITemplate;
import com.gregtechceu.gtceu.api.gui.widget.GhostCircuitSlotWidget;
import com.gregtechceu.gtceu.api.gui.widget.TankWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;
import com.gregtechceu.gtceu.common.machine.multiblock.part.FluidHatchPartMachine;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.gregtechceu.gtceu.data.lang.LangHandler;

import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fluids.FluidType;

import static com.gregtechceu.gtceu.common.data.GTMaterials.Water;

@SuppressWarnings("all")
public class AstroWaterHatch extends FluidHatchPartMachine {

    public static final int INITIAL_TANK_CAPACITY = 64 * FluidType.BUCKET_VOLUME;
    public static final boolean IS_STEEL = ConfigHolder.INSTANCE.machines.steelSteamMultiblocks;

    private static final int NO_CONFIG = -1;

    public AstroWaterHatch(IMachineBlockEntity holder, Object... args) {
        super(holder, 0, IO.IN, AstroWaterHatch.INITIAL_TANK_CAPACITY, 1, args);
    }

    @Override
    protected NotifiableFluidTank createTank(int initialCapacity, int slots, Object... args) {
        return super.createTank(initialCapacity, slots)
                .setFilter(fluidStack -> fluidStack.getFluid().is(Water.getFluidTag()));
    }

    @Override
    public ModularUI createUI(Player entityPlayer) {
        ModularUI ui = new ModularUI(176, 166, this, entityPlayer)
                .background(GuiTextures.BACKGROUND_STEAM.get(IS_STEEL))
                .widget(new ImageWidget(7, 16, 81, 55, GuiTextures.DISPLAY_STEAM.get(IS_STEEL)))
                .widget(new LabelWidget(11, 20, "gtceu.gui.fluid_amount"))
                .widget(new LabelWidget(11, 30, () -> tank.getFluidInTank(0).getAmount() + "").setTextColor(-1)
                        .setDropShadow(true))
                .widget(new LabelWidget(6, 6, getBlockState().getBlock().getDescriptionId()))
                .widget(new TankWidget(tank.getStorages()[0], 90, 35, true, true)
                        .setBackground(GuiTextures.FLUID_SLOT))
                .widget(UITemplate.bindPlayerInventory(entityPlayer.getInventory(),
                        GuiTextures.SLOT_STEAM.get(IS_STEEL), 7, 84, true));

        if (isCircuitSlotEnabled()) {
            var circuitSlot = new CustomGhostCircuitSlotWidget();
            circuitSlot.setCircuitInventory(circuitInventory.storage);
            circuitSlot.setBackground(GuiTextures.SLOT, getCircuitSlotOverlay());
            circuitSlot.setSelfPosition(151, 7);
            circuitSlot.setCanPutItems(false);
            circuitSlot.setCanTakeItems(false);
            circuitSlot.setHoverTooltips(
                    LangHandler.getMultiLang("astrogreg.gui.configurator_slot.tooltip").toArray(Component[]::new));
            ui.widget(circuitSlot);
        }

        return ui;
    }

    protected IGuiTexture getCircuitSlotOverlay() {
        return GuiTextures.INT_CIRCUIT_OVERLAY;
    }

    @Override
    public boolean swapIO() {
        return false;
    }

    private static class CustomGhostCircuitSlotWidget extends GhostCircuitSlotWidget {

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (isMouseOverElement(mouseX, mouseY) && gui != null) {
                if (Screen.hasShiftDown()) {
                    return true;
                }

                if (button == 0) {
                    int newValue = getNextValue(true);
                    setCircuitValue(newValue);
                } else if (button == 1) {
                    int newValue = getNextValue(false);
                    setCircuitValue(newValue);
                }
                return true;
            }
            return false;
        }

        private int getNextValue(boolean increment) {
            int currentValue = IntCircuitBehaviour
                    .getCircuitConfiguration(this.getCircuitInventory().getStackInSlot(0));
            if (increment) {
                if (currentValue == IntCircuitBehaviour.CIRCUIT_MAX) {
                    return 0;
                }
                if (this.getCircuitInventory().getStackInSlot(0).isEmpty()) {
                    return 1;
                }
                return currentValue + 1;
            } else {
                if (this.getCircuitInventory().getStackInSlot(0).isEmpty()) {
                    return IntCircuitBehaviour.CIRCUIT_MAX;
                }
                if (currentValue == 1) {
                    return NO_CONFIG;
                }
                return currentValue - 1;
            }
        }
    }
}
