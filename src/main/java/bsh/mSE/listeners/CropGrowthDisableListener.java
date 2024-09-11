package bsh.mSE.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;

public class CropGrowthDisableListener implements Listener {
    @EventHandler
    public void cancelCropGrowth(BlockGrowEvent event){
        event.setCancelled(true);
    }
}
