package de.lunarakai.lunaauction;

import de.lunarakai.lunaauction.commands.CommandAuction;
import de.lunarakai.lunaauction.commands.tabcompleter.AuctionTabCompleter;
import de.lunarakai.lunaauction.events.AuctionEventListener;
import de.lunarakai.lunaauction.sql.Database;
import de.lunarakai.lunaauction.utils.playerinteraction.CommandRegistration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

public final class LunaAuction extends JavaPlugin {

    //Vault API (Economy) - EssentialsX

    public static Logger LOGGER;
    private static LunaAuction plugin;
    public static String CHAT_PREFIX = "[LAuction]";
    private CommandRegistration commandRegistration;

    @Override
    public void onEnable() {

        LOGGER = getLogger();
        plugin = this;
        commandRegistration = new CommandRegistration();

        this.saveDefaultConfig();

        //Connect to Database
        try {
            Database.connect();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
        getLogger().info("Database Connection: " + Database.isConnected());

        getServer().getPluginManager().registerEvents(new AuctionEventListener(), this);

        //Command Registration
        commandRegistration.registerCommand(this, new CommandAuction(), "auction", new AuctionTabCompleter());

        getLogger().info("Plugin loaded successfully!");
    }

    @Override
    public void onDisable() {
        Database.disconnect();

        getLogger().info("Plugin unloaded successfully!");
    }

    public static LunaAuction getPlugin() {
        return plugin;
    }
}
