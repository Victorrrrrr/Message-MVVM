package com.example.mvvm_study.repository;


import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.example.mvvm_study.BaseApplication;
import com.example.mvvm_study.api.ApiService;
import com.example.mvvm_study.db.bean.Video;
import com.example.mvvm_study.model.VideoResponse;
import com.example.mvvm_study.network.BaseObserver;
import com.example.mvvm_study.network.NetworkApi;
import com.example.mvvm_study.network.utils.DateUtil;
import com.example.mvvm_study.uitls.Constant;
import com.example.mvvm_study.uitls.MVUitls;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @ProjectName: MVVM-Study
 * @Author: gxy
 * @Time: 2022/11/16 11:33
 * @Description: 描述
 */
@SuppressLint("CheckResult")
@RequiresApi(api = Build.VERSION_CODES.N)
public class VideoRepository {

    private static final String TAG = VideoRepository.class.getSimpleName();

    final MutableLiveData<VideoResponse> videos = new MutableLiveData<>();

    public final MutableLiveData<String> failed = new MutableLiveData<>();

    /**
     * 获取视频数据
     * @return video
     */
    public MutableLiveData<VideoResponse> getVideos() {
        if(MVUitls.getBoolean(Constant.IS_TODAY_REQUEST_VIDEO, false)) {
            if(DateUtil.getTimestamp() <= MVUitls.getLong(Constant.REQUEST_TIMESTAMP_VIDEO)) {
                // 数据库拿数据
                getDataFromDB();
            } else {
                // 网络请求拿数据
                getDataFromNetwork();
            }
        } else {
            // 网络请求数据
            getDataFromNetwork();
        }
        return videos;
    }

    /**
     * 从网络中获取视频数据
     */
    private void getDataFromNetwork() {
        Log.d(TAG, "getDataFromNetwork: 从网络中获取 视频数据");
        NetworkApi.createService(ApiService.class, Constant.VIDEO_LIST)
                .video()
                .compose(NetworkApi.applySchedulers(new BaseObserver<VideoResponse>() {
                    @Override
                    public void onSuccess(VideoResponse videoResponse) {
                        if(videoResponse.getErrorCode() == 0) {
                            videos.setValue(videoResponse);
                            saveVideo(videoResponse);
                        } else {
                            failed.postValue(videoResponse.getReason());
                        }
                    }

                    @Override
                    public void onFailed(Throwable e) {
                        failed.postValue(e.toString());
                    }
                }));
    }

    private void saveVideo(VideoResponse videoResponse) {
        MVUitls.put(Constant.REQUEST_TIMESTAMP_VIDEO, DateUtil.getMillisNextEarlyMorning());
        MVUitls.put(Constant.IS_TODAY_REQUEST_VIDEO, true);

        Completable deleteAll = BaseApplication.getDb().videoDao().deleteAll();
        CustomDisposable.addDisposable(deleteAll, new Action() {
            @Override
            public void run() throws Exception {
                Log.d(TAG, "run: 数据库删除成功");
                List<Video> videoList = new ArrayList<>();
                // 添加新数据
                for(VideoResponse.ResultBean resultBean : videoResponse.getResult()) {
                    videoList.add(new Video(resultBean.getTitle(), resultBean.getShareUrl(),
                            resultBean.getAuthor(), resultBean.getItemCover(), resultBean.getHotValue(),
                            resultBean.getHotWords(),resultBean.getPlayCount(), resultBean.getDiggCount(),
                            resultBean.getCommentCount()));
                }
                Completable insertAll = BaseApplication.getDb().videoDao().insertAll(videoList);
                Log.d(TAG, "run: 插入数据" + videoList.size() + "条");
                CustomDisposable.addDisposable(insertAll, () -> {
                    Log.d(TAG, "run: 数据插入成功");
                });
            }
        });
    }

    /**
     * 数据库获取视频数据
     */
    private void getDataFromDB() {
        Log.d(TAG, "getDataFromDB: 从数据库中获取数据");
        // 存结果的数组
        VideoResponse videoResponse = new VideoResponse();
        List<VideoResponse.ResultBean> resultBeans = new ArrayList<>();

        Flowable<List<Video>> flowable = BaseApplication.getDb().videoDao().getAll();
        CustomDisposable.addDisposable(flowable, videoss -> {
            for (Video video : videoss) {
                VideoResponse.ResultBean resultBean = new VideoResponse.ResultBean();
                resultBean.setAuthor(video.getAuthor());
                resultBean.setTitle(video.getTitle());
                resultBean.setCommentCount(video.getComment_count());
                resultBean.setDiggCount(video.getDigg_count());
                resultBean.setHotValue(video.getHot_value());
                resultBean.setItemCover(video.getItem_cover());
                resultBean.setHotWords(video.getHot_words());
                resultBean.setPlayCount(video.getPlay_count());
                resultBean.setShareUrl(video.getShare_url());
                resultBeans.add(resultBean);
            }
            videoResponse.setResult(resultBeans);
            videos.postValue(videoResponse);
        });
    }

}
