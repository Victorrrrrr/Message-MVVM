package com.example.mvvm_study.api;

import com.example.mvvm_study.model.BiYingResponse;
import com.example.mvvm_study.model.NewsDetailResponse;
import com.example.mvvm_study.model.NewsResponse;
import com.example.mvvm_study.model.VideoResponse;
import com.example.mvvm_study.model.WallPaperResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @ProjectName: MVVM-Study
 * @Author: gxy
 * @Time: 2022/10/13 12:18
 * @Description: 所有的Api网络接口
 */
public interface ApiService {

    /**
     * 必应每日一图
     */
    @GET("/HPImageArchive.aspx?format=js&idx=0&n=1")
    Observable<BiYingResponse> biying();

    /**
     * 热门壁纸
     */
    @GET("/v1/vertical/vertical?limit=30&skip=180&adult=false&first=0&order=hot")
    Observable<WallPaperResponse> wallPaper();


    /**
     * 聚合新闻数据
     */
    @GET("/toutiao/index?type=&page=&page_size=&is_filter=&key=99d3951ed32af2930afd9b38293a08a2")
    Observable<NewsResponse> news();

    /**
     * 聚合热门视频数据
     */
    @GET("/fapig/douyin/billboard?type=hot_video&size=20&key=a9c49939cae34fc7dae570b1a4824be4")
    Observable<VideoResponse> video();

    /**
     * 聚合新闻数据详情
     */
    @GET("/toutiao/content?key=99d3951ed32af2930afd9b38293a08a2")
    Observable<NewsDetailResponse> newsDetail(@Query("uniquekey") String uniquekey);


}
