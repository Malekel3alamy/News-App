package com.example.newsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.util.Util
import com.example.newsapp.R
import com.example.newsapp.models.Article

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {


    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val image_news: ImageView = itemView.findViewById(R.id.articleImage)
        val article_source_tv: TextView = itemView.findViewById(R.id.articleSource)
        val article_title_tv: TextView = itemView.findViewById(R.id.articleTitle)
        val article_desc_tv: TextView = itemView.findViewById(R.id.articleDescription)
        val article_date_time_tv: TextView = itemView.findViewById(R.id.articleDateTime)

    }

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
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
            Glide.with(this).load(article.url).into(holder.image_news)

            holder.article_source_tv.text = article.source?.name
            holder.article_title_tv.text = article.title
            holder.article_date_time_tv.text = article.publishedAt
            holder.article_desc_tv.text = article.description

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


    fun setOnItemClickListener(listener : (Article) -> Unit){

        onItemClickListener = listener
    }


     fun setOnClickListener(listener: (Article) -> Unit){

         onItemClickListener = listener

     }
    }
