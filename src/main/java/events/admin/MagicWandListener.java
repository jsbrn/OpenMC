package events.admin;

import io.DataStore;
import main.OpenMC;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Custom arrows. Not to be used in production! Exploding arrows and chunk resets.
 */
public class MagicWandListener implements Listener {

    @EventHandler
    public void onRightClickWithStick(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        if (p.getGameMode() != GameMode.CREATIVE) return;
        Block clicked = event.getClickedBlock();
        ItemStack item = event.getItem();
        int diameter = p.isFlying() ? 8 : 4;
        if (clicked == null || item == null) return;
        if (item.getType() != Material.STICK || item.getEnchantments().isEmpty()) return;
        boolean breaking = event.getAction() == Action.LEFT_CLICK_BLOCK;
        Location loc = clicked.getLocation();
        Material fillWith = p.getInventory().getItem(1).getType();
        int yOffset = breaking ? 0 : 1;
        for (int i = -(diameter/2); i < (diameter/2)+1; i++) {
            for (int j = -(diameter/2); j < (diameter/2)+1; j++) {
                Block block = OpenMC.OVERWORLD.getBlockAt(loc.getBlockX() + i, loc.getBlockY()+yOffset, loc.getBlockZ() + j);
                if (!breaking) {
                    if (block.getType() == Material.AIR || p.isSneaking()) block.setType(fillWith);
                } else {
                    if (block.getType() == clicked.getType() || p.isSneaking()) block.setType(Material.AIR);
                }

            }
        }
    }

    private void regen(Location loc) {
        loc.getWorld().regenerateChunk(loc.getChunk().getX(), loc.getChunk().getZ());
    }

}
