package com.kimthean.newsapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewsApiResponse {

    @SerializedName("articles")
    private final List<Article> articles;

    public NewsApiResponse(List<Article> articles) {
        this.articles = articles;
    }


    public List<Article> getArticles() {
        return articles;
    }

}