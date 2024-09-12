package bsh.mSE.utils;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class TeamAssignUtils {

    private static LuckPerms luckPerms;

    // Function to initialize LuckPerms API
    public static void initializeLuckPerms() {
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            luckPerms = provider.getProvider();
        } else {
            Bukkit.getLogger().severe(("LuckPerms provider not found."));
        }
    }


    public static List<OfflinePlayer> getPlayersFromFile(File file) {
        List<OfflinePlayer> players = new ArrayList<>();

        // Validate file existence
        if (!file.exists()) {
            Bukkit.getLogger().warning("File " + file.getName() + " does not exist.");
            return players;  // Return an empty list
        }

        // Read the file line by line and interpret as UUID or player name
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                players.add(getOfflinePlayerFromLine(line));
            }
        } catch (IOException e) {
            Bukkit.getLogger().severe("Failed to read the file: " + e.getMessage());
        }

        return players;
    }

    private static OfflinePlayer getOfflinePlayerFromLine(String line) {
        try {
            UUID uuid = UUID.fromString(line);
            return Bukkit.getOfflinePlayer(uuid);
        } catch (IllegalArgumentException e) {
            // Not a valid UUID, attempt to retrieve player by name
            return Bukkit.getOfflinePlayer(line);
        }
    }


    public static boolean assignTeam(OfflinePlayer player, String teamName) {
        Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName);

        if (team == null) {
            Bukkit.getLogger().warning("Team " + teamName + " does not exist.");
            return false;
        }

        if (player.isOnline()) {
            team.addEntry(player.getPlayer().getName());
            Bukkit.getLogger().info("Assigned player " + player.getName() + " to team " + teamName);
            return true;
        } else {
            Bukkit.getLogger().warning("Player " + player.getName() + " is offline, cannot assign to team at this time.");
            return false;
        }
    }

    public static CompletableFuture<Boolean> assignLuckPermsGroup(OfflinePlayer player, String groupName) {
        CompletableFuture<Boolean> result = new CompletableFuture<>();

        if (luckPerms == null) {
            initializeLuckPerms();
            if (luckPerms == null) {
                Bukkit.getLogger().severe("LuckPerms initialization failed.");
                result.complete(false);
                return result;
            }
        }

        luckPerms.getUserManager().loadUser(player.getUniqueId()).thenAcceptAsync(user -> {
            if (user == null) {
                Bukkit.getLogger().warning("LuckPerms user for " + player.getName() + " could not be loaded.");
                result.complete(false);
                return;
            }

            Node groupNode = Node.builder("group." + groupName).build();
            if (user.data().add(groupNode).wasSuccessful()) {
                saveLuckPermsUser(player, user, groupName, result);
            } else {
                Bukkit.getLogger().warning("Failed to add group " + groupName + " for " + player.getName());
                result.complete(false);
            }
        }).exceptionally(ex -> {
            Bukkit.getLogger().severe("Error loading LuckPerms user for " + player.getName() + ": " + ex.getMessage());
            result.complete(false);
            return null;
        });
        return result;
    }

    public static List<String> getAllTeamNames() {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        List<String> teamNames = new ArrayList<>();
        for (Team team : scoreboard.getTeams()) {
            teamNames.add(team.getName());
        }
        return teamNames;
    }

    public static List<String> getAllLuckPermsGroupNames() {
        if (luckPerms == null) {
            initializeLuckPerms();
        }
        List<String> groupNames = new ArrayList<>();
        luckPerms.getGroupManager().getLoadedGroups().forEach(group -> groupNames.add(group.getName()));
        return groupNames;
    }

    private static void saveLuckPermsUser(OfflinePlayer player, User user, String groupName, CompletableFuture<Boolean> result) {
        luckPerms.getUserManager().saveUser(user).thenRun(() -> {
            Bukkit.getLogger().info(player.getName() + " assigned to group " + groupName);
            result.complete(true);
        }).exceptionally(ex -> {
            Bukkit.getLogger().severe("Save failed for " + player.getName() + ": " + ex.getMessage());
            result.complete(false);
            return null;
        });
    }

}
