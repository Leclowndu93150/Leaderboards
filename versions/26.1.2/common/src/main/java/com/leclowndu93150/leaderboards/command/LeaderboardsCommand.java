package com.leclowndu93150.leaderboards.command;

import com.leclowndu93150.leaderboards.data.LeaderboardValue;
import com.leclowndu93150.leaderboards.network.LeaderboardResponsePacket;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public final class LeaderboardsCommand {
    private LeaderboardsCommand() {}

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher,
                                BiConsumer<ServerPlayer, CustomPacketPayload> send) {
        dispatcher.register(Commands.literal("leaderboards-demo")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.argument("count", IntegerArgumentType.integer(1, 10000))
                        .executes(ctx -> run(ctx.getSource(), IntegerArgumentType.getInteger(ctx, "count"), send)))
                .executes(ctx -> run(ctx.getSource(), 250, send)));
    }

    private static int run(CommandSourceStack source, int count,
                           BiConsumer<ServerPlayer, CustomPacketPayload> send) throws CommandSyntaxException {
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
}
