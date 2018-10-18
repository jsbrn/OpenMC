package events;

import commands.ServerGuideCommand;
import controllers.WebsocketController;
import io.DataStore;
import io.PlayerData;
import main.OpenMC;
import misc.MiscCommands;
import misc.MiscMath;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class PlayerJoinListener implements Listener {

    private Random r;

    public PlayerJoinListener() { r = new Random(); }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        PlayerData pd = DataStore.getPlayerData(p.getUniqueId());
        if (pd.getMinutesRemaining() < 1) {
            p.kickPlayer("You do not have any play time remaining!\n"
                    +ChatColor.YELLOW+"Go to "+ChatColor.AQUA+OpenMC.WEBSITE_URL+"/play"+ChatColor.YELLOW+" to get more!");
            return;
        }

        if (pd.getHoursRemaining() <= 10) {
            ChatColor color = pd.getMinutesRemaining() < 120 ? ChatColor.RED : ChatColor.WHITE;
            p.sendMessage(color+"You have "+MiscMath.timeLeftAsString(pd.getTimeRemaining())
                    +" of play time left!"+(pd.getMinutesRemaining() < 120 ? " Go to "+ChatColor.BOLD+ChatColor.AQUA+OpenMC.WEBSITE_URL+"/play"+ ChatColor.RED+" to get more." : ""));
        }

        pd.updateLastLogin();
        pd.updateLastActivity();

        if (!p.hasPlayedBefore()) {
            MiscCommands.giveSpawnItems(p);
            pd.setSpawn(MiscCommands.randomSpawn(512, 2048));
            p.teleport(pd.getSpawn());
            p.sendTitle("Welcome to OpenMC!", "Please read the server guide.",
                    MiscMath.secondsToTicks(2), MiscMath.secondsToTicks(5), MiscMath.secondsToTicks(2));
        }

        WebsocketController.sendPlayerData();
        DataStore.save();
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        WebsocketController.sendPlayerData();
        DataStore.getPlayerData(event.getPlayer().getUniqueId()).adjustTimeRemaining();
        DataStore.save();
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        event.setLeaveMessage("");
        WebsocketController.sendPlayerData();
    }

}
