package com.example.newsapp.api

import com.example.newsapp.utils.Constants.Companion.API_KEY
import com.example.newsapp.utils.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitInstance {

    companion object{

        private val retrofit by lazy{
            val logging = HttpLoggingInterceptor()


            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor{chain ->
                    val original = chain.request()
                    val http =original.url.newBuilder().addQueryParameter("apikey", API_KEY).build()
                    val request = original.newBuilder().url(http).build()
                    return@addInterceptor chain.proceed(request)
                }
                .connectTimeout(10,TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .build()


            Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val api by lazy {
            retrofit.create(NewsApi::class.java)

        }
    }
}