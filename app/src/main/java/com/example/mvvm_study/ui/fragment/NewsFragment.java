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
import com.example.mvvm_study.databinding.FragmentNewsBinding;
import com.example.mvvm_study.model.NewsResponse;
import com.example.mvvm_study.ui.adapter.NewsAdapter;
import com.example.mvvm_study.viewmodels.NewsViewModel;

public class NewsFragment extends BaseFragment {

    private FragmentNewsBinding binding;

    private NewsViewModel mViewModel;

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_news, container, false);
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        // 获取新闻数据
        mViewModel.getNews();
        binding.rv.setLayoutManager(new LinearLayoutManager(context));
        // 数据刷新
        mViewModel.news.observe(context, newsResponse -> {
            binding.rv.setAdapter(new NewsAdapter(newsResponse.getResult().getData()));
            mViewModel.failed.observe(context, this::showMsg);
        });
    }

}