package com.example.mvvm_study.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.mvvm_study.R;
import com.example.mvvm_study.databinding.ActivityNotebookBinding;
import com.example.mvvm_study.db.bean.Notebook;
import com.example.mvvm_study.ui.adapter.NotebookAdapter;
import com.example.mvvm_study.uitls.Constant;
import com.example.mvvm_study.uitls.MVUitls;

import com.example.mvvm_study.viewmodels.NotebookViewModel;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NotebookActivity extends BaseActivity implements View.OnClickListener{

    private ActivityNotebookBinding binding;

    private MenuItem itemViewType;

    private static final String TAG = NotebookActivity.class.getSimpleName();
    private NotebookViewModel viewModel;
    private boolean hasNotebook;
    // 是否全选
    private boolean isAllSelected;
    // 是否批量删除
    private boolean isBatchDeletion = false;
    // 笔记列表
    private final List<Notebook> mList = new ArrayList<>();
    // 适配器
    private NotebookAdapter notebookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notebook);
        viewModel = new ViewModelProvider(this).get(NotebookViewModel.class);

        initView();
    }

    private void initView() {
        setStatusBar(true);
        binding.toolbar.setTitle("");
        setSupportActionBar(binding.toolbar);
        back(binding.toolbar);
        // 设置监听事件
        binding.tvAllSelected.setOnClickListener(this);
        binding.tvDelete.setOnClickListener(this);
        binding.ivClear.setOnClickListener(this);
        // 初始化列表
        initList();
        // 输入框监听
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0) {
                    binding.setIsSearch(true);
                    // 搜索笔记
                    viewModel.searchNotebook(s.toString());
                } else {
                    // 获取全部笔记
                    binding.setIsSearch(false);
                    viewModel.getNotebooks();
                }
            }
        });
    }

    /**
     * 初始化列表
     */
    private void initList() {
        // 适配器
        notebookAdapter = new NotebookAdapter(mList);
        // 设置适配器
        binding.rvNotebook.setAdapter(notebookAdapter);
        binding.rvNotebook.setLayoutManager(MVUitls.getInt(Constant.NOTEBOOK_VIEW_TYPE) == 1 ?
                new GridLayoutManager(context, 2) : new LinearLayoutManager(context));
        // item点击事件
        notebookAdapter.setOnItemClickListener((adapter, view, position) -> {
            if(isBatchDeletion) {
                mList.get(position).setSelect(!mList.get(position).isSelect());
                Log.e(TAG, "initList: " + mList.get(position).isSelect());
                notebookAdapter.notifyDataSetChanged();
                // 修改页面标题
                changeTitle();
            } else{
                Intent intent = new Intent(NotebookActivity.this, EditActivity.class);
                intent.putExtra("uid",mList.get(position).getUid());
                startActivity(intent);
            }
        });
    }

    private void changeTitle() {
        int selectedNum = 0;
        for (Notebook notebook : mList) {
            if(notebook.isSelect()) {
                selectedNum++;
            }
        }
        Log.e(TAG, "changeTitle: " + selectedNum);
        binding.tvTitle.setText("已选择" + selectedNum + "项");
        binding.setIsAllSelected(selectedNum == mList.size());
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.getNotebooks();
        viewModel.notebooks.observe(this, notebooks -> {
            if(notebooks.size() > 0) {
                mList.clear();
                // 添加数据
                mList.addAll(notebooks);
                notebookAdapter.notifyDataSetChanged();
                hasNotebook = true;
            } else {
                hasNotebook = false;
            }
            binding.setHasNotebook(hasNotebook);
            // 是否显示搜索布局
            binding.setShowSearchLay(hasNotebook || !binding.etSearch.getText().toString().isEmpty());
        });
        viewModel.failed.observe(this, s -> Log.d(TAG, "onChanged: " + s));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notebook_setting,  menu);
        itemViewType = menu.findItem(R.id.item_view_type).setTitle(MVUitls.getInt(Constant.NOTEBOOK_VIEW_TYPE) == 1 ? "列表视图" : "宫格视图");
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // 0是列表视图，1是宫格视图
        int viewType = MVUitls.getInt(Constant.NOTEBOOK_VIEW_TYPE);
        if(item.getItemId() == R.id.item_view_type) {
            if(viewType == 0) {
                viewType = 1;
                itemViewType.setTitle("列表视图");
                binding.rvNotebook.setLayoutManager(new GridLayoutManager(context, 2));
            } else {
                viewType = 0;
                itemViewType.setTitle("宫格视图");
                binding.rvNotebook.setLayoutManager(new LinearLayoutManager(context));
            }
            MVUitls.put(Constant.NOTEBOOK_VIEW_TYPE, viewType);
        } else if(item.getItemId() == R.id.item_batch_deletion) { // 批量删除
            setBatchDeletionMode();
        }
        return true;
    }

    /**
     * 设置批量删除模式
     */
    private void setBatchDeletionMode() {
        // 取消之前的全选选项
        isAllSelected = false;
        binding.setIsAllSelected(isAllSelected);
        // 进入批量删除模式
        isBatchDeletion = !isBatchDeletion;
        // 设值当前页面
        binding.setIsBatchDeletion(isBatchDeletion);
        if(!isBatchDeletion) {
            for (Notebook notebook : mList) {
                notebook.setSelect(false);
            }
        }
        // 设置适配器
        notebookAdapter.setIsBatchDeletion(isBatchDeletion);
        notebookAdapter.notifyDataSetChanged();
    }


    public void toEdit(View view){
        jumpActivity(EditActivity.class);
    }

    @Override
    public void onBackPressed() {
        if(isBatchDeletion) {
            setBatchDeletionMode();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_delete:
                showConfirmDelete();
                break;
            case R.id.tv_all_selected:
                allSelected();
                break;
            case R.id.iv_clear:
                binding.etSearch.setText("");
                binding.setIsSearch(false);
            default:
                break;
        }
    }

    /**
     * 全选/取消全选
     */
    private void allSelected() {
        isAllSelected = !isAllSelected;
        // 设置适配器

        for (Notebook notebook : mList) {
            notebook.setSelect(isAllSelected);
        }
        changeTitle();
        binding.setIsAllSelected(isAllSelected);
        notebookAdapter.notifyDataSetChanged();
    }

    /**
     * 显示确认删除弹窗
     */
    private void showConfirmDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage("确定要删除所选笔记吗？")
                .setPositiveButton("确定", (dialog, which) -> {
                    dialog.dismiss();
                    List<Notebook> notebookList = new ArrayList<>();
                    // 删除所选中的笔记
                    for (Notebook notebook : mList) {
                        if(notebook.isSelect()) {
                            notebookList.add(notebook);
                        }
                    }
                    Notebook[] notebooks = notebookList.toArray(new Notebook[0]);
                    viewModel.deleteNotebook(notebooks);
                    // 设置批量删除模式
                    setBatchDeletionMode();
                    // 请求数据
                    viewModel.getNotebooks();
                }).setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
}