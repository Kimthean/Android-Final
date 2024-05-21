package com.kimthean.newsapp.ui.news;

import com.kimthean.newsapp.data.Source;

public class News {
    private String title;
    private String description;
    private String imageUrl;

    private Source newsSource;

    private String newsTimeUpdated;
    private int imageResource;

    public News(String title, String description, String imageUrl, Source newsSource, String newsTimeUpdated) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.newsSource= newsSource;
        this.newsTimeUpdated = newsTimeUpdated;
    }

    // Getter methods
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Source getNewsSource() {
        return newsSource;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public String getNewsTimeUpdated() {
        return newsTimeUpdated;
    }



    // Setter methods
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNewsSource(Source newsSource) {
        this.newsSource = newsSource;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}