package com.example.mvvm_study.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mvvm_study.db.bean.WallPaper;
import com.example.mvvm_study.repository.PictureRepository;

import java.util.List;

/**
 * @ProjectName: MVVM-Study
 * @Author: gxy
 * @Time: 2022/11/3 22:46
 * @Description: 描述
 */
public class PictureViewModel extends ViewModel {

    public MutableLiveData<List<WallPaper>> wallPaper;

    public void getWallPaper() {
        wallPaper = new PictureRepository().getWallPaper();
    }
}
