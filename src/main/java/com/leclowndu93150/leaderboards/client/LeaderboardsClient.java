package com.leclowndu93150.leaderboards.client;

import com.leclowndu93150.leaderboards.Leaderboards;
import com.leclowndu93150.leaderboards.network.RequestLeaderboardListPacket;

public class LeaderboardsClient {
    public static void openLeaderboardsList() {
        Leaderboards.NETWORK.sendToServer(new RequestLeaderboardListPacket());
    }
}
