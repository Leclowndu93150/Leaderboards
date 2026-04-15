package com.leclowndu93150.leaderboards.network;

import com.leclowndu93150.leaderboards.util.KnownPlayers;
import com.leclowndu93150.leaderboards.util.OfflinePlayerStats;
import com.leclowndu93150.leaderboards.Leaderboards;
import com.leclowndu93150.leaderboards.data.Leaderboard;
import com.leclowndu93150.leaderboards.data.LeaderboardValue;
import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record LeaderboardResponsePacket(Component title, List<LeaderboardValue> values) implements CustomPacketPayload {
    public static final Type<LeaderboardResponsePacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(Leaderboards.MODID, "leaderboard_response"));

    private static final StreamCodec<RegistryFriendlyByteBuf, LeaderboardValue> VALUE_CODEC = StreamCodec.of(
            (buf, value) -> {
                buf.writeUtf(value.username);
                buf.writeVarInt(value.rank);
                ComponentSerialization.STREAM_CODEC.encode(buf, value.value);
                buf.writeByte(value.color.getId());
            },
            buf -> {
                LeaderboardValue v = new LeaderboardValue();
                v.username = buf.readUtf();
                v.rank = buf.readVarInt();
                v.value = ComponentSerialization.STREAM_CODEC.decode(buf);
                v.color = ChatFormatting.getById(buf.readByte());
                return v;
            }
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, LeaderboardResponsePacket> STREAM_CODEC =
            StreamCodec.composite(
                    ComponentSerialization.STREAM_CODEC, LeaderboardResponsePacket::title,
                    VALUE_CODEC.apply(ByteBufCodecs.list()), LeaderboardResponsePacket::values,
                    LeaderboardResponsePacket::new);

    public static LeaderboardResponsePacket build(ServerPlayer requestingPlayer, Leaderboard leaderboard) {
        List<OfflinePlayerStats> players = new ArrayList<>();
        for (UUID uuid : KnownPlayers.all(requestingPlayer.level().getServer())) {
            OfflinePlayerStats.of(requestingPlayer.level().getServer(), uuid).ifPresent(players::add);
        }
        players.sort(leaderboard.getComparator());

        List<LeaderboardValue> values = new ArrayList<>(players.size());
        UUID requesterId = requestingPlayer.getUUID();
        for (int i = 0; i < players.size(); i++) {
            OfflinePlayerStats p = players.get(i);
            LeaderboardValue v = new LeaderboardValue();
            v.username = p.name();
            v.rank = i + 1;
            v.value = leaderboard.createValue(p);

            if (p.uuid().equals(requesterId)) {
                v.color = ChatFormatting.DARK_GREEN;
            } else if (!leaderboard.hasValidValue(p)) {
                v.color = ChatFormatting.DARK_GRAY;
            } else if (i < 3) {
                v.color = ChatFormatting.GOLD;
            } else {
                v.color = ChatFormatting.RESET;
            }
            values.add(v);
        }
        return new LeaderboardResponsePacket(leaderboard.getTitle(), values);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
