package com.example.mvvm_study.repository;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mvvm_study.BaseApplication;
import com.example.mvvm_study.api.ApiService;
import com.example.mvvm_study.db.bean.WallPaper;
import com.example.mvvm_study.db.bean.Image;
import com.example.mvvm_study.model.BiYingResponse;
import com.example.mvvm_study.model.WallPaperResponse;
import com.example.mvvm_study.network.BaseObserver;
import com.example.mvvm_study.network.NetworkApi;
import com.example.mvvm_study.network.utils.DateUtil;
import com.example.mvvm_study.network.utils.KLog;
import com.example.mvvm_study.uitls.Constant;
import com.example.mvvm_study.uitls.MVUitls;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * @ProjectName: MVVM-Study
 * @Author: gxy
 * @Time: 2022/10/13 12:20
 * @Description:  Main存储库 用于对数据进行处理
 * 本地数据库、本地缓存、网络请求数据
 */
@SuppressLint("CheckResult")
public class MainRepository {
    private static final String TAG = MainRepository.class.getSimpleName();

    /**
     * 必应图片请求数据
     */
    final MutableLiveData<BiYingResponse> biyingImage = new MutableLiveData<>();

    /**
     * 热门地址请求数据
     */
    final MutableLiveData<WallPaperResponse> wallPaper = new MutableLiveData<>();

    @Inject
    public MainRepository() {}

    /**
     * 获取必应图片数据
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public MutableLiveData<BiYingResponse> getBiYing() {
        // 今日此接口是否已请求
        if(MVUitls.getBoolean(Constant.IS_TODAY_REQUEST)) {
            if(DateUtil.getTimestamp() <= MVUitls.getLong(Constant.REQUEST_TIMESTAMP)) {
                // 当前时间未超过次日0点，从本地获取
                getLocalDB();
            } else {
                // 大于则数据需要更新，从网络获取
                requestNetworkApi();
            }
        } else {
            // 没有请求过接 或 当前实践， 从网络获取
            requestNetworkApi();
        }
        return biyingImage;
    }

    /**
     * 保存必应图片数据到数据库
     */
    private void saveImage(BiYingResponse biYingResponse) {
        // 记录今日已请求
        MVUitls.put(Constant.IS_TODAY_REQUEST, true);
        // 记录此次请求的最晚有效时间戳
        MVUitls.put(Constant.REQUEST_TIMESTAMP, DateUtil.getMillisNextEarlyMorning());

        BiYingResponse.ImagesBean bean = biYingResponse.getImages().get(0);

        // 保存到数据库
        Completable insert = BaseApplication.getDb().imageDao().insertAll(
                new Image(1,bean.getUrl(), bean.getUrlbase(), bean.getCopyright(),
                bean.getCopyrightlink(), bean.getTitle()));
        // rxjava2处理Room数据存储
        CustomDisposable.addDisposable(insert, ()-> Log.d(TAG, "saveImage: 数据插入成功"));

    }

    /**
     * 从网络中请求必应图片数据
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("CheckResult")
    private void requestNetworkApi() {
        Log.d(TAG, "requestNetworkApi: 从网络中获取");
        ApiService apiService = NetworkApi.createService(ApiService.class, Constant.BING_PICTURE);
        apiService.biying().compose(NetworkApi.applySchedulers(new BaseObserver<BiYingResponse>() {
            @Override
            public void onSuccess(BiYingResponse biYingResponse) {
                saveImage(biYingResponse);
                biyingImage.setValue(biYingResponse);
            }

            @Override
            public void onFailed(Throwable e) {
                KLog.e("Biying Error", e.toString());
            }
        }));

    }

    /**
     * 从本地数据库获取必应数据
     */
    private void getLocalDB() {
        Log.d(TAG, "getLocalDB: 从本地数据库获取必应数据");
        BiYingResponse biYingResponse = new BiYingResponse();
        // 从数据库获取
        Flowable<Image> imageFlowable = BaseApplication.getDb().imageDao().queryById(1);
        CustomDisposable.addDisposable(imageFlowable, image -> {
            BiYingResponse.ImagesBean imagesBean = new BiYingResponse.ImagesBean();
            imagesBean.setUrl(image.getUrl());
            imagesBean.setUrlbase(image.getUrlbase());
            imagesBean.setCopyright(image.getCopyright());
            imagesBean.setCopyrightlink(image.getCopyrightlink());
            imagesBean.setTitle(image.getTitle());
            List<BiYingResponse.ImagesBean> imagesBeanList = new ArrayList<>();
            imagesBeanList.add(imagesBean);
            biYingResponse.setImages(imagesBeanList);
            biyingImage.postValue(biYingResponse);
        });
    }


    /**
     * 从网络获取壁纸
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public LiveData<WallPaperResponse> requestNetworkApiForWallPaper() {
        NetworkApi.createService(ApiService.class, Constant.ADESK_LIST).wallPaper()
                .compose(NetworkApi.applySchedulers(new BaseObserver<WallPaperResponse>() {
                    @Override
                    public void onSuccess(WallPaperResponse wallPaperResponse) {
                        KLog.e("WallPaper: " + new Gson().toJson(wallPaperResponse));
                        wallPaper.setValue(wallPaperResponse);
                        // 保存到本地
                        saveWallPaper(wallPaperResponse);
                    }

                    @Override
                    public void onFailed(Throwable e) {
                        KLog.e("WallPaper Error: " + e.toString());
                    }
                }));
        return wallPaper;
    }

    /**
     * 保存热门壁纸数据
     */
    private void saveWallPaper(WallPaperResponse wallPaperResponse) {
        // 记录今日已请求
        MVUitls.put(Constant.IS_TODAY_REQUEST_WALLPAPER, true);
        // 记录此次请求的最晚有效时间戳
        MVUitls.put(Constant.REQUEST_TIMESTAMP_WALLPAPER, DateUtil.getMillisNextEarlyMorning());

        // 删除数据库原有壁纸数据
        Completable deleteAll = BaseApplication.getDb().wallPaperDao().deleteAll();
        CustomDisposable.addDisposable(deleteAll, () -> {
            // 创建新的列表
            List<WallPaper> wallPapers = new ArrayList<>();
            for (WallPaperResponse.ResBean.VerticalBean verticalBean : wallPaperResponse.getRes().getVertical()) {
                wallPapers.add(new WallPaper(verticalBean.getImg()));
            }
            // 添加到数据库
            Completable insertAll = BaseApplication.getDb().wallPaperDao().insertAll(wallPapers);
            Log.d(TAG, "saveWallPaper: 插入数据：" + wallPapers.size() + "条");
            // Rxjava2处理Room数据
            CustomDisposable.addDisposable(insertAll, ()-> Log.d(TAG, "saveWallPaper: 壁纸数据插入成功"));
        });
    }

    /**
     * 从本地数据库获取热门壁纸
     */
    private void getLocalDBForWallPaper() {
        Log.d(TAG, "getLocalDBForWallPaper: 从本地数据库获取热门壁纸数据");
        WallPaperResponse wallPaperResponse = new WallPaperResponse();
        List<WallPaperResponse.ResBean.VerticalBean> verticalBeans = new ArrayList<>();
        WallPaperResponse.ResBean resBean = new WallPaperResponse.ResBean();
        // 从数据获取数据
        Flowable<List<WallPaper>> wallPaperFlowable = BaseApplication.getDb().wallPaperDao().queryAll();
        CustomDisposable.addDisposable(wallPaperFlowable, new Consumer<List<WallPaper>>() {
            @Override
            public void accept(List<WallPaper> wallPapers) throws Exception {
                WallPaperResponse.ResBean.VerticalBean verticalBean = new WallPaperResponse.ResBean.VerticalBean();
                for(WallPaper paper : wallPapers) {
                    verticalBean.setImg(paper.getImg());
                    verticalBeans.add(verticalBean);
                }
                resBean.setVertical(verticalBeans);
                wallPaperResponse.setRes(resBean);
                wallPaper.postValue(wallPaperResponse);
            }
        });
    }

    /**
     * 获取壁纸数据
     *
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public MutableLiveData<WallPaperResponse> getWallPaper() {
        // 今日此接口是否已经请求
        if(MVUitls.getBoolean(Constant.IS_TODAY_REQUEST_WALLPAPER)) {
            if(DateUtil.getTimestamp() <= MVUitls.getLong(Constant.REQUEST_TIMESTAMP_WALLPAPER)){
                getLocalDBForWallPaper();
            } else {
                requestNetworkApiForWallPaper();
            }
        } else {
            requestNetworkApiForWallPaper();
        }
        return wallPaper;
    }

}
