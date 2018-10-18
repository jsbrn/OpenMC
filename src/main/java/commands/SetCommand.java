package commands;

import io.DataStore;
import io.PlayerData;
import misc.MiscMath;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetCommand implements CommandExecutor {

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length < 3) return false;
        int amount = Integer.parseInt(strings[2]);
        String playerName = strings[1], cmd = strings[0];
        PlayerData pd = DataStore.getPlayerData(playerName);
        if (pd == null) { commandSender.sendMessage(ChatColor.RED+"Could not find a player by that name!"); return false; }
        if (cmd.contains("rep")) {
            pd.setReputation(amount, false);
            commandSender.sendMessage(playerName+"'s reputation is now "+pd.getReputation()+".");
        } else if (cmd.contains("time")) {
            pd.addTime(-pd.getMinutesRemaining());
            pd.addTime(amount);
            commandSender.sendMessage(playerName+" now has "+pd.getMinutesRemaining()+" minutes of play time.");
        }
        return true;
    }

}
