package events;

import io.DataStore;
import io.PlayerData;
import main.OpenMC;
import misc.MiscCommands;
import misc.MiscMath;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Random;

public class PlayerJoinListener implements Listener {

    private Random r;

    public PlayerJoinListener() { r = new Random(); }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        PlayerData pd = DataStore.getPlayerData(p.getUniqueId());

        pd.updateLastLogin();
        pd.updateLastActivity();
        pd.createScoreboard();

        if (!p.hasPlayedBefore()) {
            MiscCommands.giveSpawnItems(p);
            pd.setSpawn(MiscCommands.randomSpawn(100, 3000));
            p.teleport(pd.getSpawn());
            p.sendTitle("Welcome to OpenMC!", "Please read the server guide.",
                    MiscMath.secondsToTicks(2), MiscMath.secondsToTicks(5), MiscMath.secondsToTicks(2));
        }

        DataStore.save();
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        DataStore.save();
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        event.setLeaveMessage("");
    }

}
