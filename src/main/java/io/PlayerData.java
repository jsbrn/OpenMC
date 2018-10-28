package io;

import main.OpenMC;
import misc.MiscMath;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PlayerData {

    private UUID playerID;
    private Location spawn;
    private int reputation, banCount, maxPlots;
    private long lastWarning, lastActivity, lastTeleport, lastJoin, timeRemaining;

    private ArrayList<UUID> friends;

    public PlayerData(UUID playerID) {
        this.playerID = playerID;
        this.reputation = 0;
        this.lastWarning = 0;
        this.maxPlots = 1;
        this.lastActivity = System.currentTimeMillis();
        this.lastJoin = System.currentTimeMillis();
        this.addTime(60*10);
        this.friends = new ArrayList<UUID>();
    }

    public OfflinePlayer getOfflinePlayer() {
        return OpenMC.SERVER.getOfflinePlayer(playerID);
    }
    public Player getPlayer() {
        return getOfflinePlayer().getPlayer();
    }
    public Location getRespawnLocation() {
        //first bed then spawn then capital
        if (getOfflinePlayer().getBedSpawnLocation() != null) return getOfflinePlayer().getBedSpawnLocation();
        if (getSpawn() != null) return getSpawn();
        return OpenMC.CAPITAL;
    }
    public Location getSpawn() { return spawn; }
    public void setSpawn(Location loc) { spawn = loc; }

    public boolean canClaimLand() {
        int owned = DataStore.getPlots(playerID).size();
        return owned < maxPlots;
    }

    public boolean isFriendsWith(UUID id) { return friends.contains(id); }
    public boolean addFriend(UUID id) { if (friends.contains(id)) return false; else { friends.add(id); return true; } }
    public boolean removeFriend(UUID id) {
        if (friends.contains(id)) {
            friends.remove(id);
            return true;
        }
        return false;
    }

    public int getReputation() {
        return reputation;
    }
    public void addReputation(int amount, String reason) {
        if (amount < 0 && reputation > 0) reputation = 0;
        if (reputation <= -100 && amount < 0) {
            ban();
            return;
        }
        sendMessage((amount < 0 ? ChatColor.RED : ChatColor.GREEN) + (amount > 0 ? "+" : "") + amount + " reputation "
                + (reason != null ? "(" + reason + ")" : ""));
        setReputation(reputation + amount, amount < 0 && reputation + amount < 0);
        if (reputation <= -100 && amount < 0)
            sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "You will be banned on the next offense.");
    }

    public void setReputation(int amount, boolean showReputation) {
        reputation = (int) MiscMath.clamp(amount, -100, 100);
        if (showReputation) sendTitle("", ChatColor.RED + "Reputation: " + reputation, 3);
        if (amount < 0) setWarned(); //if the rep is going down, you were probably warned
    }

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

    public int getBanCount() {
        return banCount;
    }
    public void ban() {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                "ban " + getOfflinePlayer().getName() + " §cYour reputation is " + reputation + "!");
        reputation = 0;
        banCount++;
    }

    public boolean warnedRecently(int seconds) {
        return System.currentTimeMillis() - lastWarning < (seconds * 1000);
    }
    public void setWarned() {
        lastWarning = System.currentTimeMillis();
    }

    public void updateLastTeleport() { lastTeleport = System.currentTimeMillis(); }
    public long timeSinceLastTeleport() { return System.currentTimeMillis() - lastTeleport; }
    public void updateLastActivity() {
        lastActivity = System.currentTimeMillis();
    }
    public int awayTime() {
        return (int) ((System.currentTimeMillis() - lastActivity) / 1000 / 60);
    }

    public boolean isOnline() { return getPlayer() != null ? getPlayer().isOnline() : false; }
    public void updateLastLogin() { lastJoin = System.currentTimeMillis(); }

    public void adjustTimeRemaining() { timeRemaining -= System.currentTimeMillis() - lastJoin; }
    public void addTime(int minutes) { timeRemaining += minutes*60*1000; }
    public void setTime(int minutes) { timeRemaining = minutes*60*1000; }
    public long getTimeRemaining() { return (long)MiscMath.clamp(timeRemaining, 0, Long.MAX_VALUE); }
    public int getMinutesRemaining() { return (int)Math.ceil(timeRemaining / 60 / 1000); }
    public int getHoursRemaining() { return getMinutesRemaining() / 60; }

    public boolean save(PrintWriter pw) {
        pw.println(asString());
        return true;
    }

    public boolean load(HashMap<String, String> data) {
        if (data == null) return false;
        lastJoin = Long.parseLong(data.getOrDefault("last_join", System.currentTimeMillis()+""));
        timeRemaining = Long.parseLong(data.getOrDefault("play_time", timeRemaining+""));
        reputation = Integer.parseInt(data.getOrDefault("rep", "0"));
        banCount = Integer.parseInt(data.getOrDefault("bans", "0"));
        spawn = new Location(OpenMC.OVERWORLD,
                        Integer.parseInt(data.getOrDefault("spawn_x", OpenMC.CAPITAL.getBlockX()+"")),
                        Integer.parseInt(data.getOrDefault("spawn_y", OpenMC.CAPITAL.getBlockY()+"")),
                        Integer.parseInt(data.getOrDefault("spawn_z", OpenMC.CAPITAL.getBlockZ()+"")));
        return true;
    }

    public String asString() {
        String saveString = "type = player, uuid = "+playerID+", rep = "+reputation+", bans = "+banCount
                +", play_time = "+timeRemaining+", last_join = "+lastJoin;
        if (getSpawn() != null) saveString += ", spawn_x = "+getSpawn().getBlockX()
                +", spawn_y = "+getSpawn().getBlockY()
                +", spawn_z = "+getSpawn().getBlockZ();
        return saveString;
    }

}
