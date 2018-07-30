package com.example.android.now_newsapp;

// One news article
public class Article {

    private String aId;
    private String aSectionId;
    private String aSection;
    private String aTitle;
    private String aWebUrl;
    private String aImageUrl;
    private String aPublishDate;
    private String aAuthorName;

    public Article(String _aId, String _aSectionId, String _aSection, String _aTitle, String _aWebUrl, String _aImageUrl, String _aPublishDate, String _aAuthorName) {
        this.aId          = _aId;
        this.aSectionId   = _aSectionId;
        this.aSection     = _aSection;
        this.aTitle       = _aTitle;
        this.aWebUrl      = _aWebUrl;
        this.aImageUrl    = _aImageUrl;
        this.aPublishDate = _aPublishDate;
        this.aAuthorName  = _aAuthorName;
    }

    // All functions get private variables.
    public String getaId() {
        return aId;
    }

    public String getaSectionId() {
        return aSectionId;
    }

    public String getaSection() {
        return aSection;
    }

    public String getaTitle() {
        return aTitle;
    }

    public String getaWebUrl() {
        return aWebUrl;
    }

    public String getaImageUrl() {
        return aImageUrl;
    }

    public String getaPublishDate() {
        return aPublishDate;
    }

    public String getaAuthorName() {
        return aAuthorName;
    }
}
