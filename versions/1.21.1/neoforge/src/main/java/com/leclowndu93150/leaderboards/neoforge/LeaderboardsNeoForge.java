package com.leclowndu93150.leaderboards.neoforge;

import com.leclowndu93150.leaderboards.Leaderboards;
import com.leclowndu93150.leaderboards.client.LeaderboardsClientEvents;
import com.leclowndu93150.leaderboards.command.LeaderboardsCommand;
import com.leclowndu93150.leaderboards.gui.LeaderboardListScreen;
import com.leclowndu93150.leaderboards.gui.LeaderboardScreen;
import com.leclowndu93150.leaderboards.network.LeaderboardListResponsePacket;
import com.leclowndu93150.leaderboards.network.LeaderboardResponsePacket;
import com.leclowndu93150.leaderboards.network.RequestLeaderboardListPacket;
import com.leclowndu93150.leaderboards.network.RequestLeaderboardPacket;
import com.leclowndu93150.leaderboards.network.ServerHandlers;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@Mod(Leaderboards.MODID)
public class LeaderboardsNeoForge {

    public LeaderboardsNeoForge(IEventBus modEventBus) {
        modEventBus.addListener(LeaderboardsNeoForge::registerPayloads);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            LeaderboardsClientEvents.init();
        }
        if (!FMLEnvironment.production) {
            NeoForge.EVENT_BUS.addListener((RegisterCommandsEvent event) ->
                    LeaderboardsCommand.register(event.getDispatcher(), PacketDistributor::sendToPlayer));
        }
        Leaderboards.init();
    }

    private static void registerPayloads(RegisterPayloadHandlersEvent event) {
        var r = event.registrar("1");
        r.playToServer(RequestLeaderboardListPacket.TYPE, RequestLeaderboardListPacket.STREAM_CODEC,
                (payload, ctx) -> ServerHandlers.onRequestList(payload, (ServerPlayer) ctx.player(),
                        PacketDistributor::sendToPlayer));
        r.playToServer(RequestLeaderboardPacket.TYPE, RequestLeaderboardPacket.STREAM_CODEC,
                (payload, ctx) -> ServerHandlers.onRequestLeaderboard(payload, (ServerPlayer) ctx.player(),
                        PacketDistributor::sendToPlayer));
        r.playToClient(LeaderboardListResponsePacket.TYPE, LeaderboardListResponsePacket.STREAM_CODEC,
                (payload, ctx) -> new LeaderboardListScreen(payload.leaderboards()).openGui());
        r.playToClient(LeaderboardResponsePacket.TYPE, LeaderboardResponsePacket.STREAM_CODEC,
                (payload, ctx) -> new LeaderboardScreen(payload.title(), payload.values()).openGui());
    }
}
