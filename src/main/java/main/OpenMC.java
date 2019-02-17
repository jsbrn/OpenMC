package main;

import commands.*;
import events.*;
import events.protection.CapitalProtector;
import events.protection.PlotProtector;
import io.DataConverter;
import io.DataStore;
import io.PlayerData;
import misc.MiscMath;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.TimerTask;

public class OpenMC extends JavaPlugin {

    private PluginManager pluginManager = getServer().getPluginManager();
    public static World OVERWORLD, NETHER, THE_END;
    public static Server SERVER;
    public static Location CAPITAL;
    public static int SPAWN_VILLAGE_RADIUS = 64;
    public static String WEBSITE_URL = "https://openmc.net";
    public static Configuration CONFIG;

    @Override
    public void onEnable() {

        OVERWORLD = getServer().getWorld("world");
        NETHER = getServer().getWorld("world_nether");
        THE_END = getServer().getWorld("world_the_end");
        CAPITAL = new Location(OVERWORLD, -629, 64, 288);
        SERVER = getServer();
        OVERWORLD.setSpawnLocation(CAPITAL);
        CONFIG = this.getConfig();

        CONFIG.addDefault("api_key", "");

        pluginManager.registerEvents(new CapitalProtector(), this);
        pluginManager.registerEvents(new PlotProtector(), this);
        pluginManager.registerEvents(new PlayerJoinListener(), this);
        pluginManager.registerEvents(new PlayerRespawnListener(), this);
        pluginManager.registerEvents(new SignInteractListener(), this);
        pluginManager.registerEvents(new ChatListener(), this);
        pluginManager.registerEvents(new PlayerSleepListener(), this);
        pluginManager.registerEvents(new CommandOverrider(), this);
        this.getCommand("guide").setExecutor(new GuideCommand());
        this.getCommand("set").setExecutor(new SetCommand());
        this.getCommand("add").setExecutor(new AddCommand());
        this.getCommand("plots").setExecutor(new PlotsCommand());

        DataConverter.register();
        DataStore.backup();
        DataStore.load();
        DataStore.save();
        startRecurringEvents();
    }

    @Override
    public void onDisable() {
        DataStore.save();
    }

    private void startRecurringEvents() {

        TimerTask reputationFromPlaying = new TimerTask() {
            @Override
            public void run() {
                for (Player p: SERVER.getOnlinePlayers().toArray(new Player[]{})) {
                    PlayerData pd = DataStore.getPlayerData(p.getUniqueId());
                    pd.addReputation(1, "for actively playing");
                }
            }
        };
        SERVER.getScheduler().runTaskTimer(this, reputationFromPlaying, 0, MiscMath.secondsToTicks(60*20)); //20 minutes
    }

}
