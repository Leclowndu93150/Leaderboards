package com.leclowndu93150.leaderboards.forge;

import com.leclowndu93150.leaderboards.Leaderboards;
import com.leclowndu93150.leaderboards.client.LeaderboardsClientEvents;
import com.leclowndu93150.leaderboards.command.LeaderboardsCommand;
import com.leclowndu93150.leaderboards.gui.LeaderboardListScreen;
import com.leclowndu93150.leaderboards.gui.LeaderboardScreen;
import com.leclowndu93150.leaderboards.network.LeaderboardListResponsePacket;
import com.leclowndu93150.leaderboards.network.LeaderboardResponsePacket;
import com.leclowndu93150.leaderboards.network.RequestLeaderboardListPacket;
import com.leclowndu93150.leaderboards.network.RequestLeaderboardPacket;
import com.leclowndu93150.leaderboards.network.ServerHandlers;
import dev.ftb.mods.ftblibrary.sidebar.SidebarButtonCreatedEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

@Mod(Leaderboards.MODID)
public class LeaderboardsForge {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Leaderboards.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public LeaderboardsForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.addListener(this::onRegisterCommands);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            LeaderboardsClientEvents.init();
            SidebarButtonCreatedEvent.EVENT.register(event -> {
                if (event.getButton().getId().equals(LeaderboardsClientEvents.SIDEBAR_BUTTON_ID)) {
                    event.getButton().addVisibilityCondition(LeaderboardsClientEvents::isSharedWorld);
                }
            });
        }
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            int id = 0;
            NETWORK.registerMessage(id++, RequestLeaderboardListPacket.class,
                    RequestLeaderboardListPacket::encode,
                    RequestLeaderboardListPacket::decode,
                    (packet, ctx) -> {
                        ctx.get().enqueueWork(() -> {
                            ServerPlayer sender = ctx.get().getSender();
                            if (sender != null) {
                                ServerHandlers.onRequestList(sender,
                                        (p, r) -> NETWORK.send(PacketDistributor.PLAYER.with(() -> p), r));
                            }
                        });
                        ctx.get().setPacketHandled(true);
                    });

            NETWORK.registerMessage(id++, RequestLeaderboardPacket.class,
                    RequestLeaderboardPacket::encode,
                    RequestLeaderboardPacket::decode,
                    (packet, ctx) -> {
                        ctx.get().enqueueWork(() -> {
                            ServerPlayer sender = ctx.get().getSender();
                            if (sender != null) {
                                ServerHandlers.onRequestLeaderboard(packet, sender,
                                        (p, r) -> NETWORK.send(PacketDistributor.PLAYER.with(() -> p), r));
                            }
                        });
                        ctx.get().setPacketHandled(true);
                    });

            NETWORK.registerMessage(id++, LeaderboardListResponsePacket.class,
                    LeaderboardListResponsePacket::encode,
                    LeaderboardListResponsePacket::decode,
                    (packet, ctx) -> {
                        ctx.get().enqueueWork(() -> Minecraft.getInstance().execute(() ->
                                new LeaderboardListScreen(packet.leaderboards()).openGui()));
                        ctx.get().setPacketHandled(true);
                    });

            NETWORK.registerMessage(id++, LeaderboardResponsePacket.class,
                    LeaderboardResponsePacket::encode,
                    LeaderboardResponsePacket::decode,
                    (packet, ctx) -> {
                        ctx.get().enqueueWork(() -> Minecraft.getInstance().execute(() ->
                                new LeaderboardScreen(packet.title(), packet.values()).openGui()));
                        ctx.get().setPacketHandled(true);
                    });

            Leaderboards.init();
        });
    }

    private void onRegisterCommands(RegisterCommandsEvent event) {
        if (!FMLEnvironment.production) {
            LeaderboardsCommand.register(event.getDispatcher(),
                    (p, r) -> NETWORK.send(PacketDistributor.PLAYER.with(() -> p), r));
        }
    }
}
