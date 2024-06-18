package com.kimthean.newsapp.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kimthean.newsapp.ui.news.News;

import java.util.List;

public class NewsViewModel extends ViewModel {
    private final MutableLiveData<List<News>> newsList = new MutableLiveData<>();

    public LiveData<List<News>> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> news) {
        newsList.setValue(news);
    }
}

