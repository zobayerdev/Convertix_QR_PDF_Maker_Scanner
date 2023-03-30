package com.trodev.convertix.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.trodev.convertix.R;
import com.trodev.convertix.activities.ImageViewActivity;
import com.trodev.convertix.models.ModelImage;

import java.util.ArrayList;

public class AdapterImage extends RecyclerView.Adapter<AdapterImage.HolderImage> {

    private Context context;
    private ArrayList<ModelImage> imageArrayList;

    public AdapterImage(Context context, ArrayList<ModelImage> imageArrayList) {
        this.context = context;
        this.imageArrayList = imageArrayList;
    }

    @NonNull
    @Override
    public HolderImage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_image, parent, false);
        return new HolderImage(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderImage holder, int position) {
        //set data in ui views, handle it
        ModelImage modelImage = imageArrayList.get(position);
        Uri imageUri = modelImage.getImageUri();

        Glide.with(context)
                .load(imageUri)
                .placeholder(R.drawable.ic_image_black)
                .into(holder.imageiv);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ImageViewActivity.class);
                intent.putExtra("imageUri", "" + imageUri);
                context.startActivity(intent);

            }
        });

        //handle checkBox chjeck change listener to select/unselect the image
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                //update value is model isChecked is either true if false
                modelImage.setChecked(isChecked);

            }
        });
    }

    @Override
    public int getItemCount() {
        return imageArrayList.size();
    }


    class HolderImage extends RecyclerView.ViewHolder {

        ImageView imageiv;
        CheckBox checkBox;

        public HolderImage(@NonNull View itemView) {
            super(itemView);

            imageiv = itemView.findViewById(R.id.imageIv);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}
