package com.leclowndu93150.leaderboards.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.leclowndu93150.leaderboards.Leaderboards;
import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class LeaderboardsClientConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static LeaderboardsClientConfig instance;

    public boolean hideButtonWhenOnlyOnePlayerExist = false;

    public static LeaderboardsClientConfig get() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }

    private static LeaderboardsClientConfig load() {
        Path path = configPath();
        if (Files.exists(path)) {
            try {
                LeaderboardsClientConfig parsed = GSON.fromJson(Files.readString(path), LeaderboardsClientConfig.class);
                if (parsed != null) return parsed;
            } catch (Exception e) {
                Leaderboards.LOGGER.warn("Failed to read leaderboards client config, using defaults", e);
            }
        }
        LeaderboardsClientConfig fresh = new LeaderboardsClientConfig();
        fresh.save();
        return fresh;
    }

    private void save() {
        Path path = configPath();
        try {
            Files.createDirectories(path.getParent());
            Files.writeString(path, GSON.toJson(this));
        } catch (IOException e) {
            Leaderboards.LOGGER.warn("Failed to write leaderboards client config", e);
        }
    }

    private static Path configPath() {
        return Minecraft.getInstance().gameDirectory.toPath().resolve("config").resolve("leaderboards-client.json");
    }
}
