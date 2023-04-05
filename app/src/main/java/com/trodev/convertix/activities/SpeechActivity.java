package com.trodev.convertix.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.trodev.convertix.R;

import java.util.Locale;

public class SpeechActivity extends AppCompatActivity {

     EditText textEt ;
    private Button speak, notspeak ;
    TextToSpeech mTextToSpeech ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);

        // set title in activity
        getSupportActionBar().setTitle("Text To Speech");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //init views id
        textEt = findViewById(R.id.textET);
        speak = findViewById(R.id.speakBtn);
        notspeak = findViewById(R.id.speakNotBtn);

        mTextToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                // if there is no error then set language
                if(status != TextToSpeech.ERROR)
                {
                    mTextToSpeech.setLanguage(Locale.UK);
                }
                else
                {
                    Toast.makeText(SpeechActivity.this, "Erro", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Set on Click Listener

        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get text from edit text
                String toSpeak = textEt.getText().toString().trim();
                if(toSpeak.equals(""))
                {
                    //if there is no text in edit text
                    Toast.makeText(SpeechActivity.this, "Please enter text.....", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(SpeechActivity.this, toSpeak, Toast.LENGTH_SHORT).show();
                    //speak the text
                    mTextToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });

        notspeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mTextToSpeech.isSpeaking())
                {
                    mTextToSpeech.stop();
                    //mTextToSpeech.shutdown();
                }
                else
                {
                    Toast.makeText(SpeechActivity.this, "Not Speak", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        if(mTextToSpeech != null || mTextToSpeech.isSpeaking())
        {
            mTextToSpeech.stop();
            // mTextToSpeech.shutdown();
        }
        super.onPause();
    }
}