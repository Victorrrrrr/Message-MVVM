package com.example.mvvm_study.repository;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @ProjectName: MVVM-Study
 * @Author: gxy
 * @Time: 2022/11/1 18:17
 * @Description: disposable是订阅事件，可以用来取消订阅 CompositeDisposable是一个容器
 *              用CompositeDisposable来管理订阅事件disposable，
 *              然后在acivity销毁的时候，调用compositeDisposable.dispose()就可以切断所有订阅事件，
 *              防止内存泄漏
 */
public class CustomDisposable {

    private static final CompositeDisposable compositeDisposable = new CompositeDisposable();



    /**
     * Flowable
     * @param flowable
     * @param consumer
     * @param <T>
     *  CompositeDisposable.add() 将一个Disposable添加到容器中进行管理
     *  CompositeDisposable.clear() 可以解除订阅
     */
    public static <T> void addDisposable(Flowable<T> flowable, Consumer<T> consumer) {
        compositeDisposable.add(
                // 观察者在主线程中更新UI， 被观察者在输入输出线程中读取数据
                flowable.subscribeOn(Schedulers.io())  // 设置被观察者在io线程中进行
                .observeOn(AndroidSchedulers.mainThread())  // 设置观察者在主线程中进行
                .subscribe(consumer));
    }

    /**
     * Completable
     * @param completable
     * @param action
     * @param <T>
     *  CompositeDisposable.add() 将一个Disposable添加到容器中进行管理
     *  CompositeDisposable.clear() 可以解除订阅
     */
    public static <T> void addDisposable(Completable completable, Action action) {
        compositeDisposable.add(
                completable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action));
    }


}
