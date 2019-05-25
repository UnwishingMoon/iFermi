package it.diegocastagna.ifermi.models;

import android.content.Context;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import it.diegocastagna.ifermi.activity.AgendaActivity;
import it.diegocastagna.ifermi.network.DownloadFileFromURL;
import it.diegocastagna.ifermi.utils.ICSEventsExtracter;
import it.diegocastagna.ifermi.utils.RssNews;
import it.diegocastagna.ifermi.utils.XMLEventsExtracter;
import  it.diegocastagna.ifermi.utils.Event;

/**
 * Model of the Application, it is used for all non graphics tasks
 * @version 1.0
 * @since 1.0
 */
public class Model extends Observable {
    public final static String RSSURL = "https://www.fermimn.edu.it/?action=rss";
    public final static String CLASSESURL = "https://www.fermimn.edu.it/orari/orario_in_corso/";
    public final static String IMAGERSSURL = "https://www.fermimn.edu.it/?clean=true&action=icon&newsid=";

    private ArrayList<RssNews> rssNews;
    private Map<CalendarDay, String> calendarEvents;




    private  Map<CalendarDay, ArrayList<Event>> agendaEvents;

    private final String classesCacheFile = "classesCacheFile.html";
    private List classesList;
    private String schoolClass;

    private static Model instance;

    public File getCacheDir() {
        return cacheDir;
    }

    private File cacheDir;

    /**
     * Return the instance of the Model class, is single-ton
     * @return Model
     */
    public static Model getInstance(){
        if(instance == null){
            instance = new Model();
        }
        return instance;
    }

    private Model(){
        this.rssNews = new ArrayList<>();

        this.classesList = new ArrayList();
        this.schoolClass = "1A";
    }

    /**
     * Returns all the events of the current school year
     * @return Map<CalendarDay, String>
     */
    public Map<CalendarDay, String> getCalendarEvents() {
        return calendarEvents;
    }

    /**
     * Returns all the agenda events of the current school year
     * @return Map<CalendarDay, Event> (Event is a custom class including description and time of an event)
     */
    public Map<CalendarDay, ArrayList<Event>> getAgendaEvents() {
        return agendaEvents;
    }

    public void setAgendaEvents(Map<CalendarDay, ArrayList<Event>> agendaEvents) {
        this.agendaEvents = agendaEvents;
    }
    public Map<CalendarDay, ArrayList<Event>>  getAgendaClassEvents(String c) {
        Map<CalendarDay, ArrayList<Event>>  classEvents = new HashMap<>();
        for(Map.Entry<CalendarDay, ArrayList<Event>> entry : agendaEvents.entrySet()){
            for (Event e: entry.getValue()){
                if(e.getDescription().contains(c)){
                    if(classEvents.get(entry.getKey())!=null)
                        classEvents.get(entry.getKey()).add(e);
                    else{
                        ArrayList<Event> temp = new ArrayList<>();
                        temp.add(e);
                        classEvents.put(entry.getKey(), temp );
                    }
                }
            }
        }
        return classEvents;
    }

    public ArrayList<Event> getAgendaClassEventsOnDate(CalendarDay d, String c) {
        ArrayList<Event> classEvents = getAgendaEventsOnDate(d);
        for (Iterator<Event> iterator = classEvents.iterator(); iterator.hasNext(); ) {
            Event value = iterator.next();
            if (!value.getDescription().contains(c)) {
                iterator.remove();
            }
        }
        return classEvents;
    }

    public ArrayList<Event> getAgendaEventsOnDate(CalendarDay day) {
        return agendaEvents.get(day);
    }

    public Boolean updateCalendarEvents(Context context){
        calendarEvents = new XMLEventsExtracter().extractEvents(context);
        return true;
    }

    public Boolean updateAgendaEvents(AgendaActivity a, Context context){
        try {
            ICSEventsExtracter task = new ICSEventsExtracter(a);
            task.execute(context);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Set the Path to the Cache Directory
     * @param cacheDir Path to the Cache Directory of the System
     */
    public void setCacheDir(File cacheDir) {
        this.cacheDir = cacheDir;
    }

    /**
     * It gets the news Rss file from the website, save and elaborate it
     * @return ArrayList<RssNews>
     */
    public ArrayList<RssNews> getNewsList(){
        // Check if Classes List exists
        if(!this.rssNews.isEmpty()){
            return rssNews;
        }

        // Checking if Cache file Exists and is not empty
        File f = new File(this.cacheDir, "newsCacheFile.rss");
        if(f.exists()){
            try{
                File f2 = new File(this.cacheDir, "newsCacheFileTemp.rss");
                new DownloadFileFromURL().execute(f2, Model.RSSURL).get();
                // Checking if files are the same TODO: Check Files hash? Ask Salvi
                if(!checkFiles(f,f2)){
                    // File is not the same it deletes the old one and replace it with the new one
                    if(f.delete() && f2.renameTo(f)){
                        createRssNews(f);
                    } else {
                        System.out.println("[MESSAGE] Temporary fallback to Temp File");
                        createRssNews(f2);
                    }
                }
                f2.delete();
            }catch (Exception e){
                System.out.println("[ERROR]: During the creation of Rss News Tree in Memory: " + e);
            }
        } else {
            try {
                new DownloadFileFromURL().execute(f, Model.RSSURL).get();
                createRssNews(f);
            }catch (Exception e) {
                System.out.println("[ERROR]: During the creation of Rss News Tree in Memory: " + e);
            }
        }

        //f.delete();
        return rssNews;
    }

    /**
     * It allocates the News in memory reading from the rss file
     * @param f Rss File Path
     * @throws Exception If it can't find the file
     */
    private void createRssNews(File f) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder parser =  factory.newDocumentBuilder();

        Document doc = parser.parse(f); // XML Document
        Element el = doc.getDocumentElement(); // Root Element
        NodeList itemList = el.getElementsByTagName("item"); // List of all Items
        for(int i = 0; i < itemList.getLength(); i++){
            RssNews temp = new RssNews();
            NodeList newsList = itemList.item(i).getChildNodes();

            String tempDescription = newsList.item(3).getTextContent().substring(newsList.item(3).getTextContent().lastIndexOf("<br />")+7).trim();
            String tempIconStr = newsList.item(5).getTextContent().replace("newsid=","news"); // Full String of the Icon

            temp.setTitle(newsList.item(1).getTextContent().trim()); // Get and set the title of the news
            temp.setLongDesc(tempDescription); // Get and set the Full description of the news
            temp.setShortDesc(tempDescription.substring(0,130).trim() + "...");
            temp.setIconId(tempIconStr.substring(tempIconStr.lastIndexOf("news")+4).trim()); // Only the Id of the Icon
            rssNews.add(temp);
        }
    }

    public boolean checkFiles(File f, File f2){
        return true;
    }

    public List getClassesList(){
        // Check if Classes List exists
        if(this.classesList != null){
            return classesList;
        }

        // Checking if Cache file Exists and is not empty
        return null;
    }

}
