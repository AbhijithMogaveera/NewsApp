package com.abhijith.newsapp.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.abhijith.newsapp.room.models.Article

@Dao
interface HeadlinesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun bulkInsert(articles: List<Article>)

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>

    @Query("SELECT * FROM articles WHERE category=:category ORDER BY published_at DESC")
    fun getArticleByCategory(category: String): LiveData<List<Article>>
}