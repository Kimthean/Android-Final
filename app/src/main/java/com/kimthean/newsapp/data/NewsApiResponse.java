package com.kimthean.newsapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewsApiResponse {

    @SerializedName("articles")
    private List<Article> articles;


    public List<Article> getArticles() {
        return articles;
    }

}