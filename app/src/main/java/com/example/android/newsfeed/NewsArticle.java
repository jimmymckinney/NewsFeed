package com.example.android.newsfeed;

public class NewsArticle {

    private String mImageId;
    private String mHeadline;
    private String mPublicationDate;
    private String mUrl;

    public NewsArticle(String imageId, String headline, String publicationDate, String url) {
        mImageId = imageId;
        mHeadline = headline;
        mPublicationDate = publicationDate;
        mUrl = url;
    }

    public String getImageId() {
        return mImageId;
    }

    public String getHeadline() {
        return mHeadline;
    }

    public String getPublicationDate() {
        return mPublicationDate;
    }

    public String getUrl() {
        return mUrl;
    }

}
