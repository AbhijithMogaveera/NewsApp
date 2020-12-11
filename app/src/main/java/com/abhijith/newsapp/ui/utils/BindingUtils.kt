package com.abhijith.newsapp.ui.utils

import android.net.Uri
import android.text.TextUtils
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import at.favre.lib.dali.Dali
import com.abhijith.newsapp.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

object BindingUtils {

    private val timeZone = TimeZone.getTimeZone("UTC")

    private fun getElapsedTime(utcTimeString: Long): String {

        var timeElapsedInSeconds = (System.currentTimeMillis() - utcTimeString) / 1000

        return if (timeElapsedInSeconds < 60) {
            "less than 1m"
        } else if (timeElapsedInSeconds < 3600) {
            timeElapsedInSeconds /= 60
            timeElapsedInSeconds.toString() + "m"
        } else if (timeElapsedInSeconds < 86400) {
            timeElapsedInSeconds /= 3600
            timeElapsedInSeconds.toString() + "h"
        } else {
            timeElapsedInSeconds /= 86400
            timeElapsedInSeconds.toString() + "d"
        }
    }

    @JvmStatic
    fun getSourceAndTime(sourceName: String, date: Timestamp): String {
        return sourceName + " • " + getElapsedTime(date.time)
    }


    @JvmStatic
    @BindingAdapter("bind:url", "bind:articleUrl")
    fun loadThumbnailImage(imageView: ImageView, url: String?, articleUrl: String?) {

        var url = url
        val context = imageView.context

        if (url == null) {

            val iconUrl = "https://besticon-demo.herokuapp.com/icon?url=%s&size=80..120..200"
            url = String.format(iconUrl, Uri.parse(articleUrl).authority)

        }

        Glide.with(imageView)
            .load(url)
            .apply(NewsGlideModule.roundedCornerImage(RequestOptions(), imageView.context, 4))
            .into(imageView)
    }

    @JvmStatic
    @BindingAdapter("bind:urlToImage", "bind:articleUrl")
    fun loadImage(imageView: ImageView, url: String?, articleUrl: String?) {

        var url = url
        val context = imageView.context

        if (url == null) {

            val iconUrl = "https://besticon-demo.herokuapp.com/icon?url=%s&size=80..120..200"
            url = String.format(iconUrl, Uri.parse(articleUrl).authority)

        }

        Glide.with(imageView)
            .load(url)
            .apply(
                NewsGlideModule.roundedCornerImage(
                    RequestOptions(),
                    imageView.context,
                    0
                )
            ).into(imageView)
    }

    @JvmStatic
    fun truncateExtra(content: String?): String {
        return content?.replace("(\\[\\+\\d+ chars])".toRegex(), "") ?: ""
    }

    @JvmStatic
    fun formatDateForDetails(date: Timestamp): String {
        val format = SimpleDateFormat("dd MMM yyyy | hh:mm aaa", Locale.getDefault())
        return format.format(Date(date.time))
    }

    @JvmStatic
    @BindingAdapter("bind:sourceUrl")
    fun loadSourceImage(imageView: ImageView, sourceUrl: String?) {

        var sourceUrl = sourceUrl
        val context = imageView.context
        val iconUrl = "https://besticon-demo.herokuapp.com/icon?url=%s&size=80..120..200"
        sourceUrl = String.format(iconUrl, Uri.parse(sourceUrl).authority)
        Glide.with(imageView)
            .load(sourceUrl)
            .apply(
                NewsGlideModule.roundedCornerImage(
                    RequestOptions(),
                    context,
                    4
                )
            ).into(imageView)
    }

    @JvmStatic
    fun getSourceName(category: String, country: String?): String {
        val builder = StringBuilder()
        builder.append(capitalise(category))
        if (!TextUtils.isEmpty(category) && !TextUtils.isEmpty(country)) {
            builder.append(" • ")
        }
        val locale = Locale("en", country)
        builder.append(locale.displayCountry)
        return builder.toString()
    }

    private fun capitalise(s: String): String {
        return if (TextUtils.isEmpty(s)) s else s.substring(0, 1).toUpperCase() + s.substring(1)
    }
}