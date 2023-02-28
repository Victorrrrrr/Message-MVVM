package com.example.mvvm_study.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.mvvm_study.R;
import com.example.mvvm_study.databinding.ActivitySplashBinding;
import com.example.mvvm_study.uitls.Constant;
import com.example.mvvm_study.uitls.EasyAnimation;
import com.example.mvvm_study.uitls.MVUitls;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySplashBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        setStatusBar(true);
//        EasyAnimation.moveViewWidth(binding.tvTranslate, new EasyAnimation.TranslateCallback() {
//            @Override
//            public void animationEnd() {
//                binding.tvMvvm.setVisibility(View.VISIBLE);
//                jumpActivityFinish(MVUitls.getBoolean(Constant.IS_LOGIN) ? MainActivity.class : LoginActivity.class);
//            }
//        });
        new Handler().postDelayed(() ->
            jumpActivityFinish(MVUitls.getBoolean(Constant.IS_LOGIN) ?
                    MainActivity.class : LoginActivity.class),1000);

    }
}