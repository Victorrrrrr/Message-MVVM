package com.example.mvvm_study.viewmodels;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.mvvm_study.model.BiYingResponse;
import com.example.mvvm_study.model.WallPaperResponse;
import com.example.mvvm_study.repository.MainRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * @ProjectName: MVVM-Study
 * @Author: gxy
 * @Time: 2022/10/10 22:17
 * @Description:  ViewModel获取数据
 */
@HiltViewModel
public class MainViewModel extends ViewModel {

    public LiveData<BiYingResponse> biying;
    public LiveData<WallPaperResponse> wallPaper;

    private final MainRepository mainRepository;

    @Inject
    MainViewModel(MainRepository mainRepository) {
        this.mainRepository = mainRepository;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getBiying() {
        biying = mainRepository.getBiYing();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getWallPaper() {
        wallPaper = mainRepository.requestNetworkApiForWallPaper();
    }


}
