package com.kimthean.newsapp.api;

import com.kimthean.newsapp.data.NewsApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApiService {
    @GET("top-headlines")
    Call<NewsApiResponse> getTopHeadlines(
            @Query("country") String country,
            @Query("apiKey") String apiKey

    );
}