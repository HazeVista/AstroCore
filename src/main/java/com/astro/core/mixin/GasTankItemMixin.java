package com.astro.core.mixin;

import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import com.llamalad7.mixinextras.sugar.Local;
import earth.terrarium.adastra.common.items.GasTankItem;
import earth.terrarium.adastra.common.registry.ModItems;
import earth.terrarium.botarium.common.fluid.FluidConstants;
import earth.terrarium.botarium.common.fluid.impl.SimpleFluidContainer;
import earth.terrarium.botarium.common.fluid.impl.WrappedItemFluidContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = GasTankItem.class, remap = false)
public abstract class GasTankItemMixin {

    @Overwrite
    public WrappedItemFluidContainer getFluidContainer(ItemStack holder) {
        long capacity = FluidConstants.fromMillibuckets(
                holder.getItem() == ModItems.GAS_TANK.get() ? 4000 : 8000);
        return new WrappedItemFluidContainer(
                holder,
                new SimpleFluidContainer(
                        capacity,
                        1,
                        (t, f) -> f.getFluid() == GTMaterials.Oxygen.getFluid()));
    }

    @Redirect(method = "onUseTick",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/world/entity/player/Inventory;setItem(ILnet/minecraft/world/item/ItemStack;)V"),
              remap = true)
    private void astro$fixOffHandTankWrite(Inventory inventory, int index, ItemStack updatedStack,
                                           @Local Player player) {
        if (player.getUsedItemHand() == InteractionHand.OFF_HAND) {
            inventory.offhand.set(0, updatedStack);
        } else {
            inventory.setItem(index, updatedStack);
        }
    }
}
