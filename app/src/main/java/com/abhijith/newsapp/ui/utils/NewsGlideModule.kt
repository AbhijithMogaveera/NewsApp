package com.abhijith.newsapp.ui.utils

import android.content.Context
import android.util.DisplayMetrics
import com.bumptech.glide.annotation.GlideOption
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions

object NewsGlideModule : AppGlideModule() {
    @GlideOption
    fun roundedCornerImage(options: RequestOptions, context: Context, radius: Int): RequestOptions {
        if (radius > 0) {
            val px = Math.round(radius * (context.resources.displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
            return options.transforms(CenterCrop(), RoundedCorners(px))
        }
        return options.transforms(CenterCrop())
    }
}