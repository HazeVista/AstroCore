package com.astro.core.events;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.astro.core.AstroCore;

import java.util.function.Function;

@SuppressWarnings("all")
@Mod.EventBusSubscriber(modid = AstroCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerSpawnHandler {

    private static final ResourceKey<Level> KUIPER_BELT = ResourceKey.create(
            Registries.DIMENSION,
            new ResourceLocation("ad_astra", "kuiper_belt"));

    // Structure dimensions from NBT
    private static final int STRUCT_W = 24;
    private static final int STRUCT_H = 9;
    private static final int STRUCT_D = 9;

    // Where inside the structure the player should stand
    private static final int PLAYER_LOCAL_X = 12;
    private static final int PLAYER_LOCAL_Y = 4;
    private static final int PLAYER_LOCAL_Z = 4;

    // How far out from 0, 0, 0 to search for clear space
    private static final int SEARCH_RADIUS = 200;
    private static final int PREFERRED_Y = 130;

    @SubscribeEvent
    public static void onPlayerFirstJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!player.level().dimension().equals(Level.OVERWORLD)) return;

        MinecraftServer server = player.level().getServer();
        if (server == null) return;

        ServerLevel kuiperLevel = server.getLevel(KUIPER_BELT);
        if (kuiperLevel == null) {
            AstroCore.LOGGER.warn("Kuiper Belt dimension not found, cannot teleport player {}",
                    player.getName().getString());
            return;
        }

        StationData stationData = StationData.get(kuiperLevel);

        player.changeDimension(kuiperLevel, new ITeleporter() {

            @Override
            public Entity placeEntity(Entity entity, ServerLevel currentLevel, ServerLevel destLevel,
                                      float yaw, Function<Boolean, Entity> repositionEntity) {
                entity = repositionEntity.apply(false);

                BlockPos spawnPos;

                if (!stationData.placed) {
                    // Find a clear space and place the station
                    BlockPos structureOrigin = findClearSpace(destLevel);

                    if (structureOrigin != null) {
                        placeStation(destLevel, structureOrigin);
                        spawnPos = structureOrigin.offset(PLAYER_LOCAL_X, PLAYER_LOCAL_Y, PLAYER_LOCAL_Z);
                        stationData.placed = true;
                        stationData.spawnX = spawnPos.getX();
                        stationData.spawnY = spawnPos.getY();
                        stationData.spawnZ = spawnPos.getZ();
                        stationData.setDirty();
                    } else {
                        AstroCore.LOGGER.error("Could not find clear space for kuiper station!");
                        // Fallback: just place at a hardcoded position
                        structureOrigin = new BlockPos(0, PREFERRED_Y, 0);
                        placeStation(destLevel, structureOrigin);
                        spawnPos = structureOrigin.offset(PLAYER_LOCAL_X, PLAYER_LOCAL_Y, PLAYER_LOCAL_Z);
                        stationData.placed = true;
                        stationData.spawnX = spawnPos.getX();
                        stationData.spawnY = spawnPos.getY();
                        stationData.spawnZ = spawnPos.getZ();
                        stationData.setDirty();
                    }
                } else {
                    spawnPos = new BlockPos(stationData.spawnX, stationData.spawnY, stationData.spawnZ);
                }

                entity.teleportTo(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5);
                player.setRespawnPosition(KUIPER_BELT, spawnPos, 0, true, false);

                return entity;
            }
        });
    }

    private static void placeStation(ServerLevel level, BlockPos origin) {
        ResourceLocation structureId = new ResourceLocation("astrogreg", "spawn_rocket");
        var structureManager = level.getServer().getStructureManager();

        structureManager.get(structureId).ifPresentOrElse(
                structure -> {
                    StructurePlaceSettings settings = new StructurePlaceSettings();
                    structure.placeInWorld(level, origin, origin, settings, level.random, 3);
                    AstroCore.LOGGER.info("Placed kuiper station at {}", origin);
                },
                () -> AstroCore.LOGGER.error("Could not find structure astrogreg:spawn_rocket!"));
    }

    // Searches for a location in the kuiper belt where the full structure footprint is entirely empty.
    private static BlockPos findClearSpace(ServerLevel level) {
        for (int r = 0; r <= SEARCH_RADIUS; r += 8) {
            for (int x = -r; x <= r; x += 8) {
                for (int z = -r; z <= r; z += 8) {
                    if (Math.abs(x) != r && Math.abs(z) != r) continue;

                    BlockPos candidate = new BlockPos(x, PREFERRED_Y, z);
                    if (isClearEnough(level, candidate)) {
                        return candidate;
                    }
                }
            }
        }
        return null;
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        MinecraftServer server = player.getServer();
        if (server == null) return;

        ServerLevel kuiperLevel = server.getLevel(KUIPER_BELT);
        if (kuiperLevel == null) return;

        StationData stationData = StationData.get(kuiperLevel);
        if (!stationData.placed) return;

        BlockPos spawnPos = new BlockPos(stationData.spawnX, stationData.spawnY, stationData.spawnZ);

        player.changeDimension(kuiperLevel, new ITeleporter() {

            @Override
            public Entity placeEntity(Entity entity, ServerLevel currentLevel, ServerLevel destLevel,
                                      float yaw, Function<Boolean, Entity> repositionEntity) {
                entity = repositionEntity.apply(false);
                entity.teleportTo(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5);
                return entity;
            }
        });
    }

    private static boolean isClearEnough(ServerLevel level, BlockPos origin) {
        for (int x = 0; x < STRUCT_W; x++) {
            for (int y = 0; y < STRUCT_H; y++) {
                for (int z = 0; z < STRUCT_D; z++) {
                    BlockPos check = origin.offset(x, y, z);
                    if (!level.getBlockState(check).isAir()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static class StationData extends SavedData {

        private static final String DATA_NAME = AstroCore.MOD_ID + "_station";

        public boolean placed = false;
        public int spawnX = 0;
        public int spawnY = PREFERRED_Y + PLAYER_LOCAL_Y;
        public int spawnZ = 0;

        public static StationData get(ServerLevel level) {
            return level.getDataStorage().computeIfAbsent(
                    StationData::load,
                    StationData::new,
                    DATA_NAME);
        }

        public static StationData load(CompoundTag tag) {
            StationData data = new StationData();
            data.placed = tag.getBoolean("placed");
            data.spawnX = tag.getInt("spawnX");
            data.spawnY = tag.getInt("spawnY");
            data.spawnZ = tag.getInt("spawnZ");
            return data;
        }

        @Override
        public CompoundTag save(CompoundTag tag) {
            tag.putBoolean("placed", placed);
            tag.putInt("spawnX", spawnX);
            tag.putInt("spawnY", spawnY);
            tag.putInt("spawnZ", spawnZ);
            return tag;
        }
    }
}
