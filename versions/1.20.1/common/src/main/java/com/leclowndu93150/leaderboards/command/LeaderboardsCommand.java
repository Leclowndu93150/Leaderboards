package com.leclowndu93150.leaderboards.command;

import com.leclowndu93150.leaderboards.data.LeaderboardValue;
import com.leclowndu93150.leaderboards.network.LeaderboardResponsePacket;
import com.leclowndu93150.leaderboards.util.OfflinePlayerStats;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.SharedConstants;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.level.storage.LevelResource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.BiConsumer;

public final class LeaderboardsCommand {
    private LeaderboardsCommand() {}

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher,
                                BiConsumer<ServerPlayer, LeaderboardResponsePacket> send) {
        dispatcher.register(Commands.literal("leaderboards-demo")
                .requires(src -> src.hasPermission(2))
                .then(Commands.argument("count", IntegerArgumentType.integer(1, 10000))
                        .executes(ctx -> runDemo(ctx.getSource(), IntegerArgumentType.getInteger(ctx, "count"), send)))
                .executes(ctx -> runDemo(ctx.getSource(), 250, send)));

        dispatcher.register(Commands.literal("leaderboards-fakeplayer")
                .requires(src -> src.hasPermission(2))
                .then(Commands.argument("name", StringArgumentType.word())
                        .executes(ctx -> runFakePlayer(ctx.getSource(), StringArgumentType.getString(ctx, "name"), 1)))
                .then(Commands.literal("random")
                        .then(Commands.argument("count", IntegerArgumentType.integer(1, 1000))
                                .executes(ctx -> runFakePlayer(ctx.getSource(), null, IntegerArgumentType.getInteger(ctx, "count")))))
                .executes(ctx -> runFakePlayer(ctx.getSource(), null, 1)));
    }

    private static int runDemo(CommandSourceStack source, int count,
                               BiConsumer<ServerPlayer, LeaderboardResponsePacket> send) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        List<LeaderboardValue> values = new ArrayList<>(count);
        Random rng = new Random(42);
        for (int i = 0; i < count; i++) {
            LeaderboardValue v = new LeaderboardValue();
            v.username = "DemoPlayer" + String.format("%04d", i);
            v.rank = i + 1;
            v.value = Component.literal(String.valueOf(rng.nextInt(100_000)));
            if (i < 3) v.color = ChatFormatting.GOLD;
            else v.color = ChatFormatting.RESET;
            values.add(v);
        }
        send.accept(player, new LeaderboardResponsePacket(Component.literal("Demo Leaderboard (" + count + ")"), values));
        source.sendSuccess(() -> Component.literal("Opened demo leaderboard with " + count + " entries"), false);
        return 1;
    }

    private static int runFakePlayer(CommandSourceStack source, String fixedName, int count) {
        MinecraftServer server = source.getServer();
        Random rng = new Random();
        int created = 0;
        for (int i = 0; i < count; i++) {
            String name = fixedName != null ? fixedName : "FakePlayer" + String.format("%04d", rng.nextInt(10000));
            if (createFakePlayer(server, name, rng)) {
                created++;
            }
        }
        int total = created;
        source.sendSuccess(() -> Component.literal("Created " + total + " fake player(s)"), false);
        return created;
    }

    private static boolean createFakePlayer(MinecraftServer server, String name, Random rng) {
        UUID uuid = UUIDUtil.createOfflinePlayerUUID(name);
        Path playerDataDir = server.getWorldPath(LevelResource.PLAYER_DATA_DIR);
        Path statsDir = server.getWorldPath(LevelResource.PLAYER_STATS_DIR);
        try {
            Files.createDirectories(playerDataDir);
            Files.createDirectories(statsDir);
            CompoundTag tag = new CompoundTag();
            NbtIo.writeCompressed(tag, playerDataDir.resolve(uuid + ".dat").toFile());
            Files.writeString(statsDir.resolve(uuid + ".json"), buildStatsJson(rng));
        } catch (Exception e) {
            return false;
        }
        server.getProfileCache().add(new GameProfile(uuid, name));
        OfflinePlayerStats.invalidate(uuid);
        return true;
    }

    private static String buildStatsJson(Random rng) {
        StringBuilder sb = new StringBuilder("{\"stats\":{\"minecraft:custom\":{");
        appendCustom(sb, Stats.DEATHS, rng.nextInt(50), true);
        appendCustom(sb, Stats.MOB_KILLS, rng.nextInt(500), false);
        appendCustom(sb, Stats.PLAYER_KILLS, rng.nextInt(20), false);
        appendCustom(sb, Stats.PLAY_TIME, 72000 + rng.nextInt(720000), false);
        appendCustom(sb, Stats.JUMP, rng.nextInt(5000), false);
        appendCustom(sb, Stats.WALK_ONE_CM, rng.nextInt(1_000_000), false);
        appendCustom(sb, Stats.SPRINT_ONE_CM, rng.nextInt(500_000), false);
        sb.append("}},\"DataVersion\":")
                .append(SharedConstants.getCurrentVersion().getDataVersion().getVersion())
                .append("}");
        return sb.toString();
    }

    private static void appendCustom(StringBuilder sb, ResourceLocation id, int value, boolean first) {
        if (!first) sb.append(',');
        sb.append('"').append(id).append("\":").append(value);
    }
}
