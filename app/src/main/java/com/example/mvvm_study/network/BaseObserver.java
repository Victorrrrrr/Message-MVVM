package com.example.mvvm_study.network;


import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @ProjectName: MVVM-Study
 * @Author: gxy
 * @Time: 2022/10/11 22:26
 * @Description: 自定义Observer
 */
public abstract class BaseObserver<T> implements Observer<T> {

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        onFailed(e);
    }

    @Override
    public void onComplete() {

    }

    // 请求成功
    public abstract void onSuccess(T t);

    // 请求失败
    public abstract void onFailed(Throwable e);


}
