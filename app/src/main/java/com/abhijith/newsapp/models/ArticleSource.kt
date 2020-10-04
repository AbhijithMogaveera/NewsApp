package com.abhijith.newsapp.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo

/**
 * Source of the news article
 * A minimized version of [Source] returned with [Article]
 * Only the mandatory details are included
 */
class ArticleSource : Parcelable {
    @ColumnInfo(name = "id")
    val id: String?

    @ColumnInfo(name = "name")
    val name: String?

    /**
     * @param id   id of the news source, example **cnn**
     * @param name display name of news source, example **CNN**
     */
    constructor(id: String?, name: String?) {
        this.id = id
        this.name = name
    }

    override fun toString(): String {
        return "ArticleSource{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}'
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeString(name)
    }

    protected constructor(`in`: Parcel) {
        id = `in`.readString()
        name = `in`.readString()
    }

    companion object {
        val CREATOR: Parcelable.Creator<ArticleSource> =
            object : Parcelable.Creator<ArticleSource?> {
                override fun createFromParcel(source: Parcel): ArticleSource? {
                    return ArticleSource(source)
                }

                override fun newArray(size: Int): Array<ArticleSource?> {
                    return arrayOfNulls(size)
                }
            }
    }
}