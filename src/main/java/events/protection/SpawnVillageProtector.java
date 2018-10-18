package events.protection;

import main.OpenMC;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class SpawnVillageProtector implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;
        if (event.getBlock().getLocation()
                .distance(OpenMC.SPAWN_VILLAGE) < 64
                && event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled()) return;
        if (event.getBlock().getLocation()
                .distance(OpenMC.SPAWN_VILLAGE) < 64
                && event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (event.getEntity().getLocation()
                .distance(new Location(OpenMC.OVERWORLD, 0, 64, 0)) < 64) {
            if (event.getDamager() instanceof Player) {
                if (((Player) event.getDamager()).getGameMode() != GameMode.CREATIVE) {
                    event.setCancelled(true);
                }
            } else {
                event.setCancelled(false);
            }
        }
    }

}
