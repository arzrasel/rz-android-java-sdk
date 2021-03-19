package com.rzandroid.xmlparser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

class CoreJSONFastBuilder {
    private static String methodName = "methodName-var";

    protected static Object getJSONString(Object argObject) throws JSONException {
        methodName = "Object getJSONString(Object argObject)";
        if (argObject instanceof HashMap) {
            JSONObject jsonObject = new JSONObject();
            HashMap map = (HashMap) argObject;
            for (Object key : map.keySet()) {
                jsonObject.put(key.toString(), getJSONString(map.get(key)));
            }
            return jsonObject;
        } else if (argObject instanceof Iterable) {
            JSONArray jsonArray = new JSONArray();
            for (Object value : ((Iterable) argObject)) {
                jsonArray.put(getJSONString(value));
            }
            return jsonArray;
        } else {
            return argObject;
        }
    }

    protected static boolean isEmptyObject(JSONObject argJSONObject) {
        methodName = "boolean isEmptyObject(JSONObject argJSONObject)";
        return argJSONObject.names() == null;
    }

    protected static boolean isJson(String argJson) {
        methodName = "boolean isJson(String argJson)";
        try {
            new JSONObject(argJson);
        } catch (JSONException ex) {
            try {
                new JSONArray(argJson);
            } catch (JSONException exc) {
                return false;
            }
        }
        return true;
    }
}
//https://gist.github.com/sheharyarn/cba56ff154de2cc62fc5
/*
List<HashMap<String, Object>> listItems = new ArrayList<>();
HashMap<String, Object> mapItems = new HashMap<>();
List<String> list = new ArrayList<>();
list.add("LIST_ONE");
list.add("LIST_TWO");
list.add("LIST_THREE");
mapItems.put("str_01", "str01");
mapItems.put("list_01", list);
mapItems.put("str_02", "str02");
listItems.add(mapItems);
try {
    Object object = JSONFastBuilder.getJSONString(listItems);
    System.out.println("JSON_00000000: " + object.toString());
    object = JSONFastBuilder.getJSONString(mapItems);
    System.out.println("JSON_00000000: " + object.toString());
} catch (JSONException e) {
    e.printStackTrace();
}
*/
