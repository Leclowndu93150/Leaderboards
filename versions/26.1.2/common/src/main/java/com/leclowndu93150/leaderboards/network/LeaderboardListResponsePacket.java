package com.leclowndu93150.leaderboards.network;

import com.leclowndu93150.leaderboards.Leaderboards;
import com.leclowndu93150.leaderboards.data.Leaderboard;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public record LeaderboardListResponsePacket(Map<Identifier, Component> leaderboards) implements CustomPacketPayload {
    public static final Type<LeaderboardListResponsePacket> TYPE = new Type<>(
            Identifier.fromNamespaceAndPath(Leaderboards.MODID, "leaderboard_list_response"));

    public static final StreamCodec<RegistryFriendlyByteBuf, LeaderboardListResponsePacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.map(LinkedHashMap::new, Identifier.STREAM_CODEC, ComponentSerialization.STREAM_CODEC),
                    LeaderboardListResponsePacket::leaderboards,
                    LeaderboardListResponsePacket::new);

    public static LeaderboardListResponsePacket fromLeaderboards(Map<Identifier, Leaderboard> leaderboards) {
        Map<Identifier, Component> map = new LinkedHashMap<>();
        for (Map.Entry<Identifier, Leaderboard> entry : leaderboards.entrySet()) {
            map.put(entry.getKey(), entry.getValue().getTitle());
        }
        return new LeaderboardListResponsePacket(map);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
