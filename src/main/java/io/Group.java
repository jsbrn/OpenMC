package io;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Group {

    private ArrayList<UUID> members;

    public Group(UUID owner) {
        this.members = new ArrayList<UUID>();
        this.members.add(owner);
    }

    public PlayerData getOwner() { return members.isEmpty() ? null : DataStore.getPlayerData(members.get(0)); }
    public boolean isOwnedBy(UUID playerID) { return members.isEmpty() ? false
            : getOwner().getOfflinePlayer().getUniqueId().compareTo(playerID) == 0; }
    public ArrayList<PlayerData> getMembers() {
        ArrayList<PlayerData> playerDatas = new ArrayList<PlayerData>();
        for (UUID uuid: members)
            playerDatas.add(DataStore.getPlayerData(uuid));
        return playerDatas;
    }

    public boolean hasPlayer(UUID playerID) { return members.contains(playerID); }
    public boolean addMember(UUID playerID) { if (!hasPlayer(playerID)) {members.add(playerID); return true; } else { return false; } }
    public void removeMember(UUID playerID) { members.remove(playerID); }

    public void sendMessage(String message) {
        for (UUID member: members)
            DataStore.getPlayerData(member).sendMessage(message);
    }

    public void save(PrintWriter pw) {
        String saveStr = "type = group, members = ";
        for (UUID id: members)
            saveStr += id + " ";
        pw.println(saveStr.trim().replace(" ", ","));
    }

    public boolean load(HashMap<String, String> data) {
        String members[] = data.getOrDefault("members", "").trim().split(",");
        for (String s: members)
                addMember(UUID.fromString(s));
        return true;
    }

}
