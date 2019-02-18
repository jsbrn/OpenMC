package commands.admin;

import io.DataStore;
import io.PlayerData;
import misc.MiscMath;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetCommand implements CommandExecutor {

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] arguments) {
        if (commandSender instanceof Player) {
            commandSender.sendMessage("Sorry, this is a developer command.");
            return true;
        }
        if (arguments.length < 3) return false;
        int amount = Integer.parseInt(arguments[2]);
        String playerName = arguments[1], cmd = arguments[0];
        PlayerData pd = DataStore.getPlayerData(playerName);
        if (pd == null) { commandSender.sendMessage(ChatColor.RED+"Could not find a player by that name!"); return false; }
        return true;
    }

}
