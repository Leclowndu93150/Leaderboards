package com.leclowndu93150.leaderboards;

import com.leclowndu93150.leaderboards.data.PlayerDataTracker;
import com.leclowndu93150.leaderboards.network.*;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public class Leaderboards implements ModInitializer {
    public static final String MODID = "leaderboards";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final ResourceLocation REQUEST_LEADERBOARD_LIST_PACKET = new ResourceLocation(MODID, "request_leaderboard_list");
    public static final ResourceLocation LEADERBOARD_LIST_RESPONSE_PACKET = new ResourceLocation(MODID, "leaderboard_list_response");
    public static final ResourceLocation REQUEST_LEADERBOARD_PACKET = new ResourceLocation(MODID, "request_leaderboard");
    public static final ResourceLocation LEADERBOARD_RESPONSE_PACKET = new ResourceLocation(MODID, "leaderboard_response");

    @Override
    public void onInitialize() {
        ServerPlayNetworking.registerGlobalReceiver(REQUEST_LEADERBOARD_LIST_PACKET, RequestLeaderboardListPacket::handle);
        ServerPlayNetworking.registerGlobalReceiver(REQUEST_LEADERBOARD_PACKET, RequestLeaderboardPacket::handle);
        
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            PlayerDataTracker tracker = PlayerDataTracker.get(server.overworld());
            PlayerDataTracker.setInstance(tracker);
        });
        
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            PlayerDataTracker tracker = PlayerDataTracker.get(server.overworld());
            tracker.updateLastSeen(handler.getPlayer().getUUID(), server.overworld().getGameTime());
        });
        
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            PlayerDataTracker tracker = PlayerDataTracker.get(server.overworld());
            tracker.updateLastSeen(handler.getPlayer().getUUID(), server.overworld().getGameTime());
        });
        
        LeaderboardRegistry.register();
        LOGGER.info("Leaderboards initialized");
    }
}
