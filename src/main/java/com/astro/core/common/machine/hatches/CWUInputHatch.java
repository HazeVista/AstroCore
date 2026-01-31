package com.astro.core.common.machine.hatches;

import com.gregtechceu.gtceu.api.capability.IOpticalComputationProvider;
import com.gregtechceu.gtceu.api.capability.recipe.CWURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableRecipeHandlerTrait;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.astro.core.common.machine.singleblock.CWUGeneratorMachine;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("all")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CWUInputHatch extends MetaMachine implements IOpticalComputationProvider {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            CWUInputHatch.class, MetaMachine.MANAGED_FIELD_HOLDER);

    @Persisted
    private final CWUHandler cwuHandler;

    private final int tier;

    public CWUInputHatch(IMachineBlockEntity holder, int tier, Object... args) {
        super(holder);
        this.tier = tier;
        this.cwuHandler = new CWUHandler(this);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    public int getTier() {
        return tier;
    }

    @Override
    public int requestCWUt(int cwut, boolean simulate, @NotNull Collection<IOpticalComputationProvider> seen) {
        seen.add(this);

        for (Direction direction : Direction.values()) {
            BlockEntity be = getLevel().getBlockEntity(getPos().relative(direction));
            if (be instanceof IMachineBlockEntity mbe && mbe.getMetaMachine() instanceof CWUGeneratorMachine gen) {
                int available = gen.getAvailableCWU();
                if (available > 0) {
                    if (!simulate) {
                        return gen.requestCWU(cwut);
                    }
                    return Math.min(cwut, available);
                }
            }
        }
        return 0;
    }

    @Override
    public int getMaxCWUt(@NotNull Collection<IOpticalComputationProvider> seen) {
        seen.add(this);

        int total = 0;
        for (Direction direction : Direction.values()) {
            BlockEntity be = getLevel().getBlockEntity(getPos().relative(direction));
            if (be instanceof IMachineBlockEntity mbe && mbe.getMetaMachine() instanceof CWUGeneratorMachine gen) {
                total += gen.getAvailableCWU();
            }
        }
        return total;
    }

    @Override
    public boolean canBridge(@NotNull Collection<IOpticalComputationProvider> seen) {
        return false;
    }

    private static class CWUHandler extends NotifiableRecipeHandlerTrait<Integer> {

        private final CWUInputHatch machine;

        public CWUHandler(CWUInputHatch machine) {
            super(machine);
            this.machine = machine;
        }

        @Override
        public CWURecipeCapability getCapability() {
            return CWURecipeCapability.CAP;
        }

        @Override
        public IO getHandlerIO() {
            return IO.IN;
        }

        @Override
        public List<Integer> handleRecipeInner(IO io, GTRecipe recipe, List<Integer> left, boolean simulate) {
            if (left == null || left.isEmpty()) return null;

            int amount = left.stream().mapToInt(Integer::intValue).sum();
            int provided = machine.requestCWUt(amount, simulate, new ArrayList<>());

            if (provided >= amount) {
                return null;
            }

            return Collections.singletonList(amount - provided);
        }

        @Override
        public List<Object> getContents() {
            return Collections.singletonList(machine.getMaxCWUt(new ArrayList<>()));
        }

        @Override
        public double getTotalContentAmount() {
            return machine.getMaxCWUt(new ArrayList<>());
        }
    }
}
