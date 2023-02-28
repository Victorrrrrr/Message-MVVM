package com.example.mvvm_study.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

/**
 * @ProjectName: MVVM-Study
 * @Author: gxy
 * @Time: 2022/11/16 12:08
 * @Description: 描述
 */
public class BaseViewModel extends ViewModel {

    protected static final String TAG = "ViewModel";

    public LiveData<String> failed;

    public LiveData<String> msg;
}
