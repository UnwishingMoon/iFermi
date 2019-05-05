package it.diegocastagna.ifermi;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Model extends Observable {
    public final static String rssUrl = "https://www.fermimn.edu.it/?action=rss";
    public final static String classesUrl = "https://www.fermimn.edu.it/orari/orario_in_corso/";

    private WebsiteCheck wCheck;

    private String newsCacheFile = "newsCacheFile";
    // private xmlTree;

    private String classesCacheFile = "classesCacheFile;";
    private List classesList;

    private String schoolClass;

    private static Model instance;

    public static Model getInstance(){
        if(instance == null){
            instance = new Model();
        }
        return instance;
    }

    private Model(){
        this.classesList = new ArrayList();
        this.schoolClass = "1A";
        this.wCheck = new WebsiteCheck();
    }


    public void getNewsList(){
        // Checking if Cache file Exists and is not empty
        File f = new File(getCacheDir(), "newsCacheFile");
        if(f.exists()){

        }
        if(this.classesFile == null){
            instance = new Model();
        }
    }

    public void getClassesList(){
        // Checking if Cache file Exists and is not empty
        if(this.newsRssFile == null){
            this.wCheck.execute();
        }
    }
}
