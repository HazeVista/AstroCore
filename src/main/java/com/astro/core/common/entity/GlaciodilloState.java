package com.astro.core.common.entity;

public enum GlaciodilloState {
    IDLE(false, 0) {
        @Override
        public boolean shouldHideInShell(long ticks) { return false; }
    },
    ROLLING(true, 10) {
        @Override
        public boolean shouldHideInShell(long ticks) { return ticks > 5L; }
    },
    SCARED(true, 50) {
        @Override
        public boolean shouldHideInShell(long ticks) { return true; }
    },
    UNROLLING(true, 30) {
        @Override
        public boolean shouldHideInShell(long ticks) { return ticks < 26L; }
    };

    private final boolean isThreatened;
    private final int animationDuration;

    GlaciodilloState(boolean isThreatened, int animationDuration) {
        this.isThreatened = isThreatened;
        this.animationDuration = animationDuration;
    }

    public abstract boolean shouldHideInShell(long ticks);
    public int animationDuration() { return animationDuration; }
}