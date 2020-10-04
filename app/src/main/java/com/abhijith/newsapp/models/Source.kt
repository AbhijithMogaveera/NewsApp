package com.abhijith.newsapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Source of news provided
 */
/**
 * @param id          of the news source, example **cnn**
 * @param name        display name of news source, example **CNN**
 * @param description a description of the news source
 * @param url         URL of the homepage
 * @param category    type of news to expect from this news source, example **general**
 * @param language    language that this news source writes in, example **en**
 * @param country     country this news source is based in (and primarily writes about), example **au**
 */

@Entity(tableName = "sources")
data class Source(

    @ColumnInfo(name = "id")
    @PrimaryKey val id: String,
    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "url")
    val url: String,

    @ColumnInfo(name = "category")
    val category: String,

    @ColumnInfo(name = "language")
    val language: String,

    @ColumnInfo(name = "country")
    val country: String

)