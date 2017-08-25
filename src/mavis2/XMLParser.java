/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mavis2;

import java.io.File;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author rimsha@geeks
 */
public class XMLParser {

    private File xmlFile = null;
    private Document doc = null;
    private int totalComponents = 0;
    private int cCount = 0;

    public XMLParser(File xmlFile) {
        this.xmlFile = xmlFile;
    }

    public Map parseXML() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            dBuilder.setErrorHandler(new SimpleErrorHandler());
//            File fXmlFile = new File(this.xmlFile);
            doc = dBuilder.parse(this.xmlFile);

            doc.getDocumentElement().normalize();

            NodeList resultNode = doc.getChildNodes();

            HashMap result = new HashMap();
            MyNodeList tempNodeList = new MyNodeList();

            String emptyNodeName = null, emptyNodeValue = null;

            for (int index = 0; index < resultNode.getLength(); index++) {
                Node tempNode = resultNode.item(index);

                if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                    tempNodeList.addNode(tempNode);
                }
                emptyNodeName = tempNode.getNodeName();
                emptyNodeValue = tempNode.getNodeValue();
            }

            if (tempNodeList.getLength() == 0 && emptyNodeName != null
                    && emptyNodeValue != null) {
                result.put(emptyNodeName, emptyNodeValue);
                return result;
            }

            this.parseXMLNode(tempNodeList, result);
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private void parseXMLNode(NodeList nList, HashMap result) {
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE
                    && nNode.hasChildNodes()
                    && nNode.getFirstChild() != null
                    && (nNode.getFirstChild().getNextSibling() != null
                    || nNode.getFirstChild().hasChildNodes())) {
                NodeList childNodes = nNode.getChildNodes();
                MyNodeList tempNodeList = new MyNodeList();
                for (int index = 0; index < childNodes.getLength(); index++) {
                    Node tempNode = childNodes.item(index);
                    if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                        tempNodeList.addNode(tempNode);
                    }
                }
                HashMap dataHashMap = new HashMap();
                if (result.containsKey(nNode.getNodeName()) && result.get(nNode.getNodeName()) instanceof List) {
                    List mapExisting = (List) result.get(nNode.getNodeName());
                    mapExisting.add(dataHashMap);
                } else if (result.containsKey(nNode.getNodeName())) {
                    List counterList = new ArrayList();
                    counterList.add(result.get(nNode.getNodeName()));
                    counterList.add(dataHashMap);
                    result.put(nNode.getNodeName(), counterList);
                } else {
                    result.put(nNode.getNodeName(), dataHashMap);
                }
                if (nNode.getAttributes().getLength() > 0) {
                    Map attributeMap = new HashMap();
                    for (int attributeCounter = 0;
                            attributeCounter < nNode.getAttributes().getLength();
                            attributeCounter++) {
                        attributeMap.put(
                                nNode.getAttributes().item(attributeCounter).getNodeName(),
                                nNode.getAttributes().item(attributeCounter).getNodeValue()
                        );
                    }
                    dataHashMap.put("attributes", attributeMap);
                }
                this.parseXMLNode(tempNodeList, dataHashMap);
            } else if (nNode.getNodeType() == Node.ELEMENT_NODE
                    && nNode.hasChildNodes() && nNode.getFirstChild() != null
                    && nNode.getFirstChild().getNextSibling() == null) {
                this.putValue(result, nNode);
            } else if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                this.putValue(result, nNode);
            }
        }
    }

    private void putValue(HashMap result, Node nNode) {
        HashMap attributeMap = new HashMap();
        Object nodeValue = null;
        if (nNode.getFirstChild() != null) {
            nodeValue = nNode.getFirstChild().getNodeValue();
            if (nodeValue != null) {
                nodeValue = nodeValue.toString().trim();
            }
        }
        HashMap nodeMap = new HashMap();
        nodeMap.put("value", nodeValue);
        Object putNode = nodeValue;
        if (nNode.getAttributes().getLength() > 0) {
            for (int attributeCounter = 0;
                    attributeCounter < nNode.getAttributes().getLength();
                    attributeCounter++) {
                attributeMap.put(
                        nNode.getAttributes().item(attributeCounter).getNodeName(),
                        nNode.getAttributes().item(attributeCounter).getNodeValue()
                );
            }
            nodeMap.put("attributes", attributeMap);
            putNode = nodeMap;
        }
        if (result.containsKey(nNode.getNodeName()) && result.get(nNode.getNodeName()) instanceof List) {
            List mapExisting = (List) result.get(nNode.getNodeName());
            mapExisting.add(putNode);
        } else if (result.containsKey(nNode.getNodeName())) {
            List counterList = new ArrayList();
            counterList.add(result.get(nNode.getNodeName()));
            counterList.add(putNode);
            result.put(nNode.getNodeName(), counterList);
        } else {
            result.put(nNode.getNodeName(), putNode);
        }
    }

    public int getTotalComponents(Map xmlMap) {
        int ret = 0;
        Object value = xmlMap.get("mavis");
        Map TWork = (Map) value;
        Object TitleWork = TWork.get("TitleWork");
        if (TitleWork instanceof Map) {
            Map tNode = (Map) TitleWork;
            Object components = tNode.get("components");
            Map maa = (Map) components;
            Iterator ite = maa.entrySet().iterator();
            while (ite.hasNext()) {
                Map.Entry pairs = (Map.Entry) ite.next();
                Object avalue = pairs.getValue();
                if (avalue instanceof List) {
                    ret = ret + ((List) avalue).size();
                } else {
                    ret = ret + 1;
                }
            }
        } else if (TitleWork instanceof List) {
            List list = (List) TitleWork;
            for (Integer in = 0; in < list.size(); in++) {
                Map tNode = (Map) list.get(in);
                Object components = tNode.get("components");
                Map maa = (Map) components;
                Iterator ite = maa.entrySet().iterator();
                while (ite.hasNext()) {
                    Map.Entry pairs = (Map.Entry) ite.next();
                    Object avalue = pairs.getValue();
                    if (avalue instanceof List) {
                        ret = ret + ((List) avalue).size();
                    } else {
                        ret = ret + 1;
                    }
                }
            }
        }
        return ret;
    }

    public int getTotalTitles(Map xmlMap) {
        Object value = xmlMap.get("mavis");
        Map mavis = (Map) value;
        Object mAttibutes = mavis.get("attributes");
        Map m = (Map) mAttibutes;
        Map TWork = (Map) value;
        Object TitleWork = TWork.get("TitleWork");
        if (TitleWork instanceof List) {
            return ((List) TitleWork).size();
        }
        return 1;
    }

    public int getTotalCarriers(Map xmlMap) {
        Object value = xmlMap.get("mavis");
        Map TWork = (Map) value;
        Object TitleWork = TWork.get("TitleWork");
        if (TitleWork instanceof Map) {
            Map tNode = (Map) TitleWork;
            Object components = tNode.get("components");
            Map maa = (Map) components;
            Iterator ite = maa.entrySet().iterator();
            while (ite.hasNext()) {
                Map.Entry pairs = (Map.Entry) ite.next();
                Object avalue = pairs.getValue();
                if (avalue instanceof List) {
                    List clist = (List) avalue;
                    for (Integer x = 0; x < clist.size(); x++) {
                        Object com = clist.get(x);
                        setCarriers(com);
                    }
                } else {
                    setCarriers(avalue);
                }
            }
        } else if (TitleWork instanceof List) {
            List list = (List) TitleWork;
            for (Integer in = 0; in < list.size(); in++) {
                Map tNode = (Map) list.get(in);
                Object components = tNode.get("components");
                Map maa = (Map) components;
                Iterator ite = maa.entrySet().iterator();
                while (ite.hasNext()) {
                    Map.Entry pairs = (Map.Entry) ite.next();
                    Object avalue = pairs.getValue();
                    if (avalue instanceof List) {
                        List clist = (List) avalue;
                        for (Integer x = 0; x < clist.size(); x++) {
                            Object com = clist.get(x);
                            setCarriers(com);
                        }
                    } else {
                        setCarriers(avalue);
                    }
                }
            }
        }
        return this.cCount;
    }

    private void setCarriers(Object components) {
        Iterator audioItr = ((Map) components).entrySet().iterator();
        while (audioItr.hasNext()) {
            Map.Entry pairsA = (Map.Entry) audioItr.next();
            String key = pairsA.getKey().toString();
            Object svalue = pairsA.getValue();
            if (key.equals("carriers")) {
                Iterator mm = ((Map) svalue).entrySet().iterator();
                while (mm.hasNext()) {
                    Map.Entry pair = (Map.Entry) mm.next();
                    Object value = pair.getValue();
                    if (value instanceof Map) {
                        this.cCount += 1;
                    } else if (value instanceof List) {
                        this.cCount += ((List) value).size();
                    }
                }
            }
        }
    }
}
