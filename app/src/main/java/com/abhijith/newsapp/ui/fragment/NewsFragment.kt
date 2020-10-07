package com.abhijith.newsapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import com.abhijith.newsapp.R
import com.abhijith.newsapp.ui.adapters.NewsAdapter
import com.abhijith.newsapp.ui.adapters.NewsAdapter.NewsAdapterListener
import com.abhijith.newsapp.mvvm.NewsViewModel
import com.abhijith.newsapp.room.models.Article
import com.abhijith.newsapp.newsapi.models.Specification
import com.abhijith.newsapp.newsapi.network.NewsApi
import com.abhijith.newsapp.ui.activity.DetailActivity
import com.abhijith.newsapp.ui.dialog.OptionsBottomSheet
import kotlinx.android.synthetic.main.news_fragment.*
import timber.log.Timber

class NewsFragment : Fragment(), NewsAdapterListener {

    private val newsAdapter = NewsAdapter(null, this)
    private var newsCategory: NewsApi.Category? = null
    private var showSaved = false
    private var listState: Parcelable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            newsCategory = NewsApi.Category.valueOf(arguments!!.getString(PARAM_CATEGORY)!!)
        } else {
            showSaved = true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_news_posts.adapter = newsAdapter
        if (context != null) {
            val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            divider.setDrawable(resources.getDrawable(R.drawable.recycler_view_divider))
            rv_news_posts.addItemDecoration(divider)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate( R.layout.news_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            listState = savedInstanceState.getParcelable(PARAM_LIST_STATE)
        }
        val viewModel = ViewModelProviders.of(this).get(NewsViewModel::class.java)

        if (showSaved) {

            viewModel.allSaved.observeForever { articles ->

                if (articles != null) {

                    newsAdapter.setArticles(articles)
                    restoreRecyclerViewState()

                } else {

                    newsAdapter.notifyDataSetChanged()
                    restoreRecyclerViewState()

                }
            }

        } else {

            val specs = Specification()
            specs.setCategory(newsCategory!!)

            viewModel
                .getNewsHeadlines(specs)
                .observe(this, { articles ->
                    if (articles != null) {
                        newsAdapter.setArticles(articles)
                        restoreRecyclerViewState()
                    }
                })
        }
    }

    override fun onNewsItemClicked(article: Article?) {

        Timber.d("Received article")
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra(DetailActivity.PARAM_ARTICLE, article)
        val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down)
        rv_news_posts.layoutAnimation = controller
        rv_news_posts.scheduleLayoutAnimation()
        startActivity(intent)

        if (activity != null) {
            activity!!.overridePendingTransition(R.anim.slide_up_animation, R.anim.fade_exit_transition)
        }
    }

    override fun onItemOptionsClicked(article: Article?) {
        val bottomSheet =
            OptionsBottomSheet.getInstance(article!!.title, article.url, article.id!!, showSaved)
        if (activity != null) {
            bottomSheet!!.show(activity!!.supportFragmentManager, bottomSheet.tag)
        } else {
            Timber.e("No Parent Activity was found!")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (rv_news_posts.layoutManager != null) {
            listState = rv_news_posts.layoutManager!!.onSaveInstanceState()
            outState.putParcelable(PARAM_LIST_STATE, listState)
        }
    }

    private fun restoreRecyclerViewState() {
        if (rv_news_posts.layoutManager != null) {
            rv_news_posts.layoutManager!!.onRestoreInstanceState(listState)
        }
    }

    companion object {
        const val PARAM_CATEGORY = "param-category"
        const val PARAM_LIST_STATE = "param-state"
        fun newInstance(category: NewsApi.Category?): NewsFragment {
            val fragment = NewsFragment()
            if (category == null) {
                return fragment
            }
            val args = Bundle()
            args.putString(PARAM_CATEGORY, category.name)
            fragment.arguments = args
            return fragment
        }
    }
}