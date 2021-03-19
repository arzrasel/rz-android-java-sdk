package com.rzandroid.xmlparser;

import android.content.Context;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

public class Usages {
    public static void onReadXML(Context argContext) throws IOException, ParserConfigurationException, SAXException, XmlPullParserException, Exception {
        /*try {
            onReadXML(context);
        } catch (Exception ex) {
            ex.printStackTrace();
        }*/
        XMLFeedParser xmlFeedParser = new XMLFeedParser(argContext);
        String xmlStr = xmlFeedParser.onReadAssetsFile("db_dir/test.xml");
        //|------------------------------------------------------------|
        //|------------------------------------------------------------|
        /*List<String> listXMLTagsTemp = new ArrayList<>();
        listXMLTagsTemp.add("audio_file");
        listXMLTagsTemp.add("main_word");
        listXMLTagsTemp.add("secondary_word");
        String xmlItemStartingTagTemp = "word_item";*/
        List<Map<String, String>> listItemsTemp = xmlFeedParser.withTag("audio_file")
                .withTag("main_word")
                .withTag("secondary_word")
                .withAttribute("length")
                .withAttribute("section")
                .onXMLPrepareItems(xmlStr)
                .getXMLParsedItems("word_item");
        List<Map<String, String>> listItemsAttr = xmlFeedParser.getAttributeItems();
        System.out.println("XML_TAG_LIST: " + listItemsTemp);
        System.out.println("XML_ATTR_LIST: " + listItemsAttr);
        //|------------------------------------------------------------|
        //|------------------------------------------------------------|
        List<String> listXMLTagsTemp = new ArrayList<>();
        listXMLTagsTemp.add("audio_file");
        listXMLTagsTemp.add("main_word");
        listXMLTagsTemp.add("secondary_word");
        String xmlItemStartingTagTemp = "word_item";
        List<Map<String, String>> listItemsTemp01 = xmlFeedParser.onXMLPrepareItems(xmlStr)
                .getXMLParsedItems(listXMLTagsTemp, xmlItemStartingTagTemp);
        System.out.println("XML_TAG_LIST: " + listItemsTemp01);
        //|------------------------------------------------------------|
        //|------------------------------------------------------------|
        //System.out.println("INIT_VALUE: " + xmlStr);
        String xmlTag = "word_list";
        String xmlAttr = "subjective_category";
        String xmlAttrValue = "BANK_MANAGER";
        xmlStr = xmlFeedParser.getXMLByTagAttribute(xmlStr, xmlTag, xmlAttr, xmlAttrValue);
        //System.out.println("ATTRIBUTE_VALUE: " + xmlStr);
        List<String> listXMLTags = new ArrayList<>();
        listXMLTags.add("audio_file");
        listXMLTags.add("main_word");
        listXMLTags.add("secondary_word");
        String xmlItemStartingTag = "word_item";
        List<Map<String, String>> listItems = xmlFeedParser.onXMLPrepareItems(xmlStr)
                .getXMLParsedItems(listXMLTags, xmlItemStartingTag);
        System.out.println("LIST: " + listItems.toString());
        for (Map<String, String> listItem : listItems) {
            for (Map.Entry<String, String> entry : listItem.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                System.out.println("XML_KEY: " + key + " - VALUE: " + value);
            }
        }
    }

    private void onJson() {
        String jsonObjectString = "{\"id\":4,\"reproductive_health_problem\":[{\"url\":\"http://103.108.144.134:8003/hmsapi/reproductive_health_problem/1/\",\"id\":1,\"name\":\"Hormone-related problems\",\"bn_name\":\"test name\",\"gender\":\"cow\",\"description\":\"test description\"}],\"genetic_disease\":[{\"url\":\"http://103.108.144.134:8003/hmsapi/disease/1/\",\"id\":1,\"name\":\"Premature abortion\",\"bn_name\":\"bangla name\",\"type\":\"g\",\"description\":\"\"}],\"insurance_company\":{\"url\":\"http://103.108.144.134:8003/hmsapi/farm/3/\",\"id\":3,\"name\":\"Shurjomukhi\",\"bn_name\":\"bangla name\"},\"insurance_type\":{\"url\":\"http://103.108.144.134:8003/hmsapi/insurance_type/3/\",\"id\":3,\"name\":\"Theft\",\"bn_name\":\"bangla name\"},\"bolus\":{\"url\":\"http://103.108.144.134:8003/hmsapi/bolus/4/\",\"id\":4,\"type\":\"BB\",\"bolus_number\":\"B-004\",\"description\":\"\"},\"cow_type\":{\"url\":\"http://103.108.144.134:8003/hmsapi/cow_type/3/\",\"id\":3,\"name\":\"Jersey\",\"bn_name\":\"bangla name\",\"description\":\"bangla name\"},\"farm\":{\"url\":\"http://103.108.144.134:8003/hmsapi/farm/2/\",\"id\":2,\"name\":\"Shurjo R & D\",\"bn_name\":\"bangla name\",\"farm_no\":\"02\",\"thana\":2,\"farmer\":[2],\"address\":\"\"},\"group\":{\"id\":3,\"location\":\"Uttora\",\"name\":\"Less milk producers\",\"bn_name\":\"bangla name\",\"feeds\":\"fresh\",\"animal_farm\":2},\"genetic_percentage\":{\"url\":\"http://103.108.144.134:8003/hmsapi/genetic_percentage/3/\",\"id\":3,\"name\":\"87.5\",\"bn_name\":\"87.5\"},\"cow_image\":[{\"url\":\"http://103.108.144.134:8003/hmsapi/cow_image/10/\",\"id\":10,\"cow\":4,\"type\":\"L\",\"image\":\"http://103.108.144.134:8003/media/cow-image/download.png\"},{\"url\":\"http://103.108.144.134:8003/hmsapi/cow_image/11/\",\"id\":11,\"cow\":4,\"type\":\"R\",\"image\":\"http://103.108.144.134:8003/media/cow-image/images_10.jpg\"},{\"url\":\"http://103.108.144.134:8003/hmsapi/cow_image/12/\",\"id\":12,\"cow\":4,\"type\":\"F\",\"image\":\"http://103.108.144.134:8003/media/cow-image/images_1.jpg\"}],\"bolused_date\":\"2018-05-01\",\"cow_name\":\"Priyanka\",\"ear_tag\":\"Er-10004\",\"age\":\"9.00\",\"weight\":\"54.00\",\"previous_farm\":\"Na\",\"gender\":\"cow\",\"insurance_number\":\"1005\",\"reproductive_health\":true,\"avg_milk_production\":\"3.000\",\"is_lacting\":true,\"number_of_offsprings\":2,\"last_offsprings_delivered_date\":\"2018-06-02\"}";
        String jsonArrayString = "[{\"url\":\"http://103.108.144.134:8003/hmsapi/reproductive_health_problem/1/\",\"id\":1,\"name\":\"Hormone-related problems\",\"bn_name\":\"test name\",\"gender\":\"cow\",\"description\":\"test description\"}]";
        try {
            HashMap<String, Object> jsonHashMapData = JSONFastParser.JSONObjectFeed(jsonObjectString);
            List<Object> jsonListData = JSONFastParser.JSONArrayFeed(jsonArrayString);
            System.out.println(jsonHashMapData.toString());
            System.out.println(jsonListData.toString());
            if (JSONFastParser.isMap(jsonHashMapData)) {
            }
            if (JSONFastParser.isList(jsonListData)) {
            }
            System.out.println("getObjectByKey: " + JSONFastParser.getObjectByKey(jsonHashMapData, "reproductive_health_problem"));
            System.out.println("getHashMapByKey: " + JSONFastParser.getHashMapByKey(jsonHashMapData, "reproductive_health_problem"));
            System.out.println("getArrayListMapByKey: " + JSONFastParser.getArrayListMapByKey(jsonHashMapData, "reproductive_health_problem"));
            System.out.println("getArrayListByKey: " + JSONFastParser.getArrayListByKey(jsonHashMapData, "reproductive_health_problem"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void onJSONFastBuilde() {
        List<HashMap<String, Object>> listHashMap = new ArrayList<>();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("key01", "value01");
        hashMap.put("key02", "value02");
        hashMap.put("key03", "value03");
        hashMap.put("key04", "value04");
        List<String> list = new ArrayList<>();
        list.add("list01");
        list.add("list02");
        list.add("list03");
        list.add("list04");
        listHashMap.add(hashMap);
        hashMap.put("listKey01", list);
        listHashMap.add(hashMap);
        try {
            String str = JSONFastBuilder.getJSONString(listHashMap) + "";
            System.out.println(str);
            System.out.println(JSONFastBuilder.isEmptyObject(new JSONObject(str)));
            str = JSONFastBuilder.getJSONString(hashMap) + "";
            System.out.println(str);
            System.out.println(JSONFastBuilder.isEmptyObject(new JSONObject(str)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}