package com.abhijith.newsapp.modelss

//import androidx.room.*
//import com.abhijith.newsapp.modelss.Article
//import java.sql.Timestamp
//
//@Entity(
//    foreignKeys = [
//        ForeignKey(
//            entity = Article::class,
//            parentColumns = arrayOf("id"),
//            childColumns = arrayOf("news_id")
//        )
//    ],
//
//    indices = [
//        Index(value = ["news_id"], unique = true)
//    ],
//
//    tableName = "saved"
//)
//data class SavedArticle(@ColumnInfo(name = "news_id") var newsId: Int) {
//    @PrimaryKey
//    @ColumnInfo(name = "time_saved")
//    var timestamp: Timestamp = Timestamp(System.currentTimeMillis())
//}