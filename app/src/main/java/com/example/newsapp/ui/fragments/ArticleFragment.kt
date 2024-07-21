package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.newsapp.R
import com.example.newsapp.ui.NewsActivity
import com.example.newsapp.ui.NewsViewModel
import com.example.newsapp.databinding.FragmentArticleBinding
import com.example.newsapp.models.Article
import com.google.android.material.snackbar.Snackbar


class ArticleFragment : Fragment(R.layout.fragment_article) {

    lateinit var newsViewModel: NewsViewModel
     var  article : Article? = null
    lateinit var binding : FragmentArticleBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentArticleBinding.bind(view)
        newsViewModel = (activity as NewsActivity).newsViewModel
        if (arguments!= null){
             article = arguments?.getParcelable("article")
            if (article!= null){


                binding.webView.apply {
                    settings.javaScriptEnabled = true
                    webViewClient = WebViewClient()
                    article?.url.let{
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