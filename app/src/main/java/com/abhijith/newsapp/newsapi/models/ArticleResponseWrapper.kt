package com.abhijith.newsapp.newsapi.models

import com.abhijith.newsapp.room.models.Article

/**
 * Response of network requests for News Articles
 */
/**
 * @param status       If the request was successful or not. Options: ok, error.
 * @param totalResults The total number of results available for your request.
 * @param articles     The results of the request.
 */

data class ArticleResponseWrapper(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)