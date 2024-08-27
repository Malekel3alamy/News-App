package com.example.newsapp.di

import android.content.Context
import com.example.newsapp.db.ArticleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent ::class)
object NewsModule {

    @Provides
    @Singleton
    fun provideRoomInstance(@ApplicationContext context: Context) : ArticleDatabase {
        return ArticleDatabase.getInstance(context)
    }


}