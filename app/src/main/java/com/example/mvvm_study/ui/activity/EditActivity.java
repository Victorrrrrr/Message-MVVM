package com.example.mvvm_study.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.mvvm_study.R;
import com.example.mvvm_study.databinding.ActivityEditBinding;
import com.example.mvvm_study.db.bean.Notebook;
import com.example.mvvm_study.viewmodels.EditViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EditActivity extends BaseActivity implements View.OnClickListener {

    private final static String TAG = EditActivity.class.getSimpleName();
    private ActivityEditBinding binding;

    private InputMethodManager inputMethodManager;

    private EditViewModel viewModel;

    private int uid;

    private Notebook mNotebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit);
        viewModel = new ViewModelProvider(this).get(EditViewModel.class);
        setStatusBar(true);
        back(binding.toolbar);

        initView();
    }

    private void initView() {
        // 监听输入
        listenInput(binding.etTitle);
        listenInput(binding.etContent);
        binding.ivOk.setOnClickListener(this);
        binding.ivDelete.setOnClickListener(this);

        uid = getIntent().getIntExtra("uid", -1);
        if(uid == -1) {
            // 新增笔记时 获取焦点并显示键盘
            showInput();
        } else {
            // 修改
            viewModel.queryById(uid);
            viewModel.notebookLiveData.observe(this, notebook -> {
                mNotebook = notebook;
                binding.setNotebook(mNotebook);
            });
            viewModel.failed.observe(this, s -> Log.e(TAG, "initView: " + s ));
            binding.ivDelete.setVisibility(View.VISIBLE);

        }
    }

    /**
     * 监听输入
     *
     * @param etText
     */
    private void listenInput(final AppCompatEditText etText) {
        etText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0) {
                    binding.ivOk.setVisibility(View.VISIBLE);
                } else {
                    if(binding.etTitle.getText().length() == 0 && binding.etContent.getText().length() == 0) {
                        binding.ivOk.setVisibility(View.GONE);

                    }
                }
            }
        });

    }

    /**
     * 显示键盘
     */
    public void showInput() {
        binding.etContent.requestFocus();
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * 隐藏键盘
     */
    public void dismiss() {
        if(inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(binding.etContent.getWindowToken(),0);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_ok: // 提交并保存笔记
                if(uid == -1){
                    viewModel.addNotebook(new Notebook(binding.etTitle.getText().toString(),
                            binding.etContent.getText().toString()));
                } else {
                    mNotebook.setTitle(binding.etTitle.getText().toString());
                    mNotebook.setContent(binding.etContent.getText().toString());
                    viewModel.updateNotebook(mNotebook);
                }
                finish();
                break;
            case R.id.iv_delete:
                viewModel.deleteNotebook(mNotebook);
                finish();
                break;
            default:
                break;
        }
    }
}