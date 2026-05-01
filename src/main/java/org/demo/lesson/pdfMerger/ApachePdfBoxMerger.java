package org.demo.lesson.pdfMerger;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

public class ApachePdfBoxMerger {
    public static void main(String[] args) {
        List<String> pdfUrls = new ArrayList<>(List.of(
            "https://education.ucsb.edu/sites/default/files/documents/fake-pdf.pdf"
            ,"https://education.ucsb.edu/sites/default/files/documents/fake-pdf.pdf"
            ));

        try {
            mergeUrlsIntoPdf(pdfUrls);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mergeUrlsIntoPdf(List<String> pdfUrls) {
        PDFMergerUtility merger = new PDFMergerUtility();
        merger.setDestinationFileName("./src/main/java/org/demo/lesson/pdfMerger/merge_test.pdf");

        for (String pdfUrl : pdfUrls) {
            if (pdfUrl == null) continue;

            try {
                URL url = URI.create(pdfUrl).toURL();

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(30000);

                try (InputStream in = conn.getInputStream()) {
                    byte[] bytes = in.readAllBytes();
                    merger.addSource(new RandomAccessReadBuffer(bytes));
                }

                conn.disconnect();

            } catch (Exception e) {
                System.out.println("Error reading PDF from URL:" + pdfUrl);
            }
        }

        try {
            merger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly().streamCache);
        } catch (IOException e) {
            System.out.println("Error during document merge.");
        }
    }
}