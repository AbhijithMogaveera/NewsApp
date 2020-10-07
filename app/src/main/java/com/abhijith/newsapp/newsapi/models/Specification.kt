package com.abhijith.newsapp.newsapi.models

import com.abhijith.newsapp.newsapi.network.NewsApi
import java.util.*

class Specification {
    var category: String? = null
        private set

    // Default country
    var country:String? = Locale.getDefault().country.toLowerCase()
    var language: String? = null
    fun setCategory(category: NewsApi.Category) {
        this.category = category.name
    }

    companion object {

        private val COUNTRIES_AVAILABLE = arrayOf(
            "ae", "ar", "at", "au", "be", "bg", "br", "ca", "ch", "cn", "co", "cu", "cz", "de",
            "eg", "fr", "gb", "gr", "hk", "hu", "id", "ie", "il", "in", "it", "jp", "kr", "lt",
            "lv", "ma", "mx", "my", "ng", "nl", "no", "nz", "ph", "pl", "pt", "ro", "rs", "ru",
            "sa", "se", "sg", "si", "sk", "th", "tr", "tw", "ua", "us", "ve", "za"
        )
    }
}