package bsh.mSE.managers;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.entity.Player;

public class PermissionsManager {

    public static boolean isPlayerStaff(Player player) {
        // Check if the player is an operator
        if (player.isOp()) {
            return true;
        }

        // Get LuckPerms instance
        LuckPerms luckPerms = LuckPermsProvider.get();

        // Get the LuckPerms user object for the player
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());

        if (user != null) {
            // Check if the player is in the "admin" or "camera" group
            if (user.getCachedData().getPermissionData(QueryOptions.defaultContextualOptions()).checkPermission("group.admin").asBoolean() ||
                    user.getCachedData().getPermissionData(QueryOptions.defaultContextualOptions()).checkPermission("group.camera").asBoolean()) {
                return true;
            }
        }

        // If neither an operator nor in the required groups, return false
        return false;
    }
}
