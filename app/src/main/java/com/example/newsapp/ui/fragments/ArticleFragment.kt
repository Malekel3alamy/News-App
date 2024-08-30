package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.newsapp.R
import com.example.newsapp.ui.viewmodels.NewsViewModel
import com.example.newsapp.databinding.FragmentArticleBinding
import com.example.newsapp.models.Article
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleFragment : Fragment(R.layout.fragment_article) {

    val newsViewModel by viewModels<NewsViewModel>()
     var  article : Article? = null
    lateinit var binding : FragmentArticleBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArticleBinding.bind(view)


        if (arguments!= null){
             article = arguments?.getParcelable("article")
            if (article!= null){
                binding.webView.apply {
                    settings.javaScriptEnabled = true
                    webViewClient = WebViewClient()
                    article?.link.let{
                        if (it != null) {
                            loadUrl(it)
                        }
                    }

                }
            }

        }else{
            Log.d("Arguments"," Error ")
        }




        binding.fab.setOnClickListener {
            newsViewModel.addToFavourite(article!!)
            Snackbar.make(view," Added To Favourites ",Snackbar.LENGTH_LONG).show()

        }
    }
}