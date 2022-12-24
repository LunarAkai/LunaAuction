package de.lunarakai.lunaauction;

import de.lunarakai.lunaauction.commands.CommandAuction;
import de.lunarakai.lunaauction.commands.tabcompleter.AuctionTabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

public final class LunaAuction extends JavaPlugin {

    @Override
    public void onEnable() {
       new CommandRegistration().registerCommand(this, new CommandAuction(), "auction", new AuctionTabCompleter());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
