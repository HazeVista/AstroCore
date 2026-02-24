package com.astro.core.integration.create;

import com.gregtechceu.gtceu.api.machine.feature.IMachineFeature;

import net.minecraft.core.Direction;

public interface IKineticMachine extends IMachineFeature {

    default AstroKineticMachineBlockEntity getKineticHolder() {
        return (AstroKineticMachineBlockEntity) self().getHolder();
    }

    default AstroKineticMachineDefinition getKineticDefinition() {
        return (AstroKineticMachineDefinition) self().getDefinition();
    }

    default float getRotationSpeedModifier(Direction direction) {
        return 1;
    }

    default Direction getRotationFacing() {
        var frontFacing = self().getFrontFacing();
        return getKineticDefinition().isFrontRotation() ? frontFacing :
                (frontFacing.getAxis() == Direction.Axis.Y ? Direction.NORTH : frontFacing.getClockWise());
    }

    default boolean hasShaftTowards(Direction face) {
        return face.getAxis() == getRotationFacing().getAxis();
    }
}
