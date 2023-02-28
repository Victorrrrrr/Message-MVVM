package com.example.mvvm_study.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mvvm_study.db.bean.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;


/**
 * @ProjectName: MVVM-Study
 * @Author: gxy
 * @Time: 2022/11/18 16:04
 * @Description: 描述
 */
@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    Flowable<List<User>> getAll();

    @Update
    Completable update(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(User user);

    @Query("DELETE FROM user")
    Completable deleteAll();

}
