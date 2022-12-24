package de.lunarakai.lunaauction;

import de.lunarakai.lunaauction.commands.CommandAuction;
import de.lunarakai.lunaauction.commands.tabcompleter.AuctionTabCompleter;
import de.lunarakai.lunaauction.utils.CommandRegistration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class LunaAuction extends JavaPlugin {

    public static Logger LOGGER;
    private static LunaAuction plugin;
    public static String CHAT_PREFIX = "[LAuction]";


    @Override
    public void onEnable() {
        LOGGER = getLogger();
        plugin = this;

        //Command Registration
        new CommandRegistration().registerCommand(this, new CommandAuction(), "auction", new AuctionTabCompleter());

        getLogger().info("Plugin loaded successfully!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin unloaded successfully!");
    }

    public static LunaAuction getPlugin() {
        return plugin;
    }
}
