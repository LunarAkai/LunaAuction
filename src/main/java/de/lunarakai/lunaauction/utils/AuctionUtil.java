package de.lunarakai.lunaauction.utils;

import de.lunarakai.lunaauction.sql.DatabaseQueries;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuctionUtil {
    public static ResultSet checkForAuctionId(Integer id) throws SQLException {
        return DatabaseQueries.equalsQuery("auctionID", "auctions", "auctionID", String.valueOf(id));
    }

    public static ItemStack searchForAuctionedItem(Integer id) throws SQLException {
        ItemStack itemStack;

        ResultSet resultSet = DatabaseQueries.equalsQuery("itemStack", "auctions", "auctionID", String.valueOf(id));
        String itemStackString = String.valueOf(resultSet.next());
        itemStack = ItemUtil.deserialize(itemStackString);

        return itemStack;
    }

}
