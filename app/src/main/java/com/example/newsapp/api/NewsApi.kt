package com.example.newsapp.api

import com.example.newsapp.models.NewsResponse
import com.example.newsapp.utils.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface NewsApi {

    @GET("latest")
    suspend fun  getHeadlines(
        @Query("apikey")
        apiKey:String = API_KEY,
        @Query("page")
        pageNumber : Int = 1,
        @Query("image")
        image : Int =1

    ) :Response<NewsResponse>

    @GET("v2/everything ")
    suspend fun searchforNews(

        @Query("q")
        searchQuery : String ,
        @Query("page")
        pageNumber: Int =1 ,

        @Query("apiKey")
        apiKey:String = API_KEY

    ) : Response<NewsResponse>
}