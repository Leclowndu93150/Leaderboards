package com.leclowndu93150.leaderboards.network;

import com.leclowndu93150.leaderboards.Leaderboards;
import com.leclowndu93150.leaderboards.LeaderboardRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class RequestLeaderboardListPacket {

    public RequestLeaderboardListPacket() {
    }

    public static void encode(RequestLeaderboardListPacket packet, FriendlyByteBuf buf) {
    }

    public static RequestLeaderboardListPacket decode(FriendlyByteBuf buf) {
        return new RequestLeaderboardListPacket();
    }

    public static void handle(RequestLeaderboardListPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                Leaderboards.NETWORK.send(
                    PacketDistributor.PLAYER.with(() -> player),
                    LeaderboardListResponsePacket.fromLeaderboards(LeaderboardRegistry.LEADERBOARDS)
                );
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
