package com.example.mvvm_study.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.mvvm_study.BaseApplication;
import com.example.mvvm_study.db.bean.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @ProjectName: MVVM-Study
 * @Author: gxy
 * @Time: 2022/11/18 16:15
 * @Description: 描述
 */
public class UserRepository {

    private static final String TAG = UserRepository.class.getSimpleName();
    private final MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();

    public final MutableLiveData<String> failed = new MutableLiveData<>();
    public final MutableLiveData<String> msg = new MutableLiveData<>();
    private static volatile UserRepository mInstance;


    private UserRepository(){}

    public static UserRepository getInstance(){
        if(mInstance == null) {
            synchronized (UserRepository.class) {
                if(mInstance == null) {
                    mInstance = new UserRepository();
                }
            }
        }
        return mInstance;
    }


    public MutableLiveData<User> getUser() {
        Flowable<List<User>> listFlowable = BaseApplication.getDb().userDao().getAll();
        CustomDisposable.addDisposable(listFlowable, users -> {
            if(users.size() > 0) {
                for(User user : users) {
                    if (user.getUid() == 1) {
                        userMutableLiveData.postValue(user);
                        break;
                    }
                }
            } else {
                msg.postValue("你还没注册过吧，去注册吧！");
            }
        });
        return userMutableLiveData;
    }

    /**
     * 更新用户信息
     * @param user
     */
    public void updateUser(User user) {
        Completable update = BaseApplication.getDb().userDao().update(user);
        CustomDisposable.addDisposable(update, () -> msg.postValue("200"));
    }

    /**
     * 保存用户信息
     */
    public void saveUser(User user) {
        Completable deleteAll = BaseApplication.getDb().userDao().deleteAll();
        CustomDisposable.addDisposable(deleteAll, () -> {
            // 保存到数据库
            Completable insertAll = BaseApplication.getDb().userDao().insert(user);
            CustomDisposable.addDisposable(insertAll, () -> msg.postValue("200"));
        });
    }
}
