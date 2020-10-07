package com.abhijith.newsapp.mvvm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.abhijith.newsapp.mvvm.NewsRepository
import com.abhijith.newsapp.room.models.Source
import com.abhijith.newsapp.newsapi.models.Specification

class SourceViewModel(application: Application) : AndroidViewModel(application) {

    private val newsRepository: NewsRepository = NewsRepository.getInstance(application.applicationContext)

    fun getSource(specification: Specification): LiveData<List<Source>> {
        return newsRepository.getSources(specification)
    }

}