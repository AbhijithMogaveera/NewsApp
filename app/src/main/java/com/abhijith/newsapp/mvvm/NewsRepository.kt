package com.abhijith.newsapp.mvvm

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.abhijith.newsapp.room.dao.HeadlinesDao
import com.abhijith.newsapp.room.dao.SavedDao
import com.abhijith.newsapp.room.dao.SourcesDao
import com.abhijith.newsapp.room.models.Article
import com.abhijith.newsapp.room.models.SavedArticle
import com.abhijith.newsapp.room.models.Source
import com.abhijith.newsapp.newsapi.models.Specification
import com.abhijith.newsapp.newsapi.network.NewsApiClient
import com.abhijith.newsapp.room.NewsDatabase
import com.abhijith.newsapp.ui.utils.AppExecutors
import timber.log.Timber

class NewsRepository

private constructor(context: Context) {

    //API
    private val newsApiService: NewsApiClient = NewsApiClient.getInstance(context)

    //Database
    private val headlinesDao: HeadlinesDao = NewsDatabase.getInstance(context).headlinesDao()
    private val sourcesDao: SourcesDao = NewsDatabase.getInstance(context).sourcesDao()
    private val savedDao: SavedDao = NewsDatabase.getInstance(context).savedDao()

    //LiveData
    private val networkArticleLiveData: MutableLiveData<List<Article>> = MutableLiveData()
    private val networkSourceLiveData: MutableLiveData<List<Source>> = MutableLiveData()

    //Main thread executors
    private val mExecutor: AppExecutors = AppExecutors.instance!!

    // required private constructor for Singleton pattern
    init {

        networkArticleLiveData.observeForever { articles ->
            if (articles != null) {
                mExecutor.diskIO.execute {
                    headlinesDao.bulkInsert(articles)
                }
            }
        }

        networkSourceLiveData.observeForever { sources ->
            if (sources != null) {
                mExecutor.diskIO.execute {
                    sourcesDao.bulkInsert(sources)
                }
            }
        }

    }

    fun getHeadlines(specs: Specification): LiveData<List<Article>> {
        getAndStoreLatestArticleInRoom(specs)
        return headlinesDao.getArticleByCategory(specs.category!!)
    }

    private fun getAndStoreLatestArticleInRoom(specs: Specification) {
        val newsHeadLines = newsApiService.getHeadlines(specs)
        newsHeadLines.observeForever(object : Observer<List<Article>?> {
            override fun onChanged(articles: List<Article>?) {
                if (articles != null) {
                    networkArticleLiveData.value = articles
                    newsHeadLines.removeObserver(this)
                }
            }
        })
    }

    fun getSources(specs: Specification): LiveData<List<Source>> {
        val networkData = newsApiService.getSources(specs)
        networkData.observeForever(object : Observer<List<Source>?> {
            override fun onChanged(sources: List<Source>?) {
                if (sources != null) {
                    networkSourceLiveData.value = sources
                    networkData.removeObserver(this)
                }
            }
        })
        return sourcesDao.allSources
    }

    val saved: LiveData<List<Article>>
        get() = savedDao.allSaved

    fun isSaved(articleId: Int): LiveData<Boolean> {
        return savedDao.isFavourite(articleId)
    }

    fun removeSaved(articleId: Int) {
        mExecutor.diskIO.execute { savedDao.removeSaved(articleId) }
    }

    fun save(articleId: Int) {
        mExecutor.diskIO.execute {
            val savedArticle = SavedArticle(articleId)
            savedDao.insert(savedArticle)
            Timber.d("Saved in database for id  : %s", articleId)
        }
    }

    companion object {
        private val LOCK = Any()
        private lateinit var sInstance: NewsRepository

        @Synchronized
        fun getInstance(context: Context) =
            if (!this::sInstance.isInitialized) {
                synchronized(LOCK) {
                    if (!this::sInstance.isInitialized) {
                        sInstance = NewsRepository(context)
                        sInstance
                    } else {
                        sInstance
                    }
                }
            } else {
                sInstance
            }

    }


}