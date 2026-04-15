package com.leclowndu93150.leaderboards.fabric;

import com.leclowndu93150.leaderboards.network.RequestLeaderboardListPacket;
import com.leclowndu93150.leaderboards.network.RequestLeaderboardPacket;
import com.leclowndu93150.leaderboards.platform.NetworkPlatform;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.FriendlyByteBuf;

public class FabricNetworkPlatform implements NetworkPlatform {
    @Override
    public void sendRequestList(RequestLeaderboardListPacket packet) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        RequestLeaderboardListPacket.encode(packet, buf);
        ClientPlayNetworking.send(RequestLeaderboardListPacket.ID, buf);
    }

    @Override
    public void sendRequestLeaderboard(RequestLeaderboardPacket packet) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        RequestLeaderboardPacket.encode(packet, buf);
        ClientPlayNetworking.send(RequestLeaderboardPacket.ID, buf);
    }
}
