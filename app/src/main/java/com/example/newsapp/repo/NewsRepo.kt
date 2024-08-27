package com.example.newsapp.repo

import com.example.newsapp.api.RetrofitInstance
import com.example.newsapp.db.ArticleDatabase
import com.example.newsapp.models.Article

class NewsRepo (val db: ArticleDatabase
) {


    suspend fun getHeadlines()
    = RetrofitInstance.api.getHeadlines()


    suspend fun searchforNews(searchQuery : String,pageNumber: Int =1) =

        RetrofitInstance.api.searchforNews(searchQuery, pageNumber)

    // room
    suspend fun  upsert (article: Article) = db.getArticleDao().upsert(article)

//room
    fun getAllArticles() = db.getArticleDao().getAllArticles()

   suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)

    
}