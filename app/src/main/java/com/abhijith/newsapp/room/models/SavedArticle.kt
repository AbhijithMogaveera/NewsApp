package com.abhijith.newsapp.room.models

import androidx.room.*
import com.abhijith.newsapp.room.models.Article
import java.sql.Timestamp

@Entity(

    foreignKeys = [
        ForeignKey(

            entity = Article::class,

            parentColumns = [
                "id"
            ],

            childColumns = [
                "news_id"
            ]

        )
    ],

    indices = [
        Index(
            value = [
                "news_id"
            ],
            unique = true
        )
    ],

    tableName = "saved"
)
class SavedArticle(
    newsId: Int
) {
    @ColumnInfo(name = "news_id")
    val newsId: Int = newsId

    @PrimaryKey
    @ColumnInfo(name = "time_saved")
    var timestamp: Timestamp = Timestamp(System.currentTimeMillis())
}