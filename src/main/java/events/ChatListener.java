package events;

import io.DataStore;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Set;

public class ChatListener implements Listener {

    public static int CHAT_RADIUS = 125;

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player p = event.getPlayer();
        DataStore.getPlayerData(p.getUniqueId()).updateLastActivity();
        Set<Player> recipients = event.getRecipients();
        boolean globalMessage = event.getMessage().startsWith("!");
        for (Player recipient : recipients) {
            //can only hear message if player is close enough, speaking globally, or a friend of the speaker
            boolean isSender = p.getUniqueId().compareTo(recipient.getUniqueId()) == 0;
            boolean isGroupMember = DataStore.getPlayerData(recipient.getUniqueId()).inGroupWith(p.getUniqueId());
            boolean isNear = recipient.getNearbyEntities(CHAT_RADIUS, CHAT_RADIUS, CHAT_RADIUS).contains(p) || isSender;
            boolean inGlobalChat = true; //@TODO set a flag in PlayerData

            if (!(globalMessage && inGlobalChat) && !isGroupMember && !isNear && !isSender) continue;
            if (globalMessage) {
                if (inGlobalChat) recipient.sendMessage("<"+ChatColor.BOLD+p.getName()+ChatColor.RESET+"> "
                        +event.getMessage().replaceFirst("!", ""));
            } else {
                if (isGroupMember) {
                    recipient.sendMessage("<"+ChatColor.GREEN+p.getName()+ChatColor.WHITE+"> "+event.getMessage());
                } else if (isNear) {
                    recipient.sendMessage(ChatColor.GRAY+"<"+p.getName()+"> "+event.getMessage());
                }
            }

        }
        event.setCancelled(true); //override the chat
    }

}
