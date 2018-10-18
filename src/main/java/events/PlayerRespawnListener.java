package events;

import commands.ServerGuideCommand;
import controllers.WebsocketController;
import io.DataStore;
import io.PlayerData;
import main.OpenMC;
import misc.MiscCommands;
import misc.MiscMath;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class PlayerRespawnListener implements Listener {

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        System.out.println("Respawn");
        Player p = event.getPlayer();
        PlayerData pd = DataStore.getPlayerData(p.getUniqueId());
        event.setRespawnLocation(pd.getRespawnLocation());
        WebsocketController.sendPlayerData();
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        WebsocketController.sendPlayerData();
        DataStore.getPlayerData(event.getPlayer().getUniqueId()).adjustTimeRemaining();
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        event.setLeaveMessage("");
        WebsocketController.sendPlayerData();
    }

}
