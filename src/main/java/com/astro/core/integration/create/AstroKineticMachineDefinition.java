package com.astro.core.integration.create;

import com.gregtechceu.gtceu.api.machine.MachineDefinition;

import net.minecraft.resources.ResourceLocation;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@SuppressWarnings("all")
@Accessors(chain = true)
public class AstroKineticMachineDefinition extends MachineDefinition {

    @Getter
    public final boolean isSource;
    @Getter
    public final float torque;
    /**
     * false (default) - rotation axis = frontFacing clockWise axis
     * true - rotation axis = frontFacing axis
     */
    @Getter
    @Setter
    public boolean frontRotation;

    public AstroKineticMachineDefinition(ResourceLocation id, boolean isSource, float torque) {
        super(id);
        this.isSource = isSource;
        this.torque = torque;
    }
}
