package events.protection;

import io.DataStore;
import io.PlayerData;
import io.Plot;
import main.OpenMC;
import misc.MiscMath;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class ReputationListener implements Listener {

    @EventHandler
    public void onCropTrample(PlayerInteractEvent event) {
        if (event.isCancelled()) return;
        Block block = event.getClickedBlock();
        if (block == null) return;
        if (event.getAction() != Action.PHYSICAL || block.getType() != Material.FARMLAND) return;
        if (event.getClickedBlock().getLocation().getWorld() != OpenMC.OVERWORLD) return;
        Plot here = DataStore.getPlot(block.getLocation());
        if (here == null) return;
        if (here.isForeignTo(event.getPlayer().getUniqueId())) {
            PlayerData pd = DataStore.getPlayerData(event.getPlayer().getUniqueId());
            if (!pd.warnedRecently(5)) {
                event.getPlayer().sendMessage(ChatColor.RED+"These crops are not yours!");
                event.setCancelled(true);
                pd.setWarned();
            } else {
                pd.addReputation(-5, "trampling "+here.owner().getName()+"'s crops");
            }

        }
    }

}
