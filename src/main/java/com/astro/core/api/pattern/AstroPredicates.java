package com.astro.core.api.pattern;

import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate;
import com.gregtechceu.gtceu.common.block.LampBlock;

import com.lowdragmc.lowdraglib.utils.BlockInfo;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockState;

import com.tterrag.registrate.util.entry.BlockEntry;

import java.util.*;

import static com.gregtechceu.gtceu.common.data.GTBlocks.BORDERLESS_LAMPS;
import static com.gregtechceu.gtceu.common.data.GTBlocks.LAMPS;

public class AstroPredicates {

    @SafeVarargs
    public static TraceabilityPredicate lamps(BlockEntry<LampBlock>... lampEntries) {
        return new TraceabilityPredicate(blockWorldState -> {
            BlockState state = blockWorldState.getBlockState();
            for (BlockEntry<LampBlock> entry : lampEntries) {
                if (state.is(entry.get())) return true;
            }
            return false;
        }, () -> Arrays.stream(lampEntries)
                .map(entry -> new BlockInfo(entry.get().defaultBlockState(), null))
                .toArray(BlockInfo[]::new));
    }

    public static TraceabilityPredicate anyLamp() {
        List<BlockEntry<LampBlock>> all = new ArrayList<>();
        all.addAll(LAMPS.values());
        all.addAll(BORDERLESS_LAMPS.values());
        return lamps(all.toArray(BlockEntry[]::new));
    }

    private static final Map<DyeColor, TraceabilityPredicate> LAMPS_BY_COLOR = new EnumMap<>(DyeColor.class);

    static {
        for (DyeColor color : DyeColor.values()) {
            LAMPS_BY_COLOR.put(color, lamps(LAMPS.get(color), BORDERLESS_LAMPS.get(color)));
        }
    }

    public static TraceabilityPredicate lampsByColor(DyeColor color) {
        return LAMPS_BY_COLOR.getOrDefault(color, anyLamp());
    }
}
