package com.leclowndu93150.leaderboards.platform;

import com.leclowndu93150.leaderboards.Leaderboards;

import java.util.ServiceLoader;

public final class Services {
    public static final NetworkPlatform NETWORK = load(NetworkPlatform.class);

    private Services() {}

    public static <T> T load(Class<T> clazz) {
        return ServiceLoader.load(clazz, Services.class.getClassLoader())
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
    }

    static {
        Leaderboards.LOGGER.debug("Leaderboards platform services initialized");
    }
}
