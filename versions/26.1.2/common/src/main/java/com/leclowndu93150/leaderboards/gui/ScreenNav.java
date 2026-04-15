package com.leclowndu93150.leaderboards.gui;

import com.leclowndu93150.leaderboards.mixin.TopPanelAccessor;
import dev.ftb.mods.ftblibrary.client.gui.widget.BaseScreen;
import dev.ftb.mods.ftblibrary.client.gui.widget.Panel;
import dev.ftb.mods.ftblibrary.client.gui.widget.ScreenWrapper;
import dev.ftb.mods.ftblibrary.icon.Icons;
import net.minecraft.network.chat.Component;

public final class ScreenNav {
    private ScreenNav() {}

    public static boolean hasLeaderboardsPrev(BaseScreen screen) {
        return screen.getPrevScreen() instanceof ScreenWrapper sw && sw.getGui() instanceof BaseScreen;
    }

    public static void applyBackIconIfPrev(BaseScreen screen, Panel topPanel) {
        if (hasLeaderboardsPrev(screen) && topPanel instanceof TopPanelAccessor acc) {
            var closeButton = acc.leaderboards$getCloseButton();
            closeButton.setIcon(Icons.LEFT);
            closeButton.setTitle(Component.translatable("gui.back"));
        }
    }
}
