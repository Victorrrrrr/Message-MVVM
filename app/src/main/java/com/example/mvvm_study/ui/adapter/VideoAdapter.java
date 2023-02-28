package com.example.mvvm_study.ui.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.example.mvvm_study.R;
import com.example.mvvm_study.databinding.ItemVideosBinding;
import com.example.mvvm_study.model.VideoResponse;

import java.util.List;

/**
 * @ProjectName: MVVM-Study
 * @Author: gxy
 * @Time: 2022/11/16 23:18
 * @Description: 描述
 */
public class VideoAdapter extends BaseQuickAdapter<VideoResponse.ResultBean, BaseDataBindingHolder<ItemVideosBinding>> {
    public VideoAdapter(@Nullable List<VideoResponse.ResultBean> data) {
        super(R.layout.item_videos, data);
    }

    @Override
    protected void convert(@NonNull BaseDataBindingHolder<ItemVideosBinding> itemVideosBindingBaseDataBindingHolder, VideoResponse.ResultBean resultBean) {
        ItemVideosBinding binding = itemVideosBindingBaseDataBindingHolder.getDataBinding();
        if(binding != null) {
            binding.setVideo(resultBean);
            binding.setOnClick(new ClickBinding());
            binding.executePendingBindings();
        }
    }
    public static class ClickBinding {
        public void itemClick(@NonNull VideoResponse.ResultBean resultBean, View view) {
            if(resultBean.getShareUrl() != null) {
                view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(resultBean.getShareUrl())));
            } else {
                Toast.makeText(view.getContext(), "视频地址为空", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
