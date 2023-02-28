package com.example.mvvm_study.repository;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.example.mvvm_study.api.ApiService;
import com.example.mvvm_study.model.NewsDetailResponse;
import com.example.mvvm_study.network.BaseObserver;
import com.example.mvvm_study.network.NetworkApi;
import com.example.mvvm_study.uitls.Constant;

/**
 * @ProjectName: MVVM-Study
 * @Author: gxy
 * @Time: 2022/11/17 22:43
 * @Description: 描述
 */
@SuppressLint("CheckResult")
@RequiresApi(api = Build.VERSION_CODES.N)
public class WebRepository {

    final MutableLiveData<NewsDetailResponse> newsDetail = new MutableLiveData<>();

    public final MutableLiveData<String> failed = new MutableLiveData<>();

    /**
     * 获取新闻详情数据
     * 不做缓存，直接走网络请求
     * @param uniquekey 新闻的ID
     * @return newsDetail
     */
    public MutableLiveData<NewsDetailResponse> getNewsDetail(String uniquekey) {
        NetworkApi.createService(ApiService.class, Constant.NEWS_LIST)
                .newsDetail(uniquekey).compose(NetworkApi.applySchedulers(new BaseObserver<NewsDetailResponse>() {
                    @Override
                    public void onSuccess(NewsDetailResponse newsDetailResponse) {
                        // 请求成功，
                        if(newsDetailResponse.getErrorCode() == 0) {
                            newsDetail.setValue(newsDetailResponse);
                        } else {
                            // 用failed传递错误信息，方便后面显示UI，因为需要提醒用户错误信息，所以用postValue
                            failed.postValue(newsDetailResponse.getReason());
                        }
                    }

                    @Override
                    public void onFailed(Throwable e) {
                        failed.postValue("NewsDetail Error: " + e.toString());
                    }
                }));
        return newsDetail;
    }


}
