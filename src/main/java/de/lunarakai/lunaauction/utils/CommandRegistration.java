package de.lunarakai.lunaauction.utils;

import de.lunarakai.lunaauction.LunaAuction;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;

public class CommandRegistration {

    public void registerCommand(LunaAuction plugin, CommandExecutor commandExecutor, String commandName, TabCompleter pluginTabCompleter) {
        plugin.getCommand(commandName).setExecutor(commandExecutor);
        plugin.getCommand(commandName).setTabCompleter(pluginTabCompleter);
    }

}
