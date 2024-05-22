package com.kimthean.newsapp.ui.news;

import com.kimthean.newsapp.data.Source;

public class News {
    private String title;
    private String description;
    private String imageUrl;

    private Source newsSource;

    private String newsTimeUpdated;

    private String newsUrl;

    public News(String title, String description, String imageUrl, Source newsSource, String newsTimeUpdated, String newsUrl) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.newsSource= newsSource;
        this.newsTimeUpdated = newsTimeUpdated;
        this.newsUrl = newsUrl;
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

    public String getNewsUrl() {
        return newsUrl;
    }


}