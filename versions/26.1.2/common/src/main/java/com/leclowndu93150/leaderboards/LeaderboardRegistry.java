package com.leclowndu93150.leaderboards;

import com.leclowndu93150.baguettelib.player.OfflinePlayerStats;
import com.leclowndu93150.baguettelib.player.PlayerActivityTracker;
import com.leclowndu93150.baguettelib.stats.StatFormatters;
import com.leclowndu93150.leaderboards.data.Leaderboard;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.Stats;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

public class LeaderboardRegistry {
    public static final Map<Identifier, Leaderboard> LEADERBOARDS = new LinkedHashMap<>();

    public static void register() {
        put("deaths", new Leaderboard.FromStat(
                id("deaths"), Component.translatable("leaderboard.leaderboards.deaths"),
                Stats.CUSTOM.get(Stats.DEATHS), false));

        put("mob_kills", new Leaderboard.FromStat(
                id("mob_kills"), Component.translatable("leaderboard.leaderboards.mob_kills"),
                Stats.CUSTOM.get(Stats.MOB_KILLS), false));

        put("player_kills", new Leaderboard.FromStat(
                id("player_kills"), Component.translatable("leaderboard.leaderboards.player_kills"),
                Stats.CUSTOM.get(Stats.PLAYER_KILLS), false));

        put("time_played", new Leaderboard.FromStat(
                id("time_played"), Component.translatable("leaderboard.leaderboards.time_played"),
                Stats.CUSTOM.get(Stats.PLAY_TIME), false, StatFormatters.TIME));

        put("jumps", new Leaderboard.FromStat(
                id("jumps"), Component.translatable("leaderboard.leaderboards.jumps"),
                Stats.CUSTOM.get(Stats.JUMP), false));

        put("distance_walked", new Leaderboard.FromStat(
                id("distance_walked"), Component.translatable("leaderboard.leaderboards.distance_walked"),
                Stats.CUSTOM.get(Stats.WALK_ONE_CM), false, StatFormatters.DISTANCE));

        put("distance_sprinted", new Leaderboard.FromStat(
                id("distance_sprinted"), Component.translatable("leaderboard.leaderboards.distance_sprinted"),
                Stats.CUSTOM.get(Stats.SPRINT_ONE_CM), false, StatFormatters.DISTANCE));

        put("deaths_per_hour", new Leaderboard(
                id("deaths_per_hour"), Component.translatable("leaderboard.leaderboards.deaths_per_hour"),
                player -> {
                    double dph = getDPH(player);
                    return Component.literal(dph < 0D ? "-" : String.format("%.2f", dph));
                },
                Comparator.comparingDouble(LeaderboardRegistry::getDPH).reversed(),
                player -> getDPH(player) >= 0D));

        put("last_seen", new Leaderboard(
                id("last_seen"), Component.translatable("leaderboard.leaderboards.last_seen"),
                player -> {
                    if (player.isOnline()) {
                        return Component.translatable("gui.online").withStyle(ChatFormatting.GREEN);
                    }
                    long lastSeen = PlayerActivityTracker.get(player.server()).getLastSeen(player.uuid());
                    if (lastSeen == 0L) {
                        return Component.literal("-");
                    }
                    long worldTime = player.server().overworld().getGameTime();
                    int ticks = (int) (worldTime - lastSeen);
                    return StatFormatters.TIME.apply(ticks);
                },
                Comparator.comparingLong(p -> {
                    if (p.isOnline()) return 0L;
                    return p.server().overworld().getGameTime() - PlayerActivityTracker.get(p.server()).getLastSeen(p.uuid());
                }),
                player -> player.isOnline() || PlayerActivityTracker.get(player.server()).getLastSeen(player.uuid()) != 0L));

        VanillaStatsRegistry.register();
    }

    private static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(Leaderboards.MODID, path);
    }

    private static void put(String path, Leaderboard leaderboard) {
        LEADERBOARDS.put(id(path), leaderboard);
    }

    private static double getDPH(OfflinePlayerStats player) {
        int playTime = player.stats().getValue(Stats.CUSTOM.get(Stats.PLAY_TIME));
        if (playTime > 0) {
            double hours = playTime / 72000D;
            if (hours >= 1D) {
                return (double) player.stats().getValue(Stats.CUSTOM.get(Stats.DEATHS)) / hours;
            }
        }
        return -1D;
    }
}
