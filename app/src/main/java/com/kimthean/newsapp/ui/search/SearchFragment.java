package com.kimthean.newsapp.ui.search;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

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
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchFragment extends Fragment {
    private NewsAdapter newsAdapter;
    private List<News> newsList;
    private RecyclerView rvNews;
    private ProgressBar progressBar;

    private TextView tvNoResults;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        SearchView searchView = view.findViewById(R.id.searchView);
        rvNews = view.findViewById(R.id.rvNews);
        progressBar = view.findViewById(R.id.progressBar);
        tvNoResults = view.findViewById(R.id.tvNoResults);

        newsList = new ArrayList<>();
        newsAdapter = new NewsAdapter(newsList);
        rvNews.setAdapter(newsAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                progressBar.setVisibility(View.VISIBLE);
                tvNoResults.setVisibility(View.GONE);
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        return view;
    }

    private void performSearch(String query) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.org/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NewsApiService newsApiService = retrofit.create(NewsApiService.class);
        Call<NewsApiResponse> call = newsApiService.getEverything(query, "15c48473a93d4ec988e6acdee9055ac4");
        call.enqueue(new Callback<NewsApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<NewsApiResponse> call, @NonNull Response<NewsApiResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    NewsApiResponse newsApiResponse = response.body();

                    if (newsApiResponse != null && newsApiResponse.getArticles().isEmpty()) {
                        tvNoResults.setText("No results found for \"" + query + "\"");
                        tvNoResults.setVisibility(View.VISIBLE);
                    } else {
                        tvNoResults.setVisibility(View.GONE);
                    }

                    if (newsApiResponse != null) {
                        Log.d("SearchFragment", "onResponse: " + newsApiResponse.getArticles().size());
                        List<Article> articles = newsApiResponse.getArticles();
                        newsList.clear();
                        for (Article article : articles) {
                            if (!Objects.equals(article.getTitle(), "[Removed]") && article.getUrlToImage() != null) {
                                News news = new News(
                                        article.getTitle(),
                                        article.getDescription(),
                                        article.getUrlToImage(),
                                        article.getNewsSource(),
                                        article.getPublishedAt(),
                                        article.getNewsUrl()
                                );
                                newsList.add(news);
                            }
                        }
                        newsAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewsApiResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("SearchFragment", "onFailure: ", t);
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
