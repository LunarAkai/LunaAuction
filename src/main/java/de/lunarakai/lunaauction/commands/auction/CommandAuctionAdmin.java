package de.lunarakai.lunaauction.commands.auction;

import de.lunarakai.lunaauction.LunaAuction;
import de.lunarakai.lunaauction.commands.auction.admin.CommandAdminCopy;
import de.lunarakai.lunaauction.commands.auction.admin.CommandAdminDeserialize;
import de.lunarakai.lunaauction.commands.auction.admin.CommandAdminSerialize;
import de.lunarakai.lunaauction.utils.playerinteraction.ChatBuilder;
import de.lunarakai.lunaauction.utils.playerinteraction.ItemUtil;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class CommandAuctionAdmin {

    ChatBuilder chatBuilder = new ChatBuilder();

    public void adminCommand(String[] args, Player player) throws SQLException, InvalidConfigurationException {
        if(args.length >= 2) {
            if(args[1].equalsIgnoreCase("help")) {
                //TODO admin help page
            } else if(args[1].equalsIgnoreCase("copy")) {
                CommandAdminCopy adminCopy = new CommandAdminCopy();
                adminCopy.copyItemFromAuction(args[2], player);
            } else if(args[1].equalsIgnoreCase("delete")) {
                //TODO delete auction from other player
            } else if(args[1].equalsIgnoreCase("serialize")) {
                CommandAdminSerialize adminSerialize = new CommandAdminSerialize();
                adminSerialize.adminSerialize(ItemUtil.getItemInHand(player.getUniqueId()), player);
            } else if(args[1].equalsIgnoreCase("deserialize")) {
                CommandAdminDeserialize adminDeserialize = new CommandAdminDeserialize();
                adminDeserialize.deserializeWithoutItem(args[2], player);
            } else {
                chatBuilder.sendWarningMessage(player, "Not a valid auction admin command");
            }
        } else {
            ChatBuilder chatBuilder = new ChatBuilder();
            chatBuilder.sendWarningMessage(player, "Not a valid auction admin command");
        }
    }
}
