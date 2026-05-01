package org.demo.lesson.pdfMerger;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.util.Matrix;

public class ApachePdfBoxDemo {
    public static void main(String[] args) {
        //createPDF();
        mergePDFWithLocalFiles();
    }

    public static void mergePDFWithLocalFiles() {
        String localPdfFilePath1 = "./src/main/java/org/demo/lesson/pdfMerger/pdf_test.pdf";
        String localPdfFilePath2 = "./src/main/java/org/demo/lesson/pdfMerger/pdf_test2.pdf";

        PDDocument merged = new PDDocument();

        PDFMergerUtility merger = new PDFMergerUtility();

        try {
            // Set destination file
            merger.setDestinationFileName("./src/main/java/org/demo/lesson/pdfMerger/merged.pdf");

            // Add source PDFs
            merger.addSource(localPdfFilePath1);
            merger.addSource(localPdfFilePath2);

            // Merge documents
            merger.mergeDocuments(null);

            System.out.println("PDFs merged successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createPDF() {
        try {
            PDDocument document = new PDDocument();

            addPageWithText(document, "Line 1\nLine 2\nLine 3");

            String imageFilePath = "./src/main/java/org/demo/lesson/pdfMerger/tortoise.jpg";
            addPageWithLocalImage(document, imageFilePath);

            String imageUrl = "https://s3.amazonaws.com/comicgeeks/comics/covers/large-2061139.jpg";
            addPageWithImageFromUrl(document, imageUrl);

            String watermark = "Apache PDF Box Demo";
            addWatermark(document, watermark);

            document.save("./src/main/java/org/demo/lesson/pdfMerger/pdf_test2.pdf"); //saves file to root dir
            document.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void addPageWithText(PDDocument document, String content) throws IOException {
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER),14);

        float margin = 50;
        float yStart = page.getMediaBox().getHeight() - margin;
        float yPosition = yStart;
        float lineHeight = 17;

        String[] lines = content.split("\n");

        for (String line: lines) {
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText((line));
            contentStream.endText();
            yPosition -= lineHeight;
        }

        contentStream.close();
    }

    private static void addPageWithLocalImage(PDDocument document, String imageFilePath) throws IOException {
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        PDImageXObject image = PDImageXObject.createFromFile(imageFilePath, document);
        
        addImageToPageCenter(document, page, image);
    }

    private static void addPageWithImageFromUrl(PDDocument document, String imageUrl) throws IOException {
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        URL url = new URL(imageUrl);
        BufferedImage bufferedImage = ImageIO.read(url);
        PDImageXObject image = LosslessFactory.createFromImage(document, bufferedImage);

        addImageToPageCenter(document, page, image);
    }

    private static void addImageToPage(PDDocument document, PDPage page, PDImageXObject image, float x, float y, float width, float height) throws IOException {
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.drawImage(image, x, y, width, height);
        contentStream.close();;
    }

    private static void addImageToPageCenter(PDDocument document, PDPage page, PDImageXObject image) throws IOException {
        float pageWidth = page.getMediaBox().getWidth();
        float pageHeight = page.getMediaBox().getHeight();
        float imageWidth = image.getWidth();
        float imageHeight = image.getHeight();
        float scale = Math.min(pageWidth / imageWidth, pageHeight / imageHeight);
        float scaleWidth = imageWidth * scale;
        float scaleHeight = imageHeight * scale;
        float x = (pageWidth - scaleWidth) / 2;
        float y = (pageHeight - scaleHeight) / 2;

        addImageToPage(document, page, image, x, y, scaleWidth, scaleHeight);
    }

    private static void addWatermark(PDDocument document, String watermark) throws IOException {
        for (int i = 0; i < document.getNumberOfPages(); i++) {
            PDPage page = document.getPage(i);
            PDPageContentStream contentStream = new PDPageContentStream(document, page, AppendMode.APPEND, true, true);
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.TIMES_BOLD), 120);
            
            PDExtendedGraphicsState transparency = new PDExtendedGraphicsState();
            transparency.setNonStrokingAlphaConstant(0.1f);
            contentStream.setGraphicsStateParameters(transparency);

            contentStream.beginText();
            // Rotate 45 degrees around (x, y)
            float x = 100;
            float y = 300;
            double theta = Math.PI / 4;
            contentStream.setTextMatrix(Matrix.getRotateInstance(theta, x, y));
            contentStream.showText(watermark);
            contentStream.endText();

            contentStream.close();
        }
    }
}
