package bsh.mSE.commands;

import bsh.mSE.MSE;
import bsh.mSE.managers.PermissionsManager;
import bsh.mSE.managers.PlayerMessages;
import bsh.mSE.managers.StopwatchManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CommandManager implements CommandExecutor, TabCompleter {

    public CommandManager(JavaPlugin plugin) {
        Objects.requireNonNull(plugin.getCommand("event")).setExecutor(this);
        Objects.requireNonNull(plugin.getCommand("event")).setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            PlayerMessages.sendCommandFeedback("Usage: /event <subcommand>", sender);
            return true;
        }
        // These commands are only available to operators
        if(!sender.isOp()) return true;

        String subcommand = args[0].toLowerCase();
        StopwatchManager stopwatchManager = MSE.getInstance().getStopwatchManager();


        switch (subcommand) {
            case "grace":
                Commands.handleGraceCommand(sender, args);
                break;

            case "freeze-all":
                Commands.handleFreezePlayersCommand(sender);
                break;

            case "mass-whitelist": // /event mass-whitelist players.txt
                if (args.length == 2) {
                    String fileName = args[1];
                    Commands.addToWhitelist(sender, fileName);
                } else {
                    PlayerMessages.sendCommandFeedback("Usage: /event mass-whitelist [fileName]", sender);
                }
                break;

            case "assign": // /event assign Chubop red Red
                if (args.length == 4) {
                    String playerName = args[1];
                    String teamName = args[2];
                    String lpGroupName = args[3];

                    Commands.assignPlayer(sender, Bukkit.getOfflinePlayer(playerName), teamName, lpGroupName);
                } else {
                    PlayerMessages.sendCommandFeedback("Usage: /event assign [playerName] [teamName] [luckPermsGroupName]", sender);
                }
                break;

            case "assign-teams": // /event assign-teams red red red.txt
                if (args.length == 4) {
                    String teamName = args[1];
                    String luckPermsGroupName = args[2];
                    String fileName = args[3];

                    Commands.assignTeamsAndGroups(sender, teamName, luckPermsGroupName, fileName);
                } else {
                    PlayerMessages.sendCommandFeedback("Usage: /event assign-teams [teamName] [luckPermsGroupName] [fileName]", sender);
                }
                break;

            case "start-clock":
                stopwatchManager.startClock();
                PlayerMessages.sendCommandFeedback("Event stopwatch started.", sender);
                break;

            case "pause-clock":
                stopwatchManager.pauseClock();
                PlayerMessages.sendCommandFeedback("Event stopwatch paused.", sender);
                break;

            case "stop-clock":
                stopwatchManager.stopClock();
                PlayerMessages.sendCommandFeedback("Event stopwatch stopped and reset.", sender);
                break;

            default:
                PlayerMessages.sendCommandFeedback("Invalid subcommand.", sender);
                break;
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(!sender.isOp()) new ArrayList<>();
        if (args.length == 1) {
            List<String> subcommands = Arrays.asList("assign-teams", "grace", "freeze-all", "mass-whitelist", "pause-clock", "start-clock", "stop-clock");
            return filterTabSuggestions(subcommands, args[0]);
        }
        return new ArrayList<>();
    }

    private List<String> filterTabSuggestions(List<String> options, String input) {
        List<String> completions = new ArrayList<>();
        for (String option : options) {
            if (option.toLowerCase().startsWith(input.toLowerCase())) {
                completions.add(option);
            }
        }
        return completions;
    }
}
