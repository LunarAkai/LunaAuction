package de.lunarakai.lunaauction.commands;

import de.lunarakai.lunaauction.LunaAuction;
import de.lunarakai.lunaauction.utils.ChatBuilder;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandAuction implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
           Player player = (Player) sender;
           ChatBuilder chatBuilder = new ChatBuilder();

            if (player.hasPermission("lunaauctions.*")) {
                if(args.length == 1) {
                    if(args[0].equalsIgnoreCase("help")) {
                        chatBuilder.sendDefaultMessage(player, "there is no help");
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


