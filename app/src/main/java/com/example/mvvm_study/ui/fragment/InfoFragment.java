package com.example.mvvm_study.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mvvm_study.R;
import com.example.mvvm_study.databinding.FragmentInfoBinding;
import com.example.mvvm_study.ui.adapter.InfoFragmentAdapter;

import java.util.ArrayList;
import java.util.List;


public class InfoFragment extends BaseFragment {

    public static InfoFragment newInstance() {
        return new InfoFragment();
    }

    private FragmentInfoBinding binding;

    /**
     * 标题数组
     */
    private final String[] titles = {"新闻", "视频"};
    private final List<Fragment> fragmentList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_info, container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentList.add(new NewsFragment());
        fragmentList.add(new VideoFragment());
        binding.vp.setAdapter(new InfoFragmentAdapter(getChildFragmentManager(), fragmentList, titles));
        binding.tab.setupWithViewPager(binding.vp);

    }
}