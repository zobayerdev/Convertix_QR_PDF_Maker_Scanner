package com.trodev.convertix.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import com.trodev.convertix.R;


import java.util.List;

public class ScanGalleryActivity extends AppCompatActivity {

    private static final String TAG = "IMAGE_TAG";

    private static final int STORAGE_REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 101;

    // arrays of permissions required to pick image from camera/gallery
    private String[] cameraPermission;
    private String[] storagePermission;

    private Uri imageUri = null;

    private MaterialButton cameraBtn, galleryBtn, scanBtn;
    private ImageView imageIv;
    private TextView resultTv;

    private ImageButton copyBtn, sharebtn;

    private BarcodeScannerOptions barcodeScannerOptions;
    private BarcodeScanner barcodeScanner;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_gallery);

        // set title in activity
        getSupportActionBar().setTitle("Scan From Storage");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        // bar code scanning option you can scan all type and formated qr codes
        //bar code code all
        barcodeScannerOptions = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                .build();

        barcodeScanner = BarcodeScanning.getClient(barcodeScannerOptions);


      //  cameraBtn = findViewById(R.id.cameraBtn);
        galleryBtn = findViewById(R.id.galleryBtn);
        scanBtn = findViewById(R.id.scanBtn);
        imageIv = findViewById(R.id.imageiv);
        resultTv = findViewById(R.id.resultTv);
        copyBtn = findViewById(R.id.copyBtn);
        sharebtn = findViewById(R.id.shareBtn);

/*        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkCameraPermission()) {
                    pickImageCamera();
                } else {
                    requestCameraPermission();
                }

            }
        });*/

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkStoragePermission()) {
                    pickImaageGallery();
                } else {
                    requestStoragePermission();
                }
            }
        });

        //hide the result value
        resultTv.setVisibility(View.INVISIBLE);
        copyBtn.setVisibility(View.INVISIBLE);
        sharebtn.setVisibility(View.INVISIBLE);
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageUri == null)
                {
                    Toast.makeText(ScanGalleryActivity.this, "Pick image first", Toast.LENGTH_SHORT).show();
                }
                else {
                    detectResultFromImage();
                }
            }
        });

        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(ScanGalleryActivity.this.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("TextView",resultTv.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(ScanGalleryActivity.this, "Copy successful", Toast.LENGTH_SHORT).show();
            }
        });

        sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = resultTv.getText().toString();
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, s);
                startActivity(Intent.createChooser(sharingIntent, "Share text via"));
                Toast.makeText(ScanGalleryActivity.this, "Share Scanning Text", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void detectResultFromImage() {
        try {

            InputImage inputImage = InputImage.fromFilePath(this, imageUri);
            Task<List<Barcode>> barcodeResult = barcodeScanner.process(inputImage)
                    .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                        @Override
                        public void onSuccess(List<Barcode> barcodes) {
                            extraBarCodeQRCodeInfo(barcodes);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ScanGalleryActivity.this, "Failed Detect Image due to "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            }
            catch(Exception e)
            {
                Log.d(TAG, "detectResultFromImage: ");
                Toast.makeText(this, "Failed Detect Image "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }

    }

    private void extraBarCodeQRCodeInfo(List<Barcode> barcodes) {

        for (Barcode barcode : barcodes)
        {
            Rect bounds = barcode.getBoundingBox();
            Point[] corners = barcode.getCornerPoints();

            String rawValue = barcode.getRawValue();
            Log.d(TAG, "extraBarCodeQRCodeInfo: rawValue");

            int valueType = barcode.getValueType();
            switch (valueType)
            {
                case Barcode.TYPE_WIFI:{

                    Barcode.WiFi typeWifi = barcode.getWifi();
                    String ssid = ""+typeWifi.getSsid();
                    String password = ""+ typeWifi.getPassword();
                    String encryptionType = ""+typeWifi.getEncryptionType();

                    Log.d(TAG, "extraBarCodeQRCodeInfo: ssid: "+ssid);
                    Log.d(TAG, "extraBarCodeQRCodeInfo: password: "+password);
                    Log.d(TAG, "extraBarCodeQRCodeInfo: encryptionType: "+encryptionType);

                    resultTv.setVisibility(View.VISIBLE);
                    copyBtn.setVisibility(View.VISIBLE);
                    sharebtn.setVisibility(View.VISIBLE);
                    resultTv.setText("TYPE: TYPE_WIFI \nssid:  "+ ssid + "\npassword: "+password + "\nencryptionType: "+encryptionType + "\nraw value: "+rawValue);
                }
                break;
                case Barcode.TYPE_URL:{
                    Barcode.UrlBookmark typeUrl = barcode.getUrl();

                    String title = ""+typeUrl.getTitle();
                    String url = " "+typeUrl.getUrl();

                    Log.d(TAG, "extraBarCodeQRCodeInfo: title: "+title);
                    Log.d(TAG, "extraBarCodeQRCodeInfo: url: "+url);

                    resultTv.setVisibility(View.VISIBLE);
                    copyBtn.setVisibility(View.VISIBLE);
                    sharebtn.setVisibility(View.VISIBLE);
                    resultTv.setText("TYPE: TYPE_URL \nTitle: "+title + "\nUrl: "+url + "\nraw value: "+rawValue );

                }
                break;
                default:{
                    resultTv.setVisibility(View.VISIBLE);
                    copyBtn.setVisibility(View.VISIBLE);
                    sharebtn.setVisibility(View.VISIBLE);
                    resultTv.setText("raw value: \n"+rawValue);
                }
            }

        }

    }


    private void pickImaageGallery() {
        Log.d(TAG, "pickImaageGallery: ");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {

            if (result.getResultCode() == Activity.RESULT_OK) {

                Intent data = result.getData();
                imageUri = data.getData();

                imageIv.setImageURI(imageUri);

                Log.d(TAG, "onActivityResult: Picked image gallery: " + imageUri);

            }

            else
            {
                Toast.makeText(ScanGalleryActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    });


    private void pickImageCamera() {
        Log.d(TAG, "pickImageCamera: ");

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Simple Title");

        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Simple Image Description");

        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        cameraActivityResultLauncher.launch(intent);

    }

    private ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {

            if (result.getResultCode() == Activity.RESULT_OK) {

                Intent data = result.getData();
                imageUri = data.getData();

                imageIv.setImageURI(imageUri);

            } else {
                Toast.makeText(ScanGalleryActivity.this, "Cancelled..", Toast.LENGTH_SHORT).show();
            }
        }
    });

    private boolean checkStoragePermission() {
        Log.d(TAG, "checkStoragePermission: ");
        boolean result = ContextCompat.checkSelfPermission(ScanGalleryActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        return result;
    }

    private void requestStoragePermission() {
        Log.d(TAG, "requestStoragePermission: ");
        requestPermissions(storagePermission, STORAGE_REQUEST_CODE);
    }


    private boolean checkCameraPermission() {
        Log.d(TAG, "checkCameraPermission: ");

        boolean cameraResult = ContextCompat.checkSelfPermission(ScanGalleryActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean storageResult = ContextCompat.checkSelfPermission(ScanGalleryActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        return cameraResult && storageResult;

    }

    private void requestCameraPermission() {
        Log.d(TAG, "requestCameraPermission: ");
        requestPermissions(cameraPermission, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {

                if (grantResults.length > 0) {

                    //permission granted on camera permission
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && storageAccepted) {
                        Log.d(TAG, "onRequestPermissionsResult: ");
                        pickImageCamera();
                        Toast.makeText(ScanGalleryActivity.this, "Camera Permission is enabled", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ScanGalleryActivity.this, "Camera & Storage Permission required", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d(TAG, "onRequestPermissionsResult: ");
                    Toast.makeText(ScanGalleryActivity.this, "Cancelled...", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case STORAGE_REQUEST_CODE: {

                if (grantResults.length > 0) {

                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (storageAccepted) {
                        Log.d(TAG, "onRequestPermissionsResult: ");
                        pickImaageGallery();
                        Toast.makeText(ScanGalleryActivity.this, "Storage Permission is enabled", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ScanGalleryActivity.this, "Storage Permission is required...", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ScanGalleryActivity.this, "Cancelled....", Toast.LENGTH_SHORT).show();
                }

            }
            break;

        }
    }


}