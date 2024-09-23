package bsh.mSE.listeners;

import bsh.mSE.MSE;
import bsh.mSE.managers.PermissionsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Date;

public class PlayerDeathBanListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        boolean isGracePeriod = MSE.isGracePeriod();

        Player player = event.getPlayer();
        // If it's not grace period or the player is staff, don't death ban them.
        if (!isGracePeriod && !PermissionsManager.isPlayerStaff(player)){
            // Otherwise, ban the player with a custom message for 48 hours.
            event.getPlayer().banPlayer("Thanks for playing.", Date.from(java.time.Instant.now().plusSeconds(48 * 3600)));
        }
    }
}
