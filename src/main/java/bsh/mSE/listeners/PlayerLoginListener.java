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
        try{
            stopwatchManager.addPlayer(event.getPlayer());
        }
        catch (IllegalStateException e){
            // TODO: when the user joins sometimes this error fires
        }
    }
}
