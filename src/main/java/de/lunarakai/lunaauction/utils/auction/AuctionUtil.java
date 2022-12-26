package de.lunarakai.lunaauction.utils.auction;

import com.google.gson.JsonSyntaxException;
import de.lunarakai.lunaauction.LunaAuction;
import de.lunarakai.lunaauction.sql.DatabaseQueries;
import de.lunarakai.lunaauction.utils.playerinteraction.ItemUtil;
import de.lunarakai.lunaauction.utils.json.JsonUtil;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class AuctionUtil {

    public static ResultSet getAuctionId(Integer id) throws SQLException {
        return DatabaseQueries.equalsQuery("auctionID", "auctions", "auctionID", String.valueOf(id));
    }

    public static ItemStack getAuctionedItem(Integer id) throws SQLException {
        ItemStack itemStack = null;
        String jsonString;
        Map<String, Object> stringObjectMap = null;
        ItemMeta itemMeta;

        ResultSet resultSet = DatabaseQueries.equalsQuery("itemStack", "auctions", "auctionID", String.valueOf(id));

        if(resultSet.next()) {
            jsonString = resultSet.getString(1);
            jsonString = jsonString.replace(":", "=");


            LunaAuction.LOGGER.info(jsonString);

            try {
                stringObjectMap = JsonUtil.createStringObjectMapFromJsonString(jsonString);
            } catch (JsonSyntaxException e) {
                LunaAuction.LOGGER.warning(String.valueOf(e));
            }

            if(stringObjectMap.containsKey("meta")) {
                itemMeta = ItemUtil.getItemMeta(stringObjectMap);
                itemStack.setItemMeta(itemMeta);
            }

            itemStack = ItemUtil.deserialize(stringObjectMap);
        }
        //jsonString = jsonString.replace("=",":");
        return itemStack;
    }
}


