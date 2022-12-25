package de.lunarakai.lunaauction.commands.auction;

import de.lunarakai.lunaauction.LunaAuction;
import de.lunarakai.lunaauction.commands.auction.admin.CommandAdminCopy;
import de.lunarakai.lunaauction.utils.ChatBuilder;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class CommandAuctionAdmin {

    ChatBuilder chatBuilder = new ChatBuilder();

    public void adminCommand(String[] args, Player player) {
        if(args.length > 2) {
            if(args[1].equalsIgnoreCase("help")) {
                //TODO admin help page
            } else if(args[1].equalsIgnoreCase("copy")) {
                //TODO copy item from auction
                try {
                    CommandAdminCopy adminCopy = new CommandAdminCopy();
                    adminCopy.copyItemFromAuction(args[2], player);
                } catch (IndexOutOfBoundsException | SQLException e) {
                    chatBuilder.sendWarningMessage(player, "Please enter a valid auctionId");
                    LunaAuction.LOGGER.warning(String.valueOf(e));
                }

            } else if(args[1].equalsIgnoreCase("delete")) {
                //TODO delete auction from other player
            } else {

                chatBuilder.sendWarningMessage(player, "Not a valid auction admin command");
            }
        } else {
            ChatBuilder chatBuilder = new ChatBuilder();
            chatBuilder.sendWarningMessage(player, "Not a valid auction admin command");
        }
    }
}
