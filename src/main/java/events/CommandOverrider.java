package events;

import main.OpenMC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.Arrays;

public class CommandOverrider implements Listener {

    private static String[] whitelisted = new String[]{
        "plots",
        "guide",
        "rep",
        "friend",
        "unfriend",
        "global",
        "g",
        "website",
        "discord",
        "reset",
        "help"
    };

    @EventHandler
    public void onPlayerDoCommand(PlayerCommandPreprocessEvent event) {
        if (event.getPlayer().isOp()) return;
        String command = event.getMessage().replaceFirst("/", "").split("\\s")[0];
        if (new ArrayList<String>(Arrays.asList(whitelisted)).contains(command)) {
            if (command.equals("help")) {
                event.setCancelled(true);
                Bukkit.dispatchCommand(event.getPlayer(), "guide");
            }
        } else {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED+"You aren't allowed to use this command.");
        }
    }

}
