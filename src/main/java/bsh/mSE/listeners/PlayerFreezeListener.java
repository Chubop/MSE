package bsh.mSE.listeners;

import bsh.mSE.managers.PermissionsManager;
import bsh.mSE.managers.PlayerMessages;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.entity.Player;
import bsh.mSE.MSE; // Assuming this is where the isPlayersFrozen flag is located.
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class PlayerFreezeListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        // Get the player who triggered the event
        Player player = event.getPlayer();
        boolean isPlayersFrozen = MSE.getIsPlayersFrozen();
        // Check if players are frozen and the player is not staff (camera or admin)
        if (isPlayersFrozen && !PermissionsManager.isPlayerStaff(player)) {
            // Send a message to the player (using action bar or title, as needed)
            PlayerMessages.sendNegativeMessage("All players have been frozen by an event administrator.", player);
            // Cancel the event, which prevents the player from moving
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerTakeDamage(EntityDamageEvent event){
        if(!(event.getEntity() instanceof Player player)) return;

        boolean isPlayersFrozen = MSE.getIsPlayersFrozen();
        // Check if players are frozen and the player is not staff (camera or admin)
        if (isPlayersFrozen && !PermissionsManager.isPlayerStaff(player)) {
            // Send a message to the player (using action bar or title, as needed)
            PlayerMessages.sendNegativeMessage("All players have been frozen by an event administrator.", player);
            // Cancel the event, which prevents the player from moving
            event.setCancelled(true);
        }
    }
}
