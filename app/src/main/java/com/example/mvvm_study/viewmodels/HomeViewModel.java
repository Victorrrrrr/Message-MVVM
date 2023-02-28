package com.example.mvvm_study.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mvvm_study.db.bean.User;
import com.example.mvvm_study.repository.HomeRepository;
import com.example.mvvm_study.repository.UserRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * @ProjectName: MVVM-Study
 * @Author: gxy
 * @Time: 2022/11/21 10:13
 * @Description: 描述
 */
public class HomeViewModel extends BaseViewModel {

    public LiveData<User> user;
    public String defaultName = "威克VC";
    public String defaultIntroduction = "Android | Kotlin";


    public void getUserInfo() {
        user = UserRepository.getInstance().getUser();
    }

    public void updateUser(User user) {
        UserRepository.getInstance().updateUser(user);
        msg = UserRepository.getInstance().msg;
        getUserInfo();
    }
}
