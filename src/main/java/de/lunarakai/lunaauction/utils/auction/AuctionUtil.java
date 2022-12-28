package de.lunarakai.lunaauction.utils.auction;

import de.lunarakai.lunaauction.sql.DatabaseQueries;
import de.lunarakai.lunaauction.utils.playerinteraction.ItemUtil;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuctionUtil {

    public static ResultSet getAuctionId(Integer id) throws SQLException {
        return DatabaseQueries.equalsQuery("auctionID", "auctions", "auctionID", String.valueOf(id));
    }

    public static ItemStack getAuctionedItem(Integer id) throws SQLException, InvalidConfigurationException {
        ItemStack itemStack;
        String itemString = null;
        ResultSet resultSet = DatabaseQueries.equalsQuery("itemStack", "auctions", "auctionID", String.valueOf(id));
        if(resultSet.next()) {
            itemString = resultSet.getString(1);
        }
        itemStack = ItemUtil.stringToItem(itemString);
        return itemStack;
    }

}



