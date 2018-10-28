package commands;

import io.DataStore;
import io.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddCommand implements CommandExecutor {

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            commandSender.sendMessage("Sorry, this is a developer command.");
            return true;
        }
        if (strings.length < 3) return false;
        int amount = Integer.parseInt(strings[2]);
        String playerName = strings[1], cmd = strings[0];
        PlayerData pd = DataStore.getPlayerData(playerName);
        if (pd == null) { commandSender.sendMessage(ChatColor.RED+"Could not find a player by that name!"); return false; }
        if (cmd.contains("time")) {
            String message = playerName+" now has "+pd.getMinutesRemaining()+" minutes of play time.";
            pd.addTime(amount);
            commandSender.sendMessage(message);
            System.out.println(message);
            if (amount > 0) {
                pd.sendMessage(ChatColor.GREEN+"You have received "+(amount/60)+" hours of play time.");
            } else if (amount < 0) {
                pd.sendMessage(ChatColor.RED+"You have lost "+(-amount/60)+" hours of play time.");
            }
        }
        return true;
    }
}
