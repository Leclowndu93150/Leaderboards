package com.leclowndu93150.leaderboards.platform;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface NetworkPlatform {
    void sendToServer(CustomPacketPayload payload);
}
