package com.example.mvvm_study.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.mvvm_study.BaseApplication;
import com.example.mvvm_study.db.bean.WallPaper;

import java.util.List;

import io.reactivex.Flowable;

/**
 * @ProjectName: MVVM-Study
 * @Author: gxy
 * @Time: 2022/11/3 22:42
 * @Description: 描述
 */
public class PictureRepository {

    private final MutableLiveData<List<WallPaper>> wallPaper = new MutableLiveData<>();

    public MutableLiveData<List<WallPaper>> getWallPaper() {
        Flowable<List<WallPaper>> listFlowable = BaseApplication.getDb().wallPaperDao().queryAll();
        CustomDisposable.addDisposable(listFlowable, wallPaper::postValue);
        return wallPaper;
    }

}
