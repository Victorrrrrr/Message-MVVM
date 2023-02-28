package com.example.mvvm_study.viewmodels;

import android.annotation.TargetApi;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

import com.example.mvvm_study.model.NewsDetailResponse;
import com.example.mvvm_study.repository.WebRepository;

/**
 * @ProjectName: MVVM-Study
 * @Author: gxy
 * @Time: 2022/11/17 22:58
 * @Description: 描述
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class WebViewModel extends BaseViewModel{

    public LiveData<NewsDetailResponse> newsDetail;

    public void getNewsDetail(String uniquekey) {
        WebRepository webRepository = new WebRepository();
        failed = webRepository.failed;
        newsDetail = webRepository.getNewsDetail(uniquekey);
    }

}
