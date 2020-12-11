package com.abhijith.newsapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.abhijith.newsapp.R
import com.abhijith.newsapp.databinding.FragmentHeadlinesBinding
import com.abhijith.newsapp.newsapi.network.NewsApi
import com.abhijith.newsapp.ui.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_headlines.*

class HeadlinesFragment : Fragment() {
    private val categories = arrayOf(
        NewsApi.Category.general.name,
        NewsApi.Category.business.name,
        NewsApi.Category.sports.name,
        NewsApi.Category.health.name,
        NewsApi.Category.entertainment.name,
        NewsApi.Category.technology.name,
        NewsApi.Category.science.name
    )

    private val categoryIcons = intArrayOf(
        R.drawable.ic_headlines,
        R.drawable.nav_business,
        R.drawable.nav_sports,
        R.drawable.nav_health,
        R.drawable.nav_entertainment,
        R.drawable.nav_tech,
        R.drawable.nav_science
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setElevation(tablayout_headlines, resources.getDimension(R.dimen.tab_layout_elevation))
        if (activity != null) {
            val viewPager = ViewPagerAdapter(childFragmentManager, categories)
            viewpager_headlines.adapter = viewPager
            tablayout_headlines.setupWithViewPager(viewpager_headlines)
            setupTabIcons()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate( R.layout.fragment_headlines, container, false)


        return v
    }

    private fun setupTabIcons() {
        var tab: TabLayout.Tab?
        categories.forEachIndexed{ index, _ ->
            tab = tablayout_headlines.getTabAt(index)
            if(tab!=null){
                tab!!.apply {
                    setIcon(categoryIcons[index])
                    text = categories[index]
                }
            }
        }
    }

    companion object {
        fun newInstance(): HeadlinesFragment {
            return HeadlinesFragment()
        }
    }
}