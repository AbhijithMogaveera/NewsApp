package com.abhijith.newsapp.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import com.abhijith.newsapp.R
import com.abhijith.newsapp.databinding.FragmentSourceBinding
import com.abhijith.newsapp.databinding.SourceItemBinding
import com.abhijith.newsapp.ui.adapters.SourceAdapter
import com.abhijith.newsapp.ui.adapters.SourceAdapter.SourceAdapterListener
import com.abhijith.newsapp.mvvm.SourceViewModel
import com.abhijith.newsapp.room.models.Source
import com.abhijith.newsapp.newsapi.models.Specification
import kotlinx.android.synthetic.main.news_fragment.*
import java.util.*

class SourceFragment : Fragment(), SourceAdapterListener {
    private val sourceAdapter = SourceAdapter(null, this)
    lateinit var binding:FragmentSourceBinding
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSourceBinding.inflate(inflater,container,false)
        setupViewModel()

        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvSources.adapter = sourceAdapter
        if (context != null) {
            val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            divider.setDrawable(resources.getDrawable(R.drawable.recycler_view_divider))
            binding.rvSources.addItemDecoration(divider)
        }
    }
    private fun setupViewModel() {

        val viewModel = ViewModelProviders.of(this).get(SourceViewModel::class.java)
        val specification = Specification()
        specification.language = Locale.getDefault().language
        specification.country = null
        viewModel
            .getSource(specification)
            .observe(this, { sources ->
                if (sources != null) {
                    sourceAdapter.setSources(sources)
                }
            }
            )
    }

    override fun onSourceButtonClicked(source: Source) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(source.url))
        startActivity(intent)
    }

    companion object {
        fun newInstance(): SourceFragment {
            return SourceFragment()
        }
    }
}