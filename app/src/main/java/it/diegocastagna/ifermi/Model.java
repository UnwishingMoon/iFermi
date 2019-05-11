package it.diegocastagna.ifermi;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Model of the Application, it is used for all non graphics tasks
 * @version 1.0
 * @since 1.0
 */
public class Model extends Observable {
    public final static String rssUrl = "https://www.fermimn.edu.it/?action=rss";
    public final static String classesUrl = "https://www.fermimn.edu.it/orari/orario_in_corso/";
    public final static String imageRssUrl = "https://www.fermimn.edu.it/?clean=true&action=icon&newsid=";

    private List rssNews;

    private final String classesCacheFile = "classesCacheFile.html";
    private List classesList;
    private String schoolClass;

    private static Model instance;
    private WebsiteCheck wCheck;
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
        this.rssNews = new ArrayList<RssNews>();

        this.classesList = new ArrayList();
        this.schoolClass = "1A";

        this.wCheck = new WebsiteCheck();
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
     * @return List<RssNews>
     */
    public List<RssNews> getNewsList(){
        // Check if Classes List exists
        if(this.rssNews != null){
            return rssNews;
        }

        // Checking if Cache file Exists and is not empty
        File f = new File(this.cacheDir, "newsCacheFile.rss");
        if(f.exists()){
            try{
                File f2 = new File(this.cacheDir, "newsCacheFileTemp.rss");
                downloadFile(f2, rssUrl);
                // Checking if files are the same TODO

                // If they are different file, it deletes the old one and replace it with the new one
                createRssNews(f);
                return rssNews;
            }catch (Exception e){
                // Log the error
                System.out.println("[ERROR]: During the creation of Rss News Tree in Memory");
            }
        } else {
            boolean isInternetActive;
            try{
                isInternetActive = (Boolean) wCheck.execute().get(); // Wait to check if internet is up
            }catch (ExecutionException e){
                isInternetActive = false;
            }catch (InterruptedException e){
                isInternetActive = false;
            }

            // Retrieve the file from the website
            if(isInternetActive) {
                try {
                    downloadFile(f, rssUrl);
                    createRssNews(f);
                } catch (Exception e) {
                    // Log the error
                    System.out.println("[ERROR]: During the download or creation of Rss News Tree in Memory");
                }
                return rssNews;
            }
        }
        return null;
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
            Node n = itemList.item(i);
            NodeList newsList = n.getChildNodes();

            temp.setTitle(newsList.item(0).getTextContent()); // Get and set the title of the news
            temp.setNewsDescription(newsList.item(1).getTextContent()); // Get and set the description of the news

            String tempIconStr = newsList.item(2).getTextContent(); // Full String of the Icon
            temp.setIconId(tempIconStr.substring(tempIconStr.lastIndexOf("#news"),tempIconStr.length())); // Only the Id of the Icon
            rssNews.add(temp);
        }
    }

    /**
     * Download the file from a specified Url and saves it to a specific directory
     * @param f Path where the file should be saved
     * @param urlStr URL String where the file will be located on the Internet
     * @throws IOException If some I/O Error occured
     */
    private void downloadFile(File f, String urlStr) throws IOException{
        URL url = new URL(urlStr);
        BufferedInputStream bis = new BufferedInputStream(url.openStream()); // Input Stream of the URL
        FileOutputStream fos = new FileOutputStream(f); // Where the data will be saved

        byte[] buffer = new byte[2048];
        int count;
        while((count = bis.read(buffer,0,2048)) != -1){
            fos.write(buffer, 0, count);
        }

        fos.close();
        bis.close();
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
