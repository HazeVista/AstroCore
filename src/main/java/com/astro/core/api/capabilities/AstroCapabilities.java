package com.astro.core.api.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

import com.astro.core.common.machine.trait.cwu.ILocalCWUProvider;

public class AstroCapabilities {

    /**
     * Capability for {@link ILocalCWUProvider}.
     * Used by {@link com.astro.core.common.machine.part.CWUInputHatch}
     * to discover an adjacent {@link com.astro.core.common.machine.singleblock.CWUGeneratorMachine}.
     * Intentionally separated from GTCapability.CAPABILITY_COMPUTATION_PROVIDER
     * so that optical pipes can't pick this up.
     */
    public static final Capability<ILocalCWUProvider> LOCAL_CWU_PROVIDER = CapabilityManager
            .get(new CapabilityToken<>() {});

    public static void register(RegisterCapabilitiesEvent event) {
        event.register(ILocalCWUProvider.class);
    }
}
