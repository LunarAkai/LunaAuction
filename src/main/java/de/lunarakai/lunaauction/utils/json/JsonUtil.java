package de.lunarakai.lunaauction.utils.json;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;
import java.util.Map;

public class JsonUtil {
    public static Map<String, Object> createStringObjectMapFromJsonString(String json) {

        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        Map<String, Object> map = gson.fromJson(json, type);

        return map;
    }
}


