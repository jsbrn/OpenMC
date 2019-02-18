package commands;

import io.DataStore;
import io.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DepositCommand implements CommandExecutor {

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] arguments) {
        if (commandSender instanceof Player) {
            Player p = (Player)commandSender;
            PlayerData pd = DataStore.getPlayerData(p.getUniqueId());
            try {
                int amount = Integer.parseInt(arguments[0]);
                if (p.getInventory().containsAtLeast(new ItemStack(Material.EMERALD), amount)) {
                    pd.addEmeralds(amount);
                    p.getInventory().removeItem(new ItemStack(Material.EMERALD, amount));
                    pd.sendMessage(ChatColor.GREEN+"You have deposited "+amount+" emeralds.");
                    pd.refreshScoreboard();
                } else {
                    pd.sendMessage(ChatColor.RED+"You do not have "+amount+" emeralds in your inventory!");
                }
                return true;
            } catch (NumberFormatException e) {
                pd.sendMessage(ChatColor.RED+"\""+arguments[0]+"\" is not a number!");
            }
        }
        return false;
    }

}
