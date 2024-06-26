package com.kimthean.newsapp.ui.news;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kimthean.newsapp.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder> {
    private List<News> newsList;


    public NewsAdapter(List<News> newsList) {
        this.newsList = newsList;
    }

    public void setNews(List<News> newsList) {
        this.newsList = newsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_card, parent, false);
        return new NewsViewHolder(view, newsList);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News news = newsList.get(position);
        holder.bind(news);

    }


    @Override
    public int getItemCount() {
        return newsList.size();
    }
}
