package io;

import main.OpenMC;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;

import java.io.PrintWriter;
import java.util.UUID;

public class Plot {

    private Location center, origin, origin2;
    private UUID owner;
    public static int RADIUS = 25;
    private Material banner_type;

    public Plot(UUID owner, Location loc) {
        this.origin = new Location(OpenMC.OVERWORLD, loc.getBlockX() - RADIUS, loc.getBlockY() + RADIUS, loc.getBlockZ() - RADIUS);
        this.origin2 = new Location(OpenMC.OVERWORLD, loc.getBlockX() + RADIUS, loc.getBlockY() - RADIUS, loc.getBlockZ() + RADIUS);
        this.center = loc;
        this.banner_type = center.getBlock().getBlockData().getAsString().contains("banner")
                ? center.getBlock().getType() : Material.WHITE_BANNER;
        this.owner = owner;
    }

    public Location origin() { return origin; }
    public Location center() { return center; }

    public OfflinePlayer owner() { return OpenMC.SERVER.getOfflinePlayer(owner); }
    public void setOwner(UUID uuid) { this.owner = uuid; }

    public boolean intersects(Location l) {
        boolean intersects = origin.getBlockX() < l.getBlockX() && origin2.getBlockX() > l.getBlockX() &&
                origin.getBlockY() > l.getBlockY() && origin2.getBlockY() < l.getBlockY() &&
                origin.getBlockZ() < l.getBlockZ() && origin2.getBlockZ() > l.getBlockZ();
        return intersects && l.getWorld().equals(origin.getWorld());
    }

    public boolean ownedBy(UUID playerID) { return playerID.compareTo(owner) == 0; }

    public int distanceTo(Location loc) { return (int)loc.distance(center); }
    public boolean isTooClose(Location loc) { return distanceTo(loc) <= RADIUS * 2; }
    public boolean isForeignTo(UUID playerID) { return !ownedBy(playerID) && !DataStore.getPlayerData(owner).isFriendsWith(playerID); }

    public boolean isBannerBroken() {
        return !center().getBlock().getBlockData().getAsString().contains("banner");
    }
    public boolean repair() {
        if (isBannerBroken()) { center().getBlock().setType(banner_type); return true; }
        return false;
    }

    @Override
    public String toString() {
        return "["+origin.getBlockX()+", "+origin.getBlockY()+", "+origin.getBlockZ()+"] -> ["
                +origin2.getBlockX()+", "+origin2.getBlockY()+", "+origin2.getBlockZ()+"]";
    }

    public boolean save(PrintWriter pw) {
        String saveStr = "type = plot, owner = "+owner+", x = "+center.getBlockX()+", y = "+center.getBlockY()+", z = "+center.getBlockZ();
        pw.println(saveStr);
        return true;
    }

}
