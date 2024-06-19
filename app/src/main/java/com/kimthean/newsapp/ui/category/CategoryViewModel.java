package com.kimthean.newsapp.ui.category;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kimthean.newsapp.ui.news.News;

import java.util.List;

public class CategoryViewModel extends ViewModel {
    private final MutableLiveData<List<News>> newsList = new MutableLiveData<>();
    private final MutableLiveData<String> category = new MutableLiveData<>();

    public LiveData<List<News>> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> news) {
        newsList.setValue(news);
    }

    public LiveData<String> getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category.setValue(category);
    }
}