package com.astro.core.api.block;

import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * A simplified version of the Firebox Type that doesn't
 * force registration during the "Construct" phase.
 */
public record BoilerFireboxType(String name, ResourceLocation bottom, ResourceLocation top, ResourceLocation side) {

    public static final Map<String, BoilerFireboxType> ASTRO_FIREBOX_TYPES = new HashMap<>();

    @NotNull
    @Override
    public String toString() {
        return name();
    }
}
