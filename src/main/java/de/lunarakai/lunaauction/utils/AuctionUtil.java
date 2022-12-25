package de.lunarakai.lunaauction.utils;

import com.google.gson.*;
import de.lunarakai.lunaauction.LunaAuction;
import de.lunarakai.lunaauction.sql.DatabaseQueries;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class AuctionUtil {

    public static ResultSet checkForAuctionId(Integer id) throws SQLException {
        return DatabaseQueries.equalsQuery("auctionID", "auctions", "auctionID", String.valueOf(id));
    }

    public static ItemStack searchForAuctionedItem(Integer id) throws SQLException {
        ItemStack itemStack = null;
        String jsonString;

        ResultSet resultSet = DatabaseQueries.equalsQuery("itemStack", "auctions", "auctionID", String.valueOf(id));

        if(resultSet.next()) {
            jsonString = resultSet.getString(1);

            Map<String, Object> _itemStack = createHashMapFromJsonString(jsonString);
            LunaAuction.LOGGER.info(_itemStack.toString());

            try {
                itemStack = ItemUtil.deserialize(_itemStack);
            } catch (ClassCastException e) {
                LunaAuction.LOGGER.warning(String.valueOf(e));
            }
        }
        //jsonString = jsonString.replace("=",":");
        return itemStack;
    }

    public static HashMap<String, Object> createHashMapFromJsonString(String json) {
        JsonObject object = null;

        LunaAuction.LOGGER.info(json);
        
        try {
            object = JsonParser.parseString(json).getAsJsonObject();
        } catch (IllegalStateException | JsonParseException e) {
            LunaAuction.LOGGER.warning(String.valueOf(e));
        }
        
        Set<Map.Entry<String, JsonElement>> set = object.entrySet();
        Iterator<Map.Entry<String, JsonElement>> iterator = set.iterator();
        HashMap<String, Object> map = new HashMap<String, Object>();

        while (iterator.hasNext()) {

            Map.Entry<String, JsonElement> entry = iterator.next();
            String key = entry.getKey();
            JsonElement value = entry.getValue();

            if (null != value) {
                if (!value.isJsonPrimitive()) {
                    if (value.isJsonObject()) {

                        map.put(key, createHashMapFromJsonString(value.toString()));
                    } else if (value.isJsonArray() && value.toString().contains(":")) {

                        List<HashMap<String, Object>> list = new ArrayList<>();
                        JsonArray array = value.getAsJsonArray();
                        if (null != array) {
                            for (JsonElement element : array) {
                                list.add(createHashMapFromJsonString(element.toString()));
                            }
                            map.put(key, list);
                        }
                    } else if (value.isJsonArray() && !value.toString().contains(":")) {
                        map.put(key, value.getAsJsonArray());
                    }
                } else {
                    map.put(key, value.getAsString());
                }
            }
        }
        return map;
    }
}


