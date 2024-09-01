package com.example.newsapp.ui.fragments.categories

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.newsapp.ui.NewsActivity
import com.example.newsapp.utils.Resources
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EntertainmentFragment : MainCategoryFragment() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
         categoriesViewModel.getCategoryNews("entertainment")
        //newsViewModel.getHeadlines("entertainment")

        if (categoriesViewModel.internetConnection((activity as NewsActivity).applicationContext)){
            lifecycleScope.launch {
                categoriesViewModel.sportNews.collectLatest {

                    when(it){
                        is Resources.Success<*> ->{
                            hidePR()
                            it.data?.let {

                                val firstList = it.results.toList()

                                newsAdapter.differ.submitList(firstList)
                            }
                        }
                        is Resources.Error -> {
                              hidePR()
                            it.message?.let {message ->
                                Toast.makeText(activity," Sorry Erro $message", Toast.LENGTH_LONG).show()
                                //  showErrorMessage(message)
                            }
                        }
                        is Resources.Loading -> {
                          showPR()
                        }
                    }
                }
            }
        }
        handleArticleClick()

    }


}