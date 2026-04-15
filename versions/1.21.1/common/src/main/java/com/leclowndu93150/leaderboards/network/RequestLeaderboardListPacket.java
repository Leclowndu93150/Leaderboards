package com.leclowndu93150.leaderboards.network;

import com.leclowndu93150.leaderboards.Leaderboards;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record RequestLeaderboardListPacket() implements CustomPacketPayload {
    public static final Type<RequestLeaderboardListPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(Leaderboards.MODID, "request_leaderboard_list"));

    public static final StreamCodec<FriendlyByteBuf, RequestLeaderboardListPacket> STREAM_CODEC =
            StreamCodec.unit(new RequestLeaderboardListPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
