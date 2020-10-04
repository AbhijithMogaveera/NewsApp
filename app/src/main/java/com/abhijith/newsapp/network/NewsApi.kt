package com.abhijith.newsapp.network

import com.abhijith.newsapp.models.ArticleResponseWrapper
import com.abhijith.newsapp.models.SourceResponseWrapper
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NewsApi {

    @Headers("X-Api-Key:$API_KEY")
    @GET("/v2/top-headlines")
    fun getHeadlines(

        @Query("category")
        category: String?,

        @Query("country")
        country: String?

    ): Call<ArticleResponseWrapper>

    @Headers("X-Api-Key:$API_KEY")
    @GET("/v2/top-headlines")
    fun getHeadlinesBySource(

        @Query("sources")
        source: String?

    ): Call<ArticleResponseWrapper?>?

    @Headers("X-Api-Key:$API_KEY")
    @GET("/v2/sources")
    fun getSources(
        @Query("category")
        category: String?,

        @Query("country")
        country: String?,

        @Query("language")
        language: String?

     ): Call<SourceResponseWrapper>

    enum class Category(val title: String) {
        business("Business"),
        entertainment("Entertainment"),
        general("General"),
        health("Health"),
        science("Science"),
        sports("Sports"),
        technology("Technology");
    }

    companion object {
        const val API_KEY = "949fbd55504747599dd9ddff18d251a8"
    }
}