package com.leclowndu93150.leaderboards.client;

import com.leclowndu93150.leaderboards.Leaderboards;
import com.leclowndu93150.leaderboards.network.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.FriendlyByteBuf;

public class LeaderboardsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(Leaderboards.LEADERBOARD_LIST_RESPONSE_PACKET, 
            (client, handler, buf, responseSender) -> LeaderboardListResponsePacket.handle(buf, client));
        ClientPlayNetworking.registerGlobalReceiver(Leaderboards.LEADERBOARD_RESPONSE_PACKET,
            (client, handler, buf, responseSender) -> LeaderboardResponsePacket.handle(buf, client));
        
        LeaderboardsClientEvents.init();
    }
    
    public static void openLeaderboardsList() {
        FriendlyByteBuf buf = PacketByteBufs.create();
        RequestLeaderboardListPacket.encode(new RequestLeaderboardListPacket(), buf);
        ClientPlayNetworking.send(Leaderboards.REQUEST_LEADERBOARD_LIST_PACKET, buf);
    }
    
    public static void requestLeaderboard(net.minecraft.resources.ResourceLocation id) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        RequestLeaderboardPacket.encode(new RequestLeaderboardPacket(id), buf);
        ClientPlayNetworking.send(Leaderboards.REQUEST_LEADERBOARD_PACKET, buf);
    }
}
