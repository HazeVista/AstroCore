package com.astro.core.common.machine.multiblock.primitive;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.UITemplate;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IUIMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.common.machine.multiblock.primitive.PrimitiveWorkableMachine;

import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.ProgressWidget;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.player.Player;

import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("all")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PrimitiveComposterMachine extends PrimitiveWorkableMachine implements IUIMachine {

    public PrimitiveComposterMachine(IMachineBlockEntity holder, Object... args) {
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
                .widget(new SlotWidget(importItems.storage, 0, 52, 30, true, true)
                        .setBackgroundTexture(GuiTextures.PRIMITIVE_SLOT))
                .widget(new ProgressWidget(recipeLogic::getProgressPercent, 76, 32, 20, 15,
                        GuiTextures.PRIMITIVE_BLAST_FURNACE_PROGRESS_BAR))
                .widget(new SlotWidget(exportItems.storage, 0, 103, 30, true, false)
                        .setBackgroundTexture(GuiTextures.PRIMITIVE_SLOT))
                .widget(UITemplate.bindPlayerInventory(entityPlayer.getInventory(),
                        GuiTextures.PRIMITIVE_SLOT, 7, 84, true));
    }
}
