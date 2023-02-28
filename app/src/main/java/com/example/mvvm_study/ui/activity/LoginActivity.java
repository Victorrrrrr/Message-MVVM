package com.example.mvvm_study.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.example.mvvm_study.BaseApplication;
import com.example.mvvm_study.R;
import com.example.mvvm_study.bean.User;

import com.example.mvvm_study.databinding.ActivityLoginBinding;
import com.example.mvvm_study.uitls.Constant;
import com.example.mvvm_study.uitls.MVUitls;
import com.example.mvvm_study.viewmodels.LoginViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding dataBinding;
    private LoginViewModel loginViewModel;
    private TextInputEditText etAccount, etPwd;
    private long timeMills;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 数据视图绑定
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        loginViewModel = new LoginViewModel();

        // Model -> View
        User user = new User("", "");
        loginViewModel.getUser().setValue(user);
        // 获取观察对象
        MutableLiveData<User> user1 = loginViewModel.getUser();
        user1.observe(this, user2 -> {
            Log.d("LoginActivity", "onCreate: " + user2.getAccount());
            dataBinding.setViewModels(loginViewModel);
        });
        dataBinding.btnLogin.setOnClickListener(v -> {
//            user.setAccount(dataBinding.etAccount.getText().toString().trim());
//            user.setAccount(dataBinding.etAccount.getText().toString().trim());
            if (loginViewModel.user.getValue().getAccount().isEmpty()) {
                // Toast.makeText(this, "请输入账号", Toast.LENGTH_SHORT).show();
                dataBinding.etAccount.setError("账号不能为空");
                return;
            }
            if (loginViewModel.user.getValue().getPwd().isEmpty()) {
                // Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                dataBinding.etPwd.setError("密码不能为空");
                return;
            }
            checkUser();
        });
    }

    private void checkUser() {
        loginViewModel.getLocalUser();

        loginViewModel.localUser.observe(this, localUser -> {
            if(!loginViewModel.user.getValue().getAccount().equals(localUser.getAccount())||
                    !loginViewModel.user.getValue().getPwd().equals(localUser.getPwd())){
                showShortMsg("账号或密码错误");
                return;
            }
            // 记录已经执行过登录操作
            MVUitls.put(Constant.IS_LOGIN, true);
            showShortMsg("登陆成功");
            jumpActivityFinish(MainActivity.class);
        });
        loginViewModel.msg.observe(this, this::showShortMsg);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if((System.currentTimeMillis() - timeMills) > 2000) {
                showShortMsg("再次按下退出应用程序");
                timeMills = System.currentTimeMillis();
            } else {
                exitTheProgram();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exitTheProgram() {
        BaseApplication.getActivityManager().finishAllActivity();
    }


    public void toRegister(View view) {
        jumpActivity(RegisterActivity.class);
    }
}