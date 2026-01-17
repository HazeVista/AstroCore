package com.astro.core.api;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ExtendedAEWrapper {

    public static void openPatternBufferUI(Player player, BlockEntity tile) {
        appeng.menu.MenuOpener.open(
                com.glodblock.github.extendedae.container.ContainerRenamer.TYPE,
                player,
                appeng.menu.locator.MenuLocators.forBlockEntity(tile));
    }
}
