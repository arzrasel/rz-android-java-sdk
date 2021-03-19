package com.rzandroid.xmlparser;

import android.content.Context;

import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class XMLFeedParser {
    private Context context;
    private CoreXMLFeedParser coreXMLFeedParser;
    private XmlPullParserFactory xmlFactoryObject;
    private XmlPullParser xmlPullParser;
    private InputStream inputStream;
    private String xmlFilePath;
    private boolean isXMLStringNull = false;
    private static String methodName = "methodName-var";

    public XMLFeedParser(Context argContext) {
        methodName = "XMLFeedParser(Context argContext)";
        this.context = argContext;
        isXMLStringNull = false;
        coreXMLFeedParser = new CoreXMLFeedParser(argContext);
    }

    /**
     * Function used to fetch an XML file from assets folder
     *
     * @param argFileName - XML file name to convert it to String
     * @return - return XML in String form
     */
    public String onReadAssetsFile(String argFileName) throws IOException {
        methodName = "String onReadAssetsFile(String argFileName)";
        return coreXMLFeedParser.onReadAssetsFile(argFileName);
    }

    public XMLFeedParser withTag(String argTagKey) {
        methodName = "XMLFeedParser withTag(String argTagKey)";
        //coreXMLFeedParser.withTag(argTagKey);
        return this;
    }

    public XMLFeedParser withAttribute(String argAttributeKey) {
        methodName = "XMLFeedParser withAttribute(String argAttributeKey)";
        //coreXMLFeedParser.withAttribute(argAttributeKey);
        return this;
    }

    public XMLFeedParser onXMLPrepareItems(String argXMLString) throws XmlPullParserException, UnsupportedEncodingException, IOException {
        methodName = "XMLFeedParser onXMLPrepareItems(String argXMLString)";
        //coreXMLFeedParser.onXMLPrepareItems(argXMLString);
        return this;
    }

    //|------------------------------------------------------------|
    public List<Map<String, String>> getXMLParsedItems(String argItemStartingEndingTag) throws Exception {
        methodName = "List<Map<String, String>> getXMLParsedItems(String argItemStartingEndingTag)";
        return coreXMLFeedParser.getXMLParsedItems(argItemStartingEndingTag);
    }
    //|------------------------------------------------------------|

    public List<Map<String, String>> getXMLParsedItems(List<String> argKeyList, String argItemStartingEndingTag) throws Exception {
        methodName = "List<Map<String, String>> getXMLParsedItems(List<String> argKeyList, String argItemStartingEndingTag)";
        return coreXMLFeedParser.getXMLParsedItems(argKeyList, argItemStartingEndingTag);
    }

    //|------------------------------------------------------------|
    public List<Map<String, String>> getAttributeItems() {
        methodName = "List<Map<String, String>> getAttributeItems()";
        return coreXMLFeedParser.getAttributeItems();
    }

    //|------------------------------------------------------------|
    public String getXMLByTagAttribute(String argXMLString, String argXMLTag, String argXMLAttribute, String argXMLAttributeValue) throws IOException, SAXException, ParserConfigurationException, TransformerException {
        methodName = "String getXMLByTagAttribute(String argXMLString, String argXMLTag, String argXMLAttribute, String argXMLAttributeValue)";
        return coreXMLFeedParser.getXMLByTagAttribute(argXMLString, argXMLTag, argXMLAttribute, argXMLAttributeValue);
    }
    //|------------------------------------------------------------|
}