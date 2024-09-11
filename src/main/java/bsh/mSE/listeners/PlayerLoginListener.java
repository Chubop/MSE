package bsh.mSE.listeners;

import bsh.mSE.MSE;
import bsh.mSE.managers.StopwatchManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLoginListener implements Listener {

    StopwatchManager stopwatchManager = MSE.getInstance().getStopwatchManager();

    @EventHandler
    public void addStopwatchBar(PlayerLoginEvent event){
        stopwatchManager.addPlayer(event.getPlayer());
    }
}
