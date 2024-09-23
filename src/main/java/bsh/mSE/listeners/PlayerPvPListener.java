package bsh.mSE.listeners;

import bsh.mSE.MSE;
import bsh.mSE.managers.PlayerMessages;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.entity.Player;

public class PlayerPvPListener implements Listener {

    // Event handler for PvP
    @EventHandler
    public void onPlayerPvP(EntityDamageByEntityEvent event) {
        // if Grace Period is on, we ignore this listener.
        if(!MSE.isGracePeriod()) return;

        if (event.getDamager() instanceof Player attacker && event.getEntity() instanceof Player) {
            PlayerMessages.sendCommandFeedback("PvP is not enabled during Grace Period.", attacker);
            event.setCancelled(true);
        }
    }
}
