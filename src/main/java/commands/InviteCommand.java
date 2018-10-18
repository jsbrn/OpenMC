package commands;

import io.DataStore;
import io.Group;
import io.PlayerData;
import io.Plot;
import main.OpenMC;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class InviteCommand implements CommandExecutor {

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            if (args.length != 1) return false;
            Player inviter = (Player)commandSender;
            Player invited = OpenMC.SERVER.getPlayer(args[0]);

            if (invited != null) {
                Group inviterGroup = DataStore.getGroup(inviter.getUniqueId());
                if (inviterGroup == null) inviterGroup = DataStore.createGroup(inviter.getUniqueId());
                PlayerData pd = DataStore.getPlayerData(invited.getUniqueId());
                if (!pd.inGroupWith(inviter.getUniqueId())) {
                    if (inviterGroup.isOwnedBy(inviter.getUniqueId())) {
                        if (pd.inviteToGroup(inviter.getUniqueId())) {
                            commandSender.sendMessage(ChatColor.YELLOW+invited.getName()+" has been invited to join your group.");
                            pd.sendMessage(ChatColor.YELLOW+inviter.getName()+" has invited you to join them!");
                        } else {
                            commandSender.sendMessage(ChatColor.RED+invited.getName()+" is already invited.");
                        }
                    } else {
                        commandSender.sendMessage(ChatColor.RED+"You can't invite people to someone else's group!");
                    }
                } else {
                    commandSender.sendMessage(ChatColor.RED+invited.getName()+" is already in a group with you!");
                }

            } else {
                commandSender.sendMessage(ChatColor.RED+args[0]+" is not online.");
            }
            return true;
        }
        return false;
    }

}
