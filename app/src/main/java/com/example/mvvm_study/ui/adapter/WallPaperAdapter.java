package com.example.mvvm_study.ui.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvm_study.ui.activity.PictureViewActivity;
import com.example.mvvm_study.R;
import com.example.mvvm_study.databinding.ItemWallPaperBinding;
import com.example.mvvm_study.model.WallPaperResponse;

import java.util.List;

/**
 * @ProjectName: MVVM-Study
 * @Author: gxy
 * @Time: 2022/11/3 10:07
 * @Description: 描述
 */
public class WallPaperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * 传递过来的数据
     */
    private final List<WallPaperResponse.ResBean.VerticalBean> verticalBeans;

    public WallPaperAdapter(List<WallPaperResponse.ResBean.VerticalBean> verticalBeans) {
        this.verticalBeans = verticalBeans;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemWallPaperBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_wall_paper, parent, false);
        return new ViewHolderWallPaper(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemWallPaperBinding binding = ((ViewHolderWallPaper) holder).getBinding();
        binding.setWallPaper(verticalBeans.get(position));
        binding.setOnClick(new ClickBinding());
        binding.executePendingBindings();  // 更新将表达式绑定到已修改变量的任何视图
    }

    @Override
    public int getItemCount() {
        return verticalBeans.size();
    }

    private static class ViewHolderWallPaper extends RecyclerView.ViewHolder {

        private ItemWallPaperBinding binding;

        public ItemWallPaperBinding getBinding() {
            return binding;
        }

        public void setBinding(ItemWallPaperBinding binding) {
            this.binding = binding;
        }

        public ViewHolderWallPaper(@NonNull ItemWallPaperBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }


    // 用于DataBinding数据绑定
    public static class ClickBinding {
        /**
         * item点击事件
         * @param verticalBean
         * @param view
         */
        public void itemClick(WallPaperResponse.ResBean.VerticalBean verticalBean, View view) {
            Intent intent = new Intent(view.getContext(), PictureViewActivity.class);
            intent.putExtra("img", verticalBean.getImg());
            view.getContext().startActivity(intent);
        }
    }

}
