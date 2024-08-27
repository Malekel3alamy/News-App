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



        fun getInstance(context: Context):ArticleDatabase{
           if (instance == null){
                synchronized(this){
                    if (instance == null){
                        instance = createDatabase(context.applicationContext)
                    }
                }
            }
            return instance!!
        }


    /*    operator fun invoke (context: Context) :ArticleDatabase{

            return createDatabase(context)
        }*/




     private   fun createDatabase(context: Context)  =

         Room.databaseBuilder(context.applicationContext,
             ArticleDatabase::class.java,
             "atricles_db.db").build()
    }


}