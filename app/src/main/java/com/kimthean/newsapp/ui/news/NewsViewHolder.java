package com.kimthean.newsapp.ui.news;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kimthean.newsapp.R;

public class NewsViewHolder extends RecyclerView.ViewHolder {
    private TextView newsTitle;
    private ImageView newsImage;

    private TextView newsSource;

    private TextView newsTimeUpdated;

    public NewsViewHolder(@NonNull View itemView) {
        super(itemView);
        newsTitle = itemView.findViewById(R.id.newsTitle);
        newsImage = itemView.findViewById(R.id.newsImage);
        newsSource = itemView.findViewById(R.id.newsSource);
        newsTimeUpdated = itemView.findViewById(R.id.newsTimeUpdated);
    }

    public void bind(News news) {
        newsTitle.setText(news.getTitle());
        Glide.with(itemView.getContext()).load(news.getImageUrl()).into(newsImage);
        newsSource.setText(news.getNewsSource().getName());
        newsTimeUpdated.setText(news.getNewsTimeUpdated());
        }
}