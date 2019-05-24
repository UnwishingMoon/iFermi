package it.diegocastagna.ifermi.utils;

import android.os.AsyncTask;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.client.config.CookieSpecs;
import cz.msebera.android.httpclient.client.config.RequestConfig;
import cz.msebera.android.httpclient.client.methods.CloseableHttpResponse;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import cz.msebera.android.httpclient.entity.mime.content.FileBody;
import cz.msebera.android.httpclient.impl.client.CloseableHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClients;
import it.diegocastagna.ifermi.models.Model;
import it.diegocastagna.ifermi.network.DownloadFileFromURL;

/**
* Class used to Download calendar in PDF format and to convert it in XML
*/
public class PDFtoXML extends AsyncTask {

    /**
    * Function that downloads a pdf from URL and then uses pdftables' API to convert it to an XML file
    * @return boolean (true if successful, false otherwise)
    */
    public boolean convertPDF() throws Exception {

        System.out.println("\n INIZIO "+"\n");
        Model mModel = Model.getInstance(); // Mode
        File f = new File(mModel.getCacheDir(), "calendar.pdf");
        new DownloadFileFromURL().execute(f, "https://www.fermimn.edu.it/orari/a.s.%202018-19/calendario_scolastico_2018_2019%20al%2013_05_2019.pdf").get();


        String [] a = {"3vbu4qfqq37b", "xml", f.getPath()};

        final String apiKey = a[0];
        final String format = a[1].toLowerCase();
        final String pdfFilename = a[2];


        // Avoid cookie warning with default cookie configuration
        RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();

        File inputFile = new File(pdfFilename);

        if (!inputFile.canRead()) {
            System.out.println("Can't read input PDF file: \"" + pdfFilename + "\"");
            System.exit(1);
        }

        System.out.println("\n RICHIESTA "+"\n");
        try (CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(globalConfig).build()) {
            HttpPost httppost = new HttpPost("https://pdftables.com/api?format=" + format + "&key=" + apiKey);
            FileBody fileBody = new FileBody(inputFile);

            HttpEntity requestBody = MultipartEntityBuilder.create().addPart("f", fileBody).build();
            httppost.setEntity(requestBody);

            System.out.println("Sending request");

            System.out.println("\n INVIO "+"\n");
            try (CloseableHttpResponse response = httpclient.execute(httppost)) {
                if (response.getStatusLine().getStatusCode() != 200) {
                    System.out.println(response.getStatusLine());
                    System.exit(1);
                }
                System.out.println("\n LETTURA "+"\n");
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    final String outputFilename = getOutputFilename(pdfFilename, format.replaceFirst("-.*$", ""));
                    System.out.println("\n\n\n\n\n\n\n\n\\n\n\n\n"+outputFilename+"\n\n\n\n\n\n\n\n\n\n\n\n\n");
                    final File outputFile = new File(outputFilename);
                    FileUtils.copyToFile(resEntity.getContent(), outputFile);
                    return true;
                } else {

                    System.out.println("\n\n\n\n\n\n\n\n\\n\n\n\n CIAO FALSO "+"\n\n\n\n\n\n\n\n\n\n\n\n\n");
                    return false;
                }
            }
        }
    }
    /**
    * Function that decides how the output file is going to be named
    * @return string with file name
    */
    private static String getOutputFilename(String pdfFilename, String suffix) {
        if (pdfFilename.length() >= 5 && pdfFilename.toLowerCase().endsWith(".pdf")) {
            return pdfFilename.substring(0, pdfFilename.length() - 4) + "." + suffix;
        } else {
            return pdfFilename + "." + suffix;
        }
    }

    @Override
    protected Boolean doInBackground(Object[] objects) {
        try{
            convertPDF();
            return true;
        }catch (IOException e) {
            System.out.println("[ERROR]: During the conversion of pdf to xml: " + e);
        } finally{
            return false;
        }
    }
}