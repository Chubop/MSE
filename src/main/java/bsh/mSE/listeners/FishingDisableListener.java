package bsh.mSE.listeners;

import bsh.mSE.managers.PermissionsManager;
import bsh.mSE.managers.PlayerMessages;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class FishingDisableListener implements Listener {

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        if(PermissionsManager.isPlayerStaff(event.getPlayer())) return;
        // Cancel the fishing event
        event.setCancelled(true);

        PlayerMessages.sendNegativeMessage("Fishing is disabled on this server.", event.getPlayer());

    }
}
