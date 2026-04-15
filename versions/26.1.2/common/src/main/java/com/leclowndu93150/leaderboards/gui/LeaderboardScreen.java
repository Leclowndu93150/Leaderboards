package com.leclowndu93150.leaderboards.gui;

import com.leclowndu93150.leaderboards.data.LeaderboardValue;
import dev.ftb.mods.ftblibrary.client.gui.WidgetType;
import dev.ftb.mods.ftblibrary.client.gui.screens.AbstractButtonListScreen;
import dev.ftb.mods.ftblibrary.client.gui.theme.Theme;
import dev.ftb.mods.ftblibrary.client.gui.widget.Panel;
import dev.ftb.mods.ftblibrary.client.gui.widget.Widget;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;

import java.util.List;

public class LeaderboardScreen extends AbstractButtonListScreen {
    private final List<LeaderboardValue> leaderboard;
    private int rankSize;
    private int usernameSize;
    private int valueSize;
    private int totalWidth;

    private class LeaderboardEntry extends Widget {
        private final LeaderboardValue value;
        private final String rank;

        public LeaderboardEntry(Panel panel, LeaderboardValue v) {
            super(panel);
            value = v;
            rank = value.color + "#" + String.format("%0" + String.valueOf(leaderboard.size()).length() + "d", v.rank);

            Theme theme = getGui().getTheme();
            rankSize = Math.max(rankSize, theme.getStringWidth(rank) + 4);
            usernameSize = Math.max(usernameSize, theme.getStringWidth(v.username) + 8);
            valueSize = Math.max(valueSize, theme.getStringWidth(value.value.getString()) + 8);

            setWidth(rankSize + usernameSize + valueSize);
            setHeight(14);
        }

        @Override
        public void addMouseOverText(TooltipList list) {
        }

        @Override
        public void draw(GuiGraphicsExtractor graphics, Theme theme, int x, int y, int w, int h) {
            WidgetType type = value.color == ChatFormatting.DARK_GRAY ? WidgetType.DISABLED : WidgetType.mouseOver(isMouseOver());
            int textY = y + (h - theme.getFontHeight() + 1) / 2;

            theme.drawButton(graphics, x, y, rankSize, h, type);
            theme.drawString(graphics, rank, x + 2, textY, Theme.SHADOW);

            theme.drawButton(graphics, x + rankSize, y, usernameSize, h, type);
            theme.drawString(graphics, value.color + value.username, x + 4 + rankSize, textY, Theme.SHADOW);

            int remainingWidth = w - rankSize - usernameSize;
            theme.drawButton(graphics, x + rankSize + usernameSize, y, remainingWidth, h, type);
            String formattedText = value.value.getString();
            theme.drawString(graphics, value.color + formattedText, x + rankSize + usernameSize + remainingWidth - theme.getStringWidth(formattedText) - 4, textY, Theme.SHADOW);
        }
    }

    public LeaderboardScreen(Component title, List<LeaderboardValue> leaderboard) {
        setTitle(Component.literal(I18n.get("sidebar_button.leaderboards.leaderboards") + " > " + title.getString()));
        setHasSearchBox(true);
        showBottomPanel(false);
        showCloseButton(true);
        ScreenNav.applyBackIconIfPrev(this, topPanel);
        this.leaderboard = leaderboard;
    }

    @Override
    public boolean onInit() {
        Theme theme = getTheme();
        int maxRank = 0, maxUser = 0, maxVal = 0;
        int rankDigits = String.valueOf(leaderboard.size()).length();
        for (LeaderboardValue v : leaderboard) {
            String rankText = "#" + "0".repeat(rankDigits);
            maxRank = Math.max(maxRank, theme.getStringWidth(rankText) + 4);
            maxUser = Math.max(maxUser, theme.getStringWidth(v.username) + 8);
            maxVal = Math.max(maxVal, theme.getStringWidth(v.value.getString()) + 8);
        }
        int measuredWidth = maxRank + maxUser + maxVal;

        int maxW = (int) (getWindow().getGuiScaledWidth() * 0.9f);
        int maxH = (int) (getWindow().getGuiScaledHeight() * 0.9f);
        int naturalW = Math.max(measuredWidth + 34 + 12, 150);
        int naturalH = Math.max(Math.min(leaderboard.size() * 15 + 40, 300) + 30, 180);
        setWidth(Math.min(naturalW, maxW));
        setHeight(Math.min(naturalH, maxH));
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
        int i = 0;
        rankSize = 0;
        usernameSize = 0;
        valueSize = 0;

        for (LeaderboardValue value : leaderboard) {
            value.rank = ++i;
            panel.add(new LeaderboardEntry(panel, value));
        }

        totalWidth = rankSize + usernameSize + valueSize;
        setBorder(3, 1, 1);
    }

    @Override
    public String getFilterText(Widget widget) {
        return ((LeaderboardEntry) widget).value.username.toLowerCase();
    }
}
