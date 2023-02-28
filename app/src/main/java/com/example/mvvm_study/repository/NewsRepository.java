package com.example.mvvm_study.repository;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.example.mvvm_study.BaseApplication;
import com.example.mvvm_study.api.ApiService;
import com.example.mvvm_study.db.bean.News;
import com.example.mvvm_study.model.NewsResponse;
import com.example.mvvm_study.network.BaseObserver;
import com.example.mvvm_study.network.NetworkApi;
import com.example.mvvm_study.network.utils.DateUtil;
import com.example.mvvm_study.uitls.Constant;
import com.example.mvvm_study.uitls.MVUitls;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;


/**
 * @ProjectName: MVVM-Study
 * @Author: gxy
 * @Time: 2022/11/16 11:32
 * @Description: 描述
 */
@SuppressLint("CheckResult")
@RequiresApi(api = Build.VERSION_CODES.N)
public class NewsRepository {

    private static final String TAG = NewsRepository.class.getSimpleName();
    final MutableLiveData<NewsResponse> news = new MutableLiveData<>();

    public final MutableLiveData<String> failed = new MutableLiveData<>();


    /**
     * 获取新闻数据
     * @return news
     */
    public MutableLiveData<NewsResponse> getNews() {
        // 今日此接口是否已经请求
        if(MVUitls.getBoolean(Constant.IS_TODAY_REQUEST_NEWS)) {
            if(DateUtil.getTimestamp() <= MVUitls.getLong(Constant.REQUEST_TIMESTAMP_NEWS)) {
                getNewsFromLocalDB();
            } else {
                getNewsFromNetwork();
            }
        } else {
            getNewsFromNetwork();
        }
        return news;

    }

    /**
     * 从网络中获取新闻数据
     */
    private void getNewsFromNetwork() {
        Log.d(TAG, "getNewsFromNetwork: 从网络中获取 新闻数据");
        NetworkApi.createService(ApiService.class, Constant.NEWS_LIST)
                .news()
                .compose(NetworkApi.applySchedulers(new BaseObserver<NewsResponse>() {
                    @Override
                    public void onSuccess(NewsResponse newsResponse) {
                        if(newsResponse.getErrorCode() == 0) {
                            saveNews(newsResponse);
                            news.setValue(newsResponse);
                        } else {
                            failed.postValue(newsResponse.getReason());
                        }
                    }

                    @Override
                    public void onFailed(Throwable e) {
                        failed.postValue("News Error: " + e.toString());
                    }
                }));

    }


    /**
     * 从本地数据库中获取新闻数据
     */
    private void getNewsFromLocalDB() {
        Log.d(TAG, "getNewsFromLocalDB: 从本地数据库中获取数据 新闻数据");
        // 存结果的数组
        NewsResponse newsResponse = new NewsResponse();
        NewsResponse.ResultBean resultBean = new NewsResponse.ResultBean();

        List<NewsResponse.ResultBean.DataBean> dataBeanList = new ArrayList<>();
        Flowable<List<News>> listFlowable = BaseApplication.getDb().newsDao().getAll();
        CustomDisposable.addDisposable(listFlowable, newsList -> {
            //遍历 写入结果
            for(News news1 : newsList) {
                NewsResponse.ResultBean.DataBean dataBean = new NewsResponse.ResultBean.DataBean();
                dataBean.setUniquekey(news1.getUniquekey());
                dataBean.setDate(news1.getDate());
                dataBean.setCategory(news1.getCategory());
                dataBean.setAuthorName(news1.getAuthor_name());
                dataBean.setIsContent(news1.getIs_content());
                dataBean.setTitle(news1.getTitle());
                dataBean.setThumbnailPicS(news1.getThumbnail_pic_s());
                dataBean.setUrl(news1.getUrl());
                dataBeanList.add(dataBean);
            }
            resultBean.setData(dataBeanList);
            newsResponse.setResult(resultBean);
            news.postValue(newsResponse);

        });
    }


    /**
     * 保存新闻数据
     * @param newsResponse 网络请求的新闻数据
     */
    private void saveNews(NewsResponse newsResponse) {
        MVUitls.put(Constant.IS_TODAY_REQUEST_NEWS, true);
        MVUitls.put(Constant.REQUEST_TIMESTAMP_NEWS, DateUtil.getMillisNextEarlyMorning());

        // 删除数据库已有的数据
        Completable deleteAll = BaseApplication.getDb().newsDao().deleteAll();
        CustomDisposable.addDisposable(deleteAll, () -> {
            Log.d(TAG, "saveNews: 删除数据成功");
            List<News> newsList = new ArrayList<>();
            for(NewsResponse.ResultBean.DataBean dataBean : newsResponse.getResult().getData()) {
                newsList.add(new News(dataBean.getUniquekey(), dataBean.getTitle(),
                        dataBean.getDate(), dataBean.getCategory(),dataBean.getAuthorName(),
                        dataBean.getUrl(), dataBean.getThumbnailPicS(), dataBean.getIsContent()));
            }
            // 保存到数据库
            Completable insertAll = BaseApplication.getDb().newsDao().insertAll(newsList);
            Log.d(TAG, "saveNews: 插入数据" + newsList.size() + "条");
            // Rxjava处理Room数存储
            CustomDisposable.addDisposable(insertAll, () -> {
                Log.d(TAG, "saveNews: 新数据保存成功");
            });
        });


    }
}
