package de.lunarakai.lunaauction.commands.auction.admin;

import de.lunarakai.lunaauction.LunaAuction;
import de.lunarakai.lunaauction.utils.CustomTextSerializer;
import de.lunarakai.lunaauction.utils.playerinteraction.ChatBuilder;
import de.lunarakai.lunaauction.utils.playerinteraction.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class CommandAdminGetText {

    CustomTextSerializer customTextSerializer;
    ChatBuilder chatBuilder = new ChatBuilder();

    public void getSerializedTextOfItemInHand(Player player) {
        ItemStack item = ItemUtil.getItemInHand(player.getUniqueId());
        LunaAuction.LOGGER.info("Item in Hand: " + item);
        if(item.getItemMeta().hasDisplayName()) {
            String stringItem = customTextSerializer.serialize(Objects.requireNonNull(item.getItemMeta().displayName()));
            chatBuilder.sendDefaultMessage(player, "Serialized ItemString: " + ChatColor.GOLD + stringItem);
        } else {
            chatBuilder.sendWarningMessage(player, "Item has no custom display name");
        }
    }
}
