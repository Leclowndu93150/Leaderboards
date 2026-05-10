package com.leclowndu93150.leaderboards.client;

import com.leclowndu93150.leaderboards.Leaderboards;
import com.leclowndu93150.leaderboards.network.RequestLeaderboardListPacket;
import com.leclowndu93150.leaderboards.platform.Services;
import dev.ftb.mods.ftblibrary.sidebar.RegisteredSidebarButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.LevelResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public final class LeaderboardsClient {
    public static final Identifier SIDEBAR_BUTTON_ID = Identifier.fromNamespaceAndPath(Leaderboards.MODID, "leaderboards");

    private LeaderboardsClient() {}

    public static void openLeaderboardsList() {
        Services.NETWORK.sendToServer(new RequestLeaderboardListPacket());
    }

    public static void onSidebarButtonCreated(RegisteredSidebarButton button) {
        if (button.getId().equals(SIDEBAR_BUTTON_ID)) {
            button.addVisibilityCondition(LeaderboardsClient::isSharedWorld);
        }
    }

    private static long lastCheckTick;
    private static boolean lastResult;

    private static boolean isSharedWorld() {
        Minecraft mc = Minecraft.getInstance();
        IntegratedServer integrated = mc.getSingleplayerServer();
        if (integrated == null) {
            return mc.getCurrentServer() != null;
        }
        long tick = integrated.getTickCount();
        if (tick - lastCheckTick < 40 && lastCheckTick != 0) {
            return lastResult;
        }
        lastCheckTick = tick;
        lastResult = countPlayerData(integrated.getWorldPath(LevelResource.PLAYER_DATA_DIR)) > 1;
        return lastResult;
    }

    private static int countPlayerData(Path dir) {
        if (!Files.isDirectory(dir)) {
            return 0;
        }
        try (Stream<Path> stream = Files.list(dir)) {
            return (int) stream.filter(p -> p.getFileName().toString().endsWith(".dat")).count();
        } catch (IOException e) {
            return 0;
        }
    }
}
