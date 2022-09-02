package com.app.myarticleapp.di

import android.content.Context
import androidx.room.Room
import com.app.myarticleapp.localStorage.ArticleDao
import com.app.myarticleapp.localStorage.ArticleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object LocalModule {

    @Singleton
    @Provides
    fun provideArticleDb(@ApplicationContext context: Context): ArticleDatabase {
        return Room.databaseBuilder(
            context,
            ArticleDatabase::class.java,
            ArticleDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideArticleDao(articleDatabase: ArticleDatabase): ArticleDao {
        return articleDatabase.articleDao()
    }

}