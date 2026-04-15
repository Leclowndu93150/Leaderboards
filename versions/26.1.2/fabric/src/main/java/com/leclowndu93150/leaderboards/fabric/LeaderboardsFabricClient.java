package com.leclowndu93150.leaderboards.fabric;

import com.leclowndu93150.leaderboards.Leaderboards;
import com.leclowndu93150.leaderboards.client.LeaderboardsClient;
import com.leclowndu93150.leaderboards.gui.LeaderboardListScreen;
import com.leclowndu93150.leaderboards.gui.LeaderboardScreen;
import com.leclowndu93150.leaderboards.network.LeaderboardListResponsePacket;
import com.leclowndu93150.leaderboards.network.LeaderboardResponsePacket;
import dev.ftb.mods.ftblibrary.fabric.FTBLibraryFabricEvents;
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

        FTBLibraryFabricEvents.CUSTOM_CLICK.register(data -> {
            if (data.id().getNamespace().equals(Leaderboards.MODID) && "open_leaderboards".equals(data.id().getPath())) {
                LeaderboardsClient.openLeaderboardsList();
                return true;
            }
            return false;
        });
    }
}
