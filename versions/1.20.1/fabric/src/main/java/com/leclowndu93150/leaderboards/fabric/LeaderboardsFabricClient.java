package com.leclowndu93150.leaderboards.fabric;

import com.leclowndu93150.leaderboards.client.LeaderboardsClientEvents;
import com.leclowndu93150.leaderboards.gui.LeaderboardListScreen;
import com.leclowndu93150.leaderboards.gui.LeaderboardScreen;
import com.leclowndu93150.leaderboards.network.LeaderboardListResponsePacket;
import com.leclowndu93150.leaderboards.network.LeaderboardResponsePacket;
import dev.ftb.mods.ftblibrary.sidebar.SidebarButtonCreatedEvent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class LeaderboardsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(LeaderboardListResponsePacket.ID, (client, handler, buf, sender) -> {
            LeaderboardListResponsePacket packet = LeaderboardListResponsePacket.decode(buf);
            client.execute(() -> new LeaderboardListScreen(packet.leaderboards()).openGui());
        });

        ClientPlayNetworking.registerGlobalReceiver(LeaderboardResponsePacket.ID, (client, handler, buf, sender) -> {
            LeaderboardResponsePacket packet = LeaderboardResponsePacket.decode(buf);
            client.execute(() -> new LeaderboardScreen(packet.title(), packet.values()).openGui());
        });

        LeaderboardsClientEvents.init();

        SidebarButtonCreatedEvent.EVENT.register(event -> {
            if (event.getButton().getId().equals(LeaderboardsClientEvents.SIDEBAR_BUTTON_ID)) {
                event.getButton().addVisibilityCondition(LeaderboardsClientEvents::isSharedWorld);
            }
        });
    }
}
