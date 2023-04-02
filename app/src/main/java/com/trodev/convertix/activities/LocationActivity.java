package com.trodev.convertix.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.trodev.convertix.R;

public class LocationActivity extends AppCompatActivity {
    public final static int QRCodeWidth = 500;
    Bitmap bitmap;
    private ImageButton download, Generate;
    private EditText addressET, fromET, toET;
    private ImageView imageView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        // set title in activity
        getSupportActionBar().setTitle("Create Location QR");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        fromET = findViewById(R.id.fromET);
        toET = findViewById(R.id.toET);
        addressET = findViewById(R.id.addressET);

        download = findViewById(R.id.downloadBtn);
        download.setVisibility(View.INVISIBLE);
        imageView = findViewById(R.id.imageIV);
        Generate = findViewById(R.id.Generate);

        Generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (fromET.getText().toString().length() + toET.getText().toString().length() + addressET.getText().toString().length() == 0) {
                    Toast.makeText(LocationActivity.this, "Make sure your given Text..!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        bitmap = textToImageEncode("Latitude :  " + fromET.getText().toString().trim() + "\nLongitude :  " + toET.getText().toString().trim() + "\nAddress:  " + addressET.getText().toString().trim());   // + "\n\n\nMake by Altai Platforms"
                        imageView.setImageBitmap(bitmap);
                        download.setVisibility(View.VISIBLE);
                        download.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "SMS_Identity", null);
                                Toast.makeText(LocationActivity.this, "Download Complete", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private Bitmap textToImageEncode(String value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(value, BarcodeFormat.DATA_MATRIX.QR_CODE, QRCodeWidth, QRCodeWidth, null);

        } catch (IllegalArgumentException e) {
            return null;
        }

        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];
        for (int y = 0; y < bitMatrixHeight; y++) {
            int offSet = y * bitMatrixWidth;
            for (int x = 0; x < bitMatrixWidth; x++) {
                pixels[offSet + x] = bitMatrix.get(x, y) ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
}