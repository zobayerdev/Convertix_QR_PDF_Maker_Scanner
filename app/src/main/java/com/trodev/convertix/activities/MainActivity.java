package com.trodev.convertix.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.trodev.convertix.R;
import com.trodev.convertix.fragments.HomeFragment;
import com.trodev.convertix.fragments.ImageListFragment;
import com.trodev.convertix.fragments.pdfListFragment;

import java.util.Objects;

import androidmads.library.qrgenearator.BuildConfig;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private long pressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.navigation_view);


        // #######################
        // Drawer Layout implement
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.start, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // #################################################################
        // eikhane eituku hocche amader navigation layout er kaj korar jonno.
        navigationView.setNavigationItemSelectedListener(this::onOptionsItemSelected);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        loadHomeFragment();


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();
                if (itemId == R.id.bottom_menu_image) {
                    loadImagesFragment();
                } else if (itemId == R.id.bottom_menu_pdf) {
                    loadPDFFragment();
                } else if (itemId == R.id.bottom_menu_home) {
                    loadHomeFragment();
                } else {
                    Toast.makeText(MainActivity.this, "Invalid Click", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

    }

    private void loadImagesFragment() {
        setTitle("Images");
        ImageListFragment imageListFragment = new ImageListFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, imageListFragment, "ImageListFragment");
        fragmentTransaction.commit();
    }

    private void loadPDFFragment() {
        setTitle("PDFs");
        pdfListFragment pdfListFragment = new pdfListFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, pdfListFragment, "PdfListFragment");
        fragmentTransaction.commit();
    }


    private void loadHomeFragment() {
        setTitle("Home");
        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, homeFragment, "HomeListFragment");
        fragmentTransaction.commit();
    }

/*    private void loadScannerFragment() {
        setTitle("Scanner");
        CameraFragment cameraFragment = new CameraFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, cameraFragment, "ScannerListFragment");
        fragmentTransaction.commit();*/

/*        IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
        intentIntegrator.setPrompt("For flash use volume up key");
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setCaptureActivity(Capture.class);
        intentIntegrator.initiateScan();
        }*/

/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult.getContents() != null)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Scanning Result");
            builder.setMessage(intentResult.getContents());
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    Intent intent = new Intent(MainActivity.this, MainActivity.this.getClass());
                    startActivity(intent);
                    finish();
                }
            });

            builder.setNeutralButton("Copy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ClipboardManager clipboard = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("EditText", intentResult.getContents());
                    clipboard.setPrimaryClip(clipData);

                    Toast.makeText(getApplicationContext(), "Text Copied Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, MainActivity.this.getClass());
                    startActivity(intent);
                    finish();
                }
            });

            builder.show();
        }
        else{
            Toast.makeText(this, "Opps!", Toast.LENGTH_SHORT).show();
        }
    }*/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.nav_notification_notice:
                Toast.makeText(this, "Notification", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_dev:
                final Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.developer_bottomsheet_layout);

                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.BOTTOM);
                Toast.makeText(this, "Developer", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_policy:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.app-privacy-policy.com/live.php?token=qB3iS10fUJmr6yEFtaVo9yve0uuPP3Ok")));
                Toast.makeText(this, "Privacy Policy", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_share:
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Tuna Scanner");
                    String shareMessage = "\nTuna Scanner App Download now\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                    Toast.makeText(this, "Share Apps", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    //e.toString();
                }
                break;
            case R.id.nav_rate:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                    Toast.makeText(this, "Rate us", Toast.LENGTH_SHORT).show();
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    // In this code, android lifecycle exit on 2 times back-pressed.
    @Override
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }

}