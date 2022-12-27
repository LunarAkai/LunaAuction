package de.lunarakai.lunaauction.commands.auction.admin;

import de.lunarakai.lunaauction.utils.playerinteraction.ChatBuilder;
import de.lunarakai.lunaauction.utils.playerinteraction.ItemUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandAdminSerialize {

    ChatBuilder chatBuilder = new ChatBuilder();

    public void adminSerialize(ItemStack item, Player player) {
        String itemSerialized = ItemUtil.itemToJson(item);
        chatBuilder.sendDefaultMessage(player, "Serialized Item: " + itemSerialized);
    }
}
