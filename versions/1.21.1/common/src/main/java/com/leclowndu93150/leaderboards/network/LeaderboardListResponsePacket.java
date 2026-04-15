package com.leclowndu93150.leaderboards.network;

import com.leclowndu93150.leaderboards.Leaderboards;
import com.leclowndu93150.leaderboards.data.Leaderboard;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.Map;

public record LeaderboardListResponsePacket(Map<ResourceLocation, Component> leaderboards) implements CustomPacketPayload {
    public static final Type<LeaderboardListResponsePacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(Leaderboards.MODID, "leaderboard_list_response"));

    public static final StreamCodec<RegistryFriendlyByteBuf, LeaderboardListResponsePacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.map(LinkedHashMap::new, ResourceLocation.STREAM_CODEC, ComponentSerialization.STREAM_CODEC),
                    LeaderboardListResponsePacket::leaderboards,
                    LeaderboardListResponsePacket::new);

    public static LeaderboardListResponsePacket fromLeaderboards(Map<ResourceLocation, Leaderboard> leaderboards) {
        Map<ResourceLocation, Component> map = new LinkedHashMap<>();
        for (Map.Entry<ResourceLocation, Leaderboard> entry : leaderboards.entrySet()) {
            map.put(entry.getKey(), entry.getValue().getTitle());
        }
        return new LeaderboardListResponsePacket(map);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
