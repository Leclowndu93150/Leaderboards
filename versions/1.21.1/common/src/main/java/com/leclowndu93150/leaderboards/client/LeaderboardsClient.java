package com.leclowndu93150.leaderboards.client;

import com.leclowndu93150.leaderboards.network.RequestLeaderboardListPacket;
import com.leclowndu93150.leaderboards.platform.Services;

public final class LeaderboardsClient {
    private LeaderboardsClient() {}

    public static void openLeaderboardsList() {
        Services.NETWORK.sendToServer(new RequestLeaderboardListPacket());
    }
}
