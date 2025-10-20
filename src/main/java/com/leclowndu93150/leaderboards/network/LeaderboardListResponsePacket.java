package com.leclowndu93150.leaderboards.network;

import com.leclowndu93150.leaderboards.data.Leaderboard;
import com.leclowndu93150.leaderboards.gui.LeaderboardListScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.Map;

public class LeaderboardListResponsePacket {
    private final Map<ResourceLocation, Component> leaderboards;

    public LeaderboardListResponsePacket(Map<ResourceLocation, Component> leaderboards) {
        this.leaderboards = leaderboards;
    }

    public static void encode(LeaderboardListResponsePacket packet, FriendlyByteBuf buf) {
        buf.writeVarInt(packet.leaderboards.size());
        for (Map.Entry<ResourceLocation, Component> entry : packet.leaderboards.entrySet()) {
            buf.writeResourceLocation(entry.getKey());
            buf.writeComponent(entry.getValue());
        }
    }

    public static LeaderboardListResponsePacket decode(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        Map<ResourceLocation, Component> map = new LinkedHashMap<>();
        for (int i = 0; i < size; i++) {
            ResourceLocation id = buf.readResourceLocation();
            Component title = buf.readComponent();
            map.put(id, title);
        }
        return new LeaderboardListResponsePacket(map);
    }

    public static LeaderboardListResponsePacket fromLeaderboards(Map<ResourceLocation, Leaderboard> leaderboards) {
        Map<ResourceLocation, Component> map = new LinkedHashMap<>();
        for (Map.Entry<ResourceLocation, Leaderboard> entry : leaderboards.entrySet()) {
            map.put(entry.getKey(), entry.getValue().getTitle());
        }
        return new LeaderboardListResponsePacket(map);
    }

    public static void handle(FriendlyByteBuf buf, Minecraft client) {
        LeaderboardListResponsePacket packet = decode(buf);
        client.execute(() -> {
            LeaderboardListScreen screen = new LeaderboardListScreen(packet.leaderboards);
            screen.openGui();
        });
    }
}
