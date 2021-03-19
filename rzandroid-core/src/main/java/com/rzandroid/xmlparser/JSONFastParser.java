package com.rzandroid.xmlparser;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;

public class JSONFastParser {
    private static String methodName = "methodName-var";

    public static HashMap<String, Object> JSONObjectFeed(String argJsonObject) throws JSONException {
        methodName = "HashMap<String, Object> JSONObjectFeed(String argJsonObject)";
        return CoreJSONFastParser.JSONObjectFeed(argJsonObject);
    }

    public static List<Object> JSONArrayFeed(String argJsonArray) throws JSONException {
        methodName = "List<Object> JSONArrayFeed(String argJsonArray)";
        return CoreJSONFastParser.JSONArrayFeed(argJsonArray);
    }

    public static boolean isMap(Object argObject) {
        methodName = "isMap(Object argObject)";
        //System.out.println("LOG_PRINT_OBJECT_TYPE: " + argObject.getClass());
        return CoreJSONFastParser.isMap(argObject);
    }

    public static boolean isList(Object argObject) {
        methodName = "isList(Object argObject)";
        //System.out.println("LOG_PRINT_OBJECT_TYPE: " + argObject.getClass());
        return CoreJSONFastParser.isList(argObject);
    }

    public static <T> T getObjectByKey(HashMap<String, ?> argObjectHashMap, String argKey) {
        methodName = "<T> T getObjectByKey(HashMap<String, ?> argObjectHashMap, String argKey)";
        return CoreJSONFastParser.getObjectByKey(argObjectHashMap, argKey);
    }

    public static <T> T getHashMapByKey(HashMap<String, ?> argObjectHashMap, String argKey) {
        methodName = "<T> T getHashMapByKey(HashMap<String, ?> argObjectHashMap, String argKey)";
        return CoreJSONFastParser.getHashMapByKey(argObjectHashMap, argKey);
    }

    /*@Deprecated
    public static HashMap<String, String> getHashMapStringByKey(HashMap<String, ?> argObjectHashMap, String argKey) {
        return CoreJSONFastParser.getHashMapStringByKey(argObjectHashMap, argKey);
    }*/

    public static <T> T getArrayListMapByKey(HashMap<String, ?> argObjectHashMap, String argKey) {
        methodName = "<T> T getArrayListMapByKey(HashMap<String, ?> argObjectHashMap, String argKey)";
        return CoreJSONFastParser.getArrayListMapByKey(argObjectHashMap, argKey);
    }

    /*@Deprecated
    public static ArrayList<HashMap<String, String>> getArrayListHashMapByKey(HashMap<String, ?> argObjectHashMap, String argKey) {
        return CoreJSONFastParser.getArrayListHashMapByKey(argObjectHashMap, argKey);
    }*/

    public static <T> T getArrayListByKey(HashMap<String, ?> argObjectHashMap, String argKey) {
        methodName = "<T> T getArrayListByKey(HashMap<String, ?> argObjectHashMap, String argKey)";
        return CoreJSONFastParser.getArrayListByKey(argObjectHashMap, argKey);
    }

    /*@Deprecated
    public static ArrayList<?> getArrayListObjectByKey(HashMap<String, ?> argObjectHashMap, String argKey) {
        return CoreJSONFastParser.getArrayListObjectByKey(argObjectHashMap, argKey);
    }*/
    public static boolean isJson(String argJson) {
        methodName = "boolean isJson(String argJson)";
        return CoreJSONFastParser.isJson(argJson);
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