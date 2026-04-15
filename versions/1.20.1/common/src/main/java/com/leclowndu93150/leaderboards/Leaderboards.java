package com.leclowndu93150.leaderboards;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Leaderboards {
    public static final String MODID = "leaderboards";
    public static final Logger LOGGER = LoggerFactory.getLogger("Leaderboards");

    private Leaderboards() {}

    public static void init() {
        LeaderboardRegistry.register();
        LOGGER.info("Leaderboards registered ({} entries + {} vanilla stats)",
                LeaderboardRegistry.LEADERBOARDS.size(), VanillaStatsRegistry.VANILLA_STATS.size());
    }
}
