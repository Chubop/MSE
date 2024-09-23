package bsh.mSE.listeners;

import bsh.mSE.MSE;
import bsh.mSE.managers.LogoutTimerManager;
import bsh.mSE.managers.PermissionsManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.Date;
import java.util.UUID;

public class PlayerLoginListener implements Listener {
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event){
        Player player = event.getPlayer();
        boolean isPlayerStaff = PermissionsManager.isPlayerStaff(player);
        boolean isGracePeriod = MSE.isGracePeriod();
        // if the player is NOT staff and grace period is OFF
        if( !isPlayerStaff && !isGracePeriod ){
            LogoutTimerManager ltm = MSE.getLogoutTimerManager();
            Long lastTimeStamp = ltm.getLastLogInDuration(player);
            long lastLogOutDuration = System.currentTimeMillis() - lastTimeStamp;
            ltm.incrementTotalDurationLoggedOut(player, lastLogOutDuration);
            long ONE_SECOND = 1000;
            if(ltm.getTotalLogOutDuration(player) >= ONE_SECOND){
                // If the player has been logged out for beyond our limit in milliseconds, we ban them.
                player.banPlayer("Banned: You have spent too much time logged out of the event.", Date.from(java.time.Instant.now().plusSeconds(48 * 3600)));
            }
        }
    }
}
