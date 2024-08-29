package com.example.newsapp.adapters

import android.icu.text.SimpleDateFormat
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.math.MathUtils
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.util.Util
import com.example.newsapp.R
import com.example.newsapp.models.Article
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.jvm.internal.Intrinsics.Kotlin
import kotlin.time.Duration

open class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {


    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val image_news: ImageView = itemView.findViewById(R.id.articleImage)
        val article_title_tv: TextView = itemView.findViewById(R.id.articleTitle)
        val article_category_tv: TextView = itemView.findViewById(R.id.articleCategory)


        val article_date_time_tv: TextView = itemView.findViewById(R.id.articleDateTime)

    }

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.article_id == newItem.article_id
        }

        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(

            LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article : Article = differ.currentList[position]

        holder.itemView.apply {
            Glide.with(this).load(article.image_url).into(holder.image_news)


            holder.article_title_tv.text = article.title

            holder.article_category_tv.text = article.category[0] ?: article.ai_tag
            var dateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(Calendar.getInstance().time)
            var myDate =article.pubDate
           val sdf = SimpleDateFormat("MM-dd-yyyy HH:mm:ss",Locale.ENGLISH)
            val date1 = sdf.parse(dateFormat)
            val date2 = sdf.parse(myDate)
                val difference =   Math.abs(date1.time - date2.time)
            val diff = TimeUnit.HOURS.convert(difference,TimeUnit.HOURS)
            holder.article_date_time_tv.text = (diff.toString())
            setOnClickListener{
                onItemClickListener.let {
                    it?.let { it1 ->
                        it1(article)
                    }
                }
            }

        }



    }
    private var onItemClickListener : ((Article) -> Unit)? = null

     fun setOnClickListener(listener: (Article) -> Unit){

         onItemClickListener = listener

     }
    }
