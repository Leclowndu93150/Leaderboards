package com.leclowndu93150.leaderboards.network;

import com.leclowndu93150.leaderboards.Leaderboards;
import com.leclowndu93150.leaderboards.LeaderboardRegistry;
import com.leclowndu93150.leaderboards.VanillaStatsRegistry;
import com.leclowndu93150.leaderboards.data.Leaderboard;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

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

    public static void handle(RequestLeaderboardPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                Leaderboard leaderboard = LeaderboardRegistry.LEADERBOARDS.get(packet.id);
                if (leaderboard == null) {
                    leaderboard = VanillaStatsRegistry.VANILLA_STATS.get(packet.id);
                }
                if (leaderboard != null) {
                    Leaderboards.NETWORK.send(
                        PacketDistributor.PLAYER.with(() -> player),
                        new LeaderboardResponsePacket(player, leaderboard)
                    );
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
