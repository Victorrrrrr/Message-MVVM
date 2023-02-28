package com.example.mvvm_study.ui.activity;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.mvvm_study.R;
import com.example.mvvm_study.databinding.ActivityRegisterBinding;
import com.example.mvvm_study.db.bean.User;
import com.example.mvvm_study.viewmodels.RegisterViewModel;

public class RegisterActivity extends BaseActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private ActivityRegisterBinding binding;
    private RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        registerViewModel.getUser().setValue(new User(0,"","","",null,null));
        binding.setRegister(registerViewModel);
        initView();
    }

    private void initView() {
        back(binding.toolbar);
        binding.btnRegister.setOnClickListener(v -> {
            if(registerViewModel.user.getValue().getAccount().isEmpty()) {
                binding.etAccount.setError("请输入账号");
                return;
            }
            if(registerViewModel.user.getValue().getPwd().isEmpty()) {
                binding.etPwd.setError("请输入密码");
                return;
            }
            if(registerViewModel.user.getValue().getConfirmPwd().isEmpty()) {
                binding.etConfirmPwd.setError("请确认密码");
                return;
            }
            if(!registerViewModel.user.getValue().getPwd().equals(registerViewModel.user.getValue().getConfirmPwd())) {
                binding.etConfirmPwd.setError("两次密码不一致");
                return;
            }

            registerViewModel.register();
            registerViewModel.msg.observe(this, msg -> {
                showShortMsg("200".equals(msg) ? "注册成功" : msg);
                if("200".equals(msg)) {
                    finish();
                }
            });
        });

    }
}