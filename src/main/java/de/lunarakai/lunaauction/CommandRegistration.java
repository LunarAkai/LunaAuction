package de.lunarakai.lunaauction;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;

public class CommandRegistration {

    public void registerCommand(LunaAuction plugin, CommandExecutor commandExecutor, String commandName, TabCompleter pluginTabCompleter) {
        plugin.getCommand(commandName).setExecutor(commandExecutor);
        plugin.getCommand(commandName).setTabCompleter(pluginTabCompleter);
    }

}
