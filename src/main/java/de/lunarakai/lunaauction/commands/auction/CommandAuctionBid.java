package de.lunarakai.lunaauction.commands.auction;

import de.lunarakai.lunaauction.LunaAuction;
import de.lunarakai.lunaauction.sql.DatabaseUpdate;
import de.lunarakai.lunaauction.utils.auction.AuctionUtil;
import de.lunarakai.lunaauction.utils.playerinteraction.ChatBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

public class CommandAuctionBid {

    public void bidCommand(String argId, String argPrice, UUID playerUuid) throws SQLException {

        Player player = Objects.requireNonNull(Bukkit.getPlayer(UUID.fromString(String.valueOf(playerUuid))));
        ChatBuilder chatBuilder = new ChatBuilder();

        int id = -1;
        int biddingPrice = -1;

        try {
            id = Integer.parseInt(argId);
            biddingPrice = Integer.parseInt(argPrice);
        } catch(NumberFormatException e) {
            chatBuilder.sendWarningMessage(player, "Couldn't parse id and/or bidding price to int.");
        }
        //Check if auctionID exists
        if(id != -1 && biddingPrice != -1) {
            ResultSet resultSet = AuctionUtil.getAuctionId(id);
            if(resultSet.next()) {
                //update currentPrice for auctionID
                DatabaseUpdate.insertData("auctions", "currentPrice", "auctionID", id, biddingPrice);
                chatBuilder.sendDefaultMessage(player, "Successfully bid " + biddingPrice + " " +
                        LunaAuction.getPlugin().getConfig().getString("general_config.currency_name") +
                        " for Auction: " + id);
            } else {
                chatBuilder.sendErrorMessage(player, "Auction doesn't exist. Please enter a valid auctionID. See /auction list for all live auctions.");
            }
        }
    }
}
