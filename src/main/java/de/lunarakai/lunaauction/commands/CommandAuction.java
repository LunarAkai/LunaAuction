package de.lunarakai.lunaauction.commands;

import de.lunarakai.lunaauction.LunaAuction;
import de.lunarakai.lunaauction.commands.auction.CommandAuctionAdmin;
import de.lunarakai.lunaauction.commands.auction.CommandAuctionBid;
import de.lunarakai.lunaauction.events.customevents.AuctionCreateEvent;
import de.lunarakai.lunaauction.utils.playerinteraction.ChatBuilder;
import de.lunarakai.lunaauction.utils.playerinteraction.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.sql.SQLException;

public class CommandAuction implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
           Player player = (Player) sender;
           ChatBuilder chatBuilder = new ChatBuilder();

            if (player.hasPermission("lunaauctions.*")) {
                if(args.length > 0) {
                    if(args[0].equalsIgnoreCase("bid")) {
                        //TODO Bid command check if new value is higher than stored value
                        if(!args[1].isEmpty() && !args[2].isEmpty()) {
                            CommandAuctionBid auctionBid = new CommandAuctionBid();
                            try {
                                auctionBid.bidCommand(args[1], args[2], PlayerUtil.getUuid(player));
                            } catch (SQLException e) {
                                //TODO
                            }
                        }
                    } else if(args[0].equalsIgnoreCase("help")) {
                        chatBuilder.sendDefaultMessage(player, "there is no help");
                    } else if(args[0].equalsIgnoreCase("create")) {
                        AuctionCreateEvent auctionCreateEvent;
                        try {
                            auctionCreateEvent = new AuctionCreateEvent(player.getUniqueId(), player.getName());
                        } catch (SQLException | IOException e) {
                            throw new RuntimeException(e);
                        }
                        Bukkit.getPluginManager().callEvent(auctionCreateEvent);

                        if(!auctionCreateEvent.isCancelled()) {
                            return true;
                        } else {
                            return false;
                        }
                    } else if(args[0].equalsIgnoreCase("admin")) {
                        CommandAuctionAdmin auctionAdmin = new CommandAuctionAdmin();
                        try {
                            auctionAdmin.adminCommand(args, player);
                        } catch (SQLException | InvalidConfigurationException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        chatBuilder.sendWarningMessage(player, "Not a valid command!");
                    }
                } else {
                    chatBuilder.sendDefaultMessage(player,
                            "LunaAuction "+ ChatColor.YELLOW +  "v" + LunaAuction.getPlugin().getDescription().getVersion() + ChatColor.WHITE +
                                    " by: " + ChatColor.YELLOW + LunaAuction.getPlugin().getDescription().getAuthors() + ChatColor.WHITE +
                                    "\nGitHub: " + ChatColor.YELLOW + "https://github.com/LunarAkai/LunaAuction" + ChatColor.WHITE +
                                    "\nDocs: "+ ChatColor.YELLOW + "TODO" + "\n"+
                                    ChatColor.AQUA + "Trans" + ChatColor.LIGHT_PURPLE + " rights" + ChatColor.WHITE + " are" + ChatColor.LIGHT_PURPLE + " human" + ChatColor.AQUA + " rights!");
                }
            } else {
                chatBuilder.sendErrorMessage(player, "No permission!");
            }
        }
        return true;
    }
}


