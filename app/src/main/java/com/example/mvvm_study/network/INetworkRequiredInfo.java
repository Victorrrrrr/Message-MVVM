package com.example.mvvm_study.network;

import android.app.Application;

/**
 * @ProjectName: MVVM-Study
 * @Author: gxy
 * @Time: 2022/10/11 21:08
 * @Description: 网络请求中打印日志和一些请求时间
 */
public interface INetworkRequiredInfo {
    /**
     * 获取App版本名
     */
    String getAppVersionName();

    /**
     * 获取App版本号
     */
    String getAppVersionCode();

    /**
     * 判断是否为Debug模式
     */
    boolean isDebug();

    /**
     * 获取全局上下文参数
     */
    Application getApplicationContext();

}
