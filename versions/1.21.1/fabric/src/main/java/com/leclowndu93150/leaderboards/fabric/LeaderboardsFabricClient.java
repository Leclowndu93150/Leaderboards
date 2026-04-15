package com.leclowndu93150.leaderboards.fabric;

import com.leclowndu93150.leaderboards.client.LeaderboardsClientEvents;
import com.leclowndu93150.leaderboards.gui.LeaderboardListScreen;
import com.leclowndu93150.leaderboards.gui.LeaderboardScreen;
import com.leclowndu93150.leaderboards.network.LeaderboardListResponsePacket;
import com.leclowndu93150.leaderboards.network.LeaderboardResponsePacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class LeaderboardsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(LeaderboardListResponsePacket.TYPE,
                (payload, ctx) -> ctx.client().execute(() ->
                        new LeaderboardListScreen(payload.leaderboards()).openGui()));

        ClientPlayNetworking.registerGlobalReceiver(LeaderboardResponsePacket.TYPE,
                (payload, ctx) -> ctx.client().execute(() ->
                        new LeaderboardScreen(payload.title(), payload.values()).openGui()));

        LeaderboardsClientEvents.init();
    }
}
