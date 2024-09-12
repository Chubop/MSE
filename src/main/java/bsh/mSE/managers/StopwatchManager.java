package bsh.mSE.managers;

import bsh.mSE.utils.OraxenUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import bsh.mSE.MSE;

public class StopwatchManager {

    private Scoreboard scoreboard;
    private Objective objective;
    private BukkitRunnable task;
    private int elapsedMinutes; // Tracking minutes
    private boolean isRunning;
    private String lastTimeDisplayed;

    public StopwatchManager() {
        // Initialize the scoreboard and objective
        initializeScoreboard();
        elapsedMinutes = 0;
        isRunning = false;
        lastTimeDisplayed = "";
    }

    private void initializeScoreboard() {
        // Create a new scoreboard
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        scoreboard = manager.getNewScoreboard();

        // Create a new objective in the scoreboard
        objective = scoreboard.registerNewObjective("stopwatch", Criteria.DUMMY, Component.text(OraxenUtils.getGlyph("half_brain").getCharacter()));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    // Start the stopwatch
    public void startClock() {
        if (isRunning) return; // If already running, do nothing

        isRunning = true;

        // Schedule a repeating task to update the scoreboard every minute
        task = new BukkitRunnable() {
            @Override
            public void run() {
                elapsedMinutes++;
                updateScoreboard();
            }
        };
        task.runTaskTimer(MSE.getInstance(), 0L, 1200L); // 1200 ticks = 1 minute (20 ticks per second * 60 seconds)
        this.addAllPlayers();
    }

    // Pause the stopwatch
    public void pauseClock() {
        if (task != null) {
            task.cancel();
            isRunning = false;
        }
    }

    // Stop and reset the stopwatch
    public void stopClock() {
        if (task != null) {
            task.cancel();
        }
        elapsedMinutes = 0;
        updateScoreboard();
        isRunning = false;
        this.removeAllPlayers();
    }

    private void updateScoreboard() {
        int hours = elapsedMinutes / 60;
        int minutes = elapsedMinutes % 60;

        // Format the time as 0h30m or 0h00m
        String formattedTime = String.format("%02dh%02dm", hours, minutes);

        // Remove the previous score
        if (!lastTimeDisplayed.isEmpty()) {
            scoreboard.resetScores(lastTimeDisplayed);
        }

        // Update the scoreboard with the new time
        Score score = objective.getScore(formattedTime);
        score.setScore(1); // Set a constant score of 1 for display purposes

        // Store the last displayed time to remove it in the next update
        lastTimeDisplayed = formattedTime;
    }

    // Add a player to see the scoreboard
    public void addPlayer(Player player) {
        player.setScoreboard(scoreboard);
    }

    public void addAllPlayers(){
        for(Player player: Bukkit.getOnlinePlayers()){
            this.addPlayer(player);
        }
    }

    public void removeAllPlayers(){
        for(Player player: Bukkit.getOnlinePlayers()){
            player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard()); // Reset to the main scoreboard
        }
    }
}
