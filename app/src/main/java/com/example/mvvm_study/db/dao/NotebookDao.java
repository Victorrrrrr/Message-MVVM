package com.example.mvvm_study.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mvvm_study.db.bean.Notebook;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface NotebookDao {

    @Query("SELECT * FROM notebook")
    Flowable<List<Notebook>> getAll();

    @Update
    Completable update(Notebook notebook);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(Notebook notebook);

    @Delete
    Completable delete(Notebook... notebook);

    @Query("SELECT * FROM notebook WHERE uid = :uid")
    Flowable<Notebook> findById(int uid);

    // ||相当于+号  连接符
    @Query("SELECT * FROM notebook WHERE title LIKE '%' || :input || '%' OR content LIKE '%' || :input || '%' ")
    Flowable<List<Notebook>> searchNotebook(String input);

}
