package com.leclowndu93150.leaderboards.gui;

import com.leclowndu93150.leaderboards.network.RequestLeaderboardPacket;
import com.leclowndu93150.leaderboards.platform.Services;
import dev.ftb.mods.ftblibrary.client.gui.input.MouseButton;
import dev.ftb.mods.ftblibrary.client.gui.screens.AbstractButtonListScreen;
import dev.ftb.mods.ftblibrary.client.gui.theme.Theme;
import dev.ftb.mods.ftblibrary.client.gui.widget.Panel;
import dev.ftb.mods.ftblibrary.client.gui.widget.SimpleTextButton;
import dev.ftb.mods.ftblibrary.icon.Icon;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.Map;

public class VanillaStatsListScreen extends AbstractButtonListScreen {
    private final Map<Identifier, Component> vanillaStats;

    public VanillaStatsListScreen(Map<Identifier, Component> vanillaStats) {
        this.vanillaStats = vanillaStats;
        setTitle(Component.translatable("leaderboard.leaderboards.vanilla_stats"));
        setHasSearchBox(true);
        showBottomPanel(false);
        showCloseButton(true);
        ScreenNav.applyBackIconIfPrev(this, topPanel);
    }

    @Override
    public boolean onInit() {
        Theme theme = getTheme();
        int maxLabel = 0;
        for (Component c : vanillaStats.values()) {
            maxLabel = Math.max(maxLabel, theme.getStringWidth(c) + 40);
        }
        int maxW = (int) (getWindow().getGuiScaledWidth() * 0.9f);
        int maxH = (int) (getWindow().getGuiScaledHeight() * 0.9f);
        setWidth(Math.min(Math.max(maxLabel, 150), maxW));
        setHeight(Math.min(180, maxH));
        return true;
    }

    @Override
    protected void doAccept() {
    }

    @Override
    protected void doCancel() {
        closeGui(true);
    }

    @Override
    public void addButtons(Panel panel) {
        for (Map.Entry<Identifier, Component> entry : vanillaStats.entrySet()) {
            panel.add(new SimpleTextButton(panel, entry.getValue(), Icon.empty()) {
                @Override
                public void onClicked(MouseButton button) {
                    playClickSound();
                    Services.NETWORK.sendToServer(new RequestLeaderboardPacket(entry.getKey()));
                }
            });
        }
    }
}
