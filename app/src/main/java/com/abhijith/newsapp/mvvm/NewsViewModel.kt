package com.abhijith.newsapp.mvvm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.abhijith.newsapp.mvvm.NewsRepository
import com.abhijith.newsapp.room.models.Article
import com.abhijith.newsapp.newsapi.models.Specification

class NewsViewModel(application: Application) : AndroidViewModel(application) {

    private val newsRepository: NewsRepository = NewsRepository.getInstance(application.applicationContext)

    fun getNewsHeadlines(specification: Specification?): LiveData<List<Article>> {
        return newsRepository.getHeadlines(specification!!)
    }

    val allSaved: LiveData<List<Article>> get() = newsRepository.saved

    fun isSaved(articleId: Int): LiveData<Boolean> {
        return newsRepository.isSaved(articleId)
    }

    fun toggleSave(articleId: Int) {
        newsRepository.removeSaved(articleId)
    }

}