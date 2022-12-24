package de.lunarakai.lunaauction.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandAuction implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (player.hasPermission("lunaauctions.*")) {
                if(args.length == 1) {
                    if(args[0].equalsIgnoreCase("help")) {
                        player.sendMessage("There is no help qwq");
                    }
                    if(args[0].equalsIgnoreCase("info")) {
                        player.sendMessage("~ LunaAuction by LunarAkai, 2022 ~\nWebsite: https://www.lunarakai.de/\nGitHub: ");
                    }
                } else {
                    player.sendMessage("Hello world c:");
                }
            } else {
                player.sendMessage("Not allowed!");
            }

        }
        return true;
    }
}


