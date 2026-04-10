package com.astro.core.common.entity;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

public class SpigEntity extends Hoglin {

    private static final DustParticleOptions PARTICLE =
            new DustParticleOptions(new Vector3f(0.7f, 0.5f, 1.0f), 1.0f);

    public SpigEntity(EntityType<? extends Hoglin> type, Level level) {
        super(type, level);
    }

    @Override
    public boolean isConverting() {
        return false;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 2) {
            for (int i = 0; i < 7; i++) {
                level().addParticle(PARTICLE,
                        getX() + (random.nextDouble() - 0.5) * getBbWidth(),
                        getY() + random.nextDouble() * getBbHeight(),
                        getZ() + (random.nextDouble() - 0.5) * getBbWidth(),
                        0, 0, 0);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }
}