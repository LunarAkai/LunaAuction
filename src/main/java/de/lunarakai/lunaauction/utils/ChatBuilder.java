package de.lunarakai.lunaauction.utils;

import de.lunarakai.lunaauction.LunaAuction;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;


public class ChatBuilder {

    public void sendDefaultMessage(CommandSender player, String message) {
        player.sendMessage(ChatColor.valueOf("GOLD") + LunaAuction.CHAT_PREFIX + ChatColor.valueOf("WHITE") + " " + message);
    }
    public void sendSuccessMessage(CommandSender player, String message) {
        player.sendMessage(ChatColor.valueOf("GOLD") + LunaAuction.CHAT_PREFIX + ChatColor.valueOf("GREEN") + " " + message);
    }
    public void sendSuccessfulAuctionCreationMessage(CommandSender player, ItemStack itemStack, Integer price) {

        player.sendMessage(ChatColor.valueOf("GOLD") + LunaAuction.CHAT_PREFIX + ChatColor.valueOf("WHITE") +
                " You have put " + ChatColor.YELLOW + itemStack + " " + ChatColor.WHITE +
                "with a starting price of " + ChatColor.YELLOW + price + " " + ChatColor.WHITE +
                LunaAuction.getPlugin().getConfig().getString("general_config.currency_name")
                + " up for auction.");
    }
    public void sendWarningMessage(CommandSender player, String message) {
        player.sendMessage(ChatColor.valueOf("GOLD") + LunaAuction.CHAT_PREFIX + ChatColor.valueOf("RED") + " " + message);
    }
    public void sendErrorMessage(CommandSender player, String message) {
        player.sendMessage(ChatColor.valueOf("GOLD") + LunaAuction.CHAT_PREFIX + ChatColor.valueOf("DARK_RED") + " " + message);
    }
}
