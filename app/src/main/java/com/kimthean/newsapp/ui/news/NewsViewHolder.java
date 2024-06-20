package com.kimthean.newsapp.ui.news;


import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kimthean.newsapp.NewsDetailActivity;
import com.kimthean.newsapp.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsViewHolder extends RecyclerView.ViewHolder {
    private final TextView newsTitle;
    private final ImageView newsImage;

    private final TextView newsSource;

    private final TextView newsTimeUpdated;

    private final ImageButton bookmarkButton;

    private final FirebaseFirestore firestore;
    private final FirebaseUser currentUser;
    private boolean isBookmarked;
    private DocumentReference bookmarkRef;




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

        firestore = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        bookmarkButton.setOnClickListener(v -> {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                News news = newsList.get(position);
                toggleBookmark(news);
            }
        });
    }

    public void bind(News news) {
        checkBookmarkStatus(news);
        newsTitle.setText(news.getTitle());
        Glide.with(itemView.getContext()).load(news.getImageUrl()).into(newsImage);
        newsSource.setText(news.getNewsSource().getName());
        newsTimeUpdated.setText(news.getNewsTimeUpdated());
    }

    private void toggleBookmark(News news) {
        if (currentUser != null) {
            String userId = currentUser.getUid();
            String bookmarkId = news.getTitle() + "_" + userId;

            if (isBookmarked) {
                bookmarkRef.delete()
                        .addOnSuccessListener(aVoid -> {
                            isBookmarked = false;
                            bookmarkButton.setBackground(AppCompatResources.getDrawable(itemView.getContext(), R.drawable.ic_bookmark));
                            Toast.makeText(itemView.getContext(), "Bookmark removed", Toast.LENGTH_SHORT).show();
                            bookmarkRef = null;
                        })
                        .addOnFailureListener(e -> {
                            Log.e("NewsViewHolder", "Failed to remove bookmark", e);
                            Toast.makeText(itemView.getContext(), "Failed to remove bookmark", Toast.LENGTH_SHORT).show();
                        });
            } else {
                Map<String, Object> bookmarkData = new HashMap<>();
                bookmarkData.put("title", news.getTitle());
                bookmarkData.put("url", news.getNewsUrl());
                bookmarkData.put("source", news.getNewsSource().getName());
                bookmarkData.put("imageUrl", news.getImageUrl());
                bookmarkData.put("timeUpdated", news.getNewsTimeUpdated());

                firestore.collection("users")
                        .document(userId)
                        .collection("bookmarks")
                        .document(bookmarkId)
                        .set(bookmarkData)
                        .addOnSuccessListener(aVoid -> {
                            isBookmarked = true;
                            bookmarkButton.setBackground(AppCompatResources.getDrawable(itemView.getContext(), R.drawable.ic_bookmark_filled));
                            Toast.makeText(itemView.getContext(), "Bookmark added", Toast.LENGTH_SHORT).show();
                            bookmarkRef = firestore.collection("users")
                                    .document(userId)
                                    .collection("bookmarks")
                                    .document(bookmarkId);
                        })
                        .addOnFailureListener(e -> {
                            Log.e("NewsViewHolder", "Failed to add bookmark", e);
                            Toast.makeText(itemView.getContext(), "Failed to add bookmark", Toast.LENGTH_SHORT).show();
                        });
            }
        }
    }

    private void checkBookmarkStatus(News news) {
        if (currentUser != null) {
            String userId = currentUser.getUid();
            String bookmarkId = news.getTitle() + "_" + userId;

            firestore.collection("users")
                    .document(userId)
                    .collection("bookmarks")
                    .document(bookmarkId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            isBookmarked = true;
                            bookmarkButton.setBackground(AppCompatResources.getDrawable(itemView.getContext(), R.drawable.ic_bookmark_filled));
                            bookmarkRef = documentSnapshot.getReference();
                        } else {
                            isBookmarked = false;
                            bookmarkButton.setBackground(AppCompatResources.getDrawable(itemView.getContext(), R.drawable.ic_bookmark));
                            bookmarkRef = null;
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("NewsViewHolder", "Failed to check bookmark status", e);
                        Toast.makeText(itemView.getContext(), "Failed to check bookmark status", Toast.LENGTH_SHORT).show();
                    });
        }
    }

}