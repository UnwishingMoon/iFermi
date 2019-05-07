package it.diegocastagna.ifermi;

import android.os.AsyncTask;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class WebsiteCheck extends AsyncTask {
    @Override
    protected Boolean doInBackground(Object[] objects) {
        try{
            int timeout = 1500; // Timeout in Milliseconds

            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress("https://www.fermimn.edu.it/", 443);

            sock.connect(sockaddr, timeout);
            sock.close();

            return true;
        }catch(Exception e){
            return false;
        }
    }
}
