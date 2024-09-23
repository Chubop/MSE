package bsh.mSE.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public class CancelAchievementsListener implements Listener {

    @EventHandler
    public void onPlayerAdvancement(PlayerAdvancementDoneEvent event) {
        event.message(null);
    }
}
