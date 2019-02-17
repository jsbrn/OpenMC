package controllers;

import io.DataStore;
import io.PlayerData;
import io.Plot;
import org.bukkit.Bukkit;

public class DynmapController {

    private static String PLOTS = "Plots";
    private static String WORLD = "world";

    public static void markPlot(Plot p) {
        PlayerData owner = DataStore.getPlayerData(p.owner().getUniqueId());
        String cmd = "dmarker addcircle \""+owner.getOfflinePlayer().getName()+"'s plot\"id:"+(p.owner().getUniqueId()+"_"+p.center().getBlockX()+"_"+p.center().getBlockY()+"_"+p.center().getBlockZ())+" set:"+PLOTS
                +" x:"+p.center().getBlockX()+" y:"+p.center().getBlockY()+" z:"+p.center().getBlockZ()+" world:"+WORLD
                +" radius:"+Plot.RADIUS +" color:"+owner.getColor()+" fillcolor:"+owner.getColor()+" weight:1";
        owner.sendMessage(cmd);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
    }

    public static void removePlot(Plot p) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dmarker deletecircle" +
                " id:"+(p.owner().getUniqueId()+"_"+p.center().getBlockX()+"_"+p.center().getBlockY()+"_"+p.center().getBlockZ())+" set:"+PLOTS);
    }

}
