package de.lunarakai.lunaauction.commands.auction.admin;

import de.lunarakai.lunaauction.LunaAuction;
import de.lunarakai.lunaauction.utils.auction.AuctionUtil;
import de.lunarakai.lunaauction.utils.playerinteraction.ChatBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommandAdminDeserialize {
    ChatBuilder chatBuilder = new ChatBuilder();

    public void deserializeWithoutItem(String auctionId, Player player) throws SQLException {
        ResultSet resultSet = AuctionUtil.getAuctionId(Integer.valueOf(auctionId));
        if(resultSet.next()) {
            //Query for ItemStack in AuctionTable and give a copy of the itemstack to the player
            ItemStack itemResult = AuctionUtil.getAuctionedItem(Integer.valueOf(auctionId));
            try{
                chatBuilder.sendDefaultMessage(player, itemResult.toString());
            } catch (IllegalArgumentException e) {
                chatBuilder.sendWarningMessage(player, String.valueOf(e));
                LunaAuction.LOGGER.warning(String.valueOf(e));
            }
        } else {
            chatBuilder.sendWarningMessage(player, "Not a valid auctionId");
        }
    }
}
