package com.example.mvvm_study.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import com.example.mvvm_study.R;
import com.example.mvvm_study.databinding.ActivitySettingBinding;
import com.example.mvvm_study.generated.callback.OnClickListener;
import com.example.mvvm_study.uitls.Constant;
import com.example.mvvm_study.uitls.MVUitls;

/**
 * @ProjectName: MVVM-Study
 * @Author: gxy
 * @Time: 2023/2/27 14:36
 * @Description: 模仿微信的深色模式设置
 */
public class SettingActivity extends BaseActivity {

    private ActivitySettingBinding binding;

    private static final String TAG = SettingActivity.class.getSimpleName();

    private String systemMode = Constant.IS_FOLLOW_SYSTEM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_setting);
        back(binding.toolbar);

        initView();
    }

    /**
     * 初始化UI样式
     */
    private void initView() {
        boolean isFollowSystem = MVUitls.getBoolean(Constant.IS_FOLLOW_SYSTEM);
        boolean isNightMode = MVUitls.getBoolean(Constant.IS_NIGHT_MODE);
        if(isFollowSystem) {
            binding.setIsFollowSystem(true);
        } else {
            if (isNightMode) {
                binding.setIsNightMode(true);
            } else {
                binding.setIsNightMode(false);
            }
        }

        // switch控件监听
        binding.switchMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                // 跟随系统，切换选项消失
                binding.layModeChoose.setVisibility(View.GONE);
                systemMode = Constant.IS_FOLLOW_SYSTEM;
            } else {
                // 不跟随系统，显示切换选项
                binding.layModeChoose.setVisibility(View.VISIBLE);
            }
        });
        Log.d(TAG, "onCheckedChanged: " + systemMode);

        binding.llNightMode.setOnClickListener(v -> {
            binding.setIsNightMode(true);
            systemMode = Constant.IS_NIGHT_MODE;
        });

        binding.llNormalMode.setOnClickListener(v -> {
            binding.setIsNightMode(false);
            systemMode = Constant.IS_DAY_MODE;
        });

        // 确认按钮，MVKV缓存选择
        binding.mbConfirm.setOnClickListener(v -> {
            if(Constant.IS_FOLLOW_SYSTEM.equals(systemMode)) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                jumpActivityFinishAll(MainActivity.class);
                MVUitls.put(Constant.IS_FOLLOW_SYSTEM, true);
            }
            if (Constant.IS_NIGHT_MODE.equals(systemMode)) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                MVUitls.put(Constant.IS_FOLLOW_SYSTEM, false);
                MVUitls.put(Constant.IS_NIGHT_MODE, true);
            }
            if(Constant.IS_DAY_MODE.equals(systemMode)) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                MVUitls.put(Constant.IS_FOLLOW_SYSTEM, false);
                MVUitls.put(Constant.IS_NIGHT_MODE, false);
            }
            recreate();
        });
    }
}