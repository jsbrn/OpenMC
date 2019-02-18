package commands;

import io.DataStore;
import io.PlayerData;
import javafx.scene.chart.PieChart;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleLeaderboardCommand implements CommandExecutor {

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player p = (Player)commandSender;
            PlayerData pd = DataStore.getPlayerData(p.getUniqueId());
            pd.toggleScoreboard();
        }
        return true;
    }
}
