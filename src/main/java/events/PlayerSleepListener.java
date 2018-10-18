package events;

import main.OpenMC;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerSleepListener implements Listener {

    @EventHandler
    public void onPlayerFinishSleep(PlayerBedLeaveEvent event) {
        long ticks = OpenMC.OVERWORLD.getFullTime();
        if (ticks > 12541 && ticks < 23458) {
            event.getPlayer().sendMessage("You awake from your nap feeling well-rested.");
            OpenMC.OVERWORLD.setFullTime(ticks + 1000);
        }
    }

}
