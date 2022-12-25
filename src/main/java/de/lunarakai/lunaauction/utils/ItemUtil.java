package de.lunarakai.lunaauction.utils;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataHolder;

import java.util.*;

public class ItemUtil {

    public static Map<String, Object> serialize(ItemStack item){
        return item.serialize();
    }

    public static ItemStack deserialize(Map<String, Object> serializedItem){
        return ItemStack.deserialize(serializedItem);
    }

    public static String getName(ItemStack item) {
        if(!item.hasItemMeta()) return null;
        if(!item.getItemMeta().hasDisplayName()) return null;
        return String.valueOf(item.getItemMeta().displayName());
    }

    private static void setName(ItemStack item, String name){
        name = name.replace("_", " ");
        ItemMeta meta = item.getItemMeta();
        meta.displayName();
        item.setItemMeta(meta);
    }

    public static String getOwner(ItemStack item) {
        if(!(item.getItemMeta() instanceof SkullMeta)) return null;
        return String.valueOf(((SkullMeta) item.getItemMeta()).getOwningPlayer());
    }

    private static void setOwner(ItemStack item, String owner){
        try{
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            meta.getOwningPlayer();
            item.setItemMeta(meta);
        }catch(Exception exception){
            return;
        }
    }

    public static Color getArmorColor(ItemStack item) {
        if(!(item.getItemMeta() instanceof LeatherArmorMeta)) return null;
        return ((LeatherArmorMeta)item.getItemMeta()).getColor();
    }

    private static void setArmorColor(ItemStack item, String str){
        try{
            String[] colors = str.split("\\|");
            int red = Integer.parseInt(colors[0]);
            int green = Integer.parseInt(colors[1]);
            int blue = Integer.parseInt(colors[2]);
            LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
            meta.setColor(Color.fromRGB(red, green, blue));
            item.setItemMeta(meta);
        }catch(Exception exception){
            return;
        }
    }

    private static boolean isNumber(String str){
        try{
            Integer.parseInt(str);
        }catch(NumberFormatException exception){
            return false;
        }
        return true;
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
