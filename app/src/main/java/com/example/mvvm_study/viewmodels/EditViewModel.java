package com.example.mvvm_study.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.mvvm_study.db.bean.Notebook;
import com.example.mvvm_study.repository.NotebookRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class EditViewModel extends BaseViewModel {

    private NotebookRepository notebookRepository;

    public LiveData<Notebook> notebookLiveData;

    @Inject
    public EditViewModel(NotebookRepository notebookRepository) {
        this.notebookRepository = notebookRepository;
    }

    /**
     * 添加笔记
     * @param notebook
     */
    public void addNotebook(Notebook notebook){
        failed = notebookRepository.failed;
        notebookRepository.saveNotebook(notebook);
    }

    /**
     * 根据Id搜索笔记
     * @param uid
     */
    public void queryById(int uid) {
        failed = notebookRepository.failed;
        notebookLiveData = notebookRepository.getNotebookById(uid);
    }

    /**
     * 更新笔记
     * @param notebook
     */
    public void updateNotebook(Notebook notebook) {
        failed = notebookRepository.failed;
        notebookRepository.updateNotebook(notebook);
    }

    /**
     * 删除笔记
     * @param notebook
     */
    public void deleteNotebook(Notebook notebook) {
        notebookRepository.deleteNotebook(notebook);
        failed = notebookRepository.failed;
    }

}
