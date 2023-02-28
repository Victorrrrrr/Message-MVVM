package com.example.mvvm_study.network;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.mvvm_study.network.erroehandler.HttpErrorHandler;
import com.example.mvvm_study.network.interceptor.RequestInterceptor;
import com.example.mvvm_study.network.interceptor.ResponseInterceptor;
import com.example.mvvm_study.uitls.Constant;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @ProjectName: MVVM-Study
 * @Author: gxy
 * @Time: 2022/10/12 19:59
 * @Description: 网络api
 */
public class NetworkApi {

    /**
     * 获取App运行状态及版本信息，用于日志打印
     */
    private static INetworkRequiredInfo iNetworkRequiredInfo;
    /**
     * API访问地址
     */
    private static String BASE_URL = null;

    private static OkHttpClient okHttpClient;

    private static final HashMap<String, Retrofit> retrofitHashMap = new HashMap<>();

    /**
     * 初始化
     */
    public static void init(INetworkRequiredInfo networkRequiredInfo) {
        iNetworkRequiredInfo = networkRequiredInfo;
    }

    /**
     * 设置访问Url类型
     * @param type 0 必应 1 壁纸列表
     */
    private static void setUrlType(int type){
        switch (type) {
            case Constant.BING_PICTURE:
                //必应
                BASE_URL = "https://cn.bing.com";
                break;
            case Constant.ADESK_LIST:
                //热门壁纸
                BASE_URL = "http://service.picasso.adesk.com";
                break;
            case Constant.NEWS_LIST:
                BASE_URL = "http://v.juhe.cn";
                break;
            case Constant.VIDEO_LIST:
                BASE_URL = "http://apis.juhe.cn";
                break;
            default:
                break;
        }
    }


    /**
     * 创建serviceClass的实例
     * @param serviceClass api保存的接口实例化
     * @param <T>
     * @return
     */
    public static <T> T createService(Class<T> serviceClass, int type) {
        // 设置URL类型
        setUrlType(type);
        return getRetrofit(serviceClass).create(serviceClass);
    }

    private static Retrofit getRetrofit(Class serviceClass) {
        if(retrofitHashMap.get(BASE_URL + serviceClass.getName()) != null) {
            // 刚才定义的Map键中是String，值是Retrofit，当键不为空时，必然有值，有值就直接返回
            return retrofitHashMap.get(BASE_URL + serviceClass.getName());
        }
        // 初始化Retrofit Retrofit是对OkHttp的封装，通常是对网络请求做处理，也可以处理返回的数据
        // Retrofit构建器
        Retrofit.Builder builder = new Retrofit.Builder();
        // 设置访问地址
        builder.baseUrl(BASE_URL);
        // 设置OkHttp客户端，传入上面写好的方法即可获得配置后的OkHttp客户端
        builder.client(getOkHttpClient());
        // 设置数据解析器 会自动把请求返回的结果（json字符串）通过Gson转化工厂自动转化成与其结构相符的实体Bean
        builder.addConverterFactory(GsonConverterFactory.create());
        // 设置请求回调，使用RxJava对网络返回进行处理
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        // retrofit配置完成
        Retrofit retrofit = builder.build();
        // 放入Map中
        retrofitHashMap.put(BASE_URL + serviceClass.getName(), retrofit);
        // 最后返回
        return retrofit;
    }

    /**
     * 配置Okhttp
     *
     * @return
     */
    private static OkHttpClient getOkHttpClient() {
        // 不为空，则则说明配置过了，直接返回即可
        if(okHttpClient == null) {
            // okHttp构建器
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            // 设置缓存大小,100Mb
            int cacheSize = 100 * 1024 * 1024;
            // 设置网络请求超时时长，这里设置为6
            builder.callTimeout(6, TimeUnit.SECONDS);
            // 添加请求拦截器，如果接口有请求头的话，可以放在这个拦截器里面
            builder.addInterceptor(new RequestInterceptor(iNetworkRequiredInfo));
            // 添加返回拦截器。可用于查看接口的请求耗时，对网络优化有帮助
            builder.addInterceptor(new ResponseInterceptor());
            // 当程序在debug过程中则打印数据日志，方便调试用
            if (iNetworkRequiredInfo != null && iNetworkRequiredInfo.isDebug()) {
                // iNetworkRequiredInfo不为空且处于debug状态下初始化日志拦截器
                HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
                // 设置要打印日志的内容等级，Body为主要内容，还有BASIC，HEADERS，NONE
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                // 将拦截器添加到OkHtto构建器中
                builder.addInterceptor(httpLoggingInterceptor);
            }
            // 配置完成
            okHttpClient = builder.build();
        }
        return okHttpClient;
    }

    /**
     * 配置RxJava 完成线程的切换
     * @param observer 这个observer要注意不要使用lifecycle中的Observer
     * @param <T>   泛型
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static <T> ObservableTransformer<T, T> applySchedulers(final Observer<T> observer) {
        return upstream -> {
            Observable<T> observable = upstream
                    .subscribeOn(Schedulers.io())  //  线程订阅
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(NetworkApi.getErrorHandler()) // 判断500的错误
                    .onErrorResumeNext(new HttpErrorHandler<>());  // 判断400错误
            observable.subscribe(observer);  // 订阅观察者
            return observable;
        };
    }

    private static <T> Function<T, T> getErrorHandler() {
        return response -> {
            // 当response返回出现500之类的错误时
            if (response instanceof BaseResponse && ((BaseResponse) response).responseCode >= 500) {
                //通过这个异常处理，得到用户可以知道的原因
                ExceptionHandle.ServerException exception = new ExceptionHandle.ServerException();
                exception.code = ((BaseResponse) response).responseCode;
                exception.message = ((BaseResponse) response).responseError != null ? ((BaseResponse) response).responseError : "";
                throw exception;
            }
            return response;
        };
    }


}
