package com.leclowndu93150.leaderboards.data;

import com.leclowndu93150.leaderboards.Leaderboards;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PlayerDataTracker extends SavedData {
    private static final String DATA_NAME = Leaderboards.MODID + "_player_data";
    private final Map<UUID, Long> lastSeenTimes = new HashMap<>();

    public static PlayerDataTracker create() {
        return new PlayerDataTracker();
    }

    public static PlayerDataTracker load(CompoundTag tag) {
        PlayerDataTracker data = new PlayerDataTracker();
        CompoundTag playerData = tag.getCompound("PlayerData");
        for (String key : playerData.getAllKeys()) {
            try {
                UUID uuid = UUID.fromString(key);
                long lastSeen = playerData.getLong(key);
                data.lastSeenTimes.put(uuid, lastSeen);
            } catch (IllegalArgumentException ignored) {
            }
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        CompoundTag playerData = new CompoundTag();
        for (Map.Entry<UUID, Long> entry : lastSeenTimes.entrySet()) {
            playerData.putLong(entry.getKey().toString(), entry.getValue());
        }
        tag.put("PlayerData", playerData);
        return tag;
    }

    public static PlayerDataTracker get(MinecraftServer server) {
        ServerLevel overworld = server.overworld();
        return overworld.getDataStorage().computeIfAbsent(
                PlayerDataTracker::load,
                PlayerDataTracker::create,
                DATA_NAME);
    }

    public long getLastSeen(UUID uuid) {
        return lastSeenTimes.getOrDefault(uuid, 0L);
    }

    public void markSeen(UUID uuid, long gameTime) {
        lastSeenTimes.put(uuid, gameTime);
        setDirty();
    }

    public Set<UUID> keys() {
        return lastSeenTimes.keySet();
    }
}
