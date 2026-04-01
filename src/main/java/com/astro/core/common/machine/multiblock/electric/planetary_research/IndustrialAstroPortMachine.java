package com.astro.core.common.machine.multiblock.electric.planetary_research;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockDisplayText;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.ActionResult;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.astro.core.common.data.AstroBlocks.*;

@ParametersAreNonnullByDefault
public class IndustrialAstroPortMachine extends AstroPortMachine {

    private static final int[][] HATCH_OFFSETS_RIGHT = {
            { 10, 4, 10 },
            { 10, 3, 10 },
            { 10, 2, 10 },
            { 10, 1, 10 },
            { 10, 0, 10 },
    };
    private static final int[][] HATCH_OFFSETS_LEFT = {
            { 10, 4, -10 },
            { 10, 3, -10 },
            { 10, 2, -10 },
            { 10, 1, -10 },
            { 10, 0, -10 },
    };

    private static final int[][] CORE_OFFSETS = {
            { 9, -2, -1 },
            { 9, -2, 1 },
            { 11, -2, -1 },
            { 11, -2, 1 },
    };

    @Nullable
    private List<NotifiableItemStackHandler> orderedInputBuses = null;

    @Getter
    private int coreTier = -1;

    public IndustrialAstroPortMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    protected RecipeLogic createRecipeLogic(Object... args) {
        return new AstroPortRecipeLogic(this);
    }

    // ── Structure ─────────────────────────────────────────────────────────────

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        var level = getLevel();
        if (level == null) {
            this.coreTier = -1;
            this.orderedInputBuses = null;
            return;
        }

        int lowestTier = Integer.MAX_VALUE;
        boolean foundAny = false;
        for (int[] offset : CORE_OFFSETS) {
            BlockPos corePos = resolveOffset(getPos(), getFrontFacing(), offset[0], offset[1], offset[2]);
            Block block = level.getBlockState(corePos).getBlock();
            int tier = getCoreBlockTier(block);
            if (tier != -1) {
                foundAny = true;
                lowestTier = Math.min(lowestTier, tier);
            }
        }
        this.coreTier = foundAny ? lowestTier : -1;

        List<NotifiableItemStackHandler> buses = new ArrayList<>(10);
        for (int[] offset : Stream.of(HATCH_OFFSETS_RIGHT, HATCH_OFFSETS_LEFT).flatMap(Arrays::stream)
                .toArray(int[][]::new)) {
            BlockPos hatchPos = resolveOffset(getPos(), getFrontFacing(), offset[0], offset[1], offset[2]);
            NotifiableItemStackHandler handler = findImportBusAt(hatchPos);
            if (handler != null) {
                buses.add(handler);
            } else {
                buses.add(null);
            }
        }
        this.orderedInputBuses = buses;

        getRecipeLogic().updateTickSubscription();
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        this.coreTier = -1;
        this.orderedInputBuses = null;
    }

    @Nullable
    private NotifiableItemStackHandler findImportBusAt(BlockPos pos) {
        for (IMultiPart part : getParts()) {
            if (!part.self().getPos().equals(pos)) continue;
            for (RecipeHandlerList handlerList : part.getRecipeHandlers()) {
                if (!handlerList.isValid(IO.IN)) continue;
                for (IRecipeHandler<?> handler : handlerList.getCapability(ItemRecipeCapability.CAP)) {
                    if (handler instanceof NotifiableItemStackHandler nisHandler) {
                        return nisHandler;
                    }
                }
            }
        }
        return null;
    }

    // ── Accessors ─────────────────────────────────────────────────────────────

    @SuppressWarnings("unused")
    public static Comparator<IMultiPart> partSorter(MultiblockControllerMachine mc) {
        return Comparator.comparingInt(p -> p.self().getPos().hashCode());
    }

    // ── Ordered Input Logic ───────────────────────────────────────────────────

    private boolean checkItemInputs(@NotNull GTRecipe recipe) {
        if (orderedInputBuses == null) return false;
        var itemInputs = recipe.inputs.getOrDefault(ItemRecipeCapability.CAP, Collections.emptyList());
        if (itemInputs.isEmpty()) return true;
        if (itemInputs.size() > orderedInputBuses.size()) return false;

        for (int i = 0; i < itemInputs.size(); i++) {
            NotifiableItemStackHandler bus = orderedInputBuses.get(i);
            if (bus == null) return false;
            Ingredient required = ItemRecipeCapability.CAP.of(itemInputs.get(i).content);
            boolean found = false;
            for (int slot = 0; slot < bus.getSlots(); slot++) {
                if (required.test(bus.getStackInSlot(slot))) {
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }
        return true;
    }

    private ActionResult consumeItemContents(@NotNull GTRecipe recipe) {
        if (orderedInputBuses == null) return ActionResult.FAIL_NO_REASON;
        var itemInputs = recipe.inputs.getOrDefault(ItemRecipeCapability.CAP, Collections.emptyList());
        if (itemInputs.isEmpty()) return ActionResult.SUCCESS;
        if (itemInputs.size() > orderedInputBuses.size()) return ActionResult.FAIL_NO_REASON;

        for (int i = 0; i < itemInputs.size(); i++) {
            NotifiableItemStackHandler bus = orderedInputBuses.get(i);
            if (bus == null) return ActionResult.FAIL_NO_REASON;
            Ingredient required = ItemRecipeCapability.CAP.of(itemInputs.get(i).content);
            var left = bus.handleRecipeInner(IO.IN, recipe, new ArrayList<>(List.of(required)), true);
            if (!(left == null || left.isEmpty())) return ActionResult.FAIL_NO_REASON;
        }

        for (int i = 0; i < itemInputs.size(); i++) {
            NotifiableItemStackHandler bus = orderedInputBuses.get(i);
            Ingredient required = ItemRecipeCapability.CAP.of(itemInputs.get(i).content);
            var left = bus.handleRecipeInner(IO.IN, recipe, new ArrayList<>(List.of(required)), false);
            if (!(left == null || left.isEmpty())) {
                return ActionResult.FAIL_NO_REASON;
            }
        }
        return ActionResult.SUCCESS;
    }

    // ── Recipe Modifier ───────────────────────────────────────────────────────

    public static @NotNull ModifierFunction recipeModifier(
                                                           @NotNull MetaMachine machine, @NotNull GTRecipe recipe) {
        if (!(machine instanceof IndustrialAstroPortMachine m)) return ModifierFunction.IDENTITY;
        int coreTier = m.getCoreTier();
        if (coreTier == -1) return ModifierFunction.IDENTITY;
        if (RecipeHelper.getRecipeEUtTier(recipe) > coreTier) return ModifierFunction.IDENTITY;
        long maxVoltage = GTValues.V[coreTier];
        long overclockVoltage = Math.min(m.getOverclockVoltage(), maxVoltage);
        return OverclockingLogic.PERFECT_OVERCLOCK_SUBTICK.getModifier(machine, recipe, overclockVoltage);
    }

    // ── Recipe Logic ──────────────────────────────────────────────────────────

    private class AstroPortRecipeLogic extends RecipeLogic {

        public AstroPortRecipeLogic(IndustrialAstroPortMachine machine) {
            super(machine);
        }

        @Override
        protected ActionResult matchRecipe(GTRecipe recipe) {
            ActionResult normalMatch = RecipeHelper.matchContents(machine, recipe);
            if (!normalMatch.isSuccess()) return normalMatch;
            if (!checkItemInputs(recipe)) return ActionResult.FAIL_NO_REASON;
            return ActionResult.SUCCESS;
        }

        @Override
        protected ActionResult handleRecipeIO(GTRecipe recipe, IO io) {
            if (io == IO.IN) {
                GTRecipe withoutItems = recipe.copy();
                withoutItems.inputs.remove(ItemRecipeCapability.CAP);
                ActionResult itemResult = consumeItemContents(recipe);
                if (!itemResult.isSuccess()) return itemResult;
                return RecipeHelper.handleRecipeIO(IndustrialAstroPortMachine.this, withoutItems, IO.IN,
                        this.chanceCaches);
            }
            return RecipeHelper.handleRecipeIO(IndustrialAstroPortMachine.this, recipe, io, this.chanceCaches);
        }
    }

    // ── GUI ───────────────────────────────────────────────────────────────────

    @Override
    public void addDisplayText(List<Component> textList) {
        MultiblockDisplayText.builder(textList, isFormed())
                .setWorkingStatus(recipeLogic.isWorkingEnabled(), recipeLogic.isActive())
                .setWorkingStatusKeys(
                        "gtceu.multiblock.idling",
                        "gtceu.multiblock.work_paused",
                        "gtceu.multiblock.running")
                .addEnergyUsageLine(energyContainer)
                .addCustom(tl -> {
                    if (isFormed() && coreTier != -1) {
                        tl.add(Component.translatable("astrogreg.machine.processing_core.core",
                                GTValues.VNF[coreTier])
                                .withStyle(ChatFormatting.YELLOW));
                    }
                })
                .addWorkingStatusLine()
                .addProgressLine(recipeLogic);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private static int getCoreBlockTier(Block block) {
        if (block == INDUSTRIAL_PROCESSING_CORE_MK1.get()) return GTValues.HV;
        if (block == INDUSTRIAL_PROCESSING_CORE_MK2.get()) return GTValues.EV;
        if (block == INDUSTRIAL_PROCESSING_CORE_MK3.get()) return GTValues.IV;
        return -1;
    }
}
