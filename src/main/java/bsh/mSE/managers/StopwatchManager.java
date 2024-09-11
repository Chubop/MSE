package bsh.mSE.managers;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import bsh.mSE.MSE;

public class StopwatchManager {

    private BossBar bossBar;
    private BukkitRunnable task;
    private int elapsedMinutes; // Now tracking minutes
    private boolean isRunning;

    public StopwatchManager() {
        // Initialize the boss bar
        bossBar = Bukkit.createBossBar("Stopwatch: 0 hours 0 minutes", BarColor.YELLOW, BarStyle.SOLID);
        elapsedMinutes = 0;
        isRunning = false;
    }

    // Start the stopwatch
    public void startClock() {
        if (isRunning) return; // If already running, do nothing

        isRunning = true;

        // Schedule a repeating task to update the boss bar every minute
        task = new BukkitRunnable() {
            @Override
            public void run() {
                elapsedMinutes++;
                updateBossBar();
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
        updateBossBar();
        isRunning = false;
        this.removeAllPlayers();
    }

    private void updateBossBar() {
        int hours = elapsedMinutes / 60;
        int minutes = elapsedMinutes % 60;

        // Format the time based on the number of hours
        String formattedTime;
        if (hours > 0) {
            formattedTime = String.format("%d hours %d minutes", hours, minutes - 1);
        } else {
            formattedTime = String.format("%d minutes", minutes - 1);
        }

        bossBar.setTitle(formattedTime);

        // Optionally update progress bar (e.g., every 12 hours, reset visual bar)
        bossBar.setProgress(Math.min(1.0, (elapsedMinutes % (12 * 60)) / (12.0 * 60))); // 12-hour cycle for progress
    }

    // Add a player to see the boss bar
    public void addPlayer(Player player) {
        bossBar.addPlayer(player);
    }

    public void addAllPlayers(){
        for(Player player: Bukkit.getOnlinePlayers()){
            this.addPlayer(player);
        }
    }

    public void removeAllPlayers(){
        for(Player player: Bukkit.getOnlinePlayers()){
            this.removePlayer(player);
        }
    }

    // Remove a player from the boss bar
    public void removePlayer(Player player) {
        bossBar.removePlayer(player);
    }
}
