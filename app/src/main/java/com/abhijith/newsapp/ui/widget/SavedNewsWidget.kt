package com.abhijith.newsapp.ui.widget

import android.app.PendingIntent
import android.app.PendingIntent.CanceledException
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.View
import android.widget.RemoteViews
import com.abhijith.newsapp.R
import com.abhijith.newsapp.room.models.Article
import com.abhijith.newsapp.ui.activity.MainActivity
import com.abhijith.newsapp.ui.activity.DetailActivity
import com.abhijith.newsapp.ui.services.SavedNewsService
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.AppWidgetTarget
import com.bumptech.glide.request.transition.Transition
import timber.log.Timber
class SavedNewsWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val nextIntent = Intent(context, SavedNewsService::class.java)
        nextIntent.action = SavedNewsService.ACTION_GET_NEXT
        nextIntent.putExtra(SavedNewsService.PARAM_CURRENT, -1)
        val nextPendingIntent =
            PendingIntent.getService(context, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        try {
            nextPendingIntent.send()
        } catch (e: CanceledException) {
            e.printStackTrace()
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    companion object {
        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            articles: List<Article>?,
            selected: Int,
            appWidgetId: Int
        ) {

            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.saved_news_widget)
            if (articles != null && selected > -1) {
                views.setViewVisibility(R.id.iv_news_image, View.VISIBLE)
                views.setViewVisibility(R.id.tv_news_title, View.VISIBLE)
                views.setViewVisibility(R.id.tv_page, View.VISIBLE)
                views.setViewVisibility(R.id.ib_next, View.VISIBLE)
                views.setViewVisibility(R.id.ib_previous, View.VISIBLE)
                views.setViewVisibility(R.id.tv_error, View.GONE)
                Timber.d("Articles %d", articles.size)
                if (articles.size > 0 && selected < articles.size) {
                    views.setTextViewText(R.id.tv_news_title, articles[selected].title)
                    val appWidgetTarget: AppWidgetTarget =
                        object : AppWidgetTarget(context, R.id.iv_news_image, views, appWidgetId) {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap>?
                            ) {
                                super.onResourceReady(resource, transition)
                            }
                        }
                    Glide
                        .with(context.applicationContext)
                        .asBitmap()
                        .load(articles[selected].urlToImage)
                        .into(appWidgetTarget)
                    views.setTextViewText(
                        R.id.tv_page,
                        (selected + 1).toString() + "/" + articles.size
                    )
                }
                val nextIntent = Intent(context, SavedNewsService::class.java)
                nextIntent.action = SavedNewsService.ACTION_GET_NEXT
                nextIntent.putExtra(SavedNewsService.PARAM_CURRENT, selected)
                val nextPendingIntent = PendingIntent.getService(
                    context,
                    0,
                    nextIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
                views.setOnClickPendingIntent(R.id.ib_next, nextPendingIntent)
                val previousIntent = Intent(context, SavedNewsService::class.java)
                previousIntent.action = SavedNewsService.ACTION_GET_PREVIOUS
                previousIntent.putExtra(SavedNewsService.PARAM_CURRENT, selected)
                val previousPendingIntent = PendingIntent.getService(
                    context,
                    0,
                    previousIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
                views.setOnClickPendingIntent(R.id.ib_previous, previousPendingIntent)
                val detail = Intent(context, DetailActivity::class.java)
                detail.putExtra(DetailActivity.PARAM_ARTICLE, articles[selected])
                val pendingIntent = PendingIntent.getActivity(context, 0, detail, PendingIntent.FLAG_UPDATE_CURRENT)
                views.setOnClickPendingIntent(R.id.widget_parent, pendingIntent)
            } else {
                views.setViewVisibility(R.id.iv_news_image, View.GONE)
                views.setViewVisibility(R.id.tv_news_title, View.GONE)
                views.setViewVisibility(R.id.tv_page, View.GONE)
                views.setViewVisibility(R.id.ib_next, View.GONE)
                views.setViewVisibility(R.id.ib_previous, View.GONE)
                views.setViewVisibility(R.id.tv_error, View.VISIBLE)
                val home = Intent(context, MainActivity::class.java)
                val pendingIntent =
                    PendingIntent.getActivity(context, 0, home, PendingIntent.FLAG_UPDATE_CURRENT)
                views.setOnClickPendingIntent(R.id.widget_parent, pendingIntent)
            }
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        fun updateNewsWidgets(
            context: Context,
            appWidgetManager: AppWidgetManager,
            articles: List<Article>?,
            selected: Int,
            appWidgetIds: IntArray
        ) {
            // There may be multiple widgets active, so update all of them
            for (appWidgetId in appWidgetIds) {
                updateAppWidget(context, appWidgetManager, articles, selected, appWidgetId)
            }
        }
    }
}