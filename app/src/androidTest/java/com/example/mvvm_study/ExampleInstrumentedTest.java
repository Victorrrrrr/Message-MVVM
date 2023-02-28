package com.example.mvvm_study;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.example.mvvm_study.network.BaseObserver;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
//        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        assertEquals("com.example.mvvm_study", appContext.getPackageName());
//        BaseObserver<String> baseObserver = new BaseObserver<String>(){
//            @Override
//            public void onSuccess(String s) {
//
//            }
//
//            @Override
//            public void onFailed(Throwable e) {
//
//            }
//        };
//        System.out.println(baseObserver.getClass().toString());
//        Log.d("TAG", "useAppContext:  " + baseObserver.getClass().toString());
        Observable
                .create(new ObservableOnSubscribe<Integer>() {

                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        int i = 0;
                        while(true) {
                            i++;
                            emitter.onNext(i);
                        }
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Thread.sleep(5000);
                        Log.d("TAG", "accept: " + integer);
                        System.out.println(integer);
                    }
                });

    }
}