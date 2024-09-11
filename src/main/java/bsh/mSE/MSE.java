package bsh.mSE;

import bsh.mSE.commands.CommandManager;
import bsh.mSE.listeners.*;
import bsh.mSE.managers.StopwatchManager;
import bsh.mSE.utils.GameRuleUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class MSE extends JavaPlugin {

    public static String eventName = "beyond";
    private static boolean isPlayersFrozen = false;
    private static boolean isGracePeriod = true;
    private StopwatchManager stopwatchManager;

    public StopwatchManager getStopwatchManager(){
        return stopwatchManager;
    }

    public static boolean getIsGracePeriod(){
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

    @Override
    public void onEnable() {
        new CommandManager(this);

        // BEYOND listeners.
        getServer().getPluginManager().registerEvents(new CraftingDisableListener(), this);
        getServer().getPluginManager().registerEvents(new CropGrowthDisableListener(), this);
        getServer().getPluginManager().registerEvents(new FishingDisableListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathBanListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathMessageListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerFreezeListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerLightningDeathListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathMessageListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerLoginListener(), this);

        // Setting all game rules
        GameRuleUtils.setGenericEventRules();
        if(eventName.equalsIgnoreCase("beyond")){
            GameRuleUtils.setBeyondEventRules();
        }
        Bukkit.getLogger().info("All game rules loaded.");
        stopwatchManager = new StopwatchManager();

    }

    @Override
    public void onDisable() {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tick unfreeze");
    }
}
