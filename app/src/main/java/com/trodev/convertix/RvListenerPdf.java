package com.trodev.convertix;

import com.trodev.convertix.adapters.AdapterPdf;
import com.trodev.convertix.models.ModelPdf;

public interface RvListenerPdf {

    void onPdfClick(ModelPdf modelPdf, int position);

    void onPdfMoreClick(ModelPdf modelPdf, int position, AdapterPdf.HolderPdf holder);
}
