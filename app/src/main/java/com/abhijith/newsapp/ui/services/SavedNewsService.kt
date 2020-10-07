package com.abhijith.newsapp.ui.services

import android.app.IntentService
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.abhijith.newsapp.mvvm.NewsRepository.Companion.getInstance
import com.abhijith.newsapp.room.models.Article
import com.abhijith.newsapp.ui.utils.AppExecutors
import com.abhijith.newsapp.ui.widget.SavedNewsWidget

class SavedNewsService : IntentService("SavedNewsService"), LifecycleOwner {

    override fun onHandleIntent(intent: Intent?) {

        if (intent != null) {

            when (intent.action) {

                ACTION_GET_NEXT -> {
                    val param1 = intent.getIntExtra(PARAM_CURRENT, 0)
                    handleActionNext(param1)
                }

                ACTION_GET_PREVIOUS -> {
                    val param1 = intent.getIntExtra(PARAM_CURRENT, 0)
                    handleActionPrevious(param1)
                }

                ACTION_UPDATE_WIDGETS -> {
                    AppExecutors.instance!!.mainThread.execute {
                        val articlesLiveData = getInstance(applicationContext)
                            .saved
                            .also {
                                it.observeForever(object : Observer<List<Article>?> {
                                    override fun onChanged(articles: List<Article>?) {
                                        if (articles != null && articles.isNotEmpty()) {
                                            handleUpdateWidgets(articles, 0)
                                            it.removeObserver(this)
                                        }
                                    }
                                })
                            }
                    }
                }
            }
        }
    }


    private fun handleActionNext(currentIndex: Int) {

        val articlesLiveData = getInstance(applicationContext).saved

        AppExecutors.instance!!.mainThread.execute {

            val x = articlesLiveData.observeForever(object : Observer<List<Article>?> {

                override fun onChanged(articles: List<Article>?) {

                    if (articles != null && articles.size > currentIndex + 1) {

                        handleUpdateWidgets(articles, currentIndex + 1)

                        articlesLiveData.removeObserver(this)

                    }

                }

            })

        }

    }

    private fun handleActionPrevious(currentIndex: Int) {
        val articlesLiveData = getInstance(applicationContext).saved
        AppExecutors.instance!!.mainThread.execute {
            articlesLiveData.observeForever(object : Observer<List<Article>?> {
                override fun onChanged(articles: List<Article>?) {
                    if (articles != null && articles.isNotEmpty() && currentIndex > 0) {
                        handleUpdateWidgets(articles, currentIndex - 1)
                        articlesLiveData.removeObserver(this)
                    }
                }
            })

        }
    }


    private fun handleUpdateWidgets(articles: List<Article>, selected: Int) {
        val appWidgetManager = AppWidgetManager.getInstance(applicationContext)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(
            ComponentName(
                applicationContext,
                SavedNewsWidget::class.java
            )
        )
        SavedNewsWidget.updateNewsWidgets(
            applicationContext,
            appWidgetManager,
            articles,
            selected,
            appWidgetIds
        )
    }

    companion object {
        const val ACTION_GET_NEXT = "com.abhijith.newsapp.ui.widget.action.saved.next"
        const val ACTION_GET_PREVIOUS = "com.abhijith.newsapp.ui.widget.action.saved.previous"
        private const val ACTION_UPDATE_WIDGETS =
            "com.abhijith.newsapp.ui.widget.action.update_widgets"
        const val PARAM_CURRENT = "com.abhijith.newsapp.ui.widget.extra.current_selected"
        fun startActionNext(context: Context, currentIndex: Int) {
            val intent = Intent(context, SavedNewsService::class.java)
            intent.action = ACTION_GET_NEXT
            intent.putExtra(PARAM_CURRENT, currentIndex)
            context.startService(intent)
        }

        fun startActionPrevious(context: Context, currentIndex: Int) {
            val intent = Intent(context, SavedNewsService::class.java)
            intent.action = ACTION_GET_PREVIOUS
            intent.putExtra(PARAM_CURRENT, currentIndex)
            context.startService(intent)
        }
    }

    override fun getLifecycle(): Lifecycle {
        return LifecycleRegistry(this)
    }
}