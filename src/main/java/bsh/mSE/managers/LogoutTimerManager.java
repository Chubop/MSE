package bsh.mSE.managers;

import bsh.mSE.MSE;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;
import java.util.UUID;

public class LogoutTimerManager {

    private HashMap<UUID, Long> totalDurationLoggedOut;
    private HashMap<UUID, Long> lastLogOutTimeStamp;

    // Constructor
    public LogoutTimerManager() {
        this.totalDurationLoggedOut = new HashMap<>();
        this.lastLogOutTimeStamp = new HashMap<>();
    }

    public Long getLastLogInDuration(Player player){
        return lastLogOutTimeStamp.getOrDefault(player.getUniqueId(), 0L);
    }

    public Long getTotalLogOutDuration(Player player){
        return totalDurationLoggedOut.get(player.getUniqueId());
    }

    public void setLastLogInTimestamp(Player player, Long ms){
        lastLogOutTimeStamp.put(player.getUniqueId(), ms);
    }

    public void setTotalLogOutDuration(Player player, Long ms){
        lastLogOutTimeStamp.put(player.getUniqueId(), ms);
    }

    public void incrementTotalDurationLoggedOut(Player player, Long ms){
        Long totalDuration = this.getTotalLogOutDuration(player);
        this.setTotalLogOutDuration(player, totalDuration + ms);
    }
}
