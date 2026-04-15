package com.leclowndu93150.leaderboards.client;

import com.leclowndu93150.leaderboards.network.RequestLeaderboardListPacket;
import com.leclowndu93150.leaderboards.network.RequestLeaderboardPacket;
import com.leclowndu93150.leaderboards.platform.Services;
import net.minecraft.resources.ResourceLocation;

public final class LeaderboardsClient {
    private LeaderboardsClient() {}

    public static void openLeaderboardsList() {
        Services.NETWORK.sendRequestList(new RequestLeaderboardListPacket());
    }

    public static void openLeaderboard(ResourceLocation id) {
        Services.NETWORK.sendRequestLeaderboard(new RequestLeaderboardPacket(id));
    }
}
