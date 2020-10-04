package com.abhijith.newsapp.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.abhijith.newsapp.data.dao.HeadlinesDao
import com.abhijith.newsapp.data.dao.SavedDao
import com.abhijith.newsapp.data.dao.SourcesDao
import com.abhijith.newsapp.models.Article
import com.abhijith.newsapp.models.SavedArticle
import com.abhijith.newsapp.models.Source
import com.abhijith.newsapp.models.Specification
import com.abhijith.newsapp.network.NewsApiClient
import com.abhijith.newsapp.utils.AppExecutors
import timber.log.Timber

class NewsRepository private constructor(context: Context) {
    private val newsApiService: NewsApiClient? = NewsApiClient.getInstance(context)
    private val headlinesDao: HeadlinesDao
    private val sourcesDao: SourcesDao
    private val savedDao: SavedDao
    private val mExecutor: AppExecutors
    private val networkArticleLiveData: MutableLiveData<List<Article>>
    private val networkSourceLiveData: MutableLiveData<List<Source>>
    fun getHeadlines(specs: Specification): LiveData<List<Article>> {
        val networkData = newsApiService!!.getHeadlines(specs)
        networkData.observeForever(object : Observer<List<Article>?> {
            override fun onChanged(articles: List<Article>?) {
                if (articles != null) {
                    networkArticleLiveData.value = articles
                    networkData.removeObserver(this)
                }
            }
        })
        return headlinesDao.getArticleByCategory(specs.category!!)
    }

    fun getSources(specs: Specification?): LiveData<List<Source>> {
        val networkData = newsApiService!!.getSources(
            specs!!
        )
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
        fun getInstance(context: Context): NewsRepository {
            if (!this::sInstance.isInitialized) {
                synchronized(LOCK) {
                    sInstance = NewsRepository(context)
                }
            }
            return sInstance
        }
    }

    // required private constructor for Singleton pattern
    init {
        headlinesDao = NewsDatabase.getInstance(context)?.headlinesDao()!!
        sourcesDao = NewsDatabase.getInstance(context)?.sourcesDao()!!
        savedDao = NewsDatabase.getInstance(context)?.savedDao()!!
        mExecutor = AppExecutors.instance!!
        networkArticleLiveData = MutableLiveData()
        networkSourceLiveData = MutableLiveData()
        networkArticleLiveData.observeForever { articles ->
            if (articles != null) {
                mExecutor.diskIO.execute { headlinesDao.bulkInsert(articles) }
            }
        }
        networkSourceLiveData.observeForever { sources ->
            if (sources != null) {
                mExecutor.diskIO.execute { sourcesDao.bulkInsert(sources) }
            }
        }
    }
}