package de.lunarakai.lunaauction.commands.auction;

import de.lunarakai.lunaauction.LunaAuction;
import de.lunarakai.lunaauction.sql.DatabaseUpdate;
import de.lunarakai.lunaauction.utils.auction.AuctionUtil;
import de.lunarakai.lunaauction.utils.playerinteraction.ChatBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.util.Objects;
import java.util.UUID;

public class CommandAuctionBid {

    public void bidCommand(String argId, String argPrice, UUID playerUuid) {

        Player player = Objects.requireNonNull(Bukkit.getPlayer(UUID.fromString(String.valueOf(playerUuid))));
        ChatBuilder chatBuilder = new ChatBuilder();

            try {
                int id = Integer.parseInt(argId);
                    try {
                        int biddingPrice = Integer.parseInt(argPrice);

                        //Check if auctionID exists
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
                    } catch(Exception e) {
                        chatBuilder.sendErrorMessage(player, "Couldn't parse args[2] to int. Please enter a valid value.");
                        LunaAuction.LOGGER.warning(String.valueOf(e));
                    }
            } catch (Exception e) {
                chatBuilder.sendErrorMessage(player, "Couldn't parse args[1] to int. Please enter a valid value.");
                LunaAuction.LOGGER.warning(String.valueOf(e));
            }
    }
}
