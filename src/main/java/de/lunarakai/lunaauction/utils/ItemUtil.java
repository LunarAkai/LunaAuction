package de.lunarakai.lunaauction.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class ItemUtil {

    public static Map<String, Object> serialize(ItemStack item){
        return item.serialize();
    }

    public static ItemStack deserialize(Map<String, Object> serializedItem){
        return ItemStack.deserialize(serializedItem);
    }


    public static ItemStack getItemNameInHand(UUID uuid) {
        Player player = Objects.requireNonNull(Bukkit.getPlayer(UUID.fromString(String.valueOf(uuid))));
        return player.getInventory().getItemInMainHand();
    }

    public static PersistentDataHolder getItemNBTInHand(UUID uuid) {
        Player player = Objects.requireNonNull(Bukkit.getPlayer(UUID.fromString(String.valueOf(uuid))));

        ItemStack item = player.getInventory().getItemInMainHand();

        return item.getItemMeta();
    }
}
