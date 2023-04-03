package com.trodev.convertix.models;

public class ModelLanguage {

  public  String languageCode;
  public  String languageTitle;

    public ModelLanguage(String languageCode, String languageTitle) {
        this.languageCode = languageCode;
        this.languageTitle = languageTitle;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getLanguageTitle() {
        return languageTitle;
    }

    public void setLanguageTitle(String languageTitle) {
        this.languageTitle = languageTitle;
    }
}
