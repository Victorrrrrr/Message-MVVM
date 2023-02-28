package com.example.mvvm_study.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mvvm_study.db.bean.Image;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

/**
 * @ProjectName: MVVM-Study
 * @Author: gxy
 * @Time: 2022/11/1 15:41
 * @Description: 描述
 */
@Dao
public interface ImageDao {

    @Query("SELECT * FROM image")
    Flowable<List<Image>> getAll();

    // 读取数据库的速率远大于观察者的处理速率
    @Query("SELECT * FROM image WHERE uid LIKE :uid LIMIT 1")
    Flowable<Image> queryById(int uid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(Image ...images);

    @Delete
    Completable delete(Image image);

}
