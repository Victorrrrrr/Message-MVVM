package com.example.mvvm_study.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.mvvm_study.db.bean.User;
import com.example.mvvm_study.repository.UserRepository;

/**
 * @ProjectName: MVVM-Study
 * @Author: gxy
 * @Time: 2022/11/18 16:28
 * @Description: 描述
 */
public class RegisterViewModel extends BaseViewModel{

    public MutableLiveData<User> user;

    // 单例
    public MutableLiveData<User> getUser() {
        if(user == null) {
            user = new MutableLiveData<>();
        }
        return user;
    }


    /**
     * 注册
     */
    public void register() {
        UserRepository userRepository = UserRepository.getInstance();
        failed = userRepository.failed;
        msg = userRepository.msg;
        user.getValue().setUid(1);
        Log.d(TAG, "register: " + user.getValue());
        userRepository.saveUser(user.getValue());
    }

}
