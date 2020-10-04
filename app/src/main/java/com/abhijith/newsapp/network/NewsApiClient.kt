package com.abhijith.newsapp.network

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abhijith.newsapp.models.*
import com.abhijith.newsapp.network.NewsApi
import com.abhijith.newsapp.utils.DateDeserializer
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * A Singleton client class that provides [NewsApi] instance to load network requests
 */
class NewsApiClient  // Required private constructor
private constructor() {
    fun getHeadlines(specs: Specification): LiveData<List<Article>> {
        val networkArticleLiveData = MutableLiveData<List<Article>>()
        val networkCall = sNewsApi!!.getHeadlines(
            specs.category,
            specs.country
        )
        networkCall!!.enqueue(object : Callback<ArticleResponseWrapper?> {
            override fun onResponse(
                call: Call<ArticleResponseWrapper?>,
                response: Response<ArticleResponseWrapper?>
            ) {
                if (response.raw().cacheResponse() != null) {
                    Timber.d("Response from cache")
                }
                if (response.raw().networkResponse() != null) {
                    Timber.d("Response from server")
                }
                if (response.body() != null) {
                    val articles = response.body()!!.articles
                    for (article in articles) {
                        article.category = specs.category!!
                    }
                    networkArticleLiveData.value = articles
                }
            }

            override fun onFailure(call: Call<ArticleResponseWrapper?>, t: Throwable) {}
        })
        return networkArticleLiveData
    }

    fun getSources(specs: Specification): LiveData<List<Source>> {
        val networkSourcesLiveData = MutableLiveData<List<Source>>()
        val networkCall = sNewsApi!!.getSources(
            specs.category,
            specs.country,
            null
        )
        networkCall!!.enqueue(object : Callback<SourceResponseWrapper?> {
            override fun onResponse(
                call: Call<SourceResponseWrapper?>,
                response: Response<SourceResponseWrapper?>
            ) {
                if (response.raw().cacheResponse() != null) {
                    Timber.d("Response from cache")
                }
                if (response.raw().networkResponse() != null) {
                    Timber.d("Response from server")
                }
                if (response.body() != null) {
                    networkSourcesLiveData.value = response.body()!!.sources
                }
            }

            override fun onFailure(call: Call<SourceResponseWrapper?>, t: Throwable) {}
        })
        return networkSourcesLiveData
    }

    companion object {
        private const val NEWS_API_URL = "https://newsapi.org/"
        private val LOCK = Any()
        private var sNewsApi: NewsApi? = null
        private var sInstance: NewsApiClient? = null

        /**
         * Provides instance of [NewsApi]
         *
         * @param context Context of current Activity or Application
         * @return [NewsApi]
         */
        @JvmStatic
        fun getInstance(context: Context): NewsApiClient? {
            if (sInstance == null || sNewsApi == null) {
                synchronized(LOCK) {

                    // 5 MB of cache
                    val cache = Cache(context.applicationContext.cacheDir, 5 * 1024 * 1024)

                    // Used for cache connection
                    val networkInterceptor =
                        Interceptor { chain -> // set max-age and max-stale properties for cache header
                            val cacheControl = CacheControl.Builder()
                                .maxAge(1, TimeUnit.HOURS)
                                .maxStale(3, TimeUnit.DAYS)
                                .build()
                            chain.proceed(chain.request())
                                .newBuilder()
                                .removeHeader("Pragma")
                                .header("Cache-Control", cacheControl.toString())
                                .build()
                        }

                    // For logging
                    val loggingInterceptor =
                        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)


                    // Building OkHttp client
                    val client = OkHttpClient.Builder()
                        .cache(cache)
                        .addNetworkInterceptor(networkInterceptor)
                        .addInterceptor(loggingInterceptor)
                        .build()

                    // Configure GSON
                    val gson = GsonBuilder()
                        .registerTypeAdapter(Date::class.java, DateDeserializer())
                        .create()

                    // Retrofit Builder
                    val builder = Retrofit.Builder()
                        .baseUrl(NEWS_API_URL)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                    // Set NewsApi instance
                    sNewsApi = builder.build().create(NewsApi::class.java)
                    sInstance = NewsApiClient()
                }
            }
            return sInstance
        }
    }
}