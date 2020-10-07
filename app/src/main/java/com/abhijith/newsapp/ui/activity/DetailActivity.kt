package com.abhijith.newsapp.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.abhijith.newsapp.R
import com.abhijith.newsapp.mvvm.NewsRepository
import com.abhijith.newsapp.room.models.Article
import com.abhijith.newsapp.ui.utils.BindingUtils
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var article: Article
    private var isSaved = false
    private lateinit var newsRepository: NewsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        makeUiFullscreen()
        setupToolbar()
        setupArticleAndListener()

        newsRepository = NewsRepository.getInstance(this)
        savedState
        iv_save.setOnClickListener {
            if (isSaved) {
                newsRepository.removeSaved(
                    article.id!!
                )
            } else {
                newsRepository.save(
                    article.id!!
                )
            }
        }


        BindingUtils.loadImage(iv_news_image, article.urlToImage, article.url)
        if (article.source != null)
            tv_news_source.text = article.source!!.name

        tv_news_title.text = article.title
        tv_news_desc.text = article.description
        tv_news_content.text = BindingUtils.truncateExtra(article.content)
        if (article.publishedAt != null)
            tv_time.text = BindingUtils.formatDateForDetails(article.publishedAt!!)
    }

    private val savedState: Unit
        get() {
            if (article.id != null) {
                newsRepository.isSaved(article.id!!).observe(this, { aBoolean ->
                    if (aBoolean != null) {
                        isSaved = aBoolean
                        if (isSaved) {
                            iv_save.setImageResource(R.drawable.ic_saved_item)
                        } else {
                            iv_save.setImageResource(R.drawable.ic_save)
                        }
                    }
                })
            }
        }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayShowTitleEnabled(false)
        }
    }

    private fun makeUiFullscreen() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            sv_news_scroll.fitsSystemWindows = true
        }
        val uiOptions = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.decorView.systemUiVisibility = uiOptions
    }

    private fun setupArticleAndListener() {
        val bundle = intent.extras
        if (bundle != null && bundle.containsKey(PARAM_ARTICLE)) {
            val article: Article? = bundle.getParcelable(PARAM_ARTICLE)
            if (article != null) {
                this.article = article
//                binding.article = article
                setupShareButton(article)
                setupButtonClickListener(article)
            }
        }
    }

    private fun setupShareButton(article: Article) {
        iv_share.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            val shareText = """
                ${article.title}
                ${article.url}
                """.trimIndent()
            intent.putExtra(Intent.EXTRA_TEXT, shareText)
            intent.type = "text/plain"
            startActivity(intent)
        }
    }

    private fun setupButtonClickListener(article: Article) {
        btn_read_full.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.fade_enter_transition, R.anim.slide_down_animation)
    }

    companion object {
        const val PARAM_ARTICLE = "param-article"
    }
}

