package io;

import controllers.DynmapController;
import main.OpenMC;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Array;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class DataStore {

    private static ArrayList<Plot> PLOTS = new ArrayList<Plot>();
    private static ArrayList<PlayerData> PLAYER_DATA = new ArrayList<PlayerData>();
    private static ArrayList<PlayerData> ACTIVE_TRADES

    private static boolean safeToSave = false;

    private static PlayerData registerPlayer(UUID playerID) {
        PlayerData pd = new PlayerData(playerID);
        PLAYER_DATA.add(pd);
        return pd;
    }

    public static PlayerData getPlayerData(UUID playerID) {
        for (PlayerData pd : PLAYER_DATA)
            if (pd.getOfflinePlayer().getUniqueId().compareTo(playerID) == 0)
                return pd;
        return registerPlayer(playerID);
    }

    public static ArrayList<PlayerData> getPlayerData() {
        return PLAYER_DATA;
    }

    public static PlayerData getPlayerData(String playerName) {
        for (PlayerData pd: PLAYER_DATA)
            if (pd.getOfflinePlayer().getName().equalsIgnoreCase(playerName)) return pd;
        return null;
    }

    public static Plot addPlot(UUID playerID, Location loc) {
        if (getPlot(loc) == null) {
            Plot new_ = new Plot(playerID, loc);
            PLOTS.add(new_);
            return new_;
        } else {
            return null;
        }
    }

    public static boolean removePlot(Plot p) {
        PLOTS.remove(p);
        return true;
    }

    public static Plot getPlot(Location loc) {
        for (Plot p : PLOTS)
            if (p.intersects(loc)) {
                if (p.isBannerBroken()) p.repair();
                return p;
            }
        return null;
    }

    public static ArrayList<Plot> getPlots() {
        return PLOTS;
    }

    public static ArrayList<Plot> getPlotsTooClose(Location origin) {
        ArrayList<Plot> plots = new ArrayList<Plot>();
        for (Plot plot : PLOTS) if (plot.isTooClose(origin)) plots.add(plot);
        return plots;
    }

    public static ArrayList<Plot> getPlots(UUID playerID) {
        ArrayList<Plot> plots = new ArrayList<Plot>();
        for (Plot plot : PLOTS)
            if (plot.owner().getUniqueId().compareTo(playerID) == 0)
                plots.add(plot);
        return plots;
    }

    public static boolean save() {
        if (!safeToSave) return false;
        try {
            File f = new File("./plugins/OpenMC/data.txt");
            System.out.println("Writing to " + f.getAbsolutePath() + "...");
            if (!f.exists()) f.createNewFile();
            PrintWriter writer = new PrintWriter(f, "UTF-8");
            writer.println("type = meta, version = " + DataConverter.CURRENT_VERSION);
            for (Plot plot : DataStore.getPlots())
                plot.save(writer);
            for (PlayerData pd : DataStore.getPlayerData())
                pd.save(writer);
            writer.close();
            System.out.println("Successfully wrote to " + f.getAbsolutePath() + "!");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean load() {
        File folder = new File("./plugins/OpenMC/");
        folder.mkdir();
        File file = new File("./plugins/OpenMC/data.txt");
        System.out.println("Loading from " + file.getAbsolutePath());
        ArrayList<HashMap<String, String>> data = DataConverter.parse(file);
        if (data == null) return false;
        for (HashMap<String, String> line : data) {
            String type = line.get("type");
            if (type.equals("plot")) {
                DataStore.addPlot(
                        UUID.fromString(line.get("owner")),
                        new Location(OpenMC.OVERWORLD,
                                Integer.parseInt(line.get("x")),
                                Integer.parseInt(line.get("y")),
                                Integer.parseInt(line.get("z"))));
            } else if (type.equals("player")) {
                PlayerData loaded = DataStore.registerPlayer(UUID.fromString(line.get("uuid")));
                loaded.load(line);
            }
        }
        safeToSave = true;
        System.out.println("Successfully loaded " + file.getAbsolutePath() + "!");
        return true;
    }

    public static void backup() {
        File file = new File("./plugins/OpenMC/data.txt");
        if (file.exists()) {
            String dateTime = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            File folder = new File("./plugins/OpenMC/backups/");
            folder.mkdir();
            try {
                Files.copy(
                        file.toPath(),
                        new File("./plugins/OpenMC/backups/data_" + dateTime + ".txt").toPath(),
                        StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Backed up data.txt!");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to back up data.txt.");
            }
        }
    }

    public static void getContents(File file) {

    }

}
