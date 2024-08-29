package com.example.newsapp.api

import com.example.newsapp.models.NewsResponse
import com.example.newsapp.utils.Constants.Companion.API_KEY
import com.example.newsapp.utils.Resources
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface NewsApi {











    @GET("latest")
    suspend fun  getNextPage(
        @Query("apikey")
        apiKey:String = API_KEY,
        @Query("image")
        image : Int =1,
        @Query("language")
        language:String = "en",
        @Query("removeduplicate")
        removeDuplicate:Int = 1,
        @Query("page")
        page:String =""

    ) :Response<NewsResponse>

    @GET("latest")
    suspend fun  getHeadlines(
        @Query("apikey")
        apiKey:String = API_KEY,
        @Query("image")
        image : Int =1,
        @Query("language")
        language:String = "en",
        @Query("removeduplicate")
        removeDuplicate:Int = 1 ,
        @Query("category")
        category : String ="sports"


    ) :Response<NewsResponse>

    @GET("latest")
    suspend fun searchforNews(

        @Query("q")
        searchQuery : String ,

        @Query("apiKey")
        apiKey:String = API_KEY

    ) : Response<NewsResponse>
}