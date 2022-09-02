package com.app.myarticleapp.di

import com.app.myarticleapp.apiSource.ArticleRetrofit
import com.app.myarticleapp.localStorage.ArticleDao
import com.app.myarticleapp.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun providesMainRepository(
        articleDao: ArticleDao,
        retrofit: ArticleRetrofit,
    ): Repository {
        return Repository(articleDao, retrofit)
    }
}