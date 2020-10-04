package com.abhijith.newsapp.modelss
//
//import android.os.Parcel
//import android.os.Parcelable
//import androidx.room.*
//import com.abhijith.newsapp.models.ArticleSource
//import com.google.gson.annotations.Expose
//import kotlinx.android.parcel.Parcelize
//import java.sql.Timestamp
//
///**
// * A News Article content and it's details
// * Along with the [ArticleSource] of News Article
// */
//@Parcelize
//@Entity(tableName = "articles", indices = [Index(value = ["title"], unique = true)])
//data class Article(
//    @ColumnInfo(name = "author")
//    var author: String = "",
//
//    @ColumnInfo(name = "title")
//    var title: String = "",
//
//    @ColumnInfo(name = "description")
//    var description: String = "",
//
//    @ColumnInfo(name = "url")
//    var url: String = "",
//
//    @ColumnInfo(name = "published_at")
//    var publishedAt: Timestamp?,
//
//    @ColumnInfo(name = "image_url")
//    var urlToImage: String?,
//
//    @Embedded(prefix = "source_")
//    var source: ArticleSource?,
//
//    @ColumnInfo(name = "content")
//    var content: String = "",
//
//    @ColumnInfo(name = "category")
//    @Expose(serialize = false, deserialize = false)
//    var category: String = "",
//
//    @ColumnInfo(name = "save_date")
//    @Expose(serialize = false, deserialize = false)
//    var saveDate: Timestamp? = Timestamp(System.currentTimeMillis())
//
//
//) : Parcelable {
//    @Expose(serialize = false, deserialize = false)
//    @PrimaryKey(autoGenerate = true)
//    var id: String? = null
//
//}
////
////@Entity(tableName = "articles", indices = [Index(value = ["title"], unique = true)])
////open class Articlef : Parcelable {
////
////    @PrimaryKey(autoGenerate = true)
////    @Expose(serialize = false, deserialize = false)
////    var id = 0
////
////    @ColumnInfo(name = "author")
////    var author: String?
////
////    @ColumnInfo(name = "title")
////    var title: String?
////
////    @ColumnInfo(name = "description")
////    var description: String?
////
////    @ColumnInfo(name = "url")
////    var url: String?
////
////    @ColumnInfo(name = "published_at")
////    var publishedAt: Timestamp?
////
////    @ColumnInfo(name = "image_url")
////    var urlToImage: String?
////
////    @Embedded(prefix = "source_")
////    var source: ArticleSource?
////
////    @ColumnInfo(name = "content")
////    var content: String?
////
////    @ColumnInfo(name = "category")
////    @Expose(serialize = false, deserialize = false)
////    var category: String? = null
////
////    @ColumnInfo(name = "save_date")
////    @Expose(serialize = false, deserialize = false)
////    var saveDate: Timestamp? = Timestamp(System.currentTimeMillis())
////
////    /**
////     * @param author      author of the article
////     * @param title       headline or title of the article.
////     * @param description description or snippet from the article.
////     * @param url         The direct URL to the article
////     * @param publishedAt The date and time that the article was published, in UTC (+000)
////     * @param urlToImage  The URL to a relevant image for the article.
////     * @param source      The identifier id and a display name name for the source this article came from.
////     * @param content     The unformatted content of the article, where available. This is truncated to
////     * 260 chars for Developer plan users.
////     */
////    constructor(
////        author: String?,
////        title: String?,
////        description: String?,
////        url: String?,
////        publishedAt: Timestamp?,
////        urlToImage: String?,
////        source: ArticleSource?,
////        content: String?
////    ) {
////        this.author = author
////        this.title = title
////        this.description = description
////        this.url = url
////        this.publishedAt = publishedAt
////        this.urlToImage = urlToImage
////        this.source = source
////        this.content = content
////    }
////
////    override fun toString(): String {
////        return "Article{" +
////                "id=" + id +
////                ", author='" + author + '\'' +
////                ", title='" + title + '\'' +
////                ", description='" + description + '\'' +
////                ", url='" + url + '\'' +
////                ", publishedAt=" + publishedAt +
////                ", urlToImage='" + urlToImage + '\'' +
////                ", source=" + source +
////                ", content='" + content + '\'' +
////                ", category='" + category + '\'' +
////                ", saveDate=" + saveDate +
////                '}'
////    }
////
////    override fun describeContents(): Int {
////        return 0
////    }
////
////    override fun writeToParcel(dest: Parcel, flags: Int) {
////        dest.writeInt(id)
////        dest.writeString(author)
////        dest.writeString(title)
////        dest.writeString(description)
////        dest.writeString(url)
////        dest.writeSerializable(publishedAt)
////        dest.writeString(urlToImage)
////        dest.writeParcelable(source, flags)
////        dest.writeString(content)
////        dest.writeString(this.category)
////        dest.writeSerializable(saveDate)
////    }
////
////    protected constructor(`in`: Parcel) {
////        id = `in`.readInt()
////        author = `in`.readString()
////        title = `in`.readString()
////        description = `in`.readString()
////        url = `in`.readString()
////        publishedAt = `in`.readSerializable() as Timestamp?
////        urlToImage = `in`.readString()
////        source = `in`.readParcelable(ArticleSource::class.java.classLoader)
////        content = `in`.readString()
////        this.category = `in`.readString()
////        saveDate = `in`.readSerializable() as Timestamp?
////    }
////
////    companion object {
////        var CREATOR: Parcelable.Creator<Article?> = object : Parcelable.Creator<Article?> {
////            override fun createFromParcel(source: Parcel): Article? {
////                return Article(source)
////            }
////
////            override fun newArray(size: Int): Array<Article?> {
////                return arrayOfNulls(size)
////            }
////        }
////    }
////}