package de.lunarakai.lunaauction.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
        ItemStack itemStack;

        ResultSet resultSet = DatabaseQueries.equalsQuery("itemStack", "auctions", "auctionID", String.valueOf(id));

        String jsonString = String.valueOf(resultSet.next());
        jsonString = jsonString.replace("=",":");


        Map<String, Object> _itemStack = createHashMapFromJsonString(jsonString);
        itemStack = ItemUtil.deserialize(_itemStack);

        return itemStack;
    }

    public static HashMap<String, Object> createHashMapFromJsonString(String json) {

        JsonObject object = JsonParser.parseString(json).getAsJsonObject();
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


