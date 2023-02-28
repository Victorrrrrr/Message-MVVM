package com.example.mvvm_study.view;

import android.content.Context;

import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.mvvm_study.BaseApplication;
import com.example.mvvm_study.R;
import com.example.mvvm_study.network.utils.KLog;
import com.google.android.material.imageview.ShapeableImageView;

/**
 * @ProjectName: MVVM-Study
 * @Author: gxy
 * @Time: 2022/10/13 15:15
 * @Description: 描述
 */
public class CustomImageView extends ShapeableImageView {

    private static final RequestOptions OPTIONS = new RequestOptions()
            .placeholder(R.drawable.wallpaper_bg) //图片加载出来前，显示的图片
            .fallback(R.drawable.wallpaper_bg) //url为空的时候,显示的图片
            .error(R.mipmap.ic_loading_failed);

    private static final RequestOptions OPTIONS_LOCAL = new RequestOptions()
            .placeholder(R.drawable.header)   //图片加载出来前，显示的图片
            .fallback(R.drawable.header)  //url为空的时候,显示的图片
            .error(R.mipmap.ic_loading_failed)  //图片加载失败后，显示的图片
            .diskCacheStrategy(DiskCacheStrategy.NONE)  //不做磁盘缓存
            .skipMemoryCache(true);

    public CustomImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 必应壁纸  因为拿不到完整的URL，因此需要做一次地址拼接
     * @param imageView 图片视图
     * @param url 网络URL
     */
    @BindingAdapter(value = {"biyingUrl"}, requireAll = false)
    public static void setBiYingUrl(ImageView imageView, String url) {
        String assembleUrl = "http://cn.bing.com" + url;
        KLog.d(assembleUrl);
        Glide.with(BaseApplication.getContext()).load(assembleUrl).apply(OPTIONS).into(imageView);
    }

    /**
     * 普通网络地址图片
     * @param imageView 图片视图
     * @param url 网络url
     */
    @BindingAdapter(value = {"networkUrl"}, requireAll = false)
    public static void setNetworkUrl(ImageView imageView, String url) {
        Glide.with(BaseApplication.getContext()).load(url).apply(OPTIONS).into(imageView);
    }

    @BindingAdapter(value = {"localUrl"}, requireAll = false)
    public static void setLocalUrl(ImageView imageView, String url){
        Glide.with(BaseApplication.getContext())
                .load(url)
                .apply(OPTIONS_LOCAL)
                .into(imageView);
    }


}
