package com.example.mvvm_study.ui.fragment;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mvvm_study.R;
import com.example.mvvm_study.databinding.FragmentVideoBinding;
import com.example.mvvm_study.model.VideoResponse;
import com.example.mvvm_study.ui.adapter.VideoAdapter;
import com.example.mvvm_study.viewmodels.VideoViewModel;

public class VideoFragment extends BaseFragment {

    private FragmentVideoBinding binding;

    private VideoViewModel mViewModel;

    public static VideoFragment newInstance() {
        return new VideoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_video, container, false);
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VideoViewModel.class);
        // 用VM获得视频信息数据
        mViewModel.getVideos();

        // 设置适配器
        binding.rv.setLayoutManager(new LinearLayoutManager(context));
        mViewModel.videos.observe(context, videoResponse -> {
            binding.rv.setAdapter(new VideoAdapter(videoResponse.getResult()));
            mViewModel.failed.observe(context, this::showMsg);
        });
    }

}