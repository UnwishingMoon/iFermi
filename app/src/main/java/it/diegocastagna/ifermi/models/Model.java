package it.diegocastagna.ifermi.models;

import android.content.Context;
import android.os.AsyncTask;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import it.diegocastagna.ifermi.network.DownloadFileFromURL;
import it.diegocastagna.ifermi.utils.RssNews;
import it.diegocastagna.ifermi.utils.XMLEventsExtracter;

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


    public Map<CalendarDay, String> getCalendarEvents() {
        return calendarEvents;
    }

    public Boolean updateCalendarEvents(Context context){
        calendarEvents = new XMLEventsExtracter().extractEvents(context);
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

        f.delete();
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
