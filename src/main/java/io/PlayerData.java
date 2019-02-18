package io;

import main.OpenMC;
import misc.MiscMath;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class PlayerData {

    private UUID playerID;
    private Location spawn;
    private int maxPlots, emeralds;
    private long lastActivity, firstJoin, lastJoin;
    private String addons;

    private Scoreboard scoreboard;
    private boolean showScoreboard;

    public PlayerData(UUID playerID) {
        this.playerID = playerID;
        this.maxPlots = 1;
        this.firstJoin = System.currentTimeMillis();
        this.lastActivity = System.currentTimeMillis();
        this.lastJoin = System.currentTimeMillis();
        this.addons = "";
        this.showScoreboard = true;
    }

    public void createScoreboard() {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("Player Stats", "Criteria",
                ChatColor.YELLOW + getPlayer().getDisplayName());
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.getScore(ChatColor.GREEN + "Emeralds:").setScore(emeralds);
        objective.getScore("Plots:").setScore(plotCount());
        refreshScoreboard();
    }

    public void refreshScoreboard() {
        if (showScoreboard) {
            scoreboard.getObjective("Player Stats").getScore(ChatColor.GREEN + "Emeralds:").setScore(emeralds);
            scoreboard.getObjective("Player Stats").getScore("Plots:").setScore(plotCount());
            getPlayer().setScoreboard(scoreboard);
        } else {
            getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }
    }

    public void toggleScoreboard() {
        showScoreboard = !showScoreboard;
        refreshScoreboard();
    }

    public void addEmeralds(int amount) { setEmeralds(emeralds + amount); }
    public void setEmeralds(int amount) { emeralds = (int)MiscMath.clamp(amount, 0, Integer.MAX_VALUE); }
    public int getEmeralds() { return emeralds; }

    public String getHexCode() {
        int random = new Random(playerID.toString().hashCode()).nextInt();
        String hexColor = String.format("#%06X", (0xFFFFFF & random));
        return hexColor.replace("#", "");
    }

    public OfflinePlayer getOfflinePlayer() { return OpenMC.SERVER.getOfflinePlayer(playerID); }
    public Player getPlayer() { return getOfflinePlayer().getPlayer(); }
    public Location getSpawn() { return spawn; }
    public void setSpawn(Location loc) { spawn = loc; }
    public Location getRespawnLocation() {
        //first bed then spawn then capital
        if (getOfflinePlayer().getBedSpawnLocation() != null) return getOfflinePlayer().getBedSpawnLocation();
        if (getSpawn() != null) return getSpawn();
        return OpenMC.CAPITAL;
    }

    public boolean canClaimLand() {
        int owned = DataStore.getPlots(playerID).size();
        return owned < maxPlots;
    }

    public void addMaxPlot(int amount) { maxPlots += amount; }
    public int getMaxPlots() { return maxPlots; }
    public int plotCount() { return DataStore.getPlots(playerID).size(); }

    public void addAddon(String addonCode) { addons = addons + addonCode; }
    public boolean hasAddon(String addonCode) { return addons.contains(addonCode); }

    public void sendMessage(String message) {
        if (getPlayer() != null && message != null)
            if (message.length() > 0)
                getPlayer().sendMessage(message);
    }

    public void sendTitle(String title, String subtitle, int seconds) {
        if (getPlayer() != null)
            getPlayer().sendTitle(title, subtitle, 0, MiscMath.secondsToTicks(seconds), 0);
    }

    public void sendActionText(String message, ChatColor color) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                "title " + getOfflinePlayer().getName() + " actionbar {\"text\":\"" + message + "\",\"color\":\"" + color.name().toLowerCase() + "\"}");
    }

    public void updateLastActivity() { lastActivity = System.currentTimeMillis(); }
    public int awayTime() { return (int) ((System.currentTimeMillis() - lastActivity) / 1000 / 60); }

    public boolean isOnline() { return getPlayer() != null ? getPlayer().isOnline() : false; }
    public void updateLastLogin() { lastJoin = System.currentTimeMillis(); }

    public boolean save(PrintWriter pw) {
        pw.println(asString());
        return true;
    }

    public boolean load(HashMap<String, String> data) {
        if (data == null) return false;
        firstJoin = Long.parseLong(data.getOrDefault("first_join", System.currentTimeMillis()+""));
        lastJoin = Long.parseLong(data.getOrDefault("last_join", System.currentTimeMillis()+""));
        spawn = new Location(OpenMC.OVERWORLD,
                        Integer.parseInt(data.getOrDefault("spawn_x", OpenMC.CAPITAL.getBlockX()+"")),
                        Integer.parseInt(data.getOrDefault("spawn_y", OpenMC.CAPITAL.getBlockY()+"")),
                        Integer.parseInt(data.getOrDefault("spawn_z", OpenMC.CAPITAL.getBlockZ()+"")));
        addons = data.getOrDefault("addons", "");
        maxPlots = Integer.parseInt(data.getOrDefault("max_plots", "1"));
        emeralds = Integer.parseInt(data.getOrDefault("emeralds", "0"));
        showScoreboard = Boolean.parseBoolean(data.getOrDefault("show_scoreboard", "true"));
        return true;
    }

    public String asString() {
        String saveString = "type = player, uuid = "+playerID+", last_join = "+lastJoin+", first_join = "+firstJoin+", addons = "+ addons
                +", max_plots = "+maxPlots+", emeralds = "+emeralds+", show_scoreboard = "+showScoreboard;
        if (getSpawn() != null) saveString += ", spawn_x = "+getSpawn().getBlockX()
                +", spawn_y = "+getSpawn().getBlockY()
                +", spawn_z = "+getSpawn().getBlockZ();
        return saveString;
    }

}
