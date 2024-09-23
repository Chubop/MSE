package bsh.mSE.utils;

import bsh.mSE.MSE;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.GameRule;

public class GameRuleUtils {

    public static void setGenericEventRules() {
        // Loop through all worlds
        for (World world : Bukkit.getWorlds()) {
            // Set some example game rules using the new setGameRule method
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
            world.setGameRule(GameRule.KEEP_INVENTORY, false);
            world.setGameRule(GameRule.DO_MOB_SPAWNING, true);
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            world.setGameRule(GameRule.DISABLE_RAIDS, false);
            world.setGameRule(GameRule.DO_INSOMNIA, false);
            world.setGameRule(GameRule.DO_PATROL_SPAWNING, false);
            world.setGameRule(GameRule.DO_WARDEN_SPAWNING, false);
            world.setGameRule(GameRule.DO_WARDEN_SPAWNING, false);
            world.setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE, 101);
            world.setGameRule(GameRule.RANDOM_TICK_SPEED, 2);
            world.setGameRule(GameRule.SPAWN_CHUNK_RADIUS, 0);
            world.setGameRule(GameRule.SPAWN_RADIUS, 1);
            world.setGameRule(GameRule.SPECTATORS_GENERATE_CHUNKS, false);

            if(MSE.getEventName().equalsIgnoreCase("beyond")){
                world.setGameRule(GameRule.WATER_SOURCE_CONVERSION, false);
            }
        }
    }

    public static void setBeyondEventRules() {
        // Loop through all worlds
        for (World world : Bukkit.getWorlds()) {
            world.setGameRule(GameRule.WATER_SOURCE_CONVERSION, false);
        }
    }
}
