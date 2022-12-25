package de.lunarakai.lunaauction.commands.auction;

import de.lunarakai.lunaauction.LunaAuction;
import de.lunarakai.lunaauction.sql.DatabaseQueries;
import de.lunarakai.lunaauction.sql.DatabaseUpdate;
import de.lunarakai.lunaauction.utils.ChatBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.util.Objects;
import java.util.UUID;

public class CommandAuctionBid {

    public void bidCommand(String argId, String argPrice, UUID playerUuid) {

        Player player = Objects.requireNonNull(Bukkit.getPlayer(UUID.fromString(String.valueOf(playerUuid))));
        ChatBuilder chatBuilder = new ChatBuilder();

        if(!argId.isEmpty()) {
            try {
                int id = Integer.parseInt(argId);

                if(!argPrice.isEmpty()) {
                    try {
                        int biddingPrice = Integer.parseInt(argPrice);

                        //Check if auctionID exists
                        ResultSet resultSet = DatabaseQueries.equalsQuery("auctionID", "auctions", "auctionID", argId);
                        if(resultSet.next()) {

                            //update currentPrice for auctionID
                            DatabaseUpdate.insertData("auctions", "currentPrice", "auctionID", id, biddingPrice);
                            chatBuilder.sendDefaultMessage(player, "Successfully bid " + biddingPrice + " " +
                                    LunaAuction.getPlugin().getConfig().getString("general_config.currency_name") +
                                    " for Auction: " + id);

                        }
                    } catch(Exception e) {
                        chatBuilder.sendErrorMessage(player, "Couldn't parse args[2] to int. Please enter a valid value.");
                        LunaAuction.LOGGER.warning(String.valueOf(e));
                    }

                } else {
                    chatBuilder.sendErrorMessage(player, "You need to ");
                }

            } catch (Exception e) {
                chatBuilder.sendErrorMessage(player, "Couldn't parse args[1] to int. Please enter a valid value.");
                LunaAuction.LOGGER.warning(String.valueOf(e));
            }
        } else {
            chatBuilder.sendErrorMessage(player, "You need to enter an auctionId. For the complete list of live auctions type /auctions list");
            //TODO
        }
    }


}
