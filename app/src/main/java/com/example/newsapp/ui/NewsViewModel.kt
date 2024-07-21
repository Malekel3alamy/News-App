package com.example.newsapp.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.models.Article
import com.example.newsapp.models.NewsResponse
import com.example.newsapp.repo.NewsRepo
import com.example.newsapp.utils.Resources
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(val newsRepo :NewsRepo ) : ViewModel() {

    val headlines = MutableLiveData<Resources<NewsResponse>>()
    var headlinesPage = 1
    var headlinesResponse : NewsResponse? = null

    val searchNews = MutableLiveData<Resources<NewsResponse>>()
    var  searchNewsPage = 1
    var searchNewsResponse : NewsResponse? = null

    var newSearchQuery : String? = null
    var oldSearchQuery:String? = null



  fun getHeadlines(countryCode: String) = viewModelScope.launch{

    headlines.postValue(Resources.Loading())

     val response = newsRepo.getHeadlines(countryCode,headlinesPage)

    headlines.postValue(handleHeadlinesResponse(response))
}

    // handle network Response

    private fun handleHeadlinesResponse(
        response : Response<NewsResponse>
    ) : Resources<NewsResponse>{

        if(response.isSuccessful){
            response.body()?.let{ resultResponse ->

                headlinesPage++
                if(headlinesResponse == null){
                    headlinesResponse = resultResponse
                }else{
                    val oldArticles = headlinesResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)

                }
                return Resources.Success(headlinesResponse?:resultResponse)

            }
        }

        return Resources.Error(response.message())

    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>) : Resources<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                if(searchNewsResponse == null || newSearchQuery != oldSearchQuery){
                    searchNewsResponse = resultResponse
                    oldSearchQuery = newSearchQuery

                }else{
                    searchNewsPage++
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles

                    oldArticles?.addAll(newArticles)
                }
                return Resources.Success(searchNewsResponse?:resultResponse)
            }
        }
        return Resources.Error(response.message())
    }



    fun addToFavourite(article: Article) = viewModelScope.launch {

        newsRepo.upsert(article)
    }

    fun getFavouriteNews() = newsRepo.getAllArticles()

    fun deleteArticle (article: Article) = viewModelScope.launch {

        newsRepo.deleteArticle(article)
    }

    fun internetConnection(context: Context) : Boolean {
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as  ConnectivityManager).apply{

            val internetStatus = getNetworkCapabilities(activeNetwork)?.run{
                when{
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ->     true
                    else -> false
                }
            }



            return internetStatus?:false

        }

    }



    fun getSearchNews(queryString : String) = viewModelScope.launch {

        searchNews.postValue(Resources.Loading())

        val searchResponse = newsRepo.searchforNews(queryString,searchNewsPage)
        searchNews.postValue(handleSearchNewsResponse(searchResponse))

    }




}