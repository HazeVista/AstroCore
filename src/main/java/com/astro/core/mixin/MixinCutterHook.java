package com.astro.core.mixin;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.registries.ForgeRegistries;

import com.astro.core.api.ExtendedAEWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "com.glodblock.github.extendedae.common.hooks.CutterHook", remap = false)
public class MixinCutterHook {

    @Unique
    private static final ResourceLocation phoenix$CERTUS_KNIFE = new ResourceLocation("ae2",
            "certus_quartz_cutting_knife");
    @Unique
    private static final ResourceLocation phoenix$NETHER_KNIFE = new ResourceLocation("ae2",
            "nether_quartz_cutting_knife");

    @Inject(method = "onPlayerUseBlock", at = @At("HEAD"), cancellable = true)
    private void phoenix$onPlayerUseBlock(Player player, Level level, InteractionHand hand,
                                          BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (player.isSpectator() || hand != InteractionHand.MAIN_HAND) return;

        ItemStack stack = player.getItemInHand(hand);

        boolean isKnife = stack.is(ForgeRegistries.ITEMS.getValue(phoenix$CERTUS_KNIFE)) ||
                stack.is(ForgeRegistries.ITEMS.getValue(phoenix$NETHER_KNIFE));

        if (!isKnife) return;

        BlockEntity tile = level.getBlockEntity(hitResult.getBlockPos());

        if (tile instanceof IMachineBlockEntity machineHolder) {
            var metaMachine = machineHolder.getMetaMachine();
            if (metaMachine.getClass().getName()
                    .equals("com.gregtechceu.gtceu.integration.ae2.machine.MEPatternBufferPartMachine")) {
                if (!level.isClientSide) {
                    ExtendedAEWrapper.openPatternBufferUI(player, tile);
                }
                cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide));
            }
        }
    }
}
