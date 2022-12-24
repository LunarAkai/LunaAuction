package de.lunarakai.lunaauction.utils;

import de.lunarakai.lunaauction.LunaAuction;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;


public class ChatBuilder {

    public void sendDefaultMessage(CommandSender player, String message) {
        player.sendMessage(ChatColor.valueOf("GOLD") + LunaAuction.CHAT_PREFIX + ChatColor.valueOf("WHITE") + " " + message);
    }
    public void sendWarningMessage(CommandSender player, String message) {
        player.sendMessage(ChatColor.valueOf("GOLD") + LunaAuction.CHAT_PREFIX + ChatColor.valueOf("RED") + " " + message);
    }
    public void sendErrorMessage(CommandSender player, String message) {
        player.sendMessage(ChatColor.valueOf("GOLD") + LunaAuction.CHAT_PREFIX + ChatColor.valueOf("DARK_RED") + " " + message);
    }
}
