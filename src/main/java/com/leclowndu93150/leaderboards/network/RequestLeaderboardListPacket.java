package com.leclowndu93150.leaderboards.network;

import com.leclowndu93150.leaderboards.Leaderboards;
import com.leclowndu93150.leaderboards.LeaderboardRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class RequestLeaderboardListPacket {

    public RequestLeaderboardListPacket() {
    }

    public static void encode(RequestLeaderboardListPacket packet, FriendlyByteBuf buf) {
    }

    public static RequestLeaderboardListPacket decode(FriendlyByteBuf buf) {
        return new RequestLeaderboardListPacket();
    }

    public static void handle(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, 
                              FriendlyByteBuf buf, PacketSender responseSender) {
        server.execute(() -> {
            FriendlyByteBuf responseBuf = PacketByteBufs.create();
            LeaderboardListResponsePacket response = LeaderboardListResponsePacket.fromLeaderboards(LeaderboardRegistry.LEADERBOARDS);
            LeaderboardListResponsePacket.encode(response, responseBuf);
            ServerPlayNetworking.send(player, Leaderboards.LEADERBOARD_LIST_RESPONSE_PACKET, responseBuf);
        });
    }
}
