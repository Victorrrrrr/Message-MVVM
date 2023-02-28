package com.example.mvvm_study.ui.adapter;

import android.view.View;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.example.mvvm_study.R;

import com.example.mvvm_study.databinding.ItemImageBinding;
import com.example.mvvm_study.db.bean.WallPaper;

import java.util.List;

/**
 * @ProjectName: MVVM-Study
 * @Author: gxy
 * @Time: 2022/11/3 22:25
 * @Description: 描述
 */
public class ImageAdapter extends BaseQuickAdapter<WallPaper, BaseDataBindingHolder<ItemImageBinding>>  {

    // private BottomDialogBinding dialogBinding;
    public ImageAdapter(@NonNull List<WallPaper> data) {
        super(R.layout.item_image, data);
    }
    private ImageCallBack imageCallBack;

    @Override
    protected void convert(BaseDataBindingHolder<ItemImageBinding> itemImageBindingHolder, WallPaper wallPaper) {
        if (wallPaper == null) {
            return;
        }
        ItemImageBinding binding = itemImageBindingHolder.getDataBinding();
        if(binding != null) {
            binding.setWallPaper(wallPaper);
            binding.executePendingBindings();

            binding.image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // Toast.makeText(v.getContext(), "你长按了此图片 " + wallPaper.getUid(), Toast.LENGTH_SHORT).show();
                    imageCallBack.onImageCallBack(wallPaper.getImg());
                    return true;
                }
            });
        }
    }

    /**
     * 图片uid回调接口
     * @param imageCallBack
     */
    public void setImageCallBack(ImageCallBack imageCallBack) {
        this.imageCallBack = imageCallBack;
    }

}
