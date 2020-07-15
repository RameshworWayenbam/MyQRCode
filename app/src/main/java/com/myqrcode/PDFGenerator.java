package com.myqrcode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static com.itextpdf.text.pdf.codec.BmpImage.getImage;

public class PDFGenerator {
  private static String TAG ="PDFGenerator";

    public static void generateV1(View view, Bitmap bitmap, String strName) {
        Context context = view.getContext();
        Log.d(TAG, " PDF Generation Started.... ");
        String fileName = strName;

        if(fileName.isEmpty()){
            fileName = "Sample001.pdf";
        }else {
            fileName = fileName + ".pdf";
        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        String invoiceDate= formatter.format(new Date());

        if (!fileName.isEmpty()) {
            //File file = new File(MainActivity.this.getFilesDir(), "pdf");
            File file = new File(context.getFilesDir()+"/pdf", invoiceDate);

            //File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsoluteFile()+"/MyStore",invoiceDate);
            if (!file.exists()) {
                file.mkdirs();
            }

            try {
                File gpxfile = new File(file, fileName);
                Document document = new Document(PageSize.A4);

                try {
                    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(gpxfile));
                    document.open();//PDF document opened........

                    Image image = getImage(view, bitmap);

                    PdfPTable nameTab = new PdfPTable(1);
                    nameTab.setWidthPercentage(95);
                    Paragraph ph = new Paragraph(strName);
                    PdfPCell cellh = new PdfPCell();
                    cellh.setVerticalAlignment(Element.ALIGN_CENTER);
                    cellh.addElement(ph);
                    nameTab.addCell(cellh);
                    document.add(nameTab);


                    PdfPTable table = new PdfPTable(6);
                    table.setWidthPercentage(95);
                    for (int i = 0; i < 222; i++){
                        PdfPCell cell = new PdfPCell();
                        Paragraph p = new Paragraph();
                        image.scalePercent(10);
                        p.add(new Chunk(image, 0, 0, true));
                        cell.addElement(p);
                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell);
                    }


                    document.add(table);
                    document.close();

                    writer.close();
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "Bill Generated Successfully....");
                Toast.makeText(context,"Bill Generated Successfully....", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
            }
        }
    }

    private static Image getImage(View view, Bitmap bitmap){
        Context context = view.getContext();
        //Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.mystorelogo);
        Bitmap bm = bitmap;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
        Image img = null;
        byte[] byteArray = stream.toByteArray();
        try {
            img = Image.getInstance(byteArray);
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

}
