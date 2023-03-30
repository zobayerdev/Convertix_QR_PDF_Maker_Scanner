package com.trodev.convertix.activities;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
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


public class ContactActivity extends AppCompatActivity {

    EditText name, phone, email, wpnum, fbid, twid, add, company, country;
    private ImageView imageView;
    public final static int QRCodeWidth = 500;
    Bitmap bitmap;
    private Button download, Generate;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        // set title in activity
        getSupportActionBar().setTitle("Create Contact QR");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // init all edittext id
        name = findViewById(R.id.nameET);
        email = findViewById(R.id. emailET);
        phone = findViewById(R.id. phoneET);
        wpnum = findViewById(R.id. wpET);
        fbid = findViewById(R.id. facebookET);
        twid = findViewById(R.id. twitterEt);
        add = findViewById(R.id. addressEt);
        company = findViewById(R.id. companyET);
        country = findViewById(R.id. countryET);

        // init all button
        download = findViewById(R.id.downloadBtn);
        download.setVisibility(View.INVISIBLE);
        imageView = findViewById(R.id.imageIV);
        Generate = findViewById(R.id.Generate);


        Generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (       name.getText().toString().trim().length() + email.getText().toString().length()
                        + phone.getText().toString().length() + wpnum.getText().toString().length()
                        + fbid.getText().toString().length() + twid.getText().toString().length()
                        + add.getText().toString().length() + company.getText().toString().length()
                        + country.getText().toString().length() == 0) {
                    Toast.makeText(ContactActivity.this, "Make sure your given Text..!", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        bitmap = textToImageEncode("Name:  " + name.getText().toString() + "\nE-mail:  " + email.getText().toString().trim() +
                                "\nPhone:   " + phone.getText().toString().trim() + "\nWhatsapp:  " + wpnum.getText().toString().trim() + "\nFacebook I'd:  " + fbid.getText().toString().trim()
                                + "\nTwitter I'd:  " + twid.getText().toString().trim() + "\nAddress:  " + add.getText().toString().trim() + "\nCompany Name:  " + company.getText().toString().trim()
                                + "\nCountry:  " + country.getText().toString().trim());   // + "\n\n\nMake by Altai Platforms"
                        imageView.setImageBitmap(bitmap);
                        download.setVisibility(View.VISIBLE);
                        download.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Contact_Identity"
                                        , null);
                                Toast.makeText(ContactActivity.this, "Download Complete", Toast.LENGTH_SHORT).show();
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
            bitMatrix = new MultiFormatWriter().encode(value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE, QRCodeWidth, QRCodeWidth, null);

        } catch (IllegalArgumentException e) {
            return null;
        }

        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];
        for (int y = 0; y < bitMatrixHeight; y++) {
            int offSet = y * bitMatrixWidth;
            for (int x = 0; x < bitMatrixWidth; x++) {
                pixels[offSet + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black) : getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
}