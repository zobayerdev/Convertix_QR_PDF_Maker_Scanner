package com.trodev.convertix.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.trodev.convertix.R;

import java.io.IOException;

public class ImageViewActivity extends AppCompatActivity {
    private String image;
    private ImageView imageIv;
    private static final String TAG = "IMAGE_TAG";
    private EditText resultTv;
    private TextRecognizer textRecognizer;
    private ImageButton shareBtn, copyBtn;
    ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        getSupportActionBar().setTitle("ImageView");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //init views
        imageIv = findViewById(R.id.imageIv);
        resultTv = findViewById(R.id.resultTv);
        shareBtn = findViewById(R.id.shareBtn);
        copyBtn = findViewById(R.id.generateBtn);

        // set visibility button and textviews
        resultTv.setVisibility(View.INVISIBLE);
        shareBtn.setVisibility(View.INVISIBLE);
        copyBtn.setVisibility(View.INVISIBLE);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        //init textRecognizer
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        image = getIntent().getStringExtra("imageUri");
        Log.d(TAG, "onCreate: Image: " + image);

        Glide.with(this).load(image).placeholder(R.drawable.ic_image_black).into(imageIv);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_imagetotext_scan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == R.id.images_item_scan) {
            recognizeTextFromImage();
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void recognizeTextFromImage() {

        //show progress dialog.....
        progressDialog.setTitle("Recognizing  Text......");
        progressDialog.show();

        try {
            InputImage inputImage = InputImage.fromFilePath(this, Uri.parse(image));
            Task<Text> textTaskResult = textRecognizer.process(inputImage).addOnSuccessListener(new OnSuccessListener<Text>() {
                @Override
                public void onSuccess(Text text) {

                    //progress dialog dismiss
                    progressDialog.dismiss();

                    String recognizedText = text.getText();
                    resultTv.setVisibility(View.VISIBLE);
                    copyBtn.setVisibility(View.VISIBLE);
                    shareBtn.setVisibility(View.VISIBLE);
                    resultTv.setText(recognizedText);
                    copyBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ClipboardManager clipboard = (ClipboardManager) getSystemService(ImageViewActivity.this.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("TextView", resultTv.getText().toString());
                            clipboard.setPrimaryClip(clip);
                            Toast.makeText(ImageViewActivity.this, "Copy successful", Toast.LENGTH_SHORT).show();
                        }
                    });
                    shareBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String s = resultTv.getText().toString();
                            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                            sharingIntent.setType("text/plain");
                            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
                            sharingIntent.putExtra(Intent.EXTRA_TEXT, s);
                            startActivity(Intent.createChooser(sharingIntent, "Share text via"));
                            Toast.makeText(ImageViewActivity.this, "Share Scanning Text", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(ImageViewActivity.this, "Failed to scanning "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(ImageViewActivity.this, "Failed to scanning "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}