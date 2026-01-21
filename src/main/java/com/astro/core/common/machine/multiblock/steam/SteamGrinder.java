package com.astro.core.common.machine.multiblock.steam;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;
import com.gregtechceu.gtceu.common.machine.multiblock.steam.SteamParallelMultiblockMachine;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("all")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SteamGrinder extends SteamParallelMultiblockMachine {

    private static final double STEAM_TO_EU = 2.0;
    private static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    @NotNull
    private AABB grindBound = new AABB(BlockPos.ZERO);
    @NotNull
    private final List<IItemHandler> handlers = new ArrayList<>();
    @NotNull
    private final Set<BlockPos> crushingWheelPositions = new HashSet<>();
    private TickableSubscription hurtSub;
    private boolean wasWorking = false;

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            SteamGrinder.class,
            SteamParallelMultiblockMachine.MANAGED_FIELD_HOLDER);

    public SteamGrinder(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
        setMaxParallels(ConfigHolder.INSTANCE.machines.steamMultiParallelAmount);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        updateBounds();
        findCrushingWheels();
        for (var holder : getCapabilitiesFlat(IO.IN, ItemRecipeCapability.CAP)) {
            if (holder instanceof IItemHandler ih) {
                handlers.add(ih);
            }
        }
        hurtSub = subscribeServerTick(this::spinWheels);
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        setCrushingWheelsActive(false);
        unsubscribe(hurtSub);
        hurtSub = null;
        handlers.clear();
        crushingWheelPositions.clear();
    }

    @Override
    public void onUnload() {
        super.onUnload();
        unsubscribe(hurtSub);
        hurtSub = null;
        handlers.clear();
        crushingWheelPositions.clear();
    }

    @Override
    public double getConversionRate() {
        return STEAM_TO_EU;
    }

    private void findCrushingWheels() {
        crushingWheelPositions.clear();
        for (int x = -1; x <= 1; x++) {
            for (int z = 1; z <= 3; z++) {
                BlockPos pos = RelativeDirection.offsetPos(getPos(), getFrontFacing(), getUpwardsFacing(), isFlipped(), x, 0, -z);
                crushingWheelPositions.add(pos);
            }
        }
    }

    private void updateBounds() {
        var fl = RelativeDirection.offsetPos(getPos(), getFrontFacing(), getUpwardsFacing(), isFlipped(), 1, 0, -1);
        var br = RelativeDirection.offsetPos(getPos(), getFrontFacing(), getUpwardsFacing(), isFlipped(), -1, 0, -3);
        grindBound = AABB.of(BoundingBox.fromCorners(fl, br));
    }

    private void setCrushingWheelsActive(boolean active) {
        if (getLevel() == null) return;

        for (BlockPos pos : crushingWheelPositions) {
            BlockState state = getLevel().getBlockState(pos);
            Block block = state.getBlock();

            if (state.hasProperty(ACTIVE)) {
                if (state.getValue(ACTIVE) != active) {
                    getLevel().setBlock(pos, state.setValue(ACTIVE, active), 3);
                }
            }
        }
    }

    private void spinWheels() {
        if (isRemote() || getLevel() == null) return;

        boolean isWorking = recipeLogic.isWorking();
        if (isWorking != wasWorking) {
            setCrushingWheelsActive(isWorking);
            wasWorking = isWorking;
        }

        if (getOffsetTimer() % 10 != 0) return;

        List<ItemEntity> itemEntities = new ArrayList<>();
        for (var entity : getLevel().getEntities(null, grindBound)) {
            if (entity instanceof ItemEntity ie) {
                itemEntities.add(ie);
            } else {
                if (isWorking) {
                    entity.hurt(entity.damageSources().cramming(), 2.0f);
                }
            }
        }

        if (handlers.isEmpty()) return;

        for (ItemEntity item : itemEntities) {
            if (item.isRemoved()) continue;
            for (var holder : handlers) {
                item.setItem(ItemHandlerHelper.insertItem(holder, item.getItem(), false));
                if (item.getItem().isEmpty()) {
                    item.discard();
                    break;
                }
            }
        }
    }

    public static ModifierFunction recipeModifier(@NotNull MetaMachine machine, @NotNull GTRecipe recipe) {
        if (RecipeHelper.getRecipeEUtTier(recipe) > GTValues.LV) return ModifierFunction.IDENTITY;
        int parallel = ParallelLogic.getParallelAmount(machine, recipe, ConfigHolder.INSTANCE.machines.steamMultiParallelAmount);
        return ModifierFunction.builder()
                .inputModifier(ContentModifier.multiplier(parallel))
                .outputModifier(ContentModifier.multiplier(parallel))
                .durationMultiplier(1.5)
                .parallels(parallel)
                .build();
    }
}