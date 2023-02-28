package com.example.mvvm_study.viewmodels;

import androidx.lifecycle.LiveData;

import com.example.mvvm_study.db.bean.Notebook;
import com.example.mvvm_study.repository.NotebookRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class NotebookViewModel extends BaseViewModel{

    private NotebookRepository notebookRepository;

    public LiveData<List<Notebook>> notebooks;

    @Inject
    public NotebookViewModel(NotebookRepository notebookRepository) {
        this.notebookRepository = notebookRepository;
    }

    /**
     * 获取笔记列表
     */
    public void getNotebooks(){
        failed = notebookRepository.failed;
        notebooks = notebookRepository.getAllNotebook();
    }

    /**
     * 删除笔记
     */
    public void deleteNotebook(Notebook... notebooks) {
        notebookRepository.deleteNotebook(notebooks);
        failed = notebookRepository.failed;
    }

    /**
     * 搜索笔记
     */
    public void searchNotebook(String input) {
        notebooks = notebookRepository.searchNotebook(input);
        failed = notebookRepository.failed;
    }



}
