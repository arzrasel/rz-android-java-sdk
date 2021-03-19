package com.rzandroid.xmlparser;
import android.content.Context;
import android.content.res.AssetManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

class CoreXMLFeedParser {
    private Context context;
    private XmlPullParserFactory xmlFactoryObject;
    private XmlPullParser xmlPullParser;
    private InputStream inputStream;
    private String xmlFilePath;
    private boolean isXMLStringNull = false;
    /*private TreeMap<String, String> tagTreeMap;
    private TreeMap<String, String> attributeTreeMap;*/
    private HashSet<String> tagHashSet;
    private HashSet<String> attributeHashSet;
    private List<Map<String, String>> listXMLAttributeItems;
    private static String methodName = "methodName-var";

    public CoreXMLFeedParser(Context argContext) {
        methodName = "CoreXMLFeedParser(Context argContext)";
        this.context = argContext;
        tagHashSet = new HashSet<>();
        tagHashSet.clear();
        attributeHashSet = new HashSet<>();
        attributeHashSet.clear();
        listXMLAttributeItems = new ArrayList<>();
        listXMLAttributeItems.clear();
        isXMLStringNull = false;
    }

    /**
     * Function used to fetch an XML file from assets folder
     *
     * @param argFileName - XML file name to convert it to String
     * @return - return XML in String form
     */
    protected String onReadAssetsFile(String argFileName) throws IOException {
        methodName = "String onReadAssetsFile(String argFileName)";
        String xmlString = null;
        AssetManager assetManager = context.getAssets();
        try {
            InputStream inputStream = assetManager.open(argFileName);
            int length = inputStream.available();
            byte[] byteArray = new byte[length];
            inputStream.read(byteArray);
            inputStream.close();
            xmlString = new String(byteArray);
            //String result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw ex;
        }
        return xmlString;
        //https://itekblog.com/load-your-custom-xml-from-resources-android/
        //http://www.vogella.com/tutorials/AndroidXML/article.html
    }

    protected CoreXMLFeedParser withTag(String argTagKey) {
        methodName = "CoreXMLFeedParser withTag(String argAttributeKey)";
        if (!isNullOrEmpty(argTagKey)) {
            tagHashSet.add(argTagKey);
        }
        return this;
    }

    protected CoreXMLFeedParser withAttribute(String argAttributeKey) {
        methodName = "CoreXMLFeedParser withAttribute(String argAttributeKey)";
        if (!isNullOrEmpty(argAttributeKey)) {
            attributeHashSet.add(argAttributeKey);
        }
        return this;
    }

    protected CoreXMLFeedParser onXMLPrepareItems(String argXMLString) throws XmlPullParserException, UnsupportedEncodingException, IOException {
        methodName = "CoreXMLFeedParser onXMLPrepareItems(String argXMLString)";
        //String xmlString = onReadAssetsFile(argFileName);
        //System.out.println("XML_DATA: " + xmlString);
        /*if (tagHashSet.size() <= 0) {
            return this;
        }*/
        if (isNullOrEmpty(argXMLString)) {
            isXMLStringNull = true;
            return this;
        }
        try {
            xmlFactoryObject = XmlPullParserFactory.newInstance();
            xmlPullParser = xmlFactoryObject.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            //xmlPullParser.setInput(xmlString, null);
            InputStream inputStream = new ByteArrayInputStream(argXMLString.getBytes("UTF8"));
            xmlPullParser.setInput(inputStream, null);
            inputStream.close();
        } catch (XmlPullParserException ex) {
            throw ex;
        } catch (UnsupportedEncodingException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
            //https://stackoverflow.com/questions/2912565/throwing-multiple-exceptions-in-java/29225574
        }
        return this;
    }

    //|------------------------------------------------------------|
    protected List<Map<String, String>> getXMLParsedItems(String argItemStartingEndingTag) throws Exception {
        methodName = "List<Map<String, String>> getXMLParsedItems(String argItemStartingEndingTag)";
        int eventType;
        List<Map<String, String>> listXMLTagItems = new ArrayList<>();
        if (tagHashSet.size() <= 0) {
            return listXMLTagItems;
        }
        Map<String, String> mapXMLTagItems = new HashMap<>();
        listXMLAttributeItems = new ArrayList<>();
        Map<String, String> mapXMLAttributeItems = new HashMap<>();
        if (isXMLStringNull) {
            return listXMLTagItems;
        }
        try {
            //String currentTag = null;
            eventType = xmlPullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String xmlTagName = null;
                String TAG_XML_ITEM_STARTING_ENDING = argItemStartingEndingTag;
                //String name = argXmlPullParser.getName();
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        //products = new ArrayList();
                        listXMLTagItems.clear();
                        listXMLAttributeItems.clear();
                        break;
                    case XmlPullParser.START_TAG:
                        xmlTagName = xmlPullParser.getName();
                        //LogWriter.Log("TAG_NAME: " + xmlTagName);
                        if (xmlTagName.equals(TAG_XML_ITEM_STARTING_ENDING)) {
                            mapXMLTagItems = new HashMap<>();
                            mapXMLTagItems.clear();
                            mapXMLAttributeItems = new HashMap<>();
                            mapXMLAttributeItems.clear();
                        } else {
                            //int rid = resources.getIdentifier(packageName + ":raw/" + fileName, null, null);
                            //String key = argKeyList();
                            /*if (tagName.equals(TAG_XML_LAT)) {
                                strXmlText = argXmlPullParser.nextText();
                            }*/
                            //LogWriter.Log("TAG_NAME_OUTER: " + xmlTagName);
                            /*if (!xmlPullParser.getName().equals(TAG_XML_ITEM_STARTING_ENDING)) {
                                //currentTag = xmlPullParser.getName();
                                System.out.println("getAttributeValue--------------->" + xmlPullParser.getAttributeValue(null, "section"));
                                //System.out.println("getAttributeValue---------------++" + xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "section"));
                                //System.out.println("getAttributeValue---------------++" + xmlPullParser.getAttributeType(0));
                                //System.out.println("getAttributeName---------------++" + xmlPullParser.getAttributeName(0));
                                //System.out.println("--------------->" + xmlPullParser.getAttributeValue(null, "section"));
                            }*/
                            if (containsSetKey(tagHashSet, xmlTagName)) {
                                /*System.out.println("--------------->" + xmlPullParser.getAttributeValue(null, "section"));
                                System.out.println("GET_TEXT--------------->" + xmlPullParser.getText());*/
                                //LogWriter.Log("TAG_NAME_INNER: " + xmlTagName);
                                //System.out.println("getAttributeValue--------------->" + xmlPullParser.getAttributeValue(null, "section"));
                                //|------------------------------------------------------------|
                                //|------------------------------------------------------------|
                                String attributeName = null;
                                String attributeValue = null;
                                if (xmlPullParser.getAttributeCount() > 0) {
                                    attributeName = xmlPullParser.getAttributeName(0);
                                    attributeValue = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, attributeName);
                                    //System.out.println(attributeName + " -- " + attributeValue);
                                }
                                //|------------------------------------------------------------|
                                //|------------------------------------------------------------|
                                String strXmlText = xmlPullParser.nextText();
                                strXmlText = strXmlText.replaceAll("\\s+", " ").trim();
                                //For left trim:
                                strXmlText = strXmlText.replaceAll("^\\s+", "");
                                //For right trim:
                                strXmlText = strXmlText.replaceAll("\\s+$", "");
                                strXmlText = strXmlText.trim();
                                //LogWriter.Log("TAG_VALUE: ->" + strXmlText + "<-");
                                //mapXMLTagItems.put(xmlTagName, strXmlText);
                                //|------------------------------------------------------------|
                                //|------------------------------------------------------------|
                                mapXMLTagItems.put(xmlTagName, strXmlText);
                                if (containsSetKey(attributeHashSet, attributeName)) {
                                    mapXMLAttributeItems.put(attributeName, attributeValue);
                                } else {
                                    mapXMLAttributeItems.put(xmlTagName, "attribute_not_found");
                                }
                                //|------------------------------------------------------------|
                                //|------------------------------------------------------------|
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        //currentTag = null;
                        xmlTagName = xmlPullParser.getName();
                        if (xmlTagName.equalsIgnoreCase(TAG_XML_ITEM_STARTING_ENDING) && listXMLTagItems != null) {
                            listXMLTagItems.add(mapXMLTagItems);
                            if (listXMLAttributeItems != null) {
                                listXMLAttributeItems.add(mapXMLAttributeItems);
                            }
                        }
                        break;
                }
                eventType = xmlPullParser.next();
            }
            //parsingComplete = false;
        } catch (Exception ex) {
            //ex.printStackTrace();
            throw ex;
        }
        //printProducts(products);
            /*TextView txtView = (TextView) findViewById(R.id.textView1);
            txtView.setText("Name : " + a + "\nPrice : " + b + "\nDescription : "
                    + c + "\nCalories : " + d);*/
        //adapterLvTeam.notifyDataSetChanged();
        //this.taskHandelListener.setOnTaskEndListener(lstLocationPoints);
        return listXMLTagItems;
    }

    //|------------------------------------------------------------|
    @Deprecated
    protected List<Map<String, String>> getXMLParsedItems(List<String> argKeyList, String argItemStartingEndingTag) throws Exception {
        methodName = "List<Map<String, String>> getXMLParsedItems(List<String> argKeyList, String argItemStartingEndingTag)";
        int eventType;
        List<Map<String, String>> listXMLTagItems = new ArrayList<>();
        Map<String, String> mapXMLTagItems = new HashMap<>();
        listXMLAttributeItems = new ArrayList<>();
        Map<String, String> mapXMLAttributeItems = new HashMap<>();
        //attributeHashSet.add("section");
        if (isXMLStringNull) {
            return listXMLTagItems;
        }
        try {
            //String currentTag = null;
            eventType = xmlPullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String xmlTagName = null;
                String TAG_XML_ITEM_STARTING_ENDING = argItemStartingEndingTag;
                //String name = argXmlPullParser.getName();
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        //products = new ArrayList();
                        listXMLTagItems.clear();
                        listXMLAttributeItems.clear();
                        break;
                    case XmlPullParser.START_TAG:
                        xmlTagName = xmlPullParser.getName();
                        //LogWriter.Log("TAG_NAME: " + xmlTagName);
                        if (xmlTagName.equals(TAG_XML_ITEM_STARTING_ENDING)) {
                            mapXMLTagItems = new HashMap<>();
                            mapXMLTagItems.clear();
                            mapXMLAttributeItems = new HashMap<>();
                            mapXMLAttributeItems.clear();
                        } else {
                            //int rid = resources.getIdentifier(packageName + ":raw/" + fileName, null, null);
                            //String key = argKeyList();
                            /*if (tagName.equals(TAG_XML_LAT)) {
                                strXmlText = argXmlPullParser.nextText();
                            }*/
                            //LogWriter.Log("TAG_NAME_OUTER: " + xmlTagName);
                            /*if (!xmlPullParser.getName().equals(TAG_XML_ITEM_STARTING_ENDING)) {
                                //currentTag = xmlPullParser.getName();
                                System.out.println("getAttributeValue--------------->" + xmlPullParser.getAttributeValue(null, "section"));
                                //System.out.println("getAttributeValue---------------++" + xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "section"));
                                //System.out.println("getAttributeValue---------------++" + xmlPullParser.getAttributeType(0));
                                //System.out.println("getAttributeName---------------++" + xmlPullParser.getAttributeName(0));
                                //System.out.println("--------------->" + xmlPullParser.getAttributeValue(null, "section"));
                            }*/
                            if (containsListKey(argKeyList, xmlTagName)) {
                                /*System.out.println("--------------->" + xmlPullParser.getAttributeValue(null, "section"));
                                if (xmlPullParser.getAttributeCount() > 0) {
                                    System.out.println("--------------->" + xmlPullParser.getAttributeName(0));
                                }*/
                                //System.out.println("--------------->" + xmlPullParser.getAttributeCount());
                                //System.out.println("--------------->" + xmlPullParser.getName());
                                //System.out.println("GET_TEXT--------------->" + xmlPullParser.getText());
                                //LogWriter.Log("TAG_NAME_INNER: " + xmlTagName);
                                //|------------------------------------------------------------|
                                //|------------------------------------------------------------|
                                String attributeName = null;
                                String attributeValue = null;
                                if (xmlPullParser.getAttributeCount() > 0) {
                                    attributeName = xmlPullParser.getAttributeName(0);
                                    attributeValue = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, attributeName);
                                    //System.out.println(attributeName + " -- " + attributeValue);
                                }
                                //|------------------------------------------------------------|
                                //|------------------------------------------------------------|
                                String strXmlText = xmlPullParser.nextText();
                                strXmlText = strXmlText.replaceAll("\\s+", " ").trim();
                                //For left trim:
                                strXmlText = strXmlText.replaceAll("^\\s+", "");
                                //For right trim:
                                strXmlText = strXmlText.replaceAll("\\s+$", "");
                                strXmlText = strXmlText.trim();
                                //LogWriter.Log("TAG_VALUE: ->" + strXmlText + "<-");
                                //|------------------------------------------------------------|
                                //|------------------------------------------------------------|
                                mapXMLTagItems.put(xmlTagName, strXmlText);
                                if (containsSetKey(attributeHashSet, attributeName)) {
                                    mapXMLAttributeItems.put(attributeName, attributeValue);
                                } else {
                                    mapXMLAttributeItems.put(xmlTagName, "attribute_not_found");
                                }
                                //|------------------------------------------------------------|
                                //|------------------------------------------------------------|
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        //currentTag = null;
                        xmlTagName = xmlPullParser.getName();
                        /*System.out.println("------------> " + mapXMLAttributeItems.toString());
                        System.out.println("------------) " + mapXMLTagItems.toString());*/
                        if (xmlTagName.equalsIgnoreCase(TAG_XML_ITEM_STARTING_ENDING) && listXMLTagItems != null) {
                            listXMLTagItems.add(mapXMLTagItems);
                            if (listXMLAttributeItems != null) {
                                listXMLAttributeItems.add(mapXMLAttributeItems);
                            }
                        }
                        break;
                }
                eventType = xmlPullParser.next();
            }
            //parsingComplete = false;
        } catch (Exception ex) {
            //ex.printStackTrace();
            throw ex;
        }
        //printProducts(products);
            /*TextView txtView = (TextView) findViewById(R.id.textView1);
            txtView.setText("Name : " + a + "\nPrice : " + b + "\nDescription : "
                    + c + "\nCalories : " + d);*/
        //adapterLvTeam.notifyDataSetChanged();
        //this.taskHandelListener.setOnTaskEndListener(lstLocationPoints);
        return listXMLTagItems;
    }

    //|------------------------------------------------------------|
    protected List<Map<String, String>> getAttributeItems() {
        methodName = "List<Map<String, String>> getAttributeItems()";
        return listXMLAttributeItems;
    }

    //|------------------------------------------------------------|
    private boolean containsListKey(List<String> argKeyList, String argSearchKey) {
        methodName = "boolean containsListKey(List<String> argKeyList, String argSearchKey)";
        if (isNullOrEmpty(argSearchKey)) {
            return false;
        }
        for (String item : argKeyList) {
            if (isNullOrEmpty(item)) {
                return false;
            }
            argSearchKey = argSearchKey.toLowerCase();
            if (item.trim().toLowerCase().contains(argSearchKey))
                return true;
        }
        return false;
    }

    //|------------------------------------------------------------|
    private boolean containsListKey(ArrayList<String> argKeyList, String argSearchKey) {
        methodName = "boolean containsListKey(ArrayList<String> argKeyList, String argSearchKey)";
        if (isNullOrEmpty(argSearchKey)) {
            return false;
        }
        for (String item : argKeyList) {
            if (isNullOrEmpty(item)) {
                return false;
            }
            argSearchKey = argSearchKey.toLowerCase();
            if (item.trim().toLowerCase().contains(argSearchKey))
                return true;
        }
        return false;
    }

    //|------------------------------------------------------------|
    private boolean containsSetKey(Set<String> argKeyList, String argSearchKey) {
        methodName = "boolean containsListKey(Set<String> argKeyList, String argSearchKey)";
        if (isNullOrEmpty(argSearchKey)) {
            return false;
        }
        for (String item : argKeyList) {
            if (isNullOrEmpty(item)) {
                return false;
            }
            argSearchKey = argSearchKey.toLowerCase();
            if (item.trim().toLowerCase().contains(argSearchKey))
                return true;
        }
        return false;
    }

    //|------------------------------------------------------------|
    private boolean containsSetKey(HashSet<String> argKeyList, String argSearchKey) {
        methodName = "boolean containsListKey(HashSet<String> argKeyList, String argSearchKey)";
        if (isNullOrEmpty(argSearchKey)) {
            return false;
        }
        for (String item : argKeyList) {
            if (isNullOrEmpty(item)) {
                return false;
            }
            argSearchKey = argSearchKey.toLowerCase();
            if (item.trim().toLowerCase().contains(argSearchKey))
                return true;
        }
        return false;
    }

    //|------------------------------------------------------------|
    protected String getXMLByTagAttribute(String argXMLString, String argXMLTag, String argXMLAttribute, String argXMLAttributeValue) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        methodName = "String getXMLByTagAttribute(String argXMLString, String argXMLTag, String argXMLAttribute, String argXMLAttributeValue)";
        if (isNullOrEmpty(argXMLString)) {
            return null;
        }
        //https://stackoverflow.com/questions/11863038/how-to-get-the-attribute-value-of-an-xml-node-using-java
        InputSource inputSource = new InputSource(new StringReader(argXMLString));
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setCoalescing(true);
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputSource);
            NodeList nodeList = document.getElementsByTagName(argXMLTag); //drawer_menu_group
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node currentItem = nodeList.item(i);
                String key = currentItem.getAttributes().getNamedItem(argXMLAttribute).getNodeValue(); //category
                //System.out.println(key);
                if (key.equalsIgnoreCase(argXMLAttributeValue)) { //App Self System
                    //System.out.println("NODE: " + currentItem.toString());
                    return getNodeToXMLString(currentItem);
                }
            }
        } catch (ParserConfigurationException ex) {
            //ex.printStackTrace();
            throw ex;
        } catch (SAXException ex) {
            //ex.printStackTrace();
            throw ex;
        } catch (IOException ex) {
            //ex.printStackTrace();
            throw ex;
        }
        return null;
    }

    //|------------------------------------------------------------|
    private String getNodeListToXMLString(NodeList argNodeList) throws TransformerConfigurationException, TransformerException {
        methodName = "String getNodeListToXMLString(NodeList argNodeList)";
        NodeList nodeList = argNodeList;
        DOMSource domSource = new DOMSource();
        StringWriter stringWriter = new StringWriter();
        StreamResult streamResult = new StreamResult(stringWriter);
        Transformer transformer = null;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            //transformer.transform(new DOMSource(nodeList), new StreamResult(stringWriter));
            //serializer.transform(new DOMSource(soaphead.item(0)), new StreamResult(sw));
            for (int i = 0; i < nodeList.getLength(); ++i) {
                domSource.setNode(nodeList.item(i));
                transformer.transform(domSource, streamResult);
            }
            /*String result = stringWriter.toString();
            System.out.println("XML_DATA: " + result);*/
            return stringWriter.toString();
        } catch (TransformerConfigurationException ex) {
            //ex.printStackTrace();
            throw ex;
        } catch (TransformerException ex) {
            //ex.printStackTrace();
            throw ex;
        }
        //return null;
    }

    private String getNodeToXMLString(Node argNode) throws TransformerConfigurationException, TransformerException {
        methodName = "String getNodeToXMLString(Node argNode)";
        Node node = argNode;
        DOMSource domSource = new DOMSource();
        StringWriter stringWriter = new StringWriter();
        StreamResult streamResult = new StreamResult(stringWriter);
        Transformer transformer = null;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            //transformer.transform(new DOMSource(nodeList), new StreamResult(stringWriter));
            //serializer.transform(new DOMSource(soaphead.item(0)), new StreamResult(sw));
            transformer.transform(new DOMSource(node), new StreamResult(stringWriter));
            /*for (int i = 0; i < nodeList.getLength(); ++i) {
                domSource.setNode(nodeList.item(i));
                transformer.transform(domSource, streamResult);
            }*/
            //String result = stringWriter.toString();
            //System.out.println("XML_DATA: " + result);
            return stringWriter.toString();
        } catch (TransformerConfigurationException ex) {
            //ex.printStackTrace();
            throw ex;
        } catch (TransformerException ex) {
            //ex.printStackTrace();
            throw ex;
        }
        //return null;
    }

    //|------------------------------------------------------------|
    private static boolean isNullOrEmpty(String argValue) {
        methodName = "boolean isNullOrEmpty(String argValue)";
        if (argValue == null) {
            return true;
        }
        argValue = argValue.replaceAll("\\s+", "");
        if (argValue.trim().isEmpty()) {
            return true;
        }
        if (argValue.equalsIgnoreCase("null")) {
            return true;
        }
        return false;
    }

    //|------------------------------------------------------------|
    @Deprecated
    private void onDocumentBuilderFactory(String argXmlString) {
        methodName = "void onDocumentBuilderFactory(String argXmlString)";
        //https://stackoverflow.com/questions/14236767/sax-parsing-how-to-fetch-child-nodes
        InputSource inputSource = new InputSource(new StringReader(argXmlString));
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setCoalescing(true);
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputSource);
            NodeList nodeList = document.getElementsByTagName("drawer_menu_group");
            DOMSource domSource = new DOMSource();
            StringWriter stringWriter = new StringWriter();
            StreamResult streamResult = new StreamResult(stringWriter);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            //transformer.transform(new DOMSource(nodeList), new StreamResult(stringWriter));
            //serializer.transform(new DOMSource(soaphead.item(0)), new StreamResult(sw));
            for (int i = 0; i < nodeList.getLength(); ++i) {
                domSource.setNode(nodeList.item(i));
                transformer.transform(domSource, streamResult);
            }
            String result = stringWriter.toString();
            System.out.println("XML_DATA: " + result);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
    //|------------------------------------------------------------|
}