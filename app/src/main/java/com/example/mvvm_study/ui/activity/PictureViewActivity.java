package com.example.mvvm_study.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;

import com.example.mvvm_study.R;
import com.example.mvvm_study.ui.adapter.ImageAdapter;
import com.example.mvvm_study.ui.adapter.ImageCallBack;
import com.example.mvvm_study.databinding.ActivityPictureViewBinding;

import com.example.mvvm_study.uitls.DownloadUtils;
import com.example.mvvm_study.viewmodels.PictureViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class PictureViewActivity extends BaseActivity implements ImageCallBack {

    private PictureViewModel viewModel;
    private ActivityPictureViewBinding binding;
    private BottomSheetDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_picture_view);
        viewModel = new ViewModelProvider(this).get(PictureViewModel.class);

        // 获取Intent传入的图片
        String img = getIntent().getStringExtra("img");

        // 获取热门壁纸的数据
        viewModel.getWallPaper();
        viewModel.wallPaper.observe(this, wallPapers -> {
            ImageAdapter imageAdapter = new ImageAdapter(wallPapers);
            imageAdapter.setImageCallBack(this);
            binding.vp.setAdapter(imageAdapter);

            // 找到对应壁纸的位置
            for (int i = 0; i < wallPapers.size(); i++) {
                if(img == null) {
                    return;
                }
                if(wallPapers.get(i).getImg().equals(img)) {  // 遍历判断是否是Intent传入的img
                    binding.vp.setCurrentItem(i, false);  // 这个false是指不显示滚动的动画
                    break;
                }
            }
        });

    }

    @Override
    public void onImageCallBack(String url) {
        dialog = new BottomSheetDialog(this, R.style.BottomSheetDialog);
        View bottomView = getLayoutInflater().inflate(R.layout.bottom_dialog, null);
        dialog.setContentView(bottomView);

        dialog.findViewById(R.id.tv_download).setOnClickListener(v -> {
            // Toast.makeText(this, " " + url, Toast.LENGTH_SHORT).show();
            DownloadUtils.saveImgToLocal(this, url);
            dialog.dismiss();
        });
        dialog.findViewById(R.id.tv_cancel).setOnClickListener(v -> dialog.cancel());
        // 显示底部选择框
        dialog.show();
    }

}