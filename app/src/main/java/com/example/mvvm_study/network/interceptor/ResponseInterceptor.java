package com.example.mvvm_study.network.interceptor;

import com.example.mvvm_study.network.utils.KLog;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * @ProjectName: MVVM-Study
 * @Author: gxy
 * @Time: 2022/10/12 19:56
 * @Description: 描述
 */
public class ResponseInterceptor implements Interceptor {

    public static final String TAG = "ResponseInterceptor";

    /**
     * 拦截
     * @param chain
     * @return
     * @throws IOException
     */
    @Override
    public Response intercept(Chain chain) throws IOException {
        long requestTime = System.currentTimeMillis();
        Response response = chain.proceed(chain.request());
        KLog.i(TAG, "requestSpendTime=" + (System.currentTimeMillis() - requestTime) + "ms");
        return response;
    }
}
