package com.abhijith.newsapp.ui.headlines

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.abhijith.newsapp.network.NewsApi
import com.abhijith.newsapp.ui.news.NewsFragment

class ViewPagerAdapter(fm: FragmentManager?, categories: Array<String>) : FragmentPagerAdapter(
    fm!!
) {
    private val newsFragments: Array<NewsFragment?> = arrayOfNulls(categories.size)
    override fun getItem(i: Int): Fragment {
        return newsFragments[i]!!
    }

    override fun getCount(): Int {
        return newsFragments.size ?: 0
    }

    init {
        for (i in categories.indices) {
            newsFragments[i] = NewsFragment.newInstance(NewsApi.Category.valueOf(categories[i]!!))
        }
    }
}