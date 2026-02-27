package com.astro.core.mixin;

import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import earth.terrarium.adastra.common.items.armor.SpaceSuitItem;
import earth.terrarium.botarium.common.fluid.FluidConstants;
import earth.terrarium.botarium.common.fluid.impl.SimpleFluidContainer;
import earth.terrarium.botarium.common.fluid.impl.WrappedItemFluidContainer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SpaceSuitItem.class, remap = false)
public abstract class SpaceSuitItemMixin {

    @Mutable
    @Shadow
    @Final
    protected long tankSize;

    @Shadow
    public static long getOxygenAmount(Entity entity) {
        return 0;
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void astro$increaseTankSize(ArmorMaterial material, ArmorItem.Type type, long tankSize,
                                        Item.Properties properties, CallbackInfo ci) {
        this.tankSize = (long) (tankSize * 2.5);
    }

    /**
     * @author AstroCore
     * @reason Restrict space suit fluid to gtceu:oxygen
     */
    @Overwrite
    public WrappedItemFluidContainer getFluidContainer(ItemStack holder) {
        return new WrappedItemFluidContainer(
                holder,
                new SimpleFluidContainer(
                        FluidConstants.fromMillibuckets(tankSize),
                        1,
                        (t, f) -> f.getFluid() == GTMaterials.Oxygen.getFluid()));
    }

    /**
     * @author AstroCore
     * @reason Fix >= comparison so suit can fully drain before needing refill
     */
    @Overwrite
    public static boolean hasOxygen(Entity entity) {
        return getOxygenAmount(entity) >= FluidConstants.fromMillibuckets(1);
    }
}
