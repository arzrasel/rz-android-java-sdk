package com.rzandroid.xmlparser;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONFastBuilder {
    private static String methodName = "methodName-var";

    public static Object getJSONString(Object argObject) throws JSONException {
        methodName = "Object getJSONString(Object argObject)";
        return CoreJSONFastBuilder.getJSONString(argObject);
    }

    public static boolean isEmptyObject(JSONObject argJSONObject) {
        methodName = "boolean isEmptyObject(JSONObject argJSONObject)";
        return CoreJSONFastBuilder.isEmptyObject(argJSONObject);
    }

    public static boolean isJson(String argJson) {
        methodName = "boolean isJson(String argJson)";
        return CoreJSONFastBuilder.isJson(argJson);
    }
}