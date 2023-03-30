package com.trodev.convertix.models;

import android.net.Uri;

import java.io.File;

public class ModelPdf {

    File file ;
    Uri uri ;

    public ModelPdf(File file, Uri uri) {
        this.file = file;
        this.uri = uri;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
