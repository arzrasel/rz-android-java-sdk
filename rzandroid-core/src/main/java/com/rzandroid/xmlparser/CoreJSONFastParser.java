package com.rzandroid.xmlparser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class CoreJSONFastParser {
    private static String methodName = "methodName-var";

    protected static HashMap<String, Object> JSONObjectFeed(String argJsonObject) throws JSONException {
        methodName = "HashMap<String, Object> JSONObjectFeed(String argJsonObject)";
        return getJSONToMap(new JSONObject(argJsonObject));
    }

    protected static List<Object> JSONArrayFeed(String argJsonArray) throws JSONException {
        methodName = "List<Object> JSONArrayFeed(String argJsonArray)";
        return getJSONToList(new JSONArray(argJsonArray));
    }

    private static HashMap<String, Object> getJSONToMap(JSONObject argJsonObject) throws JSONException {
        methodName = "HashMap<String, Object> getJSONToMap(JSONObject argJsonObject)";
        HashMap<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysIterator = argJsonObject.keys();
        while (keysIterator.hasNext()) {
            String key = keysIterator.next();
            Object value = argJsonObject.get(key);

            if (value instanceof JSONArray) {
                value = getJSONToList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = getJSONToMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    private static List<Object> getJSONToList(JSONArray argJsonArray) throws JSONException {
        methodName = "List<Object> getJSONToList(JSONArray argJsonArray)";
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < argJsonArray.length(); i++) {
            Object value = argJsonArray.get(i);
            if (value instanceof JSONArray) {
                value = getJSONToList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = getJSONToMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    protected static boolean isMap(Object argObject) {
        methodName = "isMap(Object argObject)";
        //System.out.println("LOG_PRINT_OBJECT_TYPE: " + argObject.getClass());
        if (argObject instanceof HashMap || argObject instanceof Map) {
            return true;
        }
        return false;
    }

    protected static boolean isList(Object argObject) {
        methodName = "isList(Object argObject)";
        //System.out.println("LOG_PRINT_OBJECT_TYPE: " + argObject.getClass());
        if (argObject instanceof ArrayList || argObject instanceof List) {
            return true;
        }
        return false;
    }

    protected static <T> T getObjectByKey(HashMap<String, ?> argObjectHashMap, String argKey) {
        methodName = "<T> T getObjectByKey(HashMap<String, ?> argObjectHashMap, String argKey)";
        if (argObjectHashMap.containsKey(argKey)) {
            Object object = argObjectHashMap.get(argKey);
            return (T) object;
        }
        return null;
    }

    protected static <T> T getHashMapByKey(HashMap<String, ?> argObjectHashMap, String argKey) {
        methodName = "<T> T getHashMapByKey(HashMap<String, ?> argObjectHashMap, String argKey)";
        HashMap<String, ?> hashMap = null;
        if (argObjectHashMap.containsKey(argKey)) {
            Object object = argObjectHashMap.get(argKey);
            if (isMap(object)) {
                hashMap = (HashMap<String, ?>) object;
            }
            return (T) hashMap;
        }
        return (T) hashMap;
    }

    @Deprecated
    protected static HashMap<String, String> getHashMapStringByKey(HashMap<String, ?> argObjectHashMap, String argKey) {
        methodName = "HashMap<String, String> getHashMapStringByKey(HashMap<String, ?> argObjectHashMap, String argKey)";
        HashMap<String, String> hashMap = null;
        if (argObjectHashMap.containsKey(argKey)) {
            Object object = argObjectHashMap.get(argKey);
            if (isMap(object)) {
                hashMap = (HashMap<String, String>) object;
            }
            return hashMap;
        }
        return hashMap;
    }

    protected static <T> T getArrayListMapByKey(HashMap<String, ?> argObjectHashMap, String argKey) {
        methodName = "<T> T getArrayListMapByKey(HashMap<String, ?> argObjectHashMap, String argKey)";
        ArrayList<HashMap<String, ?>> arrayListHashMap = null;
        if (argObjectHashMap.containsKey(argKey)) {
            Object object = argObjectHashMap.get(argKey);
            if (isList(object)) {
                arrayListHashMap = (ArrayList<HashMap<String, ?>>) object;
                if (arrayListHashMap.size() > 0) {
                    if (isMap(arrayListHashMap.get(0))) {
                        return (T) arrayListHashMap;
                    }
                }
                return null;
            }
            return (T) arrayListHashMap;
        }
        return (T) arrayListHashMap;
    }

    @Deprecated
    protected static ArrayList<HashMap<String, String>> getArrayListHashMapByKey(HashMap<String, ?> argObjectHashMap, String argKey) {
        methodName = "ArrayList<HashMap<String, String>> getArrayListHashMapByKey(HashMap<String, ?> argObjectHashMap, String argKey)";
        ArrayList<HashMap<String, String>> arrayListHashMap = null;
        if (argObjectHashMap.containsKey(argKey)) {
            Object object = argObjectHashMap.get(argKey);
            if (isList(object)) {
                arrayListHashMap = (ArrayList<HashMap<String, String>>) object;
                /*System.out.println("LOG_PRINT_CALLED");
                System.out.println("LOG_PRINT_CALLED: " + arrayListHashMap.size());*/
                if (arrayListHashMap.size() > 0) {
                    if (isMap(arrayListHashMap.get(0))) {
                        return arrayListHashMap;
                    }
                }
                return null;
            }
            return arrayListHashMap;
        }
        return arrayListHashMap;
    }

    protected static <T> T getArrayListByKey(HashMap<String, ?> argObjectHashMap, String argKey) {
        methodName = "<T> T getArrayListByKey(HashMap<String, ?> argObjectHashMap, String argKey)";
        //ArrayList<String>
        ArrayList<?> arrayList = null;
        if (argObjectHashMap.containsKey(argKey)) {
            Object object = argObjectHashMap.get(argKey);
            if (isList(object)) {
                arrayList = (ArrayList<?>) object;
            }
            return (T) arrayList;
        }
        return (T) arrayList;
    }

    @Deprecated
    protected static ArrayList<?> getArrayListObjectByKey(HashMap<String, ?> argObjectHashMap, String argKey) {
        methodName = "ArrayList<?> getArrayListObjectByKey(HashMap<String, ?> argObjectHashMap, String argKey)";
        //ArrayList<String>
        ArrayList<?> arrayList = null;
        if (argObjectHashMap.containsKey(argKey)) {
            Object object = argObjectHashMap.get(argKey);
            if (isList(object)) {
                arrayList = (ArrayList<?>) object;
            }
            return arrayList;
        }
        return arrayList;
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
//https://www.programcreek.com/java-api-examples/?class=org.json.JSONObject&method=keys
//https://www.quora.com/How-do-I-convert-JSONObject-to-HashMap-in-Java
//https://stackoverflow.com/questions/1066589/iterate-through-a-hashmap
//https://tympanus.net/codrops/2013/04/17/slide-and-push-menus/
//https://codepen.io/marclloyd77/pen/gtypB
//https://www.cssscript.com/tag/push-menu/
//http://responsive-nav.com/
//https://aatul.me/tag/passing-array-between-activities-in-android-using-intent-and-bundle/
//https://aatul.me/tag/passing-array-between-activities-in-android-using-intent-and-bundle/
//https://medium.com/the-wtf-files/the-mysterious-case-of-the-bundle-and-the-map-7b15279a794e

//https://beginnersbook.com/2013/12/how-to-loop-hashmap-in-java/
//
/*
try {
    //Map<String, Object> mapObject = FeedJSONParser.FeedJSONObject(argResponse);
    Map<String, Object> mapObject = JSONFastParser.JSONObjectFeed(argResponse);
    for (Map.Entry<String, Object> entry : mapObject.entrySet()) {
        String key = entry.getKey();
        Object value = entry.getValue();
        if (value instanceof List) {
            //LogWriter.Log("LIST_KEY: " + key);
        } else if (value instanceof String) {
            //LogWriter.Log("STRING_KEY: " + key);
        }
    }
} catch (JSONException e) {
    e.printStackTrace();
}
*/
//https://www.codeaffine.com/2015/03/04/map-distinct-value-types-using-java-generics/