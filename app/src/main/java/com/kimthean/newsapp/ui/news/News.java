package com.kimthean.newsapp.ui.news;

import com.kimthean.newsapp.data.Source;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class News {
    private final String title;
    private final String imageUrl;

    private final Source newsSource;

    private final String newsTimeUpdated;

    private final String newsUrl;

    public News(String title, String imageUrl, Source newsSource, String newsTimeUpdated, String newsUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.newsSource= newsSource;
        this.newsTimeUpdated = newsTimeUpdated;
        this.newsUrl = newsUrl;
    }

    public String getTitle() {
        return title;
    }

    public Source getNewsSource() {
        return newsSource;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public String getNewsTimeUpdated() {
        try {
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = isoFormat.parse(newsTimeUpdated);

            SimpleDateFormat readableFormat = new SimpleDateFormat("dd MMM yyyy, hh:mm a");
            return readableFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return newsTimeUpdated;
        }
    }

    public String getNewsUrl() {
        return newsUrl;
    }


}