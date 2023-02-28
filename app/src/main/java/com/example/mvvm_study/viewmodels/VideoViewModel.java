package com.example.mvvm_study.viewmodels;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mvvm_study.db.bean.Video;
import com.example.mvvm_study.model.VideoResponse;
import com.example.mvvm_study.repository.VideoRepository;

@RequiresApi(api = Build.VERSION_CODES.N)
public class VideoViewModel extends BaseViewModel {

    public MutableLiveData<VideoResponse> videos;

    public void getVideos() {
        VideoRepository videoRepository = new VideoRepository();
        failed = videoRepository.failed;
        videos =  videoRepository.getVideos();
    }

}