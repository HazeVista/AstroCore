package com.astro.core.mixin;

import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import earth.terrarium.adastra.common.items.GasTankItem;
import earth.terrarium.adastra.common.registry.ModItems;
import earth.terrarium.adastra.common.utils.FluidUtils;
import earth.terrarium.botarium.common.fluid.FluidConstants;
import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import earth.terrarium.botarium.common.fluid.impl.SimpleFluidContainer;
import earth.terrarium.botarium.common.fluid.impl.WrappedItemFluidContainer;
import earth.terrarium.botarium.common.item.ItemStackHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

// remap=true so Mixin resolves vanilla method names correctly in production
@Mixin(value = GasTankItem.class, remap = true)
public abstract class GasTankItemMixin {

    @Shadow(remap = false)
    public abstract boolean distributeSequential(ItemStackHolder from, FluidHolder container, Inventory inventory);

    /**
     * @author AstroCore
     * @reason Increase tank capacity and restrict to gtceu:oxygen
     */
    @Overwrite(remap = false)
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

    /**
     * @author Haze Vista & the TerraFirmaGreg Team
     * @reason Fix off-hand gas tank incorrectly writing to main-hand slot
     */
    @Overwrite
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseDuration) {
        if (level.isClientSide()) return;
        if (!(entity instanceof Player player)) return;
        Inventory inventory = player.getInventory();
        var container = FluidUtils.getTank(stack);
        if (container.getFluidAmount() == 0) return;
        ItemStackHolder from = new ItemStackHolder(stack);
        if (!distributeSequential(from, container, inventory)) return;
        if (player.getUsedItemHand() == InteractionHand.OFF_HAND) {
            inventory.offhand.set(0, from.getStack());
        } else {
            inventory.setItem(inventory.selected, from.getStack());
        }
        if (entity.tickCount % 4 == 0) {
            level.playSound(null, player.blockPosition(), SoundEvents.GENERIC_DRINK, player.getSoundSource(), 1.0F,
                    1.0F);
        }
    }
}
