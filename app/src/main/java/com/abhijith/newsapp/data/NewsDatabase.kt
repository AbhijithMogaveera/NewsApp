package com.abhijith.newsapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.abhijith.newsapp.data.dao.HeadlinesDao
import com.abhijith.newsapp.data.dao.SavedDao
import com.abhijith.newsapp.data.dao.SourcesDao
import com.abhijith.newsapp.models.Article
import com.abhijith.newsapp.models.SavedArticle
import com.abhijith.newsapp.models.Source

@Database(
    entities = [
        Article::class,
        Source::class,
        SavedArticle::class
    ],

    version = 1,

    exportSchema = false
)
@TypeConverters(
    DatabaseConverters::class
)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun headlinesDao(): HeadlinesDao
    abstract fun sourcesDao(): SourcesDao
    abstract fun savedDao(): SavedDao

    companion object {
        private val LOCK = Any()
        private const val DATABASE_NAME = "news"
        private lateinit var sInstance: NewsDatabase
        fun getInstance(context: Context): NewsDatabase {
            if (!this::sInstance.isInitialized) {
                synchronized(LOCK) {
                    sInstance =
                        Room
                            .databaseBuilder(
                                context.applicationContext,
                                NewsDatabase::class.java,
                                DATABASE_NAME
                            )
                            .build()
                }
            }
            return sInstance
        }
    }
}