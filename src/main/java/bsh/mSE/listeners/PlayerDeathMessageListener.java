package bsh.mSE.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scoreboard.Team;

public class PlayerDeathMessageListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        // Get the player who died
        Player deadPlayer = event.getEntity();

        // Check if the killer is a player
        if (deadPlayer.getKiller() != null) {
            Player killer = deadPlayer.getKiller();

            // Get the killer's team (if any)
            Team killerTeam = killer.getScoreboard().getEntryTeam(killer.getName());

            if (killerTeam != null) {
                // Get the team name and build the death message using Kyori's Adventure API
                String teamName = killerTeam.getName();
                Component deathMessage = Component.text(deadPlayer.getName())
                        .append(Component.text(" was killed by an enemy "))
                        .append(Component.text(teamName).decorate(TextDecoration.BOLD));

                // Set the new death message using Adventure API
                event.deathMessage(deathMessage);
            } else {
                // If the killer is not on a team, set a default death message
                Component deathMessage = Component.text(deadPlayer.getName())
                        .append(Component.text(" was killed by "))
                        .append(Component.text(killer.getName()));

                event.deathMessage(deathMessage);
            }
        }
    }
}
