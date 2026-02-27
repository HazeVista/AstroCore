package com.astro.core.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import com.astro.core.common.data.AstroItems;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.botania.api.recipe.RunicAltarRecipe;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.RunicAltarBlockEntity;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.botania.common.helper.EntityHelper;
import vazkii.botania.common.item.material.RuneItem;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("all")
@Mixin(value = RunicAltarBlockEntity.class, remap = false)
public abstract class RunicAltarBlockEntityMixin {

    @Shadow
    private void updateRecipe() {}

    @Shadow
    private void tickCooldown() {}

    @Shadow
    public int manaToGet;

    @Shadow
    public int signal;

    @Inject(method = "addItem", at = @At("HEAD"), cancellable = true)
    private void astro$addItem(@Nullable Player player, ItemStack stack,
                               @Nullable InteractionHand hand,
                               CallbackInfoReturnable<Boolean> cir) {
        RunicAltarBlockEntity self = (RunicAltarBlockEntity) (Object) this;

        if (stack.is(AstroItems.RUNE_TABLET.get())) {
            if (!self.getLevel().isClientSide) {
                ItemStack toSpawn = player != null && player.getAbilities().instabuild ? stack.copy().split(1) :
                        stack.split(1);
                ItemEntity item = new ItemEntity(
                        self.getLevel(),
                        self.getBlockPos().getX() + 0.5,
                        self.getBlockPos().getY() + 1,
                        self.getBlockPos().getZ() + 0.5,
                        toSpawn);
                item.setPickUpDelay(40);
                item.setDeltaMovement(Vec3.ZERO);
                self.getLevel().addFreshEntity(item);
            }
            cir.setReturnValue(true);
            return;
        }

        if (stack.is(BotaniaBlocks.livingrock.asItem())) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "serverTick", at = @At("HEAD"), cancellable = true)
    private static void astro$serverTick(Level level, BlockPos worldPosition, BlockState state,
                                         RunicAltarBlockEntity self, CallbackInfo ci) {
        RunicAltarBlockEntityMixin mixin = (RunicAltarBlockEntityMixin) (Object) self;

        if (self.manaToGet == 0) {
            List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class,
                    new AABB(worldPosition, worldPosition.offset(1, 1, 1)));

            for (ItemEntity item : items) {
                if (item.isAlive() && !item.getItem().isEmpty() &&
                        !item.getItem().is(BotaniaBlocks.livingrock.asItem()) &&
                        !item.getItem().is(AstroItems.RUNE_TABLET.get()) &&
                        !XplatAbstractions.INSTANCE.itemFlagsComponent(item).runicAltarSpawned) {
                    ItemStack stack = item.getItem();
                    if (self.addItem(null, stack, null)) {
                        EntityHelper.syncItem(item);
                    }
                }
            }
        }

        int newSignal = 0;
        if (self.manaToGet > 0) {
            newSignal++;
            if (self.getCurrentMana() >= self.manaToGet) {
                newSignal++;
            }
        }

        if (newSignal != self.signal) {
            self.signal = newSignal;
            level.updateNeighbourForOutputSignal(worldPosition, state.getBlock());
        }

        mixin.updateRecipe();
        mixin.tickCooldown();

        ci.cancel();
    }

    @Inject(method = "onUsedByWand", at = @At("HEAD"), cancellable = true)
    private void astro$onUsedByWand(@Nullable Player player, ItemStack wand,
                                    net.minecraft.core.Direction side,
                                    CallbackInfoReturnable<Boolean> cir) {
        RunicAltarBlockEntity self = (RunicAltarBlockEntity) (Object) this;

        if (self.getLevel().isClientSide) {
            cir.setReturnValue(true);
            return;
        }

        RunicAltarRecipe recipe = null;
        Optional<RunicAltarRecipe> maybeRecipe = self.getLevel().getRecipeManager()
                .getRecipeFor(BotaniaRecipeTypes.RUNE_TYPE, self.getItemHandler(), self.getLevel());
        if (maybeRecipe.isPresent()) {
            recipe = maybeRecipe.get();
        }

        if (recipe != null && self.manaToGet > 0 && self.getCurrentMana() >= self.manaToGet) {
            AABB box = new AABB(self.getBlockPos(), self.getBlockPos().offset(1, 1, 1));
            List<ItemEntity> items = self.getLevel().getEntitiesOfClass(ItemEntity.class, box);

            ItemEntity runeTablet = null;
            for (ItemEntity item : items) {
                if (item.isAlive() && !item.getItem().isEmpty() && item.getItem().is(AstroItems.RUNE_TABLET.get())) {
                    runeTablet = item;
                    break;
                }
            }

            if (runeTablet != null) {
                self.receiveMana(-recipe.getManaUsage());

                ItemStack output = recipe.assemble(self.getItemHandler(), self.getLevel().registryAccess());
                ItemEntity outputItem = new ItemEntity(
                        self.getLevel(),
                        self.getBlockPos().getX() + 0.5,
                        self.getBlockPos().getY() + 1.5,
                        self.getBlockPos().getZ() + 0.5,
                        output);
                XplatAbstractions.INSTANCE.itemFlagsComponent(outputItem).runicAltarSpawned = true;

                if (player != null) {
                    player.triggerRecipeCrafted(recipe, List.of(output));
                    output.onCraftedBy(self.getLevel(), player, output.getCount());
                }

                self.getLevel().addFreshEntity(outputItem);
                self.getLevel().gameEvent(null, GameEvent.BLOCK_ACTIVATE, self.getBlockPos());
                self.getLevel().blockEvent(self.getBlockPos(), BotaniaBlocks.runeAltar, 1, 60);  // SET_COOLDOWN_EVENT
                self.getLevel().blockEvent(self.getBlockPos(), BotaniaBlocks.runeAltar, 2, 0);   // CRAFT_EFFECT_EVENT

                try {
                    var method = RunicAltarBlockEntity.class.getDeclaredMethod("saveLastRecipe");
                    method.setAccessible(true);
                    method.invoke(self);
                } catch (Exception ignored) {}

                for (int i = 0; i < self.inventorySize(); i++) {
                    ItemStack stack = self.getItemHandler().getItem(i);
                    if (!stack.isEmpty()) {
                        if (stack.getItem() instanceof RuneItem &&
                                (player == null || !player.getAbilities().instabuild)) {
                            ItemEntity outputRune = new ItemEntity(
                                    self.getLevel(),
                                    self.getBlockPos().getX() + 0.5,
                                    self.getBlockPos().getY() + 1.5,
                                    self.getBlockPos().getZ() + 0.5,
                                    stack.copy());
                            XplatAbstractions.INSTANCE.itemFlagsComponent(outputRune).runicAltarSpawned = true;
                            self.getLevel().addFreshEntity(outputRune);
                        }
                        self.getItemHandler().setItem(i, ItemStack.EMPTY);
                    }
                }

                EntityHelper.shrinkItem(runeTablet);
            }
        }

        cir.setReturnValue(true);
    }
}
