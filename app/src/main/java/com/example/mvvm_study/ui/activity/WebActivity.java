package com.example.mvvm_study.ui.activity;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.mvvm_study.R;
import com.example.mvvm_study.databinding.ActivityWebBinding;
import com.example.mvvm_study.viewmodels.WebViewModel;
import com.tencent.smtt.export.external.interfaces.HttpAuthHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * @ProjectName: MVVM-Study
 * @Author: gxy
 * @Time: 2022/11/17 11:18
 * @Description: 描述
 */
public class WebActivity extends BaseActivity{
    private final WebViewClient client = new WebViewClient() {
        /**
         * 防止加载页面时调起系统浏览器
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            // 用app内的wevView调用url
            webView.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedHttpAuthRequest(WebView webView,
                                              HttpAuthHandler httpAuthHandler,
                                              String host,
                                              String realm) {
            boolean flag = httpAuthHandler.useHttpAuthUsernamePassword();
        }

        @Override
        public void onPageFinished(WebView webView, String s) {
            super.onPageFinished(webView, s);
        }

        @Override
        public void onReceivedError(WebView webView, int i, String s, String s1) {
            System.out.println("***********onReceivedError ************");
            super.onReceivedError(webView, i, s, s1);
        }

        @Override
        public void onReceivedHttpError(WebView webView, WebResourceRequest webResourceRequest, WebResourceResponse webResourceResponse) {
            System.out.println("***********onReceivedHttpError ************");
            super.onReceivedHttpError(webView, webResourceRequest, webResourceResponse);
        }
    };


    @Override
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityWebBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_web);
        WebViewModel viewModel = new ViewModelProvider(this).get(WebViewModel.class);
        binding.webView.setWebViewClient(client);
        setStatusBar(!isNight());
//        binding.webView.getSettingsExtension().setDayOrNight(!isNight());  // true为白天、 false为夜间
        // 在调用TBS初始化、创建WebView之前进行如下配置
        String uniquekey = getIntent().getStringExtra("uniquekey");
        if(uniquekey != null) {
            viewModel.getNewsDetail(uniquekey);
            viewModel.newsDetail.observe(context, newsDetailResponse ->
                    binding.webView.loadUrl(newsDetailResponse.getResult().getDetail().getUrl()));
            viewModel.failed.observe(context, this::showShortMsg);
        }

    }





}
