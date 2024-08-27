package com.example.newsapp.models

data class NewsResponse(
    val nextPage: String,
    val results: MutableList<Article>,
    val status: String,
    val totalResults: Int
)