package com.abhijith.newsapp.models

import java.util.*
/**
 * @param status  If the request was successful or not. Options: ok, error.
 * @param sources The results of the request.
 */
data class SourceResponseWrapper(
    var status: String,
    val sources: ArrayList<Source>
)