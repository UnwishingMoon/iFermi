package it.diegocastagna.ifermi.models;

import android.os.AsyncTask;

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
import java.util.List;
import java.util.Observable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import it.diegocastagna.ifermi.network.DownloadFileFromURL;
import it.diegocastagna.ifermi.utils.RssNews;

/**
 * Model of the Application, it is used for all non graphics tasks
 * @version 1.0
 * @since 1.0
 */
public class Model extends Observable {
    public final static String rssUrl = "https://www.fermimn.edu.it/?action=rss";
    public final static String classesUrl = "https://www.fermimn.edu.it/orari/orario_in_corso/";
    public final static String imageRssUrl = "https://www.fermimn.edu.it/?clean=true&action=icon&newsid=";

    private ArrayList<RssNews> rssNews;

    private final String classesCacheFile = "classesCacheFile.html";
    private List classesList;
    private String schoolClass;

    private static Model instance;
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
            System.out.println("Esiste");
            try{
                File f2 = new File(this.cacheDir, "newsCacheFileTemp.rss");
                //new DownloadFileFromURL().execute(f2, Model.rssUrl);
                downloadFile(f2, Model.rssUrl);
                // Checking if files are the same TODO: Check Files hash? Ask Salvi

                // If they are different file, it deletes the old one and replace it with the new one
                if(f.delete() && f2.renameTo(f)){
                    createRssNews(f);
                } else {
                    System.out.println("[MESSAGE] Temporary fallback to Temp File");
                    createRssNews(f2);
                }

                f2.delete();
            }catch (Exception e){
                System.out.println("[ERROR]: During the creation of Rss News Tree in Memory: " + e);
            }
        } else {
            try {
                //new DownloadFileFromURL().execute(f, Model.rssUrl).get();
                downloadFile(f, Model.rssUrl);
                System.out.println("111 Creiamo l'array");
                createRssNews(f);
            }catch (Exception e) {
                System.out.println("[ERROR]: During the creation of Rss News Tree in Memory: " + e);
            }
        }

        f.delete();
        return rssNews;
    }

    private void downloadFile(File f, String urlStr) throws IOException {
        URL url = new URL(urlStr);
        InputStream bis = url.openStream();
        OutputStream fos = new FileOutputStream(f); // Where the data will be saved

        byte[] buffer = new byte[2048];
        int count;
        while((count = bis.read(buffer,0,2048)) != -1){
            fos.write(buffer, 0, count);
        }

        fos.close();
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
        System.out.println();
        for(int i = 0; i < itemList.getLength(); i++){
            RssNews temp = new RssNews();
            Node n = itemList.item(i);
            n.normalize();
            NodeList newsList = n.getChildNodes();

            temp.setTitle(newsList.item(1).getTextContent()); // Get and set the title of the news
            temp.setDescription(newsList.item(3).getTextContent()); // Get and set the description of the news
            String tempIconStr = newsList.item(5).getTextContent(); // Full String of the Icon

            System.out.println(newsList.item(0).getTextContent()+"1");

            //temp.setIconId(tempIconStr.substring(tempIconStr.lastIndexOf("newsid="))); // Only the Id of the Icon
            //rssNews.add(temp);
        }
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
