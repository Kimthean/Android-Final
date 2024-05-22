package com.kimthean.newsapp;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class NewsDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail);

        WebView webView = findViewById(R.id.wvNewsDetail);

        Intent intent = getIntent();

        String newsUrl = intent.getStringExtra("url");

        webView.loadUrl(newsUrl);

    }

}
