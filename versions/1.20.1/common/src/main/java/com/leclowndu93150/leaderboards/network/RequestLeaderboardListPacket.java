package com.leclowndu93150.leaderboards.network;

import com.leclowndu93150.leaderboards.Leaderboards;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class RequestLeaderboardListPacket {
    public static final ResourceLocation ID = new ResourceLocation(Leaderboards.MODID, "request_leaderboard_list");

    public RequestLeaderboardListPacket() {}

    public static void encode(RequestLeaderboardListPacket packet, FriendlyByteBuf buf) {}

    public static RequestLeaderboardListPacket decode(FriendlyByteBuf buf) {
        return new RequestLeaderboardListPacket();
    }
}
