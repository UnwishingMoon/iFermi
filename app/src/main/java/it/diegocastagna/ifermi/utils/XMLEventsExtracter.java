package it.diegocastagna.ifermi.utils;

import android.content.Context;
import android.content.res.Resources;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.io.File;
import java.io.InputStream;
import java.time.Month;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import it.diegocastagna.ifermi.R;
import it.diegocastagna.ifermi.models.Model;

/**
* Class that parses an XML calendar in order to extrect events
*/
public class XMLEventsExtracter {
    /**
     * Function that parses an XML calendar in order to extrect events
     * @return map with events
     */
    public Map extractEvents(Context c) {
        try {
            Map<CalendarDay, String> map = new HashMap<CalendarDay, String>();
            String mesi[] = {"09", "10", "11", "12", "01", "02", "03", "04", "05", "06", "07", "08"};
            String eventi[] = new String[373];
            Model mModel = Model.getInstance(); // Mode
            //File inputFile = new File(mModel.getCacheDir(), "calendar.xml");
            //File inputFile = new File("../res/calendar.xml");
            InputStream inputStream = c.getResources().openRawResource(R.raw.calendar);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputStream);
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

            for(int i=1; i<eventi.length-1; i++){
                if(i%31==0)
                    i++;
                if(!eventi[i].isEmpty()){
                    if (Integer.valueOf(mesi[i/31])< 9){
                            map.put( CalendarDay.from(Calendar.getInstance().get(Calendar.YEAR) , Integer.valueOf(mesi[i/31]), i%31) , eventi[i]);
                    }

                    else{
                            map.put( CalendarDay.from(Calendar.getInstance().get(Calendar.YEAR) - 1, Integer.valueOf(mesi[i/31]), i%31) , eventi[i]);
                    }
                }
            }

            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

