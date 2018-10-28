package misc;

import io.PlayerData;
import main.OpenMC;
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

    public static void giveSpawnItems(Player p) {
        giveServerGuide(p);
        ItemStack kompass = new ItemStack(Material.COMPASS);
        p.getInventory().addItem(kompass);
        ItemStack banner = new ItemStack(Material.WHITE_BANNER);
        p.getInventory().addItem(banner);
        ItemStack emeralds = new ItemStack(Material.EMERALD, 16);
        p.getInventory().addItem(emeralds);
    }

    public static void giveServerGuide(Player p) {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        bookMeta.setTitle("Server Guide");
        bookMeta.setAuthor("OpenMC");
        bookMeta.addPage(

        ChatColor.BOLD+"Welcome to OpenMC!\n\n"+ChatColor.RESET+
                "This server aims to be a high-quality Minecraft experience for solo players or groups of friends." +
                "\n\nCONTENTS:\n  1. Quick Start\n  2. Features\n  3. Commands\n  4. More Info",

                ChatColor.BOLD+"Quick Start\n\n"+ChatColor.RESET+
                        "1. Place a banner to claim and protect your land\n\n" +
                        "2. Befriend people\n\n" +
                        "3. Sell goods and buy more land at the Capital (just follow the compass)\n\n",

                ChatColor.BOLD+"Features\n\n" + ChatColor.RESET +
                        "- randomized spawns\n"+
                        "- place banners to claim land plots\n" +
                        "- invite friends to your plots\n" +
                        "- local/group chat\n" +
                        "- player reputation\n"+
                        "- buy land in the Capital city\n",

                ChatColor.BOLD+"Commands\n\n" + ChatColor.RESET +
                        "/guide (read the Guide again)\n"+
                        "/plots (show your plots)\n"+
                        "/invite (invite someone to your group)\n"+
                        "/leave (leave your group)\n"+
                        "/global (toggle global chat)",

                ChatColor.ITALIC+"(continued)\n\n" + ChatColor.RESET +
                        "/stuck (if you can't get out of a hole)\n"+
                        "/reset (START OVER AS NEW PLAYER)\n",

                ChatColor.BOLD+"More Info\n\n"+ChatColor.RESET+
                        ChatColor.ITALIC+"Donations\n\n"+ChatColor.RESET+
                        "There are no micro-transactions in OpenMC. Instead, the server is pay-as-you-play. \n\n"+
                        "This solves two problems:",

                "First, players will be far less likely to cause trouble.\n\nSecond, " +
                        "if someone doesn't play for a long time, it does not cost them anything.",

                "You can donate at "+ChatColor.DARK_AQUA+OpenMC.WEBSITE_URL+ChatColor.BLACK+". " +
                        "For every dollar you contribute, you get 24 hours of play time.\n\nNew players start with 24.",

                ChatColor.ITALIC+"Reputation\n\n"+ChatColor.RESET+
                        "Griefing, killing or stealing from other players will give you a bad reputation. " +
                        "When reputation drops below -50, you will be banned for two weeks. " +
                        "This will not affect your available play time.",

                ChatColor.ITALIC+"That's all!\n\n"+ChatColor.RESET+
                        "I hope you enjoy playing on OpenMC. Please visit "+ChatColor.DARK_AQUA+OpenMC.WEBSITE_URL+"/contact"+ChatColor.BLACK+" to leave feedback if you have any issues."

        );
        book.setItemMeta(bookMeta);
        p.getInventory().addItem(book);
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
