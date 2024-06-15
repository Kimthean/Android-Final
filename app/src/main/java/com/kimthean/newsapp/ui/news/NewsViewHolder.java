package com.kimthean.newsapp.ui.news;


import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kimthean.newsapp.NewsDetailActivity;
import com.kimthean.newsapp.R;

import java.util.List;

public class NewsViewHolder extends RecyclerView.ViewHolder {
    private final TextView newsTitle;
    private final ImageView newsImage;

    private final TextView newsSource;

    private final TextView newsTimeUpdated;

    private final ImageButton bookmarkButton;

    public NewsViewHolder(@NonNull View itemView, List<News> newsList) {
        super(itemView);
        newsTitle = itemView.findViewById(R.id.newsTitle);
        newsImage = itemView.findViewById(R.id.newsImage);
        newsSource = itemView.findViewById(R.id.newsSource);
        newsTimeUpdated = itemView.findViewById(R.id.newsTimeUpdated);
        bookmarkButton = itemView.findViewById(R.id.bookmarkButton);


        itemView.setOnClickListener(v -> {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                News clickedNews = newsList.get(position);

                Intent intent = new Intent(v.getContext(), NewsDetailActivity.class);
                intent.putExtra("url", clickedNews.getNewsUrl());

                v.getContext().startActivity(intent);
            }
        });
    }

    public void bind(News news) {
        newsTitle.setText(news.getTitle());
        Glide.with(itemView.getContext()).load(news.getImageUrl()).into(newsImage);
        newsSource.setText(news.getNewsSource().getName());
        newsTimeUpdated.setText(news.getNewsTimeUpdated());
        }
}