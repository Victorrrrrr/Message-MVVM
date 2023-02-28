package com.example.mvvm_study.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.mvvm_study.BaseApplication;
import com.example.mvvm_study.db.bean.Notebook;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class NotebookRepository {

    private static final String TAG = NotebookRepository.class.getSimpleName();

    @Inject
    NotebookRepository(){}

    private final MutableLiveData<Notebook> notebookLiveData = new MutableLiveData<>();

    private final MutableLiveData<List<Notebook>> notebookMutableLivedata = new MutableLiveData<>();

    public final MutableLiveData<String> failed = new MutableLiveData<>();

    public final List<Notebook> emptyList = new ArrayList<>();

    /**
     * 添加笔记
     */
    public void saveNotebook(Notebook notebook) {
        Completable insert = BaseApplication.getDb().notebookDao().insert(notebook);
        CustomDisposable.addDisposable(insert, () -> Log.d(TAG, "saveNotebook: 保存笔记成功" ));
    }


    /**
     * 获取所有笔记
     */
    public MutableLiveData<List<Notebook>> getAllNotebook(){
        Flowable<List<Notebook>> listFlowable = BaseApplication.getDb().notebookDao().getAll();
        CustomDisposable.addDisposable(listFlowable, notebooks -> {
            if(notebooks.size() > 0) {
                notebookMutableLivedata.postValue(notebooks);
            } else {
                notebookMutableLivedata.postValue(emptyList);
                failed.postValue("暂无数据");
            }
        });
        return notebookMutableLivedata;
    }


    /**
     * 根据id获取笔记
     */
    public MutableLiveData<Notebook> getNotebookById(int uid) {
        Flowable<Notebook> flowable = BaseApplication.getDb().notebookDao().findById(uid);
        CustomDisposable.addDisposable(flowable, notebook -> {
            if(notebook!=null) {
                notebookLiveData.postValue(notebook);
            } else {
                failed.postValue("未查询到笔记");
            }
        });
        return notebookLiveData;
    }

    /**
     * 更新笔记
     */
    public void updateNotebook(Notebook notebook) {
        Completable update = BaseApplication.getDb().notebookDao().update(notebook);
        CustomDisposable.addDisposable(update, () -> {
            Log.d(TAG, "updateNotebook: " + "更新成功");
            failed.postValue("200");
        });
    }

    /**
     * 删除笔记
     */
    public void deleteNotebook(Notebook... notebook) {
        Completable delete = BaseApplication.getDb().notebookDao().delete(notebook);
        CustomDisposable.addDisposable(delete, () -> {
            Log.d(TAG, "deleteNotebook: " + "删除成功");
            failed.postValue("200");
        });
    }

    /**
     * 搜索笔记
     */
    public MutableLiveData<List<Notebook>> searchNotebook(String input) {
        Flowable<List<Notebook>> listFlowable = BaseApplication.getDb().notebookDao().searchNotebook(input);
        CustomDisposable.addDisposable(listFlowable, notebooks -> {
            if(notebooks.size() > 0) {
                notebookMutableLivedata.postValue(notebooks);
            } else {
                notebookMutableLivedata.postValue(emptyList);
                failed.postValue("暂无数据");
            }
        });
        return notebookMutableLivedata;
    }


}
