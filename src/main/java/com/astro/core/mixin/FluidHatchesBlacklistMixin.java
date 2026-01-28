package com.astro.core.mixin;

import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.common.machine.multiblock.part.FluidHatchPartMachine;

import com.astro.core.common.data.tag.AstroTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FluidHatchPartMachine.class, remap = false)
public class FluidHatchesBlacklistMixin {

    @Inject(method = "createTank", at = @At("RETURN"), cancellable = true)
    private void astrocore$applyManaBlacklist(int initialCapacity, int slots, Object[] args,
                                              CallbackInfoReturnable<NotifiableFluidTank> cir) {
        NotifiableFluidTank tank = cir.getReturnValue();

        tank.setFilter(fluidStack -> {
            String fluidId = net.minecraftforge.registries.ForgeRegistries.FLUIDS.getKey(fluidStack.getFluid())
                    .toString();
            if (fluidId.equals(AstroTags.EXOTIC_MATTER)) {
                return false;
            }

            return true;
        });
    }
}
