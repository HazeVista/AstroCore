package com.astro.core.mixin;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import net.minecraftforge.fml.loading.LoadingModList;

import java.util.List;
import java.util.Set;

public class AstroCoreMixinPlugin implements IMixinConfigPlugin {

    @Override
    public void onLoad(String mixinPackage) {}

    @Override
    public String getRefMapperConfig() {
        // Must match the "refmap" entry in your astrocore.mixins.json
        return "mixins.astrocore.refmap.json";
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        // List of mixins that strictly require Extended AE to function
        List<String> conditionalMixins = List.of(
                "com.astro.core.mixin.MixinContainerRenamer",
                "com.astro.core.mixin.MixinCutterHook",
                "com.astro.core.mixin.MixinMEPatternBufferCustomName"
        );

        if (conditionalMixins.contains(mixinClassName)) {
            // Returns true only if Extended AE is actually loaded
            return LoadingModList.get().getModFileById("extendedae") != null;
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() { return List.of(); }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}