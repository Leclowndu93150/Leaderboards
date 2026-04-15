package com.leclowndu93150.leaderboards.network;

import com.leclowndu93150.leaderboards.Leaderboards;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record RequestLeaderboardPacket(Identifier id) implements CustomPacketPayload {
    public static final Type<RequestLeaderboardPacket> TYPE = new Type<>(
            Identifier.fromNamespaceAndPath(Leaderboards.MODID, "request_leaderboard"));

    public static final StreamCodec<FriendlyByteBuf, RequestLeaderboardPacket> STREAM_CODEC =
            StreamCodec.composite(
                    Identifier.STREAM_CODEC, RequestLeaderboardPacket::id,
                    RequestLeaderboardPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
