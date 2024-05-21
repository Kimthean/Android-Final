package com.kimthean.newsapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.kimthean.newsapp.R;
import com.kimthean.newsapp.api.NewsApiService;
import com.kimthean.newsapp.data.Article;
import com.kimthean.newsapp.data.NewsApiResponse;
import com.kimthean.newsapp.ui.news.News;
import com.kimthean.newsapp.ui.news.NewsAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {
    private NewsAdapter newsAdapter;
    private List<News> newsList;

    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        progressBar= view.findViewById(R.id.progressBar);
        RecyclerView rvNews = view.findViewById(R.id.rvNews);

        newsList = new ArrayList<>();
        newsAdapter = new NewsAdapter(newsList);

        rvNews.setAdapter(newsAdapter);

        fetchNewsData();

        return view;
    }

    private void fetchNewsData() {

        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.org/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NewsApiService newsApiService = retrofit.create(NewsApiService.class);

        Call<NewsApiResponse> call = newsApiService.getTopHeadlines("us", "15c48473a93d4ec988e6acdee9055ac4");
        call.enqueue(new Callback<NewsApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<NewsApiResponse> call, @NonNull Response<NewsApiResponse> response) {
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    NewsApiResponse newsApiResponse = response.body();
                    assert newsApiResponse != null;
                    List<Article> articles = newsApiResponse.getArticles();

                    newsList.clear();
                    for (Article article : articles) {
                        News news = new News(
                                article.getTitle(),
                                article.getDescription(),
                                article.getUrlToImage(),
                                article.getNewsSource(),
                                article.getPublishedAt()
                        );
                        newsList.add(news);
                    }
                    newsAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(@NonNull Call<NewsApiResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });
    }
}