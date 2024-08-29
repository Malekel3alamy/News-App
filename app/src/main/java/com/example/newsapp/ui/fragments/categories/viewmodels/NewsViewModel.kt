package com.example.newsapp.ui.fragments.categories.viewmodels

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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
open class NewsViewModel @Inject constructor(private val newsRepo :NewsRepo ) : ViewModel() {

    val headlines = MutableLiveData<Resources<NewsResponse>>()
    var headlinesPage = 1
    var headlinesResponse : NewsResponse? = null

    val searchNews = MutableLiveData<Resources<NewsResponse>>()
    var  searchNewsPage = 1
    var searchNewsResponse : NewsResponse? = null

    var newSearchQuery : String? = null
    var oldSearchQuery:String? = null

    var roomArticles :MutableLiveData<List<Article>?>? = null
init {


    getFavouriteNews()
}


  fun getHeadlines(category:String) = viewModelScope.launch{

    headlines.postValue(Resources.Loading())

     val response = newsRepo.getHeadlines(category)

    headlines.postValue(handleHeadlinesResponse(response))
}
    fun getNextPage(nextPage:String) {

        viewModelScope.launch {
                    newsRepo.getNextPage(headlines.value!!.data!!.nextPage)
                   val result =  newsRepo.getNextPage(nextPage)
                headlines.postValue(handleHeadlinesResponse(result))

        }
    }

    // handle network Response

     fun handleHeadlinesResponse(
        response : Response<NewsResponse>
    ) : Resources<NewsResponse>{

        if(response.isSuccessful){
            response.body()?.let{ resultResponse ->

                headlinesPage++
                if(headlinesResponse == null){
                    headlinesResponse = resultResponse
                }else{
                    val oldArticles = headlinesResponse?.results
                    val newArticles = resultResponse.results
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
                    val oldArticles = searchNewsResponse?.results
                    val newArticles = resultResponse.results

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

   private fun getFavouriteNews() = {
       val roomResult = newsRepo.getAllArticles().value
       if (roomResult!= null){
           roomArticles?.postValue(roomResult)
       }

   }

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

        val searchResponse = newsRepo.searchforNews(queryString)
        searchNews.postValue(handleSearchNewsResponse(searchResponse))

    }




}