package commands;

import io.DataStore;
import io.Group;
import io.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCommand implements CommandExecutor {

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            Player acceptor = (Player)commandSender;
            PlayerData pd = DataStore.getPlayerData(acceptor.getUniqueId());
            Group current = DataStore.getGroup(acceptor.getUniqueId());
            if (current == null) {
                acceptor.sendMessage(ChatColor.RED+"You are not in a group!");
            } else if (current.getMembers().size() <= 1) {
                acceptor.sendMessage(ChatColor.RED+"You are not in a group!");
            } else if (current.isOwnedBy(acceptor.getUniqueId())) {
                acceptor.sendMessage(ChatColor.RED+"You have left your group.");
                current.getOwner();
            } else {
                current.removeMember(acceptor.getUniqueId());
                PlayerData owner = current.getOwner();
                acceptor.sendMessage(ChatColor.YELLOW+"You have left "+owner.getOfflinePlayer().getName()+"'s group.");
            }
        }
        return false;
    }

}
