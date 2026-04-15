package com.leclowndu93150.leaderboards;

import com.leclowndu93150.baguettelib.player.OfflinePlayerStats;
import com.leclowndu93150.baguettelib.stats.StatFormatters;
import com.leclowndu93150.leaderboards.data.Leaderboard;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import net.minecraft.stats.Stats;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

public class VanillaStatsRegistry {
    public static final Map<Identifier, Leaderboard> VANILLA_STATS = new LinkedHashMap<>();

    public static void register() {
        registerAggregate("total_blocks_mined", Stats.BLOCK_MINED);
        registerAggregate("total_items_crafted", Stats.ITEM_CRAFTED);
        registerAggregate("total_items_used", Stats.ITEM_USED);
        registerAggregate("total_items_broken", Stats.ITEM_BROKEN);
        registerAggregate("total_items_picked_up", Stats.ITEM_PICKED_UP);

        registerStat("damage_dealt", Stats.DAMAGE_DEALT, StatFormatters.DAMAGE);
        registerStat("damage_taken", Stats.DAMAGE_TAKEN, StatFormatters.DAMAGE);
        registerStat("damage_blocked_by_shield", Stats.DAMAGE_BLOCKED_BY_SHIELD, StatFormatters.DAMAGE);
        registerStat("damage_absorbed", Stats.DAMAGE_ABSORBED, StatFormatters.DAMAGE);
        registerStat("damage_resisted", Stats.DAMAGE_RESISTED, StatFormatters.DAMAGE);
        registerStat("damage_dealt_absorbed", Stats.DAMAGE_DEALT_ABSORBED, StatFormatters.DAMAGE);
        registerStat("damage_dealt_resisted", Stats.DAMAGE_DEALT_RESISTED, StatFormatters.DAMAGE);

        registerStat("animals_bred", Stats.ANIMALS_BRED, StatFormatters.DEFAULT);
        registerStat("fish_caught", Stats.FISH_CAUGHT, StatFormatters.DEFAULT);

        registerStat("fly_distance", Stats.FLY_ONE_CM, StatFormatters.DISTANCE);
        registerStat("swim_distance", Stats.SWIM_ONE_CM, StatFormatters.DISTANCE);
        registerStat("horse_distance", Stats.HORSE_ONE_CM, StatFormatters.DISTANCE);
        registerStat("boat_distance", Stats.BOAT_ONE_CM, StatFormatters.DISTANCE);
        registerStat("elytra_distance", Stats.AVIATE_ONE_CM, StatFormatters.DISTANCE);
        registerStat("minecart_distance", Stats.MINECART_ONE_CM, StatFormatters.DISTANCE);
        registerStat("pig_distance", Stats.PIG_ONE_CM, StatFormatters.DISTANCE);
        registerStat("strider_distance", Stats.STRIDER_ONE_CM, StatFormatters.DISTANCE);
        registerStat("walk_under_water_distance", Stats.WALK_UNDER_WATER_ONE_CM, StatFormatters.DISTANCE);
        registerStat("walk_on_water_distance", Stats.WALK_ON_WATER_ONE_CM, StatFormatters.DISTANCE);
        registerStat("climb_distance", Stats.CLIMB_ONE_CM, StatFormatters.DISTANCE);
        registerStat("fall_distance", Stats.FALL_ONE_CM, StatFormatters.DISTANCE);
        registerStat("crouch_distance", Stats.CROUCH_ONE_CM, StatFormatters.DISTANCE);

        registerStat("crouch_time", Stats.CROUCH_TIME, StatFormatters.TIME);
        registerStat("time_since_death", Stats.TIME_SINCE_DEATH, StatFormatters.TIME);
        registerStat("time_since_rest", Stats.TIME_SINCE_REST, StatFormatters.TIME);
        registerStat("total_world_time", Stats.TOTAL_WORLD_TIME, StatFormatters.TIME);

        registerStat("villager_trades", Stats.TRADED_WITH_VILLAGER, StatFormatters.DEFAULT);
        registerStat("talked_to_villager", Stats.TALKED_TO_VILLAGER, StatFormatters.DEFAULT);
        registerStat("raids_won", Stats.RAID_WIN, StatFormatters.DEFAULT);
        registerStat("raids_triggered", Stats.RAID_TRIGGER, StatFormatters.DEFAULT);
        registerStat("target_hit", Stats.TARGET_HIT, StatFormatters.DEFAULT);
        registerStat("bells_rung", Stats.BELL_RING, StatFormatters.DEFAULT);
        registerStat("items_dropped", Stats.DROP, StatFormatters.DEFAULT);
        registerStat("enchantments_done", Stats.ENCHANT_ITEM, StatFormatters.DEFAULT);
        registerStat("times_slept", Stats.SLEEP_IN_BED, StatFormatters.DEFAULT);
        registerStat("cake_slices_eaten", Stats.EAT_CAKE_SLICE, StatFormatters.DEFAULT);
        registerStat("chests_opened", Stats.OPEN_CHEST, StatFormatters.DEFAULT);
        registerStat("ender_chests_opened", Stats.OPEN_ENDERCHEST, StatFormatters.DEFAULT);
        registerStat("shulker_boxes_opened", Stats.OPEN_SHULKER_BOX, StatFormatters.DEFAULT);
        registerStat("barrels_opened", Stats.OPEN_BARREL, StatFormatters.DEFAULT);
        registerStat("furnace_interactions", Stats.INTERACT_WITH_FURNACE, StatFormatters.DEFAULT);
        registerStat("crafting_table_interactions", Stats.INTERACT_WITH_CRAFTING_TABLE, StatFormatters.DEFAULT);
        registerStat("blast_furnace_interactions", Stats.INTERACT_WITH_BLAST_FURNACE, StatFormatters.DEFAULT);
        registerStat("smoker_interactions", Stats.INTERACT_WITH_SMOKER, StatFormatters.DEFAULT);
        registerStat("anvil_interactions", Stats.INTERACT_WITH_ANVIL, StatFormatters.DEFAULT);
        registerStat("grindstone_interactions", Stats.INTERACT_WITH_GRINDSTONE, StatFormatters.DEFAULT);
        registerStat("smithing_table_interactions", Stats.INTERACT_WITH_SMITHING_TABLE, StatFormatters.DEFAULT);
        registerStat("beacon_interactions", Stats.INTERACT_WITH_BEACON, StatFormatters.DEFAULT);
        registerStat("brewing_stand_interactions", Stats.INTERACT_WITH_BREWINGSTAND, StatFormatters.DEFAULT);
        registerStat("lectern_interactions", Stats.INTERACT_WITH_LECTERN, StatFormatters.DEFAULT);
        registerStat("campfire_interactions", Stats.INTERACT_WITH_CAMPFIRE, StatFormatters.DEFAULT);
        registerStat("cartography_table_interactions", Stats.INTERACT_WITH_CARTOGRAPHY_TABLE, StatFormatters.DEFAULT);
        registerStat("loom_interactions", Stats.INTERACT_WITH_LOOM, StatFormatters.DEFAULT);
        registerStat("stonecutter_interactions", Stats.INTERACT_WITH_STONECUTTER, StatFormatters.DEFAULT);
        registerStat("hopper_inspections", Stats.INSPECT_HOPPER, StatFormatters.DEFAULT);
        registerStat("dropper_inspections", Stats.INSPECT_DROPPER, StatFormatters.DEFAULT);
        registerStat("dispenser_inspections", Stats.INSPECT_DISPENSER, StatFormatters.DEFAULT);
        registerStat("noteblocks_played", Stats.PLAY_NOTEBLOCK, StatFormatters.DEFAULT);
        registerStat("noteblocks_tuned", Stats.TUNE_NOTEBLOCK, StatFormatters.DEFAULT);
        registerStat("flowers_potted", Stats.POT_FLOWER, StatFormatters.DEFAULT);
        registerStat("trapped_chests_triggered", Stats.TRIGGER_TRAPPED_CHEST, StatFormatters.DEFAULT);
        registerStat("records_played", Stats.PLAY_RECORD, StatFormatters.DEFAULT);
        registerStat("cauldrons_filled", Stats.FILL_CAULDRON, StatFormatters.DEFAULT);
        registerStat("cauldrons_used", Stats.USE_CAULDRON, StatFormatters.DEFAULT);
        registerStat("armor_pieces_cleaned", Stats.CLEAN_ARMOR, StatFormatters.DEFAULT);
        registerStat("banners_cleaned", Stats.CLEAN_BANNER, StatFormatters.DEFAULT);
        registerStat("shulker_boxes_cleaned", Stats.CLEAN_SHULKER_BOX, StatFormatters.DEFAULT);
        registerStat("leave_game", Stats.LEAVE_GAME, StatFormatters.DEFAULT);
    }

    private static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(Leaderboards.MODID, path);
    }

    private static Component title(String path) {
        return Component.translatable("leaderboard.leaderboards." + path);
    }

    private static void registerStat(String path, Identifier statKey, IntFunction<Component> formatter) {
        VANILLA_STATS.put(id(path),
                new Leaderboard.FromStat(id(path), title(path), Stats.CUSTOM.get(statKey), false, formatter));
    }

    private static <T> void registerAggregate(String path, StatType<T> statType) {
        ToIntFunction<OfflinePlayerStats> sum = player -> {
            int total = 0;
            for (Stat<T> stat : statType) {
                total += player.stats().getValue(stat);
            }
            return total;
        };
        VANILLA_STATS.put(id(path), new Leaderboard(
                id(path), title(path),
                player -> Component.literal(String.valueOf(sum.applyAsInt(player))),
                Comparator.comparingInt(sum).reversed(),
                player -> sum.applyAsInt(player) > 0));
    }
}
