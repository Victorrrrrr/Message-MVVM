package com.example.mvvm_study.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mvvm_study.bean.User;
import com.example.mvvm_study.repository.UserRepository;

/**
 * @ProjectName: MVVM-Study
 * @Author: gxy
 * @Time: 2022/10/13 14:11
 * @Description: 描述
 */
public class LoginViewModel extends BaseViewModel {

    public MutableLiveData<User> user;

    public MutableLiveData<User> getUser() {
        if(user == null) {
            user = new MutableLiveData<>();
        }
        return user;
    }

    public LiveData<com.example.mvvm_study.db.bean.User> localUser;

    public void getLocalUser() {
        UserRepository userRepository = UserRepository.getInstance();
        localUser = userRepository.getUser();
        msg = userRepository.msg;
    }
}
