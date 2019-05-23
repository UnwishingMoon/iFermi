package it.diegocastagna.ifermi.utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
/*
* Class that parses an XML calendar in order to extrect events
* */
public class XMLEventsExtracter {

    /*
     * Function that parses an XML calendar in order to extrect events
     * @return map with events
     * */
    public Map extractEvents() {

        try {
            Map<String, String> map = new HashMap<String, String>();
            String mesi[] = {"09", "10", "11", "12", "01", "02", "03", "04", "05", "06", "07", "08"};
            String eventi[] = new String[373];
            File inputFile = new File("C:\\Users\\sahni\\Documents\\NetBeansProjects\\PDFToXML\\calendar.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            Element rootNode = doc.getDocumentElement();
            NodeList nList = doc.getElementsByTagName("page");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode.getChildNodes().item(1);
                    NodeList trList = eElement.getChildNodes();
                    for(int j=1; j < trList.getLength(); j++){
                        NodeList tdList = trList.item(j).getChildNodes();
                        for(int z=2; z< tdList.getLength(); z+=3){
                            eventi[31*(i*4+z/3)+j]= tdList.item(z).getTextContent();
                        }
                    }
                }
            }

            for(int i=1; i<eventi.length; i++)
                if(!eventi[i].isEmpty()){
                    map.put(i%31 +"/"+ mesi[i/31] , eventi[i]);
                }

            for (String name: map.keySet()){
                String key = name.toString();
                String value = map.get(name).toString();
                System.out.println(key + " " + value);
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
