package com.example.newsapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsapp.models.Article


@Database(
    entities = [Article::class],
    version = 1

)
@TypeConverters(Converters::class)

abstract class ArticleDatabase : RoomDatabase() {

    abstract fun getArticleDao() : ArticleDao

    companion object{

        @Volatile
        private var instance : ArticleDatabase? = null
        private val  Lock = Any()
      /* fun getInstance(context:Context) = instance ?: Synchronized(Lock){

            instance ?: createDatabase(context).also{
                instance = it
            }
        }*/


        operator fun invoke (context: Context) :ArticleDatabase{

            return createDatabase(context)
        }




     private   fun createDatabase(context: Context)  =

         Room.databaseBuilder(context.applicationContext,
             ArticleDatabase::class.java,
             "atricles_db.db").build()
    }


}