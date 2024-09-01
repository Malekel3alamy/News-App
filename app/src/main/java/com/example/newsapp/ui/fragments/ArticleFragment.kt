package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.SnapHelper
import com.example.newsapp.R
import com.example.newsapp.ui.viewmodels.NewsViewModel
import com.example.newsapp.databinding.FragmentArticleBinding
import com.example.newsapp.models.Article
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
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
            if (article!= null   ){
                if(article?.link != null){
                    binding.webView.apply {

                        settings.javaScriptEnabled = true
                        webViewClient = WebViewClient()
                        article?.link.let{
                            if (it != null) {
                                loadUrl(it)
                                visibility = View.VISIBLE
                                showFloatButton()
                            }
                        }

                    }
                }

            }

        }else{
            Log.d("Arguments"," Error ")
        }


        binding.fab.setOnClickListener {
            Log.d("Article",article!!.title)
            article!!.id = article!!.article_id.toInt()
            newsViewModel.addToFavourite(article!!)
            Snackbar.make(requireView()," Added To Favourites ",Snackbar.LENGTH_SHORT).show()
            Log.d("Article",article!!.id.toString())
        }
    }
    private fun showFloatButton(){
        binding.fab.visibility = View.VISIBLE
    }
    private fun hideFloatButton(){
        binding.fab.visibility = View.GONE
    }
}