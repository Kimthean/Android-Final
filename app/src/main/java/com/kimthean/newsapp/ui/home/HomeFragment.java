package com.kimthean.newsapp.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.lifecycle.ViewModelProvider;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.kimthean.newsapp.R;
import com.kimthean.newsapp.api.NewsApiService;
import com.kimthean.newsapp.data.Article;
import com.kimthean.newsapp.data.NewsApiResponse;
import com.kimthean.newsapp.ui.news.News;
import com.kimthean.newsapp.ui.news.NewsAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {
    private NewsAdapter newsAdapter;
    private NewsViewModel newsViewModel;

    private List<News> newsList;

    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        super.onCreate(savedInstanceState);

        progressBar= view.findViewById(R.id.progressBar);
        RecyclerView rvNews = view.findViewById(R.id.rvNews);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        newsViewModel = new ViewModelProvider(requireActivity()).get(NewsViewModel.class);
        newsList = newsViewModel.getNewsList().getValue();
        Log.d("HomeFragment", "onCreateView: " + newsList);

        if (newsList == null || newsList.isEmpty()) {
            newsList = new ArrayList<>();
            fetchNewsData();
            newsAdapter = new NewsAdapter(newsList);
        } else {
            progressBar.setVisibility(View.GONE);
        }


        rvNews.setAdapter(newsAdapter);
        swipeRefreshLayout.setOnRefreshListener(this::fetchNewsData);

        return view;
    }

    private void fetchNewsData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.org/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NewsApiService newsApiService = retrofit.create(NewsApiService.class);
        Call<NewsApiResponse> call = newsApiService.getTopHeadlines("us", "15c48473a93d4ec988e6acdee9055ac4", null);
        call.enqueue(new Callback<NewsApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<NewsApiResponse> call, @NonNull Response<NewsApiResponse> response) {
                progressBar.setVisibility(View.VISIBLE);
                Log.d("HomeFragment", "onResponse: " + response);
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    NewsApiResponse newsApiResponse = response.body();
                    assert newsApiResponse != null;
                    List<Article> articles = newsApiResponse.getArticles();

                    newsList.clear();
                    for (Article article : articles) {
                        if (!Objects.equals(article.getTitle(), "[Removed]") && article.getUrlToImage() != null) {
                            News news = new News(
                                    article.getTitle(),
                                    article.getUrlToImage(),
                                    article.getNewsSource(),
                                    article.getPublishedAt(),
                                    article.getNewsUrl()
                            );
                            newsList.add(news);
                        }
                    }
                    newsViewModel.setNewsList(newsList);
                    newsAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(@NonNull Call<NewsApiResponse> call, @NonNull Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });
    }
}