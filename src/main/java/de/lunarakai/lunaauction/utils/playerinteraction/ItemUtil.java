package de.lunarakai.lunaauction.utils.playerinteraction;

import de.lunarakai.lunaauction.LunaAuction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataHolder;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public interface ItemUtil {

    static Map<String, Object> serialize(ItemStack item){
        return item.serialize();
    }

    static ItemStack deserialize(Map<String, Object> serializedItem){
        return ItemStack.deserialize(serializedItem);
    }

    static ItemStack getItemInHand(UUID uuid) {
        Player player = Objects.requireNonNull(Bukkit.getPlayer(UUID.fromString(String.valueOf(uuid))));
        return player.getInventory().getItemInMainHand();
    }

    static PersistentDataHolder getItemNBTInHand(UUID uuid) {
        Player player = Objects.requireNonNull(Bukkit.getPlayer(UUID.fromString(String.valueOf(uuid))));

        ItemStack item = player.getInventory().getItemInMainHand();

        return item.getItemMeta();
    }

    static ItemMeta getItemMeta(Map<String, Object> jsonObject) {
        ItemMeta meta = null;

        if(jsonObject.containsKey("display-name")) {
            meta.displayName(getItemName(jsonObject.get("display-name")));
        }
        return meta;
    }

    static Component getItemName(Object object) {
        Component component = GsonComponentSerializer.gson().deserialize((String) object);
        LunaAuction.LOGGER.info(component.toString());
        return component;
    }
}
