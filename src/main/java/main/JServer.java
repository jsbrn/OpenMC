package main;

import commands.MarkCommand;
import commands.UnmarkCommand;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class JServer extends JavaPlugin {

    private PluginManager pluginManager = getServer().getPluginManager();
    public static World OVERWORLD, NETHER, THE_END;
    public static Server SERVER;
    public static Configuration CONFIG;

    @Override
    public void onEnable() {

        OVERWORLD = getServer().getWorld("world");
        NETHER = getServer().getWorld("world_nether");
        THE_END = getServer().getWorld("world_the_end");
        SERVER = getServer();

        this.getCommand("mark").setExecutor(new MarkCommand());
        this.getCommand("unmark").setExecutor(new UnmarkCommand());

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dmarker deleteset Markers");

        startRecurringEvents();
    }

    @Override
    public void onDisable() {}

    private void startRecurringEvents() {

        /*TimerTask reputationFromPlaying = new TimerTask() {
            @Override
            public void run() {
                for (Player p: SERVER.getOnlinePlayers().toArray(new Player[]{})) {
                    PlayerData pd = DataStore.getPlayerData(p.getUniqueId());
                    pd.addReputation(1, "for actively playing");
                }
            }
        };
        SERVER.getScheduler().runTaskTimer(this, reputationFromPlaying, 0, MiscMath.secondsToTicks(60*20)); //20 minutes*/
    }

}
