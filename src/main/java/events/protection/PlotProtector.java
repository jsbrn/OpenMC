package events.protection;

import io.DataStore;
import io.PlayerData;
import io.Plot;
import main.OpenMC;
import misc.MiscMath;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.Arrays;

public class PlotProtector implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled()) return;
        if (event.getPlayer() == null) return;
        if (event.getPlayer().isOp()) return;
        if (event.getBlock().getLocation().getWorld() != OpenMC.OVERWORLD) return;
        PlayerData pd = DataStore.getPlayerData(event.getPlayer().getUniqueId());
        Plot here = DataStore.getPlot(event.getBlock().getLocation());
        if (event.getBlock().getBlockData().getAsString().contains("banner")) { claimPlot(event); return; }
        if (here != null) {
            boolean foreign = here.isForeignTo(event.getPlayer().getUniqueId());
            if (foreign) {
                event.setCancelled(true);
                event.getPlayer().sendTitle("", "You can't build here.", 0, MiscMath.secondsToTicks(1), 0);
            } else { pd.sendActionText(ChatColor.GREEN+"✔ Protected", ChatColor.GREEN); }
        } else {
            if (event.getBlock().getType() == Material.TNT) {
                event.setCancelled(true);
                event.getPlayer().sendMessage("You must claim this land to place TNT.");
            } else {
                pd.sendActionText("✖ Unprotected", ChatColor.WHITE);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;
        if (event.getPlayer() == null) return;
        if (event.getBlock().getLocation().getWorld() != OpenMC.OVERWORLD) return;
        Plot here = DataStore.getPlot(event.getBlock().getLocation());
        boolean foreign = here == null ? false : here.isForeignTo(event.getPlayer().getUniqueId());
        boolean isBanner = event.getBlock().getBlockData().getAsString().contains("banner");
        if (foreign) {
            event.getPlayer().sendTitle("", "You can't build here.", 0, MiscMath.secondsToTicks(1), 0);
            event.setCancelled(true);
        } else if (isBanner) { declaimPlot(event); return; }
    }

    @EventHandler
    public void onPlayerInteractBlock(PlayerInteractEvent event) {
        //update player's last activity
        DataStore.getPlayerData(event.getPlayer().getUniqueId()).updateLastActivity();
        if (event.isCancelled()) return;
        Block clicked = event.getClickedBlock();
        if (clicked == null) return;
        if (clicked.getLocation().getWorld() != OpenMC.OVERWORLD) return;
        Plot here = DataStore.getPlot(clicked.getLocation());
        if (here != null) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (here.isForeignTo(event.getPlayer().getUniqueId())) {
                    Material[] interactingDisabled = new Material[]{
                        Material.TNT,
                        Material.STONE_BUTTON,
                        Material.ARMOR_STAND,
                        Material.ITEM_FRAME,
                        Material.LEVER,
                        Material.COMPARATOR,
                        Material.REPEATER,
                        Material.CHEST,
                        Material.FURNACE,
                        Material.BREWING_STAND
                    };
                    if (Arrays.asList(interactingDisabled).contains(clicked.getType())) event.setCancelled(true);
                    if (clicked.getType().name().contains("GATE")) {
                        event.getPlayer().sendTitle("", "This gate is locked.", 0, MiscMath.secondsToTicks(1), 0);
                        event.setCancelled(true);
                    }
                    if (clicked.getType().name().contains("DOOR")) {
                        event.getPlayer().sendTitle("", "This door is locked.", 0, MiscMath.secondsToTicks(1), 0);
                        event.setCancelled(true);
                    }
                    if (clicked.getType().name().contains("BUTTON")) {event.setCancelled(true); }
                    if (clicked.getType() == Material.CHEST) {
                        event.getPlayer().sendTitle("", "This chest is locked.", 0, MiscMath.secondsToTicks(1), 0);
                        event.getPlayer().playSound(clicked.getLocation(), Sound.BLOCK_CHEST_LOCKED, 0.8f, 0);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerRightClickArmorStand(PlayerInteractAtEntityEvent event) {
        if (event.isCancelled()) return;
        if (event.getRightClicked().getType() != EntityType.ARMOR_STAND) return;
        if (event.getRightClicked().getLocation().getWorld() != OpenMC.OVERWORLD) return;
        Plot here = DataStore.getPlot(event.getRightClicked().getLocation());
        if (here != null) {
            if (here.isForeignTo(event.getPlayer().getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByPlayer(EntityDamageByEntityEvent e) {
        if (e.isCancelled()) return;
        if (e.getEntity().getLocation().getWorld() != OpenMC.OVERWORLD) return;
        Plot here = DataStore.getPlot(e.getEntity().getLocation());
        Player attacker = null;

        if (e.getDamager() instanceof Projectile) {
            if (((Projectile)e.getDamager()).getShooter() instanceof Player) {
                attacker = ((Player)((Projectile)e.getDamager()).getShooter());
            }
        } else if (e.getDamager() instanceof Player) attacker = ((Player)e.getDamager());

        if (here != null && attacker != null) {
            if (here.isForeignTo(attacker.getUniqueId())) {
                EntityType[] safe = new EntityType[]{
                    EntityType.OCELOT, EntityType.WOLF, EntityType.PARROT, EntityType.RABBIT,
                    EntityType.COW, EntityType.PIG, EntityType.SHEEP, EntityType.CHICKEN,
                    EntityType.HORSE, EntityType.SKELETON_HORSE, EntityType.MULE, EntityType.LLAMA,
                    EntityType.ARMOR_STAND, EntityType.ITEM_FRAME
                };
                boolean isProtected = Arrays.asList(safe).contains(e.getEntityType());
                e.setCancelled(isProtected);
                if (e.isCancelled()) attacker.sendTitle("", "You can't do that here.", 0, MiscMath.secondsToTicks(1), 0);
            }
        }
    }

    @EventHandler
    public void onExplosion(ExplosionPrimeEvent event) {
        Plot here = DataStore.getPlot(event.getEntity().getLocation());
        if (here != null) {
            Player owner = here.owner().getPlayer();
            boolean allowExplosion = true;
            if (owner == null) allowExplosion = false;
            if (owner != null) if (!here.intersects(owner.getLocation())) allowExplosion = false;
            if (!allowExplosion) {
                event.getEntity().remove();
                event.setCancelled(true);
            }
        }
    }

    private static void claimPlot(BlockPlaceEvent event) {
        PlayerData pd = DataStore.getPlayerData(event.getPlayer().getUniqueId());
        if (!pd.canClaimLand()) {
            event.getPlayer().sendMessage("You don't have any plots left to claim! Use \"/plots buy <amount>\" to get more.");
            event.setCancelled(true);
            return;
        }
        Location placement = event.getBlock().getLocation();
        Plot here = DataStore.getPlot(placement);
        if (here == null) {
            ArrayList<Plot> tooClose = DataStore.getPlotsTooClose(placement);
            if (tooClose.isEmpty()) {
                DataStore.addPlot(event.getPlayer().getUniqueId(), placement);
                event.getPlayer().sendMessage("You claimed this plot (25 block radius)!");
            } else {
                event.getPlayer().sendMessage("This area is too close to "+tooClose.size()
                        +" other plot"+(tooClose.size() > 1 ? "s" : "")+"!");
                event.setCancelled(true);
            }
        } else {
            boolean owned = here.ownedBy(event.getPlayer().getUniqueId());
            event.getPlayer().sendMessage(!owned
                    ? "This land is already owned by "+here.owner().getName()+"!"
                    : "You already own this land! (hold SHIFT to place normally)");
            event.setCancelled(true);
        }
    }

    public static void declaimPlot(BlockBreakEvent event) {
        Plot here = DataStore.getPlot(event.getBlock().getLocation());
        if (here != null) {
            boolean owned = !here.ownedBy(event.getPlayer().getUniqueId());
            if (!owned) {
                if (DataStore.removePlot(here))
                    event.getPlayer().sendMessage("You no longer own this plot (25 block radius)!");
            } else {
                event.getPlayer().sendMessage("This banner is owned by " + here.owner().getName() + ".");
                event.setCancelled(true);
            }
        }
    }

}
