package commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnmarkCommand implements CommandExecutor {

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            Player p = (Player)commandSender;

            String name = "";
            for (int i = 0; i < args.length; i++) name += args[i]+ " ";
            name = name.replaceAll("[^A-Za-z0-9,#()!?$&*@ ]", "");
            String label = name;

            //p.sendMessage(ChatColor.YELLOW + name + " " + ChatColor.RED + label);

            String cmd = "dmarker delete " +
                    "label:\"" + label + "\" " +
                    "set:\"Points of Interest\" ";

            //p.sendMessage(cmd);
            Bukkit.dispatchCommand(p, cmd);
        }
        return true;
    }
}
