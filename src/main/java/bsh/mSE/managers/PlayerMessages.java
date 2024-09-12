package bsh.mSE.managers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.util.Ticks;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class PlayerMessages {

    private static final Component PREFIX = Component.text("[", NamedTextColor.DARK_GRAY, TextDecoration.BOLD)
            .append(Component.text("M", NamedTextColor.DARK_PURPLE, TextDecoration.BOLD))
            .append(Component.text("SE", NamedTextColor.GOLD, TextDecoration.BOLD))
            .append(Component.text("]", NamedTextColor.DARK_GRAY, TextDecoration.BOLD))
            .append(Component.text(" ", Style.empty())); // Reset styling with an empty Style


    private static void sendPrefixedMessage(Component component, CommandSender sender){
        sender.sendMessage(PREFIX.append(component));
    }

    public static void sendCommandFeedback(String message, CommandSender sender){
        Component component = Component.text(message, NamedTextColor.GRAY);
        sendPrefixedMessage(component, sender);
    }

    public static void sendNegativeMessage(String message, Player player){
        Component component = Component.text(message, NamedTextColor.RED);
        sendPrefixedMessage(component, player);
    }

    public static void sendTitle(String title, @Nullable String subtitle, NamedTextColor color, Player player) {
        // Create the title component with the specified color
        Component titleComponent = Component.text(title, color);

        // If the subtitle is provided, create the subtitle component, otherwise leave it empty
        Component subtitleComponent = subtitle != null ? Component.text(subtitle, NamedTextColor.WHITE) : Component.empty();

        // Send the title and subtitle to the player
        player.showTitle(Title.title(
                titleComponent,
                subtitleComponent,
                // Fade-in, stay, and fade-out durations
                Title.Times.times(Ticks.duration(0), Ticks.duration(80), Ticks.duration(80))
        ));
    }

    public static void sendTitleToAllPlayers(String title, @Nullable String subtitle, NamedTextColor color){
        for(Player player : Bukkit.getOnlinePlayers()){
            sendTitle(title, subtitle, color, player);
        }
    }

}
