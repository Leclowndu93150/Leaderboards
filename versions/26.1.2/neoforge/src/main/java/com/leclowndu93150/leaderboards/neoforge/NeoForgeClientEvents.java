package com.leclowndu93150.leaderboards.neoforge;

import com.leclowndu93150.leaderboards.Leaderboards;
import com.leclowndu93150.leaderboards.client.LeaderboardsClient;
import dev.ftb.mods.ftblibrary.api.neoforge.FTBLibraryEvent;
import net.neoforged.bus.api.IEventBus;

public final class NeoForgeClientEvents {
    private NeoForgeClientEvents() {}

    public static void register(IEventBus gameBus) {
        gameBus.addListener(NeoForgeClientEvents::onCustomClick);
        gameBus.addListener(NeoForgeClientEvents::onSidebarButtonCreated);
    }

    private static void onCustomClick(FTBLibraryEvent.CustomClick event) {
        var id = event.getEventData().id();
        if (id.getNamespace().equals(Leaderboards.MODID) && "open_leaderboards".equals(id.getPath())) {
            LeaderboardsClient.openLeaderboardsList();
            event.setCanceled(true);
        }
    }

    private static void onSidebarButtonCreated(FTBLibraryEvent.SidebarButtonCreated event) {
        LeaderboardsClient.onSidebarButtonCreated(event.getButton());
    }
}
