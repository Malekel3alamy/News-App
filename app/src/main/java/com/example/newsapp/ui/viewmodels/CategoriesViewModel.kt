package com.example.newsapp.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.models.NewsResponse
import com.example.newsapp.repo.NewsRepo
import com.example.newsapp.utils.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel@Inject constructor(val  newsRepo: NewsRepo) : NewsViewModel(newsRepo) {


    val sportNews= MutableLiveData<Resources<NewsResponse>>()
    val entertainmentNews= MutableLiveData<Resources<NewsResponse>>()
    val technologiesNews= MutableLiveData<Resources<NewsResponse>>()
    val businessNews= MutableLiveData<Resources<NewsResponse>>()

    fun getCategoryNews(category: String) = viewModelScope.launch {
       val result =  newsRepo.getHeadlines(category = category)

        when(category){
            "sports" -> {
                val response = super.handleHeadlinesResponse(result)
                sportNews.postValue(response)
            }

            "technology" ->{
                val response = super.handleHeadlinesResponse(result)
                technologiesNews.postValue(response)
            }
            "business" ->{
                val response = super.handleHeadlinesResponse(result)
                businessNews.postValue(response)
            }
            "entertainment" ->{
                val response = super.handleHeadlinesResponse(result)
                entertainmentNews.postValue(response)
            }
            else -> Unit
        }


    }
}