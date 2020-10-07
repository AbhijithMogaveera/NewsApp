package com.abhijith.newsapp.room.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import kotlinx.android.parcel.Parcelize

/**
 * Source of the news article
 * A minimized version of [Source] returned with [Article]
 * Only the mandatory details are included
 */

@Parcelize
data class ArticleSource(
    @ColumnInfo(name = "id")
    var id: String? = null,

    @ColumnInfo(name = "name")
    var name: String = ""
) : Parcelable