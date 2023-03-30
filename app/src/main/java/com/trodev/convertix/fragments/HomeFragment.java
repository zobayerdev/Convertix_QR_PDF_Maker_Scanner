package com.trodev.convertix.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.trodev.convertix.R;
import com.trodev.convertix.activities.BarCodeGenerator;
import com.trodev.convertix.activities.ContactActivity;
import com.trodev.convertix.activities.MessageActivity;
import com.trodev.convertix.activities.ProductQRActivity;
import com.trodev.convertix.activities.ScanGalleryActivity;
import com.trodev.convertix.activities.ScannerActivity;
import com.trodev.convertix.activities.URLActivity;
import com.trodev.convertix.activities.WifiQRActivity;

public class HomeFragment extends Fragment {
    private CardView contact, product_qr, weburl, message, barcode, scanqrbar, scangallery, wifi;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        contact = view.findViewById(R.id.contact);
        product_qr = view.findViewById(R.id.product_qr);
        weburl = view.findViewById(R.id.weburl);
        message = view.findViewById(R.id.message);
        barcode = view.findViewById(R.id.barcode);
        // scanqrbar = view.findViewById(R.id.scanqrbar);
        // scangallery = view.findViewById(R.id.scangallery);
        wifi = view.findViewById(R.id.wifi);

        wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WifiQRActivity.class);
                startActivity(intent);
            }
        });

        product_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProductQRActivity.class);
                startActivity(intent);
            }
        });

        weburl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), URLActivity.class);
                startActivity(intent);
            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MessageActivity.class);
                startActivity(intent);
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ContactActivity.class);
                startActivity(intent);
            }
        });

        barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BarCodeGenerator.class);
                startActivity(intent);
            }
        });


        return view;

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.menu_scan, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == R.id.images_item_scan) {
            Intent intent = new Intent(getContext(), ScannerActivity.class);
            startActivity(intent);
        } else if (itemId == R.id.gallery_item_scan) {
            Intent intent = new Intent(getContext(), ScanGalleryActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


}