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

    public static String serialize(ItemStack item){
        StringBuilder builder = new StringBuilder();
        builder.append(item.getType().toString());
        if(item.getItemMeta().hasDestroyableKeys()) {
            builder.append(":" + item.getItemMeta().getDestroyableKeys());
        }
        builder.append(" " + item.getAmount());
        for(Enchantment enchant:item.getEnchantments().keySet()) {
            builder.append(" " + enchant.getKey() + ":" + item.getEnchantments().get(enchant));
        }
        String name = getName(item);
        if(name != null ) builder.append(" name:" + name);
        Color color = getArmorColor(item);
        if(color != null) builder.append(" rgb:" + color.getRed() + "|" + color.getGreen() + "|" + color.getBlue());
        String owner = getOwner(item);
        if(owner != null) builder.append(" owner:" + owner);

        return builder.toString();
    }

    public static ItemStack deserialize(String serializedItem){
        String[] strings = serializedItem.split(" ");
        Map<Enchantment, Integer> enchants = new HashMap<Enchantment, Integer>();
        String[] args;
        ItemStack item = new ItemStack(Material.AIR);
        for(String str: strings){
            args = str.split(":");
            if(Material.matchMaterial(args[0]) != null && item.getType() == Material.AIR){
                item.setType(Material.matchMaterial(args[0]));
                if(args.length == 2) {

                }
                break;
            }
        }
        if (item.getType() == Material.AIR) {
            Bukkit.getLogger().info("Could not find a valid material for the item in \"" + serializedItem + "\"");
            return null;
        }
        for(String str:strings){
            args = str.split(":", 2);
            if(isNumber(args[0])) item.setAmount(Integer.parseInt(args[0]));
            if(args.length == 1) continue;
            if(args[0].equalsIgnoreCase("name")){
                setName(item, ChatColor.translateAlternateColorCodes('&', args[1]));
                continue;
            }
            if(args[0].equalsIgnoreCase("rgb")){
                setArmorColor(item, args[1]);
                continue;
            }
            if(args[0].equalsIgnoreCase("owner")){
                setOwner(item, args[1]);
                continue;
            }
            if(Enchantment.getByKey(NamespacedKey.minecraft(args[0].toUpperCase())) != null){
                enchants.put(Enchantment.getByKey(NamespacedKey.minecraft(args[0].toUpperCase())), Integer.parseInt(args[1]));
            }
        }
        item.addUnsafeEnchantments(enchants);
        return item.getType().equals(Material.AIR) ? null : item;
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
