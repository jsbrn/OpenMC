package misc;

import io.DataStore;
import io.PlayerData;

public class Statistics {

    public static String asPacket() {
        PlayerData nicest = getNicestPlayer();
        PlayerData worst = getWorstPlayer();
        return nicest.getOfflinePlayer().getName()+" "+nicest.getReputation()
                +" "+worst.getOfflinePlayer().getName()+" "+worst.getReputation()+" "+getTopDonator();
    }

    public static PlayerData getNicestPlayer() {
        int highestRep = Integer.MIN_VALUE;
        PlayerData nicest = null;
        for (PlayerData pd : DataStore.getPlayerData())
            if (pd.getReputation() > highestRep) {
                nicest = pd;
                highestRep = pd.getReputation();
            }
        return nicest;
    }

    public static PlayerData getWorstPlayer() {
        int lowestRep = Integer.MAX_VALUE;
        PlayerData worst = null;
        for (PlayerData pd : DataStore.getPlayerData())
            if (pd.getReputation() < lowestRep) {
                worst = pd;
                lowestRep = pd.getReputation();
            }
        return worst;
    }

    public static String getTopDonator() {
        return "Computerology";
    }

}
