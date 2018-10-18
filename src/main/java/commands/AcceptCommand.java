package commands;

import io.DataStore;
import io.Group;
import io.PlayerData;
import javafx.scene.chart.PieChart;
import main.OpenMC;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AcceptCommand implements CommandExecutor {

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            if (args.length != 1) return false;
            Player acceptor = (Player)commandSender;
            PlayerData inviter = DataStore.getPlayerData(args[0]);
            PlayerData acceptorPD = DataStore.getPlayerData(acceptor.getUniqueId());

            if (inviter != null) {
                if (!inviter.inGroupWith(acceptor.getUniqueId())) {
                    if (!acceptorPD.isOwnerOfGroup()) {
                        if (DataStore.getPlayerData(acceptor.getUniqueId())
                                .acceptInvitation(inviter.getOfflinePlayer().getUniqueId())) {
                            commandSender.sendMessage(ChatColor.GREEN + "You accepted " + args[0] + "'s invitation!");
                            inviter.sendMessage(ChatColor.GREEN + acceptor.getName() + " accepted your invitation!");
                        } else {
                            commandSender.sendMessage(ChatColor.RED + args[0] + " hasn't invited you to a group.");
                        }
                    } else {
                        commandSender.sendMessage(ChatColor.RED + "You can't leave your own group. Leave before");
                    }
                } else {
                    commandSender.sendMessage(ChatColor.RED+"You are already in a group with "+args[0]+"!");
                }
            } else {
                commandSender.sendMessage(ChatColor.RED+"Could not find a player by that name.");
            }
            return true;
        }
        return false;
    }

}
