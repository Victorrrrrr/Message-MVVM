package com.example.mvvm_study.ui.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class InfoFragmentAdapter extends FragmentPagerAdapter {

    String titleArr[];
    List<Fragment> mFragmentList;

    public InfoFragmentAdapter(@NonNull FragmentManager fm, List<Fragment> list, String[] titleArr) {
        super(fm);
        mFragmentList = list;
        this.titleArr = titleArr;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList != null ? mFragmentList.size() : 0;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleArr[position];
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        super.destroyItem(container, position, object);
    }
}
