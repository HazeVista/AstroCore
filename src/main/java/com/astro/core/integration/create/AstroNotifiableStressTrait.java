package com.astro.core.integration.create;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableRecipeHandlerTrait;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import net.minecraft.util.Mth;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("all")
public class AstroNotifiableStressTrait extends NotifiableRecipeHandlerTrait<Float> {

    @Getter
    public final IO handlerIO;
    @Getter
    public final IO capabilityIO;
    private float available;
    private float lastSpeed;

    public AstroNotifiableStressTrait(MetaMachine machine, IO handlerIO, IO capabilityIO) {
        super(machine);
        this.handlerIO = handlerIO;
        this.capabilityIO = capabilityIO;
        this.lastSpeed = 0;
    }

    @Override
    public void onMachineLoad() {
        super.onMachineLoad();
        if (machine instanceof IKineticMachine kineticMachine) {
            machine.subscribeServerTick(() -> {
                var speed = kineticMachine.getKineticHolder().getSpeed();
                if (speed != lastSpeed) {
                    lastSpeed = speed;
                    notifyListeners();
                }
            });
        }
    }

    @Override
    public List<Float> handleRecipeInner(IO io, GTRecipe recipe, List<Float> left, boolean simulate) {
        if (machine instanceof IKineticMachine kineticMachine) {
            float sum = left.stream().reduce(0f, Float::sum);
            var kineticDefinition = kineticMachine.getKineticDefinition();

            if (io == IO.IN && !kineticDefinition.isSource()) {
                float capacity = Mth.abs(kineticMachine.getKineticHolder().getSpeed()) * kineticDefinition.getTorque();
                if (capacity > 0) {
                    sum = sum - capacity;
                }
            } else if (io == IO.OUT && kineticDefinition.isSource()) {
                if (simulate) {
                    available = kineticMachine.getKineticHolder().scheduleWorking(sum, true);
                }
                sum = sum - available;
            }
            return sum <= 0 ? null : Collections.singletonList(sum);
        }
        return left;
    }

    @Override
    public List<Object> getContents() {
        return List.of(available);
    }

    @Override
    public double getTotalContentAmount() {
        return available;
    }

    @Override
    public RecipeCapability<Float> getCapability() {
        return AstroStressRecipeCapability.CAP;
    }
}
