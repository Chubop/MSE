package bsh.mSE.listeners;

import bsh.mSE.MSE;
import bsh.mSE.managers.LogoutTimerManager;
import bsh.mSE.managers.PermissionsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLogoutListener implements Listener {

    @EventHandler
    public void onPlayerLogout(PlayerQuitEvent event){
        Player player = event.getPlayer();
        boolean isPlayerStaff = PermissionsManager.isPlayerStaff(player);
        boolean isGracePeriod = MSE.isGracePeriod();
        // if the player is NOT staff and grace period is OFF
        if( !isPlayerStaff && !isGracePeriod ){
            LogoutTimerManager ltm = MSE.getLogoutTimerManager();
            ltm.setLastLogInTimestamp(player, System.currentTimeMillis());
        }
    }
}
