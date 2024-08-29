package com.example.newsapp.repo

import com.example.newsapp.api.RetrofitInstance
import com.example.newsapp.db.ArticleDatabase
import com.example.newsapp.models.Article
import javax.inject.Inject


class NewsRepo @Inject constructor (val db: ArticleDatabase
) {


    suspend fun getHeadlines(category: String)
    = RetrofitInstance.api.getHeadlines(category)

    suspend fun getNextPage(nextPage:String) = RetrofitInstance.api.getNextPage(nextPage)


    suspend fun searchforNews(searchQuery : String) =

        RetrofitInstance.api.searchforNews(searchQuery)

    // room
    suspend fun  upsert (article: Article) = db.getArticleDao().upsert(article)

//room
    fun getAllArticles() = db.getArticleDao().getAllArticles()

   suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)

    
}