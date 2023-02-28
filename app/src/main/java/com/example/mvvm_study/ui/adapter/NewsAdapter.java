package com.example.mvvm_study.ui.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.example.mvvm_study.R;
import com.example.mvvm_study.databinding.ItemNewsBinding;
import com.example.mvvm_study.model.NewsResponse;
import com.example.mvvm_study.ui.activity.WebActivity;

import java.util.List;

/**
 * @ProjectName: MVVM-Study
 * @Author: gxy
 * @Time: 2022/11/16 16:43
 * @Description: 描述
 */
public class NewsAdapter extends BaseQuickAdapter<NewsResponse.ResultBean.DataBean, BaseDataBindingHolder<ItemNewsBinding>> {
    public NewsAdapter(@Nullable List<NewsResponse.ResultBean.DataBean> data) {
        super(R.layout.item_news, data);
    }

    @Override
    protected void convert(@NonNull BaseDataBindingHolder<ItemNewsBinding> itemNewsBindingBaseDataBindingHolder, NewsResponse.ResultBean.DataBean dataBean) {
        ItemNewsBinding binding = itemNewsBindingBaseDataBindingHolder.getDataBinding();
        if(binding != null) {
            binding.setNews(dataBean);
            binding.setOnClick(new ClickBinding());
            binding.executePendingBindings();
        }
    }

    public static class ClickBinding {
        public void itemClick(NewsResponse.ResultBean.DataBean dataBean, View view) {
            if("1".equals(dataBean.getIsContent())) {
                Intent intent = new Intent(view.getContext(), WebActivity.class);
                intent.putExtra("uniquekey", dataBean.getUniquekey());
                view.getContext().startActivity(intent);
            } else {
                Toast.makeText(view.getContext(), "没有详情信息", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
