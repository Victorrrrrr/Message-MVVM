package com.example.mvvm_study;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.room.Room;

import com.example.mvvm_study.db.AppDatabase;
import com.example.mvvm_study.network.NetworkApi;
import com.example.mvvm_study.ui.activity.ActivityManager;
import com.example.mvvm_study.uitls.Constant;
import com.example.mvvm_study.uitls.MVUitls;
import com.tencent.mmkv.MMKV;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;

import java.util.HashMap;

import dagger.hilt.android.HiltAndroidApp;

/**
 * @ProjectName: MVVM-Study
 * @Author: gxy
 * @Time: 2022/10/13 14:36
 * @Description: 描述
 */
@HiltAndroidApp
public class BaseApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    public static Context context;

    // 数据库
    public static AppDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        // 初始化 NetworkApi
        NetworkApi.init(new NetworkRequiredInfo(this));
        // 初始化MMKV
        MMKV.initialize(this);  // 存储路径 /data/user/0/com.example.mvvm_study/files/mmkv
        // 初始化MVUtils工具类
        MVUitls.getInstance();
        // 创建本地数据库
        db = AppDatabase.getInstance(this);
        // 腾讯WebView初始化
        initX5WebView();
        // 判断之前是否手动设置深色模式
        initNightMode();
    }

    private void initNightMode() {
        boolean isFollowSystem = MVUitls.getBoolean(Constant.IS_FOLLOW_SYSTEM);
        boolean isNightMode = MVUitls.getBoolean(Constant.IS_NIGHT_MODE);
        if(isFollowSystem) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        } else {
            if (isNightMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }
    }

    public static Context getContext() {
        return context;
    }

    public static AppDatabase getDb() {
        return db;
    }

    private void initX5WebView() {
        HashMap map = new HashMap(2);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
        QbSdk.initTbsSettings(map);
        // 搜集本地tbs内核信息并上报给服务器
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
            }

            @Override
            public void onViewInitFinished(boolean b) {
                // x5核初始化完成的回调，为true表示xs内核加载成功，否则表示x5内核加载失败
                // 加载失败会自动切换到系统内核。
                Log.d("application", "onViewInitFinished is " + b);
            }
        };
        // x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }




    public static ActivityManager getActivityManager() {
        return ActivityManager.getInstance();
    }

}
