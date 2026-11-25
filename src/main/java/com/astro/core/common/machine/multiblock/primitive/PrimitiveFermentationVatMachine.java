package com.astro.core.common.machine.multiblock.primitive;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.UITemplate;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.gui.widget.TankWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IUIMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.common.machine.multiblock.primitive.PrimitiveWorkableMachine;

import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.texture.ProgressTexture;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.ProgressWidget;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.player.Player;

import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("all")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PrimitiveFermentationVatMachine extends PrimitiveWorkableMachine implements IUIMachine {

    public PrimitiveFermentationVatMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    protected @NotNull RecipeLogic createRecipeLogic(Object... args) {
        return new RecipeLogic(this);
    }

    @Override
    public ModularUI createUI(Player entityPlayer) {
        return new ModularUI(176, 166, this, entityPlayer)
                .background(GuiTextures.PRIMITIVE_BACKGROUND)
                .widget(new LabelWidget(5, 5, getBlockState().getBlock().getDescriptionId()))

                .widget(new TankWidget(importFluids.getStorages()[0], 15, 18, 20, 58, true, true)
                        .setBackground(GuiTextures.PRIMITIVE_LARGE_FLUID_TANK)
                        .setFillDirection(ProgressTexture.FillDirection.DOWN_TO_UP)
                        .setShowAmountOverlay(false)
                        .setOverlay(GuiTextures.PRIMITIVE_LARGE_FLUID_TANK_OVERLAY))

                .widget(new SlotWidget(importItems.storage, 0, 52, 38, true, true)
                        .setBackgroundTexture(GuiTextures.PRIMITIVE_SLOT))

                .widget(new ProgressWidget(recipeLogic::getProgressPercent, 78, 40, 20, 15,
                        GuiTextures.PRIMITIVE_BLAST_FURNACE_PROGRESS_BAR))

                .widget(new SlotWidget(exportItems.storage, 0, 106, 38, true, false)
                        .setBackgroundTexture(GuiTextures.PRIMITIVE_SLOT))

                .widget(new TankWidget(exportFluids.getStorages()[0], 141, 18, 20, 58, true, false)
                        .setBackground(GuiTextures.PRIMITIVE_LARGE_FLUID_TANK)
                        .setFillDirection(ProgressTexture.FillDirection.DOWN_TO_UP)
                        .setShowAmountOverlay(false)
                        .setOverlay(GuiTextures.PRIMITIVE_LARGE_FLUID_TANK_OVERLAY))

                .widget(UITemplate.bindPlayerInventory(entityPlayer.getInventory(), GuiTextures.PRIMITIVE_SLOT, 7, 84,
                        true));
    }
}