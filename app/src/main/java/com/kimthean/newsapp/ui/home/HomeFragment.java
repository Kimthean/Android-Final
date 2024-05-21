package com.kimthean.newsapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.kimthean.newsapp.R;
import com.kimthean.newsapp.ui.news.News;
import com.kimthean.newsapp.ui.news.NewsAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView rvNews;
    private NewsAdapter newsAdapter;
    private List<News> newsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        rvNews = view.findViewById(R.id.rvNews);

        newsList = new ArrayList<>();
        newsAdapter = new NewsAdapter(newsList);

        rvNews.setAdapter(newsAdapter);

        fillDummyData();

        return view;
    }

    private void fillDummyData() {
        for (int i = 0; i < 10; i++) {
            News news = new News();
            news.setTitle("News Title " + (i + 1));
            news.setDescription("News Description " + (i + 1));
            news.setImageResource(R.drawable.news_placeholder);
            newsList.add(news);
        }

        newsAdapter.notifyDataSetChanged();
    }
}