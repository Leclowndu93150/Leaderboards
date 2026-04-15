package com.leclowndu93150.leaderboards.platform;

import com.leclowndu93150.leaderboards.network.RequestLeaderboardListPacket;
import com.leclowndu93150.leaderboards.network.RequestLeaderboardPacket;

public interface NetworkPlatform {
    void sendRequestList(RequestLeaderboardListPacket packet);

    void sendRequestLeaderboard(RequestLeaderboardPacket packet);
}
