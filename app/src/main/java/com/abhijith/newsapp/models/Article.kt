package com.abhijith.newsapp.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*
import com.abhijith.newsapp.models.ArticleSource
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import org.jetbrains.annotations.NotNull
import java.sql.Timestamp

@Entity(

    tableName = "articles",

    indices = [
        Index(
            value = ["title"],
            unique = true
        )
    ]
)

@Parcelize
data class Article(
    @ColumnInfo(name = "author")
    var author: String = "",

    @ColumnInfo(name = "title")
    var title: String = "",

    @ColumnInfo(name = "description")
    var description: String = "",

    @ColumnInfo(name = "url")
    var url: String = "",

    @ColumnInfo(name = "published_at")
    var publishedAt: Timestamp? = null,


    @ColumnInfo(name = "image_url")
    var urlToImage: String? = "",

    @Embedded(prefix = "source_")
    var source: ArticleSource?=null,

    @ColumnInfo(name = "content")
    var content: String = "",

    @ColumnInfo(name = "category")
    @Expose(serialize = false, deserialize = false)
    var category: String = "",

    @ColumnInfo(name = "save_date")
    @Expose(serialize = false, deserialize = false)
    var saveDate: Timestamp = Timestamp(System.currentTimeMillis()),

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Int? = null

) : Parcelable