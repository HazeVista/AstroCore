package com.astro.core.events;

import com.astro.core.AstroCore;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Function;

@Mod.EventBusSubscriber(modid = AstroCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerSpawnHandler {

    private static final ResourceKey<Level> KUIPER_BELT = ResourceKey.create(
        Registries.DIMENSION,
        new ResourceLocation("ad_astra", "kuiper_belt")
    );

    private static final int SPAWN_SEARCH_RADIUS = 100;

    @SubscribeEvent
    public static void onPlayerFirstJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!player.level().dimension().equals(Level.OVERWORLD)) return;

        MinecraftServer server = player.level().getServer();
        if (server == null) return;

        ServerLevel kuiperLevel = server.getLevel(KUIPER_BELT);
        if (kuiperLevel == null) {
            AstroCore.LOGGER.warn("Kuiper Belt dimension not found, cannot teleport player {}", player.getName().getString());
            return;
        }

        player.changeDimension(kuiperLevel, new ITeleporter() {
            @Override
            public Entity placeEntity(Entity entity, ServerLevel currentLevel, ServerLevel destLevel,
                                      float yaw, Function<Boolean, Entity> repositionEntity) {
                entity = repositionEntity.apply(false);

                BlockPos spawnPos = findSpawnPos(destLevel, entity.blockPosition());
                if (spawnPos != null) {
                    entity.teleportTo(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5);
                } else {
                    AstroCore.LOGGER.warn("Could not find valid spawn position for player {}", entity.getName().getString());
                }

                return entity;
            }
        });

        player.setRespawnPosition(KUIPER_BELT, player.blockPosition(), 0, true, false);
    }

    /**
     * Searches outward from the center for a valid spawn position:
     * a solid block with two air blocks above it.
     */
    private static BlockPos findSpawnPos(ServerLevel level, BlockPos center) {
        int maxHeight = level.getMaxBuildHeight() - 1;

        for (int r = 0; r <= SPAWN_SEARCH_RADIUS; r++) {
            for (int x = center.getX() - r; x <= center.getX() + r; x++) {
                for (int z = center.getZ() - r; z <= center.getZ() + r; z++) {
                    // Only check the outer ring of each radius step
                    if (Math.abs(x - center.getX()) != r && Math.abs(z - center.getZ()) != r) continue;

                    for (int y = center.getY() + SPAWN_SEARCH_RADIUS; y >= center.getY() - SPAWN_SEARCH_RADIUS; y--) {
                        BlockPos ground = new BlockPos(x, y, z);
                        if (!level.getBlockState(ground).isSolid()) continue;

                        BlockPos feet = ground.above();
                        BlockPos head = feet.above();

                        if (y + 2 < maxHeight
                                && level.getBlockState(feet).isAir()
                                && level.getBlockState(head).isAir()) {
                            return feet;
                        }
                    }
                }
            }
        }

        return null;
    }
}