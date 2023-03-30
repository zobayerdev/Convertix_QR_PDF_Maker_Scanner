package com.trodev.convertix.models;

import android.net.Uri;

public class ModelImage {

    Uri imageUri;
    boolean checked;

    public ModelImage(Uri imageUri, boolean checked) {
        this.imageUri = imageUri;
        this.checked = checked;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
