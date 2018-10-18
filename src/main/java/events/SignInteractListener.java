package events;

import io.DataStore;
import io.PlayerData;
import io.Plot;
import main.OpenMC;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.ArrayList;
import java.util.Random;

public class SignInteractListener implements Listener {

    @EventHandler
    public void onSignClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block clicked = event.getClickedBlock();
        if (clicked.getLocation().distance(OpenMC.SPAWN_VILLAGE) >= OpenMC.SPAWN_VILLAGE_RADIUS) return;
        System.out.println("Clicked block "+clicked.getType());
        if (clicked.getType() != Material.WALL_SIGN && clicked.getType() != Material.SIGN) return;
        String[] text = ((Sign)clicked.getState()).getLines();
        //if (text[1].contains("AWAY")) randomSpawn(event.getPlayer(), 4096, 8192);
        //if (text[1].contains("CLOSE")) randomSpawn(event.getPlayer(), 128, 2048);
    }

}