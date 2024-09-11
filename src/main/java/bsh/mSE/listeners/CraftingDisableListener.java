package bsh.mSE.listeners;

import bsh.mSE.MSE;
import bsh.mSE.managers.PermissionsManager;
import bsh.mSE.managers.PlayerMessages;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CraftingDisableListener implements Listener {

    // List of materials that are disabled for crafting
    public final List<Material> BEYOND_MATERIALS = Arrays.asList(
            Material.BUCKET,
            Material.FISHING_ROD,
            Material.FURNACE,
            Material.GLASS_BOTTLE,
            Material.CAULDRON,
            Material.ENCHANTING_TABLE
    );

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        if(PermissionsManager.isPlayerStaff(Objects.requireNonNull(event.getWhoClicked().getKiller()))) return;

        if(MSE.getEventName().equalsIgnoreCase("beyond")) return;
        // Get the item being crafted
        ItemStack item = event.getCurrentItem();

        // Check if the crafted item is in the disabled list
        if (item != null && BEYOND_MATERIALS.contains(item.getType())) {
            // Cancel the crafting if the item is disabled
            event.setCancelled(true);

            // Optionally, notify the player
            if (event.getWhoClicked() instanceof Player player) {
                PlayerMessages.sendNegativeMessage("You cannot craft this item.", player);
            }
        }
    }
}
