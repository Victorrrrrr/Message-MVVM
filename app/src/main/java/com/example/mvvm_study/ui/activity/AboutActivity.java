package com.example.mvvm_study.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.mvvm_study.R;
import com.example.mvvm_study.databinding.ActivityAboutBinding;
import com.google.android.material.appbar.MaterialToolbar;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAboutBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_about);
        back(binding.toolbar);
        binding.llBlog.setOnClickListener(v -> jumpUrl("https://blog.csdn.net/Victorrrrt?type=blog"));
        binding.llCodeAddress.setOnClickListener(v -> jumpUrl(""));
        binding.llEmail.setOnClickListener(v -> {
            copyEmail();
            showShortMsg("邮箱已复制到剪贴板");
        });
    }

    /**
     * 跳转URL
     *
     * @param url
     */
    private void jumpUrl(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    /**
     * 复制邮箱到剪贴板
     */
    private void copyEmail() {
        ClipboardManager myClipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData myClip = ClipData.newPlainText("text", "yyguanxiongyao@163.com");
        myClipboard.setPrimaryClip(myClip);
        showShortMsg("邮箱已复制");
    }


}