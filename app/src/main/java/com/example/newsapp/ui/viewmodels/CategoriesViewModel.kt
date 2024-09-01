package com.example.newsapp.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.models.NewsResponse
import com.example.newsapp.repo.NewsRepo
import com.example.newsapp.utils.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel@Inject constructor(val  newsRepo: NewsRepo) : NewsViewModel(newsRepo) {


    val sportNews= MutableStateFlow<Resources<NewsResponse>>(Resources.Loading())
    val entertainmentNews= MutableStateFlow<Resources<NewsResponse>>(Resources.Loading())
    val technologiesNews= MutableStateFlow<Resources<NewsResponse>>(Resources.Loading())
    val businessNews= MutableStateFlow<Resources<NewsResponse>>(Resources.Loading())

    fun getCategoryNews(category: String) = viewModelScope.launch {
       val result =  newsRepo.getHeadlines(category = category)

        when(category){
            "sports" -> {
                val response = super.handleHeadlinesResponse(result)
                sportNews.emit(response)
            }

            "technology" ->{
                val response = super.handleHeadlinesResponse(result)
                technologiesNews.emit(response)
            }
            "business" ->{
                val response = super.handleHeadlinesResponse(result)
                businessNews.emit(response)
            }
            "entertainment" ->{
                val response = super.handleHeadlinesResponse(result)
                entertainmentNews.emit(response)
            }
            else -> Unit
        }


    }
}