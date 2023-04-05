package com.trodev.convertix.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.trodev.convertix.R;

import java.util.Calendar;

public class ProductQRActivity extends AppCompatActivity {

    public final static int QRCodeWidth = 500;
    Bitmap bitmap;
    private ImageButton download, Generate;
    private EditText makeET, expireET, productET, companyET;
    private ImageView imageView;
    private ImageButton dateBtn, expireDateBtn;

    //define calender & datepicker
    Calendar c;
    DatePickerDialog dpd;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_qractivity);

        // set title in activity
        getSupportActionBar().setTitle("Create Product QR");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        makeET = findViewById(R.id.makeDateET);
        expireET = findViewById(R.id.expiredateET);
        productET = findViewById(R.id.productCodeET);
        companyET = findViewById(R.id.companyET);

        download = findViewById(R.id.downloadBtn);
        download.setVisibility(View.INVISIBLE);
        imageView = findViewById(R.id.imageIV);
        Generate = findViewById(R.id.Generate);
        dateBtn = findViewById(R.id.dateBtn);
        expireDateBtn = findViewById(R.id. expireDateBtn);


        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                dpd = new DatePickerDialog(ProductQRActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        makeET.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);
                    }
                }, day, month, year);

                dpd.show();

            }
        });

        expireDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                dpd = new DatePickerDialog(ProductQRActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        expireET.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);
                    }
                }, day, month, year);

                dpd.show();

            }
        });


        Generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (makeET.getText().toString().trim().length() + expireET.getText().toString().length()
                        + productET.getText().toString().length() + companyET.getText().toString().length() == 0) {
                    Toast.makeText(ProductQRActivity.this, "Make sure your given Text..!", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        bitmap = textToImageEncode("Manufacture Date:  " + makeET.getText().toString() + "\nExpire Date:  " + expireET.getText().toString().trim() +
                                "\nProduct Code:  " + productET.getText().toString().trim() + "\nCompany Name:  " + companyET.getText().toString().trim());   // + "\n\n\nMake by Altai Platforms"
                        imageView.setImageBitmap(bitmap);
                        download.setVisibility(View.VISIBLE);
                        download.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Product_Identity"
                                        , null);
                                Toast.makeText(ProductQRActivity.this, "Download Complete", Toast.LENGTH_SHORT).show();
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