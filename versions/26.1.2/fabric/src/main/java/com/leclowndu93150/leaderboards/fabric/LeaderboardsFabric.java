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
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;

public class LeaderboardsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        PayloadTypeRegistry.serverboundPlay().register(RequestLeaderboardListPacket.TYPE, RequestLeaderboardListPacket.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(RequestLeaderboardPacket.TYPE, RequestLeaderboardPacket.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(LeaderboardListResponsePacket.TYPE, LeaderboardListResponsePacket.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(LeaderboardResponsePacket.TYPE, LeaderboardResponsePacket.STREAM_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(RequestLeaderboardListPacket.TYPE,
                (payload, ctx) -> ctx.server().execute(() ->
                        ServerHandlers.onRequestList(payload, ctx.player(), ServerPlayNetworking::send)));

        ServerPlayNetworking.registerGlobalReceiver(RequestLeaderboardPacket.TYPE,
                (payload, ctx) -> ctx.server().execute(() ->
                        ServerHandlers.onRequestLeaderboard(payload, ctx.player(), ServerPlayNetworking::send)));

        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, selection) ->
                    LeaderboardsCommand.register(dispatcher, ServerPlayNetworking::send));
        }

        Leaderboards.init();
    }
}
