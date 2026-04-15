package com.leclowndu93150.leaderboards.gui;

import com.leclowndu93150.leaderboards.client.LeaderboardsClient;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.ui.misc.AbstractButtonListScreen;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.SimpleTextButton;
import dev.ftb.mods.ftblibrary.icon.Icon;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class VanillaStatsListScreen extends AbstractButtonListScreen {
    private final Map<ResourceLocation, Component> vanillaStats;

    public VanillaStatsListScreen(Map<ResourceLocation, Component> vanillaStats) {
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
        int maxW = (int) (getScreen().getGuiScaledWidth() * 0.9f);
        int maxH = (int) (getScreen().getGuiScaledHeight() * 0.9f);
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
        for (Map.Entry<ResourceLocation, Component> entry : vanillaStats.entrySet()) {
            panel.add(new SimpleTextButton(panel, entry.getValue(), Icon.empty()) {
                @Override
                public void onClicked(MouseButton button) {
                    playClickSound();
                    LeaderboardsClient.openLeaderboard(entry.getKey());
                }
            });
        }
    }
}
