package com.astro.core.common.machine.part;

import com.gregtechceu.gtceu.api.capability.IOpticalComputationProvider;
import com.gregtechceu.gtceu.api.capability.forge.GTCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableComputationContainer;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("all")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CWUInputHatch extends TieredPartMachine {

    public final LocalCWUComputationTrait computationTrait;

    public CWUInputHatch(IMachineBlockEntity holder, int tier) {
        super(holder, tier);
        this.computationTrait = new LocalCWUComputationTrait(this);
    }

    public static class LocalCWUComputationTrait extends NotifiableComputationContainer {

        private final CWUInputHatch hatch;

        public LocalCWUComputationTrait(CWUInputHatch hatch) {
            super(hatch, IO.IN, false);
            this.hatch = hatch;
        }

        @Override
        public int requestCWUt(int cwut, boolean simulate,
                               @NotNull Collection<IOpticalComputationProvider> seen) {
            if (seen.contains(this)) return 0;
            seen.add(this);
            IOpticalComputationProvider provider = findAdjacentProvider();
            if (provider == null) return 0;
            return provider.requestCWUt(cwut, simulate);
        }

        @Override
        public int getMaxCWUt(@NotNull Collection<IOpticalComputationProvider> seen) {
            if (seen.contains(this)) return 0;
            seen.add(this);
            IOpticalComputationProvider provider = findAdjacentProvider();
            if (provider == null) return 0;
            return provider.getMaxCWUt();
        }

        @Override
        public boolean canBridge(@NotNull Collection<IOpticalComputationProvider> seen) {
            return false;
        }

        public IOpticalComputationProvider findAdjacentProvider() {
            Level level = hatch.self().getLevel();
            if (level == null) return null;
            BlockPos pos = hatch.self().getPos();

            for (Direction face : Direction.values()) {
                var blockEntity = level.getBlockEntity(pos.relative(face));
                if (blockEntity == null) continue;

                var cap = blockEntity.getCapability(
                        GTCapability.CAPABILITY_COMPUTATION_PROVIDER, face.getOpposite());
                if (cap.isPresent()) {
                    return cap.resolve().orElse(null);
                }
            }
            return null;
        }

        @Override
        public List<Integer> handleRecipeInner(IO io, GTRecipe recipe, List<Integer> left, boolean simulate) {
            if (io != IO.IN) return left;
            IOpticalComputationProvider provider = findAdjacentProvider();
            if (provider == null) return left;
            int sum = left.stream().mapToInt(Integer::intValue).sum();
            int provided = provider.requestCWUt(sum, simulate);
            sum -= provided;
            return sum <= 0 ? null : Collections.singletonList(sum);
        }
    }
}
