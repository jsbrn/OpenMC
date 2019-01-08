package events;

import io.DataStore;
import io.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        System.out.println("Respawn");
        Player p = event.getPlayer();
        PlayerData pd = DataStore.getPlayerData(p.getUniqueId());
        event.setRespawnLocation(pd.getRespawnLocation());
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        event.setLeaveMessage("");
    }

}
