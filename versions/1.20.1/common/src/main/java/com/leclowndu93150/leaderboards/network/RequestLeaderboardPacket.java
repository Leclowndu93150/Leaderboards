package com.leclowndu93150.leaderboards.network;

import com.leclowndu93150.leaderboards.Leaderboards;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class RequestLeaderboardPacket {
    public static final ResourceLocation ID = new ResourceLocation(Leaderboards.MODID, "request_leaderboard");

    private final ResourceLocation leaderboardId;

    public RequestLeaderboardPacket(ResourceLocation leaderboardId) {
        this.leaderboardId = leaderboardId;
    }

    public ResourceLocation id() {
        return leaderboardId;
    }

    public static void encode(RequestLeaderboardPacket packet, FriendlyByteBuf buf) {
        buf.writeResourceLocation(packet.leaderboardId);
    }

    public static RequestLeaderboardPacket decode(FriendlyByteBuf buf) {
        return new RequestLeaderboardPacket(buf.readResourceLocation());
    }
}
