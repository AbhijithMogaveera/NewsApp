package com.abhijith.newsapp.ui.activity

//import com.google.firebase.analytics.FirebaseAnalytics

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BuildConfig
import androidx.drawerlayout.widget.DrawerLayout
import com.abhijith.newsapp.R
import com.abhijith.newsapp.databinding.ActivityMainBinding
import com.abhijith.newsapp.mvvm.NewsRepository
import com.abhijith.newsapp.ui.dialog.OptionsBottomSheet.OptionsBottomSheetListener
import com.abhijith.newsapp.ui.fragment.HeadlinesFragment
import com.abhijith.newsapp.ui.fragment.NewsFragment
import com.abhijith.newsapp.ui.fragment.SourceFragment
import com.abhijith.newsapp.ui.widget.SavedNewsWidget
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import timber.log.Timber.DebugTree


class MainActivity : AppCompatActivity(), OptionsBottomSheetListener {
    private val fragmentManager = supportFragmentManager
    private lateinit var binding: ActivityMainBinding
    private var headlinesFragment: HeadlinesFragment? = null
    private var sourceFragment: SourceFragment? = null
    private var newsFragment: NewsFragment? = null

    //    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }

        // Bind data using DataBinding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        if (savedInstanceState == null) {
            // Add a default fragment
            headlinesFragment = HeadlinesFragment.newInstance()
            fragmentManager.beginTransaction()
                .add(R.id.fragment_container, headlinesFragment as HeadlinesFragment)
                .commit()
        }

        setupToolbar()

        setSupportActionBar(binding.toolbar)
        //

        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = true
        toggle.syncState()
        //
        nav_view.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_headlines -> {
                    fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, headlinesFragment!!)
                        .commit()
                    true
                }
                R.id.navigation_saved -> {
                    if (newsFragment == null) {
                        newsFragment = NewsFragment.newInstance(null)
                    }
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, newsFragment!!)
                        .commit()
                    true
                }
                R.id.navigation_sources -> {
                    if (sourceFragment == null) {
                        sourceFragment = SourceFragment.newInstance()
                    }
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, sourceFragment!!)
                        .commit()
                    true
                }
                else -> {
                    false
                }
            }
        }

        val appWidgetManager = AppWidgetManager.getInstance(this)
        val saved = NewsRepository.getInstance(this).saved

        saved.observe(this,
            { articles ->
                if (articles != null) {
                    val appWidgetIds = appWidgetManager.getAppWidgetIds(
                        ComponentName(
                            applicationContext,
                            SavedNewsWidget::class.java
                        )
                    )
                    if (articles.isEmpty()) {
                        SavedNewsWidget.updateNewsWidgets(
                            applicationContext,
                            appWidgetManager,
                            articles,
                            -1,
                            appWidgetIds
                        )
                    } else {
                        SavedNewsWidget.updateNewsWidgets(
                            applicationContext,
                            appWidgetManager,
                            articles,
                            0,
                            appWidgetIds
                        )
                    }
                }
            })
    }

    private fun setupToolbar() {
//        setSupportActionBar(binding.toolbar)
//        val actionBar = supportActionBar
//        if (actionBar != null) {
//            actionBar.title = getString(R.string.app_name)
//            //Remove trailing space from toolbar
//            binding.toolbar.setContentInsetsAbsolute(10, 10)
//        }
    }

    override fun onSaveToggle(text: String?) {
        if (snackbar == null) {
            snackbar = Snackbar.make(binding.coordinator, "Hello", Snackbar.LENGTH_SHORT)
            val params = snackbar!!.view.layoutParams as CoordinatorLayout.LayoutParams
            params.setMargins(
                resources.getDimension(R.dimen.snackbar_margin_vertical).toInt(),
                0,
                resources.getDimension(R.dimen.snackbar_margin_vertical).toInt(),
                resources.getDimension(R.dimen.snackbar_margin_horizontal).toInt()
            )
            snackbar!!.view.layoutParams = params
            snackbar!!.view.setPadding(
                resources.getDimension(R.dimen.snackbar_padding).toInt(),
                resources.getDimension(R.dimen.snackbar_padding).toInt(),
                resources.getDimension(R.dimen.snackbar_padding).toInt(),
                resources.getDimension(R.dimen.snackbar_padding).toInt()
            )
        }
        if (snackbar!!.isShown) {
            snackbar!!.dismiss()
        }
        if (text != null) {
            snackbar!!.setText(text)
        }
        snackbar!!.show()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}