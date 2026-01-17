package com.astro.core.mixin;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import net.minecraft.network.chat.Component;

import com.astro.core.api.CustomNameAccess;
import com.glodblock.github.extendedae.container.ContainerRenamer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Mixin(value = ContainerRenamer.class, remap = false)
@SuppressWarnings("MixinAnnotationTarget")
public abstract class MixinContainerRenamer {

    @Shadow
    @Final
    @Mutable
    private Consumer<String> setter;

    @Shadow
    @Final
    @Mutable
    private Supplier<Component> getter;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void monilabs$init(
                               int id,
                               net.minecraft.world.entity.player.Inventory inv,
                               Object host,
                               CallbackInfo ci) {
        Object target = resolveTarget(host, inv.player);
        if (!(target instanceof IMachineBlockEntity holder)) {
            return;
        }

        Object meta = holder.getMetaMachine();
        if (!(meta instanceof CustomNameAccess access)) {
            return;
        }

        Consumer<String> setterFn = name -> {
            try {
                meta.getClass().getMethod("setCustomName", String.class).invoke(meta, name);
            } catch (Exception ignored) {}
        };

        apply(setterFn, access::astro$getCustomName);
    }

    private void apply(Consumer<String> setter, Supplier<String> getter) {
        this.setter = setter;
        this.getter = () -> Component.literal(getter.get());
        ((ContainerRenamer) (Object) this).setValidMenu(true);
    }

    private Object resolveTarget(Object host, net.minecraft.world.entity.player.Player player) {
        if (!host.getClass().getSimpleName().contains("Locator")) {
            return host;
        }

        try {
            var locate = host.getClass()
                    .getMethod("locate", net.minecraft.world.entity.player.Player.class, Class.class);
            Object result = locate.invoke(host, player, IMachineBlockEntity.class);
            if (result == null) return host;

            try {
                return result.getClass().getMethod("getTarget").invoke(result);
            } catch (NoSuchMethodException e) {
                return result.getClass().getMethod("host").invoke(result);
            }
        } catch (Exception e) {
            return host;
        }
    }
}
