package com.trodev.convertix.models;

import android.graphics.Bitmap;
import android.net.Uri;

public class ModelPdfView {

    Uri pdfUri;
    int pageNumber;
    int pageCount;
    Bitmap bitmap;

    public ModelPdfView(Uri pdfUri, int pageNumber, int pageCount, Bitmap bitmap) {
        this.pdfUri = pdfUri;
        this.pageNumber = pageNumber;
        this.pageCount = pageCount;
        this.bitmap = bitmap;
    }

    public Uri getPdfUri() {
        return pdfUri;
    }

    public void setPdfUri(Uri pdfUri) {
        this.pdfUri = pdfUri;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
