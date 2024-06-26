package com.kimthean.newsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

public class NewsDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail);

        WebView webView = findViewById(R.id.wvNewsDetail);
        Toolbar toolbar = findViewById(R.id.backHome);

        Intent intent = getIntent();

        String newsUrl = intent.getStringExtra("url");

        if (newsUrl != null) {
            webView.loadUrl(newsUrl);
        } else {
            Toast.makeText(this, "No URL found", Toast.LENGTH_SHORT).show();
            finish();
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                   finish();
                }
            }
        });

    }

}
