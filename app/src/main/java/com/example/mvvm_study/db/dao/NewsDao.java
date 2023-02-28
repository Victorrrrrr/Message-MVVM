package com.example.mvvm_study.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mvvm_study.db.bean.News;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

/**
 * @ProjectName: MVVM-Study
 * @Author: gxy
 * @Time: 2022/11/15 23:50
 * @Description: 描述
 */
@Dao
public interface NewsDao {

    @Query("SELECT * FROM news")
    Flowable<List<News>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(List<News> news);

    @Query(("DELETE FROM news"))
    Completable deleteAll();

}
