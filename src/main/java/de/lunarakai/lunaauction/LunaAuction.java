package de.lunarakai.lunaauction;

import de.lunarakai.lunaauction.commands.CommandAuction;
import de.lunarakai.lunaauction.commands.tabcompleter.AuctionTabCompleter;
import de.lunarakai.lunaauction.sql.Database;
import de.lunarakai.lunaauction.utils.CommandRegistration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

public final class LunaAuction extends JavaPlugin {

    public static Logger LOGGER;
    private static LunaAuction plugin;
    public static String CHAT_PREFIX = "[LAuction]";
    private CommandRegistration commandRegistration;
    private Database database;

    @Override
    public void onEnable() {

        LOGGER = getLogger();
        plugin = this;
        commandRegistration = new CommandRegistration();
        database = new Database();

        this.saveDefaultConfig();

        //Connect to Database
        try {
            database.connect();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
        getLogger().info("Database Connection: " + database.isConnected());

        //Command Registration
        commandRegistration.registerCommand(this, new CommandAuction(), "auction", new AuctionTabCompleter());

        getLogger().info("Plugin loaded successfully!");
    }

    @Override
    public void onDisable() {
        database.disconnect();

        getLogger().info("Plugin unloaded successfully!");
    }

    public static LunaAuction getPlugin() {
        return plugin;
    }
}
