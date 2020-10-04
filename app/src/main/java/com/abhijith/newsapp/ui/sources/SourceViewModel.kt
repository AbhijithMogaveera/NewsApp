package com.abhijith.newsapp.ui.sources

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.abhijith.newsapp.data.NewsRepository
import com.abhijith.newsapp.models.Source
import com.abhijith.newsapp.models.Specification

class SourceViewModel(application: Application) : AndroidViewModel(application) {

    private val newsRepository: NewsRepository = NewsRepository.getInstance(application.applicationContext)

    fun getSource(specification: Specification): LiveData<List<Source>> {
        return newsRepository.getSources(specification)
    }

}