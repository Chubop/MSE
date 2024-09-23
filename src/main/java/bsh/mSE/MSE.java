package bsh.mSE;

import bsh.mSE.commands.CommandManager;
import bsh.mSE.listeners.*;
import bsh.mSE.managers.LogoutTimerManager;
import bsh.mSE.utils.GameRuleUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class MSE extends JavaPlugin {

    public static String eventName = "beyond";
    private static boolean isPlayersFrozen = false;
    private static boolean isGracePeriod = true;
    private static LogoutTimerManager ltm;

    public static LogoutTimerManager getLogoutTimerManager(){
        return ltm;
    }

    public static boolean isGracePeriod(){
        return isGracePeriod;
    }

    public static void turnOffGracePeriod(){
        isGracePeriod = false;
    }

    public static void turnOnGracePeriod(){
        isGracePeriod = true;
    }

    public static String getEventName(){
        return eventName;
    }

    public static boolean getIsPlayersFrozen(){
        return isPlayersFrozen;
    }

    public static void setIsPlayersFrozen(boolean value){
        isPlayersFrozen = value;
    }

    public static void togglePlayersFrozen(){
        isPlayersFrozen = !isPlayersFrozen;
    }


    public static MSE getInstance() {
        return JavaPlugin.getPlugin(MSE.class);
    }

    private void initFolder(){
        File pluginFolder = new File(this.getDataFolder(), "MSE");
        if (!pluginFolder.exists()) {
            pluginFolder.mkdirs();
        }
    }

    @Override
    public void onEnable() {
        initFolder();

        new CommandManager(this);
        ltm = new LogoutTimerManager();

        // BEYOND listeners.
        getServer().getPluginManager().registerEvents(new CraftingDisableListener(), this);
        getServer().getPluginManager().registerEvents(new CropGrowthDisableListener(), this);
        getServer().getPluginManager().registerEvents(new FishingDisableListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathBanListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerFreezeListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerLightningDeathListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerLoginListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerPvPListener(), this);
        getServer().getPluginManager().registerEvents(new CancelAchievementsListener(), this);


        // Setting all game rules
        GameRuleUtils.setGenericEventRules();
        if(eventName.equalsIgnoreCase("beyond")){
            GameRuleUtils.setBeyondEventRules();
        }
        Bukkit.getLogger().info("All game rules loaded.");
    }

    @Override
    public void onDisable() {
        // Unfreeze the world in case a crash happens when the world is frozen.
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tick unfreeze");
    }
}
