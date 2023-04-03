package com.trodev.convertix.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.trodev.convertix.R;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class TextToPdfActivity extends AppCompatActivity {

    private static final int STORAGE_CODE =  1000 ;
    EditText textEt;
    ImageButton saveBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_pdf);

        // set title in activity
        getSupportActionBar().setTitle("Create Text to PDF");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textEt = findViewById(R.id.textEt);
        saveBtn = findViewById(R.id.savePdf);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //we need to handle permission to save this file on storage
                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)  == PackageManager.PERMISSION_DENIED)
                    {
                        //permission was not granted, request it.
                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions, STORAGE_CODE);
                    }
                    else
                    {
                        //permission already granted
                        savePDF();
                    }
                }
                else
                {
                    savePDF();
                }
            }
        });
    }

    private void savePDF() {
      //  Document mDoc = new Document();
        Document mDoc = new Document(PageSize.A4);

        //pdf file name
      //  String mFileName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        String FILE = Environment.getExternalStorageDirectory().toString() +"/emulated/"+".pdf";

        //pdf file path
        // String mFilePath = Environment.getExternalStorageDirectory() .toString();
// Create Directory in External Storage
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/PDF");
        myDir.mkdirs();

        try
            {
                //create instant pdfview class
                PdfWriter.getInstance(mDoc, new FileOutputStream(FILE));

                //open the document for writing
                mDoc.open();

                //get text from editext
                String mText = textEt.getText().toString();

                //add author of the document (optional)
                mDoc.addAuthor("Md. Zobayer Hasan Nayem");

                //add paragraph to document
                mDoc.add(new Paragraph(mText));

                //close the documents
                mDoc.close();
                //show message that file is saved
                Toast.makeText(this, mText+".pdf\nsaved to\n"+FILE, Toast.LENGTH_LONG).show();
            }
        catch (Exception e)
        {
            //if any thing goes wrong causing exception
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case STORAGE_CODE:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //permission wand granted
                    savePDF();
                }
                else
                {
                    //permission was denided
                    Toast.makeText(this, "Permission denied.....", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }
}