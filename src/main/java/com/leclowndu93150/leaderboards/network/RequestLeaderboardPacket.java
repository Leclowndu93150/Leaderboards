package com.leclowndu93150.leaderboards.network;

import com.leclowndu93150.leaderboards.Leaderboards;
import com.leclowndu93150.leaderboards.LeaderboardRegistry;
import com.leclowndu93150.leaderboards.VanillaStatsRegistry;
import com.leclowndu93150.leaderboards.data.Leaderboard;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class RequestLeaderboardPacket {
    private final ResourceLocation id;

    public RequestLeaderboardPacket(ResourceLocation id) {
        this.id = id;
    }

    public static void encode(RequestLeaderboardPacket packet, FriendlyByteBuf buf) {
        buf.writeResourceLocation(packet.id);
    }

    public static RequestLeaderboardPacket decode(FriendlyByteBuf buf) {
        return new RequestLeaderboardPacket(buf.readResourceLocation());
    }

    public static void handle(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler,
                              FriendlyByteBuf buf, PacketSender responseSender) {
        RequestLeaderboardPacket packet = decode(buf);
        server.execute(() -> {
            Leaderboard leaderboard = LeaderboardRegistry.LEADERBOARDS.get(packet.id);
            if (leaderboard == null) {
                leaderboard = VanillaStatsRegistry.VANILLA_STATS.get(packet.id);
            }
            if (leaderboard != null) {
                FriendlyByteBuf responseBuf = PacketByteBufs.create();
                LeaderboardResponsePacket response = new LeaderboardResponsePacket(player, leaderboard);
                LeaderboardResponsePacket.encode(response, responseBuf);
                ServerPlayNetworking.send(player, Leaderboards.LEADERBOARD_RESPONSE_PACKET, responseBuf);
            }
        });
    }
}
