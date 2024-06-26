package com.kimthean.newsapp.ui.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.kimthean.newsapp.BuildConfig;
import com.kimthean.newsapp.R;
import com.kimthean.newsapp.api.NewsApiService;
import com.kimthean.newsapp.data.Article;
import com.kimthean.newsapp.data.NewsApiResponse;
import com.kimthean.newsapp.ui.news.News;
import com.kimthean.newsapp.ui.news.NewsAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategoryFragment extends Fragment {

    private NewsAdapter newsAdapter;
    private ProgressBar progressBar;
    private List<News> newsList = new ArrayList<>();
    private RecyclerView rvNews;

    private SwipeRefreshLayout swipeRefreshLayout;

    private CategoryViewModel categoryViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        LinearLayout categoryLayout = view.findViewById(R.id.categoryLayout);
        TextView categoryTitle = view.findViewById(R.id.tvCategory);
        progressBar = view.findViewById(R.id.progressBar);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        rvNews = view.findViewById(R.id.rvNews);
        newsAdapter = new NewsAdapter(newsList);
        rvNews.setAdapter(newsAdapter);

        categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);

        categoryViewModel.getNewsList().observe(getViewLifecycleOwner(), new Observer<List<News>>() {
            @Override
            public void onChanged(List<News> news) {
                newsAdapter.setNews(news);
            }
        });

        categoryViewModel.getCategory().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String category) {
                categoryTitle.setText(category.toUpperCase(Locale.ROOT));
            }
        });

        if (categoryViewModel.getCategory().getValue() == null) {
            categoryViewModel.setCategory("Technology");
        }

        progressBar.setVisibility(View.VISIBLE);

        fetchNewsData(categoryViewModel.getCategory().getValue());

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchNewsData(categoryViewModel.getCategory().getValue());
            }
        });

        String[] categories = { "Technology" ,"Business", "Entertainment", "General", "Health", "Science", "Sports"};

        for (String category : categories) {
            Button button = new Button(getContext());
            button.setText(category);
            button.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.rounded_button));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < categoryLayout.getChildCount(); i++) {
                        View child = categoryLayout.getChildAt(i);
                        if (child instanceof Button) {
                            child.setSelected(false);
                        }
                    }
                    progressBar.setVisibility(View.VISIBLE);
                    rvNews.setVisibility(View.GONE);
                    String selectedCategory = category.toLowerCase(Locale.ROOT);
                    categoryViewModel.setCategory(selectedCategory);
                    fetchNewsData(selectedCategory);
                    v.setSelected(true);
                    Toast.makeText(getContext(), "Selected category: " + category, Toast.LENGTH_SHORT).show();
                }
            });

            if (category.equalsIgnoreCase(categoryViewModel.getCategory().getValue())) {
                button.setSelected(true);
            }

            categoryLayout.addView(button);
        }

        return view;
    }

    private void fetchNewsData(String category) {

        String apiKey = BuildConfig.NewsAPIKey;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.org/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NewsApiService newsApiService = retrofit.create(NewsApiService.class);

        Call<NewsApiResponse> call = newsApiService.getTopHeadlines("us", apiKey, category);
        call.enqueue(new Callback<NewsApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<NewsApiResponse> call, @NonNull Response<NewsApiResponse> response) {
                if (response.isSuccessful()) {
                    swipeRefreshLayout.setRefreshing(false);
                    progressBar.setVisibility(View.GONE);
                    rvNews.setVisibility(View.VISIBLE);
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
                    categoryViewModel.setNewsList(newsList);
                    categoryViewModel.setCategory(category);
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