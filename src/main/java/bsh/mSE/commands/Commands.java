package bsh.mSE.commands;

import bsh.mSE.MSE;
import bsh.mSE.managers.PermissionsManager;
import bsh.mSE.managers.PlayerMessages;
import bsh.mSE.utils.TeamAssignUtils;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;
import java.util.Objects;


public class Commands {

    public static void handleGraceCommand(@NotNull CommandSender sender, @NotNull String[] args) {
        if (args.length == 2) {
            boolean gracePeriodStatus = MSE.getIsGracePeriod();
            PlayerMessages.sendCommandFeedback("Grace period is " + (gracePeriodStatus ? "on" : "off"), sender);
        } else if (args.length == 3) {
            String action = args[2].toLowerCase();
            switch (action) {
                case "on":
                    MSE.turnOnGracePeriod();
                    PlayerMessages.sendCommandFeedback("Grace period turned on.", sender);
                    break;
                case "off":
                    MSE.turnOffGracePeriod();
                    PlayerMessages.sendCommandFeedback("Grace period turned off.", sender);
                    break;
                default:
                    PlayerMessages.sendCommandFeedback("Invalid argument. Use 'on' or 'off'.", sender);
                    break;
            }
        } else {
            PlayerMessages.sendCommandFeedback("Usage: /event grace [on/off]", sender);
        }
    }

    public static void handleFreezePlayersCommand(@NotNull CommandSender sender) {
        boolean isPlayersFrozen = MSE.getIsPlayersFrozen();
        // Freeze the game
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), isPlayersFrozen ? "tick unfreeze" : "tick freeze");
        MSE.togglePlayersFrozen();
        if(isPlayersFrozen){
            PlayerMessages.sendCommandFeedback("You have UNFROZEN all players.", sender);
            for(Player player : Bukkit.getOnlinePlayers()){
                if(!PermissionsManager.isPlayerStaff(player)){
                    PlayerMessages.sendTitle("", "You have been unfrozen.", NamedTextColor.GREEN, player);
                }
                else{
                    PlayerMessages.sendCommandFeedback(sender.getName() + " has unfrozen the world.", player);
                }
            }
        }
        else{
            PlayerMessages.sendCommandFeedback("You have FROZEN all players.", sender);
        }
    }

    public static boolean assignPlayer(@NotNull CommandSender sender, @NotNull OfflinePlayer player, String teamName, String luckPermsGroupName) {
        String playerName = player.getName();

        if (playerName == null || (!player.hasPlayedBefore() && !player.isOnline())) {
            PlayerMessages.sendCommandFeedback(playerName + " has never played on this server.", sender);
            return false;
        }

        boolean teamAssigned = tryAssignTeam(sender, player, teamName);
        boolean groupAssigned = tryAssignLuckPermsGroup(sender, player, luckPermsGroupName);

        if (teamAssigned && groupAssigned) {
            PlayerMessages.sendCommandFeedback(playerName + " was successfully assigned to team '" + teamName + "' and LuckPerms group '" + luckPermsGroupName + "'.", sender);
            return true;
        } else {
            if (teamAssigned) {
                PlayerMessages.sendCommandFeedback("Only team assignment succeeded for " + playerName, sender);
            } else if (groupAssigned) {
                PlayerMessages.sendCommandFeedback("Only LuckPerms group assignment succeeded for " + playerName, sender);
            }
            return false;
        }
    }

    private static boolean tryAssignTeam(@NotNull CommandSender sender, OfflinePlayer player, String teamName) {
        try {
            boolean success = TeamAssignUtils.assignTeam(player, teamName);
            if (!success) {
                PlayerMessages.sendCommandFeedback("Failed to assign " + player.getName() + " to team: " + teamName, sender);
            }
            return success;
        } catch (Exception e) {
            logAndSendError(sender, "Error assigning " + player.getName() + " to team: " + teamName, e);
            return false;
        }
    }

    private static boolean tryAssignLuckPermsGroup(@NotNull CommandSender sender, OfflinePlayer player, String groupName) {
        try {
            boolean success = TeamAssignUtils.assignLuckPermsGroup(player, groupName).get();
            if (!success) {
                PlayerMessages.sendCommandFeedback("Failed to assign " + player.getName() + " to LuckPerms group: " + groupName, sender);
            }
            return success;
        } catch (Exception e) {
            logAndSendError(sender, "Error assigning " + player.getName() + " to LuckPerms group: " + groupName, e);
            return false;
        }
    }

    private static void logAndSendError(@NotNull CommandSender sender, String message, Exception e) {
        Bukkit.getLogger().severe(message + ": " + e.getMessage());
        PlayerMessages.sendCommandFeedback(message, sender);
        e.printStackTrace();
    }


    public static void assignTeamsAndGroups(CommandSender sender, String teamName, String luckPermsGroupName, String fileName) {
        File pluginDirectory = Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("MSE")).getDataFolder();
        File file = new File(pluginDirectory, fileName);

        // Validate the existence of the file
        if (!file.exists() || !file.isFile()) {
            PlayerMessages.sendCommandFeedback("The file '" + fileName + "' does not exist or is not a valid file.", sender);
            return;
        }

        // Get players from the file
        List<OfflinePlayer> players = TeamAssignUtils.getPlayersFromFile(file);
        if (players.isEmpty()) {
            PlayerMessages.sendCommandFeedback("No players found in the file: " + fileName, sender);
            return;
        }

        // Assign each player to the team and LuckPerms group
        int successCount = 0;
        for (OfflinePlayer offlinePlayer : players) {
            boolean didAssign = assignPlayer(sender, offlinePlayer, teamName, luckPermsGroupName);
            if (didAssign) {
                successCount++;
            } else {
                PlayerMessages.sendCommandFeedback("Failed to assign " + offlinePlayer.getName() + " to the team or LuckPerms group.", sender);
            }
        }

        // Provide feedback on how many players were successfully assigned
        if (successCount > 0) {
            PlayerMessages.sendCommandFeedback("Successfully assigned " + successCount + " players to the team '" + teamName + "' and LuckPerms group '" + luckPermsGroupName + "'.", sender);
        } else {
            PlayerMessages.sendCommandFeedback("No players were successfully assigned. Please check the logs for more information.", sender);
        }
    }

    public static void addToWhitelist(CommandSender sender, String fileName) {
        File pluginDirectory = Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("MSE")).getDataFolder();
        File file = new File(pluginDirectory, fileName);

        // Validate the existence of the file
        if (!file.exists() || !file.isFile()) {
            PlayerMessages.sendCommandFeedback("The file '" + fileName + "' does not exist or is not a valid file.", sender);
            return;
        }

        // Get the list of players from the file
        List<OfflinePlayer> players = TeamAssignUtils.getPlayersFromFile(file);
        if (players.isEmpty()) {
            PlayerMessages.sendCommandFeedback("No players found in the file: " + fileName, sender);
            return;
        }

        // Add each player to the whitelist
        int successCount = 0;
        for (OfflinePlayer offlinePlayer : players) {
            if (tryWhitelistPlayer(sender, offlinePlayer)) {
                successCount++;
            }
        }

        // Provide feedback on how many players were successfully whitelisted
        if (successCount > 0) {
            PlayerMessages.sendCommandFeedback("Successfully whitelisted " + successCount + " players.", sender);
        } else {
            PlayerMessages.sendCommandFeedback("No players were successfully whitelisted. Please check the logs for more information.", sender);
        }
    }

    private static boolean tryWhitelistPlayer(CommandSender sender, OfflinePlayer player) {
        try {
            if (!player.isWhitelisted()) {
                player.setWhitelisted(true);
                return true;
            }
        } catch (Exception e) {
            PlayerMessages.sendCommandFeedback("Failed to whitelist " + player.getName() + ".", sender);
            logError("Error whitelisting player " + player.getName(), e);
        }
        return false;
    }

    private static void logError(String message, Exception e) {
        Bukkit.getLogger().severe(message + ": " + e.getMessage());
        e.printStackTrace();
    }

}
