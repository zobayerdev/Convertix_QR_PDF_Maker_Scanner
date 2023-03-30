package com.trodev.convertix.activities;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.trodev.convertix.R;


public class WifiQRActivity extends AppCompatActivity {

    String[] items = {"WPA", "WPA2", "WPA3"};
    String[] item_two = {"Yes", "No"};
    AutoCompleteTextView ssid, encryption;
    ArrayAdapter<String> adapterItems;

    public final static int QRCodeWidth = 500;
    Bitmap bitmap;
    private Button download, Generate;

    private EditText networkname, password;

    private ImageView imageView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_qractivity);

        // set title in activity
        getSupportActionBar().setTitle("Wifi QR");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ssid = findViewById(R.id.ssid_text);
        encryption = findViewById(R.id.encryption);
        networkname = findViewById(R.id.networkEt);
        password  = findViewById(R.id.passeordEt);

        download = findViewById(R.id.downloadBtn);
        download.setVisibility(View.INVISIBLE);
        imageView = findViewById(R.id.imageIV);
        Generate = findViewById(R.id.Generate);

        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, items);
        ssid.setAdapter(adapterItems);

        ssid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, item_two);
        encryption.setAdapter(adapterItems);

        encryption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ssid.getText().toString().length() + networkname.getText().toString().length() + password.getText().toString().length() + encryption.getText().toString().length()== 0) {
                    Toast.makeText(WifiQRActivity.this, "Make sure your given Text..!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        bitmap = textToImageEncode("ssid :  " + ssid.getText().toString().trim() + "\nNetwork Name :  " + networkname.getText().toString().trim() + "\nPassword:  " + password.getText().toString().trim()
                                + "\nEncryption:  " + encryption.getText().toString().trim());
                        // + "\n\n\nMake by Altai Platforms"
                        imageView.setImageBitmap(bitmap);
                        download.setVisibility(View.VISIBLE);
                        download.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "wifi_Identity", null);
                                Toast.makeText(WifiQRActivity.this, "Download Complete", Toast.LENGTH_SHORT).show();
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