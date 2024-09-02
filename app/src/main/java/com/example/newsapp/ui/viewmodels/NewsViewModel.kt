package com.example.newsapp.ui.viewmodels

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.models.Article
import com.example.newsapp.models.NewsResponse
import com.example.newsapp.repo.NewsRepo
import com.example.newsapp.utils.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
open class NewsViewModel @Inject constructor(private val newsRepo :NewsRepo ) : ViewModel() {

    val headlines = MutableSharedFlow<Resources<NewsResponse>>()
    var headlinesPage = 1
    var headlinesResponse : NewsResponse? = null

    val searchNews = MutableSharedFlow<Resources<NewsResponse>>()
    var  searchNewsPage = 1
    var searchNewsResponse : NewsResponse? = null

    var newSearchQuery : String? = null
    var oldSearchQuery:String? = null

    var roomArticles = MutableStateFlow< Resources<List<Article>>>(Resources.Loading())



  fun getHeadlines(category:String) = viewModelScope.launch{

    headlines.emit(Resources.Loading())

     val response = newsRepo.getHeadlines(category = category)

    headlines.emit(handleHeadlinesResponse(response))
}
    fun getNextPage(nextPage:String) {

        viewModelScope.launch {

                   val result =  newsRepo.getNextPage(nextPage)
                headlines.emit(handleHeadlinesResponse(result))

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
          Log.d("ArticleUpsert",article.title)
        val result = newsRepo.upsert(article)
        if (result > 0){
            Log.d("UpsertResult" ," Succeeded To Upsert ")
        }else{
            Log.d("UpsertResult" ," Failed To Upsert ")
        }

    }

    fun getFavouriteNews() =viewModelScope.launch(Dispatchers.Default){
        roomArticles.emit(Resources.Loading())
       val roomResult = newsRepo.getAllArticles()

        roomArticles.emit(Resources.Success(roomResult))

   }

    fun deleteArticle (article: Article) = viewModelScope.launch {

        newsRepo.deleteArticle(article)
        getFavouriteNews()
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

        searchNews.emit(Resources.Loading())

        val searchResponse = newsRepo.searchforNews(queryString)
        searchNews.emit(handleSearchNewsResponse(searchResponse))

    }




}