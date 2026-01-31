package com.astro.core.integration.kubejs;

import com.astro.core.AstroCore;
import com.astro.core.client.AstroGUITextures;
import com.astro.core.client.AstroMachines;
import com.astro.core.client.AstroSoundEntries;
import com.astro.core.common.data.AstroBlocks;
import com.astro.core.common.data.AstroItems;
import com.astro.core.common.data.AstroRecipeTypes;
import com.astro.core.common.data.configs.AstroConfigs;
import com.astro.core.common.data.materials.AstroElements;
import com.astro.core.common.data.materials.AstroMaterials;
import com.astro.core.common.machine.trait.AstroPartAbility;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ClassFilter;

public class AstroKubeJSPlugin extends KubeJSPlugin {

    @Override
    public void initStartup() {
        super.initStartup();
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void registerClasses(ScriptType type, ClassFilter filter) {
        super.registerClasses(type, filter);
        filter.allow("com.astro.core");
    }

    @Override
    public void registerBindings(BindingsEvent event) {
        super.registerBindings(event);
        event.add("AstroMaterials", AstroMaterials.class);
        event.add("AstroConfigs", AstroConfigs.class);
        event.add("AstroGUITextures", AstroGUITextures.class);
        event.add("AstroElements", AstroElements.class);
        event.add("AstroBlocks", AstroBlocks.class);
        event.add("AstroMachines", AstroMachines.class);
        event.add("AstroItems", AstroItems.class);
        event.add("AstroRecipeTypes", AstroRecipeTypes.class);
        event.add("AstroSoundEntries", AstroSoundEntries.class);
        event.add("AstroPartAbility", AstroPartAbility.class);

        event.add("AstroCore", AstroCore.class);
    }
}
