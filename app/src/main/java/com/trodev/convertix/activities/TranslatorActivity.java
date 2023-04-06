package com.trodev.convertix.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.trodev.convertix.R;
import com.trodev.convertix.models.ModelLanguage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TranslatorActivity extends AppCompatActivity {

    private EditText sourceLanguageET;
    private TextView resultTv;
    private MaterialButton sourceLanguageChooseBtn, destinationLanguageChooseBtn;
    private ImageButton translateBtn;
    private TranslatorOptions translatorOptions;
    private Translator translator;
    private ProgressDialog progressDialog;
    private ArrayList<ModelLanguage> languageArrayList;
    private static final String TAG = "MAIN_TAG";
    private String sourceLanguageCode = "en";
    private String sourceLanguageTitle = "English";
    private String destintaionLanguageCode = "bn";
    private String destintaionLanguageTitle = "Bangla";

    private ImageButton shareBtn, copyBtn;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translator);

        // set title in activity
        getSupportActionBar().setTitle("Language Translator");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // all button are here
        sourceLanguageET = findViewById(R.id.sourceLanguageET);
        resultTv = findViewById(R.id.resultTv);
        sourceLanguageChooseBtn = findViewById(R.id.sourceLanguageChooseBtn);
        destinationLanguageChooseBtn = findViewById(R.id.destinationLanguageChooseBtn);
        translateBtn = findViewById(R.id.translateBtn);

        shareBtn = findViewById(R.id.shareBtn);
        copyBtn = findViewById(R.id.copyBtn);

        // set visibility button
        shareBtn.setVisibility(View.INVISIBLE);
        copyBtn.setVisibility(View.INVISIBLE);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait,\nDownload Language Model");
        progressDialog.setCanceledOnTouchOutside(false);

        loadAvailableLanguage();

        sourceLanguageChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sourceLanguageChoose();
            }
        });

        destinationLanguageChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                destinationLanguageChoose();
            }
        });

        translateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
    }

    private String sourceLanguageText = "";

    private void validateData() {
        sourceLanguageText = sourceLanguageET.getText().toString().trim();

        Log.d(TAG, "validateData: sourceLanguageText: " + sourceLanguageText);

        if (sourceLanguageText.isEmpty()) {
            Toast.makeText(this, "Text field is Empty", Toast.LENGTH_SHORT).show();
        } else {
            startTranlations();
        }


    }

    private void startTranlations() {
        progressDialog.setMessage("Processing Language");
        progressDialog.show();

        translatorOptions = new TranslatorOptions.Builder().setSourceLanguage(sourceLanguageCode).setTargetLanguage(destintaionLanguageCode).build();

        translator = Translation.getClient(translatorOptions);

        DownloadConditions downloadConditions = new DownloadConditions.Builder().requireWifi().build();

        translator.downloadModelIfNeeded(downloadConditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                // translation model ready to be translated, lets translate
                progressDialog.setMessage("Translating");
                Log.d(TAG, "onSuccess: model ready, starting translate");

                translator.translate(sourceLanguageText).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.d(TAG, "onSuccess: " + s);
                        resultTv.setText(s);
                        copyBtn.setVisibility(View.VISIBLE);
                        shareBtn.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();

                        copyBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ClipboardManager clipboard = (ClipboardManager) getSystemService(TranslatorActivity.this.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("TextView", resultTv.getText().toString());
                                clipboard.setPrimaryClip(clip);
                                Toast.makeText(TranslatorActivity.this, "Copy successful", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(TranslatorActivity.this, "Share Translating Text", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d(TAG, "onFailure: " + e.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(TranslatorActivity.this, "Translation Failed", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Log.d(TAG, "onFailure: " + e.getMessage());
                Toast.makeText(TranslatorActivity.this, "Failed to ready model due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sourceLanguageChoose() {
        PopupMenu popupMenu = new PopupMenu(this, sourceLanguageChooseBtn);
        for (int i = 0; i < languageArrayList.size(); i++) {
            popupMenu.getMenu().add(Menu.NONE, i, i, languageArrayList.get(i).languageTitle);
        }

        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                int position = menuItem.getItemId();
                sourceLanguageCode = languageArrayList.get(position).languageCode;
                sourceLanguageTitle = languageArrayList.get(position).languageTitle;

                sourceLanguageChooseBtn.setText(sourceLanguageTitle);
                sourceLanguageET.setHint("Enter " + sourceLanguageTitle);


                Log.d(TAG, "onMenuItemClick: sourceLanguageCode: " + sourceLanguageTitle);
                return false;
            }
        });
    }

    private void destinationLanguageChoose() {
        PopupMenu popupMenu = new PopupMenu(this, destinationLanguageChooseBtn);

        for (int i = 0; i < languageArrayList.size(); i++) {
            popupMenu.getMenu().add(Menu.NONE, i, i, languageArrayList.get(i).getLanguageTitle());
        }

        popupMenu.show();


        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                int position = menuItem.getItemId();

                destintaionLanguageCode = languageArrayList.get(position).languageCode;
                destintaionLanguageTitle = languageArrayList.get(position).languageTitle;

                destinationLanguageChooseBtn.setText(destintaionLanguageTitle);

                Log.d(TAG, "onMenuItemClick: destintaionLanguageCode:  " + destintaionLanguageCode);
                Log.d(TAG, "onMenuItemClick: destintaionLanguageTitle:  " + destintaionLanguageTitle);

                return false;
            }
        });

    }

    private void loadAvailableLanguage() {

        languageArrayList = new ArrayList<>();

        List<String> languageCodeList = TranslateLanguage.getAllLanguages();

        for (String languageCode : languageCodeList) {
            String languageTitle = new Locale(languageCode).getDisplayLanguage(); //e.g en -> English

            Log.d(TAG, "loadAvailableLanguage: languageCode: " + languageCode);
            Log.d(TAG, "loadAvailableLanguage: languageTitle: " + languageTitle);

            ModelLanguage modelLanguage = new ModelLanguage(languageCode, languageTitle);
            languageArrayList.add(modelLanguage);
        }
    }
}