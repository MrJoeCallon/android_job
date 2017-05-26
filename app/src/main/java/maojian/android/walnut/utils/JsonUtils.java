package maojian.android.walnut.utils;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * @author hezuzhi
 * @Description: (json管理)
 * @date 2016/4/29  11:01.
 * @version: 1.0
 */
public class JsonUtils {
    public static Gson gson = new Gson();

    public static <T> T object(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    public static <T> String toJson(Object object) {
        return gson.toJson(object);
    }

    //    public static LinkedList<T> T object(String json, Class<T> classOfT) {
//        Type listType = new TypeToken<LinkedList<T>>(){}.getType();
//        return gson.fromJson(json, listType);
//    }
    public static void JsonObject2HashMap(JSONObject jo, List<Map<?, ?>> rstList) {
        for (Iterator<String> keys = jo.keys(); keys.hasNext(); ) {
            try {
                String key1 = keys.next();
                System.out.println("key1---" + key1 + "------" + jo.get(key1)
                        + (jo.get(key1) instanceof JSONObject) + jo.get(key1)
                        + (jo.get(key1) instanceof JSONArray));
                if (jo.get(key1) instanceof JSONObject) {

                    JsonObject2HashMap((JSONObject) jo.get(key1), rstList);
                    continue;
                }
                if (jo.get(key1) instanceof JSONArray) {
                    JsonArray2HashMap(key1, (JSONArray) jo.get(key1), rstList);
                    continue;
                }
                System.out.println("key1:" + key1 + "----------jo.get(key1):"
                        + jo.get(key1));
                json2HashMap(key1, jo.get(key1), rstList);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public static void json2HashMap(String key, Object value,
                                    List<Map<?, ?>> rstList) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(key, value);
        rstList.add(map);
    }

    public static void JsonArray2HashMap(String key, JSONArray joArr,
                                         List<Map<?, ?>> rstList) {
        List<String> nameList = new ArrayList<>();
        for (int i = 0; i < joArr.length(); i++) {
            try {
                if (joArr.get(i) instanceof JSONObject) {

                    JsonObject2HashMap((JSONObject) joArr.get(i), rstList);
                    continue;
                }
                if (joArr.get(i) instanceof JSONArray) {

                    JsonArray2HashMap(key, (JSONArray) joArr.get(i), rstList);
                    continue;
                }
                nameList.add(joArr.get(i).toString());
                System.out.println("Excepton~~~~~");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        Map<String ,List<String>> map  = new HashMap<>();
        map.put(key, nameList);
        rstList.add(map);
    }
}

