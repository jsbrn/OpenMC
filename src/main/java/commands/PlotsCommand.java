package commands;

import io.DataStore;
import io.PlayerData;
import io.Plot;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PlotsCommand implements CommandExecutor {

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You can only use this command as a player.");
            return true;
        }
        PlayerData pd = DataStore.getPlayerData(((Player) commandSender).getUniqueId());
        ArrayList<Plot> plots = DataStore.getPlots(((Player) commandSender).getUniqueId());
        commandSender.sendMessage(ChatColor.YELLOW+"You have "+(plots.size() == 0 ? "no" : plots.size())
                +" plot"+(plots.size() == 1 ? "": "s")
                +(plots.size() > 0 ? ":" : "."));
        for (int i = 0; i < plots.size(); i++){
            Plot p = plots.get(i);
            commandSender.sendMessage((i+1)+". ("+p.center().getBlockX()+", "+p.center().getBlockY()+", "+p.center().getBlockZ()+")");
        }
        return true;
    }
}
