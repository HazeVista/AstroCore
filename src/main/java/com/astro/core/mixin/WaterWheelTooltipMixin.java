package com.astro.core.mixin;

import com.astro.core.integration.create.WaterWheelDisplaySU;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.waterwheel.WaterWheelBlockEntity;
import com.simibubi.create.foundation.utility.CreateLang;
import net.createmod.catnip.lang.LangBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings("all")
@Mixin(value = GeneratingKineticBlockEntity.class, remap = false)
public abstract class WaterWheelTooltipMixin {

    @Redirect(
            method = "addToGoggleTooltip",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/foundation/utility/CreateLang;number(D)Lnet/createmod/catnip/lang/LangBuilder;",
                    remap = false
            ),
            remap = false
    )
    private LangBuilder astrogreg$redirectStressDisplay(double stressTotal) {
        if (!(((Object) this) instanceof WaterWheelBlockEntity self)) {
            return CreateLang.number(stressTotal);
        }

        float penalty = ((WaterWheelDisplaySU) self).astrogreg$getPenaltyFactor();
        double result = Math.floor(stressTotal * penalty);
        return CreateLang.number(result);
    }
}