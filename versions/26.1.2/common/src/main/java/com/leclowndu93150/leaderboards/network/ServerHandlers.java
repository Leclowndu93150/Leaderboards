package com.leclowndu93150.leaderboards.network;

import com.leclowndu93150.leaderboards.LeaderboardRegistry;
import com.leclowndu93150.leaderboards.VanillaStatsRegistry;
import com.leclowndu93150.leaderboards.data.Leaderboard;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.BiConsumer;

public final class ServerHandlers {
    private ServerHandlers() {}

    public static void onRequestList(RequestLeaderboardListPacket packet, ServerPlayer sender,
                                     BiConsumer<ServerPlayer, CustomPacketPayload> send) {
        send.accept(sender, LeaderboardListResponsePacket.fromLeaderboards(LeaderboardRegistry.LEADERBOARDS));
    }

    public static void onRequestLeaderboard(RequestLeaderboardPacket packet, ServerPlayer sender,
                                            BiConsumer<ServerPlayer, CustomPacketPayload> send) {
        Leaderboard leaderboard = LeaderboardRegistry.LEADERBOARDS.get(packet.id());
        if (leaderboard == null) {
            leaderboard = VanillaStatsRegistry.VANILLA_STATS.get(packet.id());
        }
        if (leaderboard != null) {
            send.accept(sender, LeaderboardResponsePacket.build(sender, leaderboard));
        }
    }
}
