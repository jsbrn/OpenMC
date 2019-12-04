package commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MarkCommand implements CommandExecutor {

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            Player p = (Player)commandSender;
            String icon = args[args.length-1];

            String name = "";
            for (int i = 0; i < args.length-1; i++) name += args[i]+ " ";
            name = name.replaceAll("[^A-Za-z0-9,#()!?$&*@ ]", "");

            String label = name;
            String id = name.replaceAll("[^A-Za-z0-9]", "");

            //p.sendMessage(ChatColor.YELLOW + name + " " + ChatColor.RED + label + " " + ChatColor.BLUE + id);
            String cmd = "dmarker add " +
                    "id:\"" + id + "\" " +
                    "label:\"" + label + "\" " +
                    "set:\"Points of Interest\" " +
                    "icon:" + icon;

            //p.sendMessage(cmd);
            Bukkit.dispatchCommand(p, cmd);
        }
        return true;
    }
}
