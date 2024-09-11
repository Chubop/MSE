package bsh.mSE.managers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
}
