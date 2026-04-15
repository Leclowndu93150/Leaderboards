package com.leclowndu93150.leaderboards.fabric;

import com.leclowndu93150.leaderboards.Leaderboards;
import com.leclowndu93150.leaderboards.command.LeaderboardsCommand;
import com.leclowndu93150.leaderboards.network.LeaderboardListResponsePacket;
import com.leclowndu93150.leaderboards.network.LeaderboardResponsePacket;
import com.leclowndu93150.leaderboards.network.RequestLeaderboardListPacket;
import com.leclowndu93150.leaderboards.network.RequestLeaderboardPacket;
import com.leclowndu93150.leaderboards.network.ServerHandlers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.FriendlyByteBuf;

public class LeaderboardsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerPlayNetworking.registerGlobalReceiver(RequestLeaderboardListPacket.ID, (server, player, handler, buf, sender) -> {
            RequestLeaderboardListPacket.decode(buf);
            server.execute(() -> ServerHandlers.onRequestList(player, (p, response) -> {
                FriendlyByteBuf out = PacketByteBufs.create();
                LeaderboardListResponsePacket.encode(response, out);
                ServerPlayNetworking.send(p, LeaderboardListResponsePacket.ID, out);
            }));
        });

        ServerPlayNetworking.registerGlobalReceiver(RequestLeaderboardPacket.ID, (server, player, handler, buf, sender) -> {
            RequestLeaderboardPacket packet = RequestLeaderboardPacket.decode(buf);
            server.execute(() -> ServerHandlers.onRequestLeaderboard(packet, player, (p, response) -> {
                FriendlyByteBuf out = PacketByteBufs.create();
                LeaderboardResponsePacket.encode(response, out);
                ServerPlayNetworking.send(p, LeaderboardResponsePacket.ID, out);
            }));
        });

        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, selection) ->
                    LeaderboardsCommand.register(dispatcher, (p, response) -> {
                        FriendlyByteBuf out = PacketByteBufs.create();
                        LeaderboardResponsePacket.encode(response, out);
                        ServerPlayNetworking.send(p, LeaderboardResponsePacket.ID, out);
                    }));
        }

        Leaderboards.init();
    }
}
