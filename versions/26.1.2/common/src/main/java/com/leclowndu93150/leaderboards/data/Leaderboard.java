package com.leclowndu93150.leaderboards.data;

import com.leclowndu93150.baguettelib.player.OfflinePlayerStats;
import com.leclowndu93150.baguettelib.stats.StatFormatters;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.Stat;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;

public class Leaderboard {
    public final Identifier id;
    private final Component title;
    private final Function<OfflinePlayerStats, Component> playerToValue;
    private final Comparator<OfflinePlayerStats> comparator;
    private final Predicate<OfflinePlayerStats> validValue;

    public Leaderboard(Identifier id, Component title,
                       Function<OfflinePlayerStats, Component> valueFunction,
                       Comparator<OfflinePlayerStats> comparator,
                       Predicate<OfflinePlayerStats> validValue) {
        this.id = id;
        this.title = title;
        this.playerToValue = valueFunction;
        this.comparator = comparator.thenComparing((p1, p2) -> p1.name().compareToIgnoreCase(p2.name()));
        this.validValue = validValue;
    }

    public Component getTitle() {
        return title;
    }

    public Comparator<OfflinePlayerStats> getComparator() {
        return comparator;
    }

    public Component createValue(OfflinePlayerStats player) {
        return playerToValue.apply(player);
    }

    public boolean hasValidValue(OfflinePlayerStats player) {
        return validValue.test(player);
    }

    public static class FromStat extends Leaderboard {
        public FromStat(Identifier id, Component title, Stat<?> stat, boolean ascending, IntFunction<Component> valueFormatter) {
            super(id, title,
                    player -> valueFormatter.apply(player.stats().getValue(stat)),
                    (p1, p2) -> {
                        int result = Integer.compare(p1.stats().getValue(stat), p2.stats().getValue(stat));
                        return ascending ? result : -result;
                    },
                    player -> player.stats().getValue(stat) > 0
            );
        }

        public FromStat(Identifier id, Component title, Stat<?> stat, boolean ascending) {
            this(id, title, stat, ascending, StatFormatters.DEFAULT);
        }
    }
}
