package de.lunarakai.lunaauction.utils.playerinteraction;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;

import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

public interface ItemUtil {
     static String itemToString(ItemStack stack) {
        YamlConfiguration conf = new YamlConfiguration();
        if (stack != null) {
            conf.set("displayItem", stack);
        }
        return conf.saveToString();
    }

    static ItemStack stringToItem(String itemString) throws InvalidConfigurationException {
        ItemStack displayItem = null;
        if (itemString != null) {
            YamlConfiguration conf = new YamlConfiguration();
            conf.loadFromString(itemString);
            displayItem = conf.getItemStack("displayItem");
        }
        return displayItem;
    }

    static ItemStack getItemInHand(UUID uuid) {
        Player player = Objects.requireNonNull(Bukkit.getPlayer(uuid));
        return player.getInventory().getItemInMainHand();
    }

    //TODO: Change DB table to be able to delete this :v
    static PersistentDataHolder getItemNBTInHand(UUID uuid) {
        Player player = Objects.requireNonNull(Bukkit.getPlayer(uuid));
        ItemStack item = player.getInventory().getItemInMainHand();
        return item.getItemMeta();
    }
}
