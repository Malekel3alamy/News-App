package com.example.newsapp.ui.fragments.categories.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.models.Article
import com.example.newsapp.models.NewsResponse
import com.example.newsapp.repo.NewsRepo
import com.example.newsapp.utils.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel@Inject constructor(val  newsRepo: NewsRepo) : NewsViewModel(newsRepo) {


    val sportNews= MutableLiveData<Resources<NewsResponse>>()

    fun getSportsNews(category: String) = viewModelScope.launch {
       val result =  newsRepo.getHeadlines(category = category)

        val response = super.handleHeadlinesResponse(result)
        sportNews.postValue(response)

    }
}