package com.abhijith.newsapp.ui.news

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.abhijith.newsapp.R
import com.abhijith.newsapp.data.NewsRepository
import com.abhijith.newsapp.databinding.ActivityDetailBinding
import com.abhijith.newsapp.models.Article

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var article: Article
    private var isSaved = false
    private lateinit var newsRepository: NewsRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        makeUiFullscreen()
        setupToolbar()
        setupArticleAndListener()
        newsRepository = NewsRepository.getInstance(this)
        savedState
        binding.ivSave.setOnClickListener {
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
    }

    private val savedState: Unit
        get() {
            if (article.id != null) {
                newsRepository.isSaved(article.id!!).observe(this, { aBoolean ->
                    if (aBoolean != null) {
                        isSaved = aBoolean
                        if (isSaved) {
                            binding.ivSave.setImageResource(R.drawable.ic_saved_item)
                        } else {
                            binding.ivSave.setImageResource(R.drawable.ic_save)
                        }
                    }
                })
            }
        }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayShowTitleEnabled(false)
        }
    }

    private fun makeUiFullscreen() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            binding.root.fitsSystemWindows = true
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
                binding.article = article
                setupShareButton(article)
                setupButtonClickListener(article)
            }
        }
    }

    private fun setupShareButton(article: Article) {
        binding.ivShare.setOnClickListener {
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
        binding.btnReadFull.setOnClickListener {
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

