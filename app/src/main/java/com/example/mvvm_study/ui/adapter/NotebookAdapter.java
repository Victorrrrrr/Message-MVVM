package com.example.mvvm_study.ui.adapter;

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.example.mvvm_study.R;

import com.example.mvvm_study.databinding.ItemNotebookBinding;
import com.example.mvvm_study.db.bean.Notebook;
import com.example.mvvm_study.ui.activity.EditActivity;

import java.util.List;

public class NotebookAdapter extends BaseQuickAdapter<Notebook, BaseDataBindingHolder<ItemNotebookBinding>> {

    public NotebookAdapter(@Nullable List<Notebook> data) {
        super(R.layout.item_notebook, data);
    }

    private boolean isBatchDeletion;

    public void setIsBatchDeletion(boolean isBatchDeletion) {
        this.isBatchDeletion = isBatchDeletion;
    }


    @Override
    protected void convert(@NonNull BaseDataBindingHolder<ItemNotebookBinding> bindingHolder, Notebook notebook) {
        ItemNotebookBinding binding = bindingHolder.getDataBinding();
        if(binding != null) {
            binding.setNotebook(notebook);
            binding.setIsBatchDeletion(isBatchDeletion);
            binding.executePendingBindings();
        }

    }


}
