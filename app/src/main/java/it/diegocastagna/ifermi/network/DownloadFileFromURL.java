package it.diegocastagna.ifermi.network;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import it.diegocastagna.ifermi.models.Model;

/**
 * Download a file froma URL Async
 */
public class DownloadFileFromURL extends AsyncTask {
    private File f;
    private String urlStr;

    @Override
    protected Boolean doInBackground(Object[] objects) {
        this.f = (File) objects[0];
        this.urlStr = (String) objects[1];
        System.out.println("downloading");
        try{
            downloadFile(f, urlStr);
            return true;
        }catch (IOException e) {
            System.out.println("[ERROR]: During the download of Rss News Tree in Memory: " + e);
        } finally{
            return false;
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
        InputStream bis = url.openStream();
        OutputStream fos = new FileOutputStream(f); // Where the data will be saved

        byte[] buffer = new byte[2048];
        int count;
        while((count = bis.read(buffer,0,2048)) != -1){
            fos.write(buffer, 0, count);
        }

        fos.close();
    }
}
