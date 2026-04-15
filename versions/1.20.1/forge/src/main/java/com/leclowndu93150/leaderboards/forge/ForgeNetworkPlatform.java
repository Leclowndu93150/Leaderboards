package com.leclowndu93150.leaderboards.forge;

import com.leclowndu93150.leaderboards.network.RequestLeaderboardListPacket;
import com.leclowndu93150.leaderboards.network.RequestLeaderboardPacket;
import com.leclowndu93150.leaderboards.platform.NetworkPlatform;

public class ForgeNetworkPlatform implements NetworkPlatform {
    @Override
    public void sendRequestList(RequestLeaderboardListPacket packet) {
        LeaderboardsForge.NETWORK.sendToServer(packet);
    }

    @Override
    public void sendRequestLeaderboard(RequestLeaderboardPacket packet) {
        LeaderboardsForge.NETWORK.sendToServer(packet);
    }
}
