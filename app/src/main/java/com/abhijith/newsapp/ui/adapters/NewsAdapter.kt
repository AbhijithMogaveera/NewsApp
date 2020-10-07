package com.abhijith.newsapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.abhijith.newsapp.R
import com.abhijith.newsapp.ui.adapters.NewsAdapter.NewsViewHolder
import com.abhijith.newsapp.databinding.NewsItemBinding
import com.abhijith.newsapp.room.models.Article

class NewsAdapter(private var articles: List<Article>?, private val listener: NewsAdapterListener) :
    RecyclerView.Adapter<NewsViewHolder>() {
    private var layoutInflater: LayoutInflater? = null
    override fun onCreateViewHolder(parent: ViewGroup, i: Int): NewsViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: NewsItemBinding = DataBindingUtil.inflate(
            layoutInflater!!, R.layout.news_item, parent, false
        )
        return NewsViewHolder(binding)
    }

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