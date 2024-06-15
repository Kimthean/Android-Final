package com.kimthean.newsapp.data;

import com.google.gson.annotations.SerializedName;

public class Article {
    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("urlToImage")
    private String urlToImage;

    @SerializedName("source")
    private Source newsSource;

    @SerializedName("publishedAt")
    private String publishedAt;

    @SerializedName("url")
    private String newsUrl;

    // Getter methods
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public Source getNewsSource() {
        return newsSource;
    }
    public String getPublishedAt() {
        return publishedAt;
    }
    public String getNewsUrl() {
        return newsUrl;
    }


}

