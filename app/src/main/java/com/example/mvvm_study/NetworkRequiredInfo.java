package com.example.mvvm_study;

import android.app.Application;

import com.example.mvvm_study.network.INetworkRequiredInfo;

/**
 * @ProjectName: MVVM-Study
 * @Author: gxy
 * @Time: 2022/10/13 14:34
 * @Description: 网络访问信息
 */
public class NetworkRequiredInfo implements INetworkRequiredInfo {

    private final Application application;

    public NetworkRequiredInfo(Application application) {
        this.application = application;
    }

    @Override
    public String getAppVersionName() {
        return BuildConfig.VERSION_NAME;
    }

    @Override
    public String getAppVersionCode() {
        return String.valueOf(BuildConfig.VERSION_CODE);
    }

    @Override
    public boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    @Override
    public Application getApplicationContext() {
        return application;
    }
}
