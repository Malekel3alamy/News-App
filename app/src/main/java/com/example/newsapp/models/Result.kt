package com.example.newsapp.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

@Entity(
    tableName = "articles"
)
@Parcelize
data class Article(
    @PrimaryKey
 val id : Int?,
    val ai_org: String,
    val ai_region: String,
    val ai_tag: String,
    val article_id: String,
    val category: List<String>,
    val content: String,
    val country: List<String>,
    val creator: List<String>?=null,
    val description: String,
    val duplicate: Boolean,
    val image_url: String,
    val keywords: List<String>?=null,
    val language: String,
    val link: String,
    val pubDate: String,
    val sentiment: String,
    val sentiment_stats: String,
    val source_icon: String?=null,
    val source_id: String,
    val source_name: String,
    val source_priority: Int,
    val source_url: String,
    val title: String,
    val video_url: String?=null
):Parcelable



