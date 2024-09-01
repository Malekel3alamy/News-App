package com.example.newsapp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.models.Article


class RoomNewsAdapter: RecyclerView.Adapter<RoomNewsAdapter.RoomNewsViewHolder>() {

    inner class RoomNewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageRoom: ImageView = itemView.findViewById(R.id.RoomHeadlinesImage)
        val articleTitleRoom: TextView = itemView.findViewById(R.id.RoomHeadlinesTitle)
        val articleCategoryRoom: TextView = itemView.findViewById(R.id.RoomNewsCategory)
        val articleDateRoom: TextView = itemView.findViewById(R.id.RoomNewsDate)
        val deleteImage:ImageView = itemView.findViewById(R.id.deleteImage)

    }

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.article_id == newItem.article_id
        }

        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

    }
    val roomDiffer = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomNewsViewHolder {
        return RoomNewsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.room_news_item, parent, false))
    }

    override fun getItemCount(): Int {
        return roomDiffer.currentList.size
    }

    override fun onBindViewHolder(holder: RoomNewsViewHolder, position: Int) {
        val article : Article = roomDiffer.currentList[position]

        holder.itemView.apply {
            Glide.with(this).load(article.image_url).into(holder.imageRoom)
            holder.articleTitleRoom.text = article.title
           Log.d("ArticleTitle",article.title)
            holder.articleCategoryRoom.text = article.category[0]
            var myDate =article.pubDate
            holder.articleDateRoom.text = myDate
            setOnClickListener{
                onItemClickListener?.invoke(article)
            }
          holder.deleteImage.setOnClickListener {
              onDeleteImageClickListener?.invoke(article)
          }
        }
    }
     var onItemClickListener : ((Article) -> Unit)? = null
    var onDeleteImageClickListener : ((Article) -> Unit)? = null

}