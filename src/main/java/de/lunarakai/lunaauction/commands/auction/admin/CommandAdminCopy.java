package de.lunarakai.lunaauction.commands.auction.admin;

import de.lunarakai.lunaauction.utils.AuctionUtil;
import de.lunarakai.lunaauction.utils.ChatBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommandAdminCopy {

    ChatBuilder chatBuilder = new ChatBuilder();

    public void copyItemFromAuction(String auctionId, Player player) throws SQLException {
        ResultSet resultSet = AuctionUtil.checkForAuctionId(Integer.valueOf(auctionId));
        if(resultSet.next()) {
            //Query for ItemStack in AuctionTable and give a copy of the itemstack to the player
            ItemStack itemResult = AuctionUtil.searchForAuctionedItem(Integer.valueOf(auctionId));
            player.getInventory().addItem(itemResult);

        } else {
            chatBuilder.sendWarningMessage(player, "Not a valid auctionId");
        }
    }
}
