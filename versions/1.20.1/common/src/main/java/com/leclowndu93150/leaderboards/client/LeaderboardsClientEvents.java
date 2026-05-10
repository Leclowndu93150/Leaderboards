package com.leclowndu93150.leaderboards.client;

import com.leclowndu93150.leaderboards.Leaderboards;
import dev.architectury.event.EventResult;
import dev.ftb.mods.ftblibrary.ui.CustomClickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.LevelResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public final class LeaderboardsClientEvents {
    public static final ResourceLocation SIDEBAR_BUTTON_ID = new ResourceLocation(Leaderboards.MODID, "leaderboards");

    private LeaderboardsClientEvents() {}

    public static void init() {
        CustomClickEvent.EVENT.register(event -> {
            if (event.id().getNamespace().equals(Leaderboards.MODID)
                    && "open_leaderboards".equals(event.id().getPath())) {
                LeaderboardsClient.openLeaderboardsList();
                return EventResult.interruptFalse();
            }
            return EventResult.pass();
        });
    }

    private static long lastCheckTick;
    private static boolean lastResult;

    public static boolean isSharedWorld() {
        if (!LeaderboardsClientConfig.get().hideButtonWhenOnlyOnePlayerExist) {
            return true;
        }
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
