package it.diegocastagna.ifermi.utils;

import android.os.Build;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class PDFtoXML{

    public static void convertPDF() throws Exception {
        URL url = new URL("https://www.fermimn.edu.it/orari/a.s.%202018-19/calendario_scolastico_2018_2019%20al%2013_05_2019.pdf");
        InputStream in = url.openStream();
        FileOutputStream fos = new FileOutputStream(new File("calendar.pdf"));
        int length = -1;
        byte[] buffer = new byte[1024];// buffer for portion of data from connection
        while ((length = in.read(buffer)) > -1) {
            fos.write(buffer, 0, length);
        }
        fos.close();
        in.close();

        String [] a = {"3vbu4qfqq37b", "xml", "calendar.pdf"};

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

        try (CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(globalConfig).build()) {
            HttpPost httppost = new HttpPost("https://pdftables.com/api?format=" + format + "&key=" + apiKey);
            FileBody fileBody = new FileBody(inputFile);

            HttpEntity requestBody = MultipartEntityBuilder.create().addPart("f", fileBody).build();
            httppost.setEntity(requestBody);

            System.out.println("Sending request");

            try (CloseableHttpResponse response = httpclient.execute(httppost)) {
                if (response.getStatusLine().getStatusCode() != 200) {
                    System.out.println(response.getStatusLine());
                    System.exit(1);
                }
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    final String outputFilename = getOutputFilename(pdfFilename, format.replaceFirst("-.*$", ""));
                    System.out.println("Writing output to " + outputFilename);

                    final File outputFile = new File(outputFilename);
                    FileUtils.copyToFile(resEntity.getContent(), outputFile);
                } else {
                    System.out.println("Error: file missing from response");
                    System.exit(1);
                }
            }
        }
    }

    private static String getOutputFilename(String pdfFilename, String suffix) {
        if (pdfFilename.length() >= 5 && pdfFilename.toLowerCase().endsWith(".pdf")) {
            return pdfFilename.substring(0, pdfFilename.length() - 4) + "." + suffix;
        } else {
            return pdfFilename + "." + suffix;
        }
    }
}