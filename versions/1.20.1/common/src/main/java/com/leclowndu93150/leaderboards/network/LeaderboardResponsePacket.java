package com.leclowndu93150.leaderboards.network;

import com.leclowndu93150.leaderboards.Leaderboards;
import com.leclowndu93150.leaderboards.data.Leaderboard;
import com.leclowndu93150.leaderboards.data.LeaderboardValue;
import com.leclowndu93150.leaderboards.util.KnownPlayers;
import com.leclowndu93150.leaderboards.util.OfflinePlayerStats;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LeaderboardResponsePacket {
    public static final ResourceLocation ID = new ResourceLocation(Leaderboards.MODID, "leaderboard_response");

    private final Component title;
    private final List<LeaderboardValue> values;

    public LeaderboardResponsePacket(Component title, List<LeaderboardValue> values) {
        this.title = title;
        this.values = values;
    }

    public Component title() {
        return title;
    }

    public List<LeaderboardValue> values() {
        return values;
    }

    public static void encode(LeaderboardResponsePacket packet, FriendlyByteBuf buf) {
        buf.writeComponent(packet.title);
        buf.writeVarInt(packet.values.size());
        for (LeaderboardValue v : packet.values) {
            buf.writeUtf(v.username);
            buf.writeVarInt(v.rank);
            buf.writeComponent(v.value);
            buf.writeByte(v.color.getId());
        }
    }

    public static LeaderboardResponsePacket decode(FriendlyByteBuf buf) {
        Component title = buf.readComponent();
        int size = buf.readVarInt();
        List<LeaderboardValue> values = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            LeaderboardValue v = new LeaderboardValue();
            v.username = buf.readUtf();
            v.rank = buf.readVarInt();
            v.value = buf.readComponent();
            v.color = ChatFormatting.getById(buf.readByte());
            values.add(v);
        }
        return new LeaderboardResponsePacket(title, values);
    }

    public static LeaderboardResponsePacket build(ServerPlayer requestingPlayer, Leaderboard leaderboard) {
        List<OfflinePlayerStats> players = new ArrayList<>();
        for (UUID uuid : KnownPlayers.all(requestingPlayer.server)) {
            OfflinePlayerStats.of(requestingPlayer.server, uuid).ifPresent(players::add);
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
}
