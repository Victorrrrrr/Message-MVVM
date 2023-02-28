package com.example.mvvm_study.bean;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.mvvm_study.BR;


/**
 * @ProjectName: MVVM-Study
 * @Author: gxy
 * @Time: 2022/10/11 15:16
 * @Description: 描述
 */
public class User extends BaseObservable {

    private String account;
    private String pwd;

    public User(String account, String pwd) {
        this.account = account;
        this.pwd = pwd;
    }

    @Bindable
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
        //notifyChange();  //通知改变 所有参数改变
        notifyPropertyChanged(BR.account);
    }

    @Bindable
    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
        notifyPropertyChanged(BR.pwd);
    }

}
