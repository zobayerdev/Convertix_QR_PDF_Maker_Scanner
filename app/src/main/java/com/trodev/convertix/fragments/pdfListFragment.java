package com.trodev.convertix.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.trodev.convertix.Constant;
import com.trodev.convertix.R;
import com.trodev.convertix.RvListenerPdf;
import com.trodev.convertix.activities.PdfViewActivity;
import com.trodev.convertix.adapters.AdapterPdf;
import com.trodev.convertix.models.ModelPdf;
import java.io.File;
import java.util.ArrayList;


public class pdfListFragment extends Fragment {

    private RecyclerView pdfRv;

    private Context mContext;
    private ArrayList<ModelPdf> pdfArrayList;
    private AdapterPdf adapterPdf;

    private static final String TAG = "PDF_LIST_TAG";

    public pdfListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        mContext = context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pdf_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pdfRv = view.findViewById(R.id.pdfRv);
        loadPdfDocuments();
    }

    private void loadPdfDocuments() {
        pdfArrayList = new ArrayList<>();
        adapterPdf = new AdapterPdf(mContext, pdfArrayList, new RvListenerPdf() {
            @Override
            public void onPdfClick(ModelPdf modelPdf, int position) {
                Intent intent = new Intent(mContext, PdfViewActivity.class);
                intent.putExtra("pdfUri", "" + modelPdf.getUri());
                startActivity(intent);
            }

            @Override
            public void onPdfMoreClick(ModelPdf modelPdf, int position, AdapterPdf.HolderPdf holder) {
                //show dialog with options rename, delete, share
                showMoreOptions(modelPdf, holder);

            }
        });

        pdfRv.setAdapter(adapterPdf);

        File folder = new File(mContext.getExternalFilesDir(null), Constant.PDF_FOLDER);

        if (folder.exists()) {
            File[] files = folder.listFiles();
            Log.d(TAG, "loadPdfDocuments: Files Count " + files.length);

            for (File fileEntry : files) {
                Log.d(TAG, "loadPdfDocuments:  File Name: " + fileEntry.getName());

                Uri uri = Uri.fromFile(fileEntry);

                ModelPdf modelPdf = new ModelPdf(fileEntry, uri);

                pdfArrayList.add(modelPdf);
                adapterPdf.notifyItemInserted(pdfArrayList.size());
            }
        }
    }

    private void showMoreOptions(ModelPdf modelPdf, AdapterPdf.HolderPdf holder) {
        Log.d(TAG, "showMoreOptions: ");

        PopupMenu popupMenu = new PopupMenu(mContext, holder.moreBtn);
        popupMenu.getMenu().add(Menu.NONE, 0, 0, "Rename");
        popupMenu.getMenu().add(Menu.NONE, 1, 1, "Delete");
        popupMenu.getMenu().add(Menu.NONE, 2, 2, "Share");

        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //get item is that is clicked from popupmenu
                int itemId = item.getItemId();

                if (itemId == 0) {
                    pdfRename(modelPdf);
                } else if (itemId == 1) {
                    pdfDelete(modelPdf);
                } else if (itemId == 2) {
                    pdfShare(modelPdf);
                }


                return false;
            }
        });

    }

    private void pdfDelete(ModelPdf modelPdf) {
        Log.d(TAG, "pdfDelete: ");

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Delete File")
                .setMessage("Are you sure want to delete the " + modelPdf.getFile().getName())
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            modelPdf.getFile().delete();
                            loadPdfDocuments();
                            Toast.makeText(mContext, "Delete successfully....", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.d(TAG, "pdfDelete onClick: ", e);
                            Toast.makeText(mContext, "Failed to delete due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();

    }


    private void pdfRename(ModelPdf modelPdf) {
        Log.d(TAG, "pdfRename: ");

        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_rename, null);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        EditText pdfNewNameEt = view.findViewById(R.id.pdfNewNameEt);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button renameBtn = view.findViewById(R.id.renameBtn);

        //get current name of the pdf document to show in the pdfNewNameEt EditText
        String previousName = "" + modelPdf.getFile().getName();
        Log.d(TAG, "pdfRename: previous Name" + previousName);

        pdfNewNameEt.setText(previousName);


        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        renameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = pdfNewNameEt.getText().toString().trim();
                Log.d(TAG, "onClick: newName: " + newName);

                if (newName.isEmpty()) {
                    Toast.makeText(mContext, "Enter PDF name", Toast.LENGTH_SHORT).show();
                } else {
                    try {

                        File newFile = new File(mContext.getExternalFilesDir(null), Constant.PDF_FOLDER + "/" + newName + ".pdf");

                        modelPdf.getFile().renameTo(newFile);
                        Toast.makeText(mContext, "Rename Successfully...", Toast.LENGTH_SHORT).show();
                        loadPdfDocuments();
                    } catch (Exception e) {
                        Log.d(TAG, "renameBtn onClick: ", e);
                        Toast.makeText(mContext, "Failed to rename due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    alertDialog.dismiss();
                }
            }
        });
    }


    //share er khetre jekono file nite hole manifest e apnar eikhane provider er onsho tuku change kore nite hobe apnar application er package file onusare
    //then eikhane code tuku nite hobe and add korte hobe.
    private void pdfShare(ModelPdf modelPdf)
    {
        Log.d(TAG, "sharePdf:  ");

        File file = modelPdf.getFile();

        //generate uri
        Uri uri = FileProvider.getUriForFile(mContext, "com.trodev.tunascanner.fileprovider", file);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/pdf");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(intent, "Share PDF"));

    }
}