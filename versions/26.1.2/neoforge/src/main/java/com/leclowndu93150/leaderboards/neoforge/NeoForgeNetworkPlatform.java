package com.leclowndu93150.leaderboards.neoforge;

import com.leclowndu93150.leaderboards.platform.NetworkPlatform;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor;

public class NeoForgeNetworkPlatform implements NetworkPlatform {
    @Override
    public void sendToServer(CustomPacketPayload payload) {
        ClientPacketDistributor.sendToServer(payload);
    }
}
