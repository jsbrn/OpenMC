package commands;

import io.DataStore;
import io.PlayerData;
import misc.MiscCommands;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class WithdrawCommand implements CommandExecutor {

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] arguments) {
        if (commandSender instanceof Player) {
            Player p = (Player)commandSender;
            PlayerData pd = DataStore.getPlayerData(p.getUniqueId());
            try {
                int amount = Integer.parseInt(arguments[0]);
                if (pd.getEmeralds() >= amount) {
                    HashMap<Integer, ItemStack> leftovers = p.getInventory().addItem(new ItemStack(Material.EMERALD, amount));
                    //don't withdraw the ones that do not fit
                    int leftoverAmount = 0;
                    for (ItemStack stack: leftovers.values()) leftoverAmount += stack.getAmount();
                    pd.sendMessage(ChatColor.GREEN+"You have withdrawn "+(amount-leftoverAmount)+" emeralds.");
                    pd.addEmeralds(-(amount-leftoverAmount));
                    pd.refreshScoreboard();
                } else {
                    pd.sendMessage(ChatColor.RED+"You only have "+pd.getEmeralds()+" emeralds!");
                }
                return true;
            } catch (NumberFormatException e) {
                pd.sendMessage(ChatColor.RED+"\""+arguments[0]+"\" is not a number!");
                return true;
            }
        }
        return false;
    }

}
