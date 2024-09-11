package bsh.mSE.listeners;

import bsh.mSE.managers.PermissionsManager;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerLightningDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        if(PermissionsManager.isPlayerStaff(event.getPlayer())) return;

        World world = event.getPlayer().getWorld();
        world.strikeLightningEffect(event.getEntity().getLocation());
    }
}
