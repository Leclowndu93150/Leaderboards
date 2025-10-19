package com.leclowndu93150.leaderboards;

import com.leclowndu93150.leaderboards.data.PlayerDataTracker;
import com.leclowndu93150.leaderboards.network.*;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.slf4j.Logger;

@Mod(Leaderboards.MODID)
public class Leaderboards {
    public static final String MODID = "leaderboards";
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public Leaderboards() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        
        MinecraftForge.EVENT_BUS.addListener(this::onServerStarted);
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerLogin);
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerLogout);
        
        if (FMLEnvironment.dist == Dist.CLIENT) {
            com.leclowndu93150.leaderboards.client.LeaderboardsClientEvents.init();
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            int id = 0;
            NETWORK.registerMessage(id++, RequestLeaderboardListPacket.class, 
                RequestLeaderboardListPacket::encode, 
                RequestLeaderboardListPacket::decode, 
                RequestLeaderboardListPacket::handle);
            NETWORK.registerMessage(id++, LeaderboardListResponsePacket.class,
                LeaderboardListResponsePacket::encode,
                LeaderboardListResponsePacket::decode,
                LeaderboardListResponsePacket::handle);
            NETWORK.registerMessage(id++, RequestLeaderboardPacket.class,
                RequestLeaderboardPacket::encode,
                RequestLeaderboardPacket::decode,
                RequestLeaderboardPacket::handle);
            NETWORK.registerMessage(id++, LeaderboardResponsePacket.class,
                LeaderboardResponsePacket::encode,
                LeaderboardResponsePacket::decode,
                LeaderboardResponsePacket::handle);
            
            LeaderboardRegistry.register();
            LOGGER.info("Leaderboards registered");
        });
    }

    private void onServerStarted(ServerStartedEvent event) {
        PlayerDataTracker tracker = PlayerDataTracker.get(event.getServer().overworld());
        PlayerDataTracker.setInstance(tracker);
    }

    private void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity().level().isClientSide) return;
        PlayerDataTracker tracker = PlayerDataTracker.get(event.getEntity().getServer().overworld());
        tracker.updateLastSeen(event.getEntity().getUUID(), event.getEntity().getServer().overworld().getGameTime());
    }

    private void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity().level().isClientSide) return;
        PlayerDataTracker tracker = PlayerDataTracker.get(event.getEntity().getServer().overworld());
        tracker.updateLastSeen(event.getEntity().getUUID(), event.getEntity().getServer().overworld().getGameTime());
    }
}
