package misc;

import io.FileLoader;
import io.PlayerData;
import main.OpenMC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MiscCommands {

    public static void sendMessage(String message, ArrayList<PlayerData> players) {
        for (PlayerData pd: players) pd.sendMessage(message);
    }

    private static String guide;
    public static void giveServerGuide(Player p) {
        if (guide == null) guide = ChatColor.translateAlternateColorCodes('§', FileLoader.getContents("/books/guide.txt", false))
                .replaceAll("[^ -~§\\n]", "");
        String[] pages = guide.split("\\n==========================\\n\\n");
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta)book.getItemMeta();
        meta.setAuthor("OpenMC");
        meta.setTitle("Server Guide");

        ArrayList<String> lore = new ArrayList<String>();
        lore.add("Read this to know what's going on.");

        meta.setLore(lore);
        //meta.addPage("This is a test.");
        for (String page: pages) {
            System.out.println("_=_=_=_NEW PAGE_=_=_=_");
            System.out.println(page);
            meta.addPage(page);
        }
        book.setItemMeta(meta);
        p.getInventory().addItem(book);
    }

    public static void giveSpawnItems(Player p) {
        giveServerGuide(p);
        ItemStack kompass = new ItemStack(Material.COMPASS);
        p.getInventory().addItem(kompass);
        ItemStack banner = new ItemStack(Material.WHITE_BANNER);
        p.getInventory().addItem(banner);
        ItemStack emeralds = new ItemStack(Material.EMERALD, 16);
        p.getInventory().addItem(emeralds);
    }

    public static Location randomSpawn(int minDist, int maxDist) {
        //p.sendMessage(ChatColor.YELLOW+"Hold on tight...");
        Location newSpawn;
        Random r = new Random();
        while (true) {
            int rx = (r.nextInt() % (maxDist-minDist)) + minDist + OpenMC.SPAWN_VILLAGE_RADIUS;
            int rz = (r.nextInt() % (maxDist-minDist)) + minDist + OpenMC.SPAWN_VILLAGE_RADIUS;
            Biome biome = OpenMC.OVERWORLD.getBiome(rx, rz);
            newSpawn = new Location(OpenMC.OVERWORLD, rx, OpenMC.OVERWORLD.getHighestBlockYAt(rx, rz) + 2, rz);
            if (!biome.name().toLowerCase().contains("ocean")) break;
        }
        return newSpawn;
    }

    public static void printMap(HashMap<String, String> map) {
        System.out.println("Outputting map: ");
        for (String key: map.keySet())
            System.out.println(key +"->"+ map.get(key));
    }

}
