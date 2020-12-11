package com.abhijith.newsapp.ui.adapters

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Build.ID
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import at.favre.lib.dali.Dali
import com.abhijith.newsapp.R
import com.abhijith.newsapp.ui.adapters.NewsAdapter.NewsViewHolder
import com.abhijith.newsapp.databinding.NewsItemBinding
import com.abhijith.newsapp.room.models.Article
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition

class NewsAdapter(
    private var articles: List<Article>?,
    private val listener: NewsAdapterListener
) : RecyclerView.Adapter<NewsViewHolder>() {

    private var layoutInflater: LayoutInflater? = null

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): NewsViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }

        val binding: NewsItemBinding = DataBindingUtil.inflate(
            layoutInflater!!,
            R.layout.news_item,
            parent,
            false
        )

        return NewsViewHolder(binding)

    }

    @SuppressLint("LogNotTimber")
    override fun onBindViewHolder(newsViewHolder: NewsViewHolder, i: Int) {
        newsViewHolder.binding.news = articles!![i]
        newsViewHolder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return if (articles == null) 0 else articles!!.size
    }

    fun setArticles(articles: List<Article>?) {
        if (articles != null) {
            this.articles = articles
            notifyDataSetChanged()
        }
    }

    interface NewsAdapterListener {
        fun onNewsItemClicked(article: Article?)
        fun onItemOptionsClicked(article: Article?)
    }

    inner class NewsViewHolder(val binding: NewsItemBinding) : RecyclerView.ViewHolder(
        binding.root
    ), View.OnClickListener {
        override fun onClick(v: View) {
            val index = this.adapterPosition
            if (v is ImageView) {
                listener.onItemOptionsClicked(articles!![index])
            } else {
                listener.onNewsItemClicked(articles!![index])
            }
        }

        init {
            binding.ivOptions.setOnClickListener(this)
            binding.root.setOnClickListener(this)
        }
    }
}