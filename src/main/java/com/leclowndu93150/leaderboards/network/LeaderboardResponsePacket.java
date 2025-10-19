package com.leclowndu93150.leaderboards.network;

import com.leclowndu93150.leaderboards.data.Leaderboard;
import com.leclowndu93150.leaderboards.data.LeaderboardValue;
import com.leclowndu93150.leaderboards.data.PlayerDataTracker;
import com.leclowndu93150.leaderboards.data.PlayerStatsWrapper;
import com.leclowndu93150.leaderboards.gui.LeaderboardScreen;
import com.mojang.authlib.GameProfile;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.network.NetworkEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class LeaderboardResponsePacket {
    private final Component title;
    private final List<LeaderboardValue> values;

    public LeaderboardResponsePacket(Component title, List<LeaderboardValue> values) {
        this.title = title;
        this.values = values;
    }

    public LeaderboardResponsePacket(ServerPlayer requestingPlayer, Leaderboard leaderboard) {
        this(leaderboard.getTitle(), createValues(requestingPlayer, leaderboard));
    }

    public static void encode(LeaderboardResponsePacket packet, FriendlyByteBuf buf) {
        buf.writeComponent(packet.title);
        buf.writeVarInt(packet.values.size());
        for (LeaderboardValue value : packet.values) {
            buf.writeUtf(value.username);
            buf.writeComponent(value.value);
            buf.writeByte(value.color.getId());
        }
    }

    public static LeaderboardResponsePacket decode(FriendlyByteBuf buf) {
        Component title = buf.readComponent();
        int size = buf.readVarInt();
        List<LeaderboardValue> values = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            LeaderboardValue value = new LeaderboardValue();
            value.username = buf.readUtf();
            value.value = buf.readComponent();
            value.color = ChatFormatting.getById(buf.readByte());
            values.add(value);
        }
        return new LeaderboardResponsePacket(title, values);
    }

    private static ServerStatsCounter loadPlayerStats(MinecraftServer server, UUID uuid) {
        File statsDir = server.getWorldPath(LevelResource.PLAYER_STATS_DIR).toFile();
        File statsFile = new File(statsDir, uuid.toString() + ".json");
        return new ServerStatsCounter(server, statsFile);
    }

    private static List<LeaderboardValue> createValues(ServerPlayer requestingPlayer, Leaderboard leaderboard) {
        List<LeaderboardValue> values = new ArrayList<>();
        List<PlayerStatsWrapper> players = new ArrayList<>();
        
        PlayerDataTracker tracker = PlayerDataTracker.get(requestingPlayer.server.overworld());
        
        for (ServerPlayer onlinePlayer : requestingPlayer.server.getPlayerList().getPlayers()) {
            players.add(new PlayerStatsWrapper(onlinePlayer));
        }
        
        tracker.getAllPlayerUUIDs().forEach(uuid -> {
            if (requestingPlayer.server.getPlayerList().getPlayer(uuid) == null) {
                GameProfile profile = requestingPlayer.server.getProfileCache().get(uuid).orElse(null);
                if (profile != null) {
                    ServerStatsCounter stats = loadPlayerStats(requestingPlayer.server, uuid);
                    players.add(new PlayerStatsWrapper(uuid, profile, stats, requestingPlayer.server));
                }
            }
        });
        
        players.sort(leaderboard.getComparator());

        for (int i = 0; i < players.size(); i++) {
            PlayerStatsWrapper player = players.get(i);
            LeaderboardValue value = new LeaderboardValue();
            value.username = player.getGameProfile().getName();
            value.value = leaderboard.createValue(player);

            if (player.getUUID().equals(requestingPlayer.getUUID())) {
                value.color = ChatFormatting.DARK_GREEN;
            } else if (!leaderboard.hasValidValue(player)) {
                value.color = ChatFormatting.DARK_GRAY;
            } else if (i < 3) {
                value.color = ChatFormatting.GOLD;
            } else {
                value.color = ChatFormatting.RESET;
            }

            values.add(value);
        }

        return values;
    }

    public static void handle(LeaderboardResponsePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft.getInstance().execute(() -> {
                LeaderboardScreen screen = new LeaderboardScreen(packet.title, packet.values);
                screen.openGui();
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
