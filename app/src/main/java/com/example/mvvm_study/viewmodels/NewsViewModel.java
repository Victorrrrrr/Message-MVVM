package com.example.mvvm_study.viewmodels;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.mvvm_study.model.NewsResponse;
import com.example.mvvm_study.repository.NewsRepository;

@RequiresApi(api = Build.VERSION_CODES.N)
public class NewsViewModel extends BaseViewModel {

    public LiveData<NewsResponse> news;

    public void getNews() {
        NewsRepository newsRepository = new NewsRepository();
        failed = newsRepository.failed;
        news = newsRepository.getNews();
    }

}