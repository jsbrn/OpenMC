package main;

import commands.*;
import controllers.WebsocketController;
import events.*;
import events.admin.MagicWandListener;
import events.protection.ReputationListener;
import events.protection.SpawnVillageProtector;
import events.protection.PlotProtector;
import io.DataConverter;
import io.DataStore;
import io.PlayerData;
import misc.MiscMath;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.TimerTask;

public class OpenMC extends JavaPlugin {

    private PluginManager pluginManager = getServer().getPluginManager();
    public static World OVERWORLD, NETHER, THE_END;
    public static Server SERVER;
    public static Location SPAWN_VILLAGE;
    public static int SPAWN_VILLAGE_RADIUS = 64;
    public static String WEBSITE_URL = "192.168.0.199";

    @Override
    public void onEnable() {

        OVERWORLD = getServer().getWorld("world");
        NETHER = getServer().getWorld("world_nether");
        THE_END = getServer().getWorld("world_the_end");
        SPAWN_VILLAGE = new Location(OVERWORLD, -40, 70, -4);
        SERVER = getServer();
        OVERWORLD.setSpawnLocation(SPAWN_VILLAGE);

        pluginManager.registerEvents(new SpawnVillageProtector(), this);
        pluginManager.registerEvents(new PlotProtector(), this);
        pluginManager.registerEvents(new ReputationListener(), this);
        pluginManager.registerEvents(new PlayerJoinListener(), this);
        pluginManager.registerEvents(new PlayerRespawnListener(), this);
        pluginManager.registerEvents(new MagicWandListener(), this);
        pluginManager.registerEvents(new SignInteractListener(), this);
        pluginManager.registerEvents(new ChatListener(), this);
        pluginManager.registerEvents(new PlayerSleepListener(), this);
        this.getCommand("guide").setExecutor(new ServerGuideCommand());
        this.getCommand("set").setExecutor(new SetCommand());
        this.getCommand("invite").setExecutor(new InviteCommand());
        this.getCommand("accept").setExecutor(new AcceptCommand());
        this.getCommand("leave").setExecutor(new LeaveCommand());

        DataConverter.register();
        DataStore.backup();
        DataStore.load();
        DataStore.save();
        startRecurringEvents();
        WebsocketController.connect("localhost");
    }

    @Override
    public void onDisable() {
        WebsocketController.disconnect();
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