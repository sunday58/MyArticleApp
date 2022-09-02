package com.app.myarticleapp.localStorage

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.myarticleapp.apiSource.converter.ArticleConverter
import com.app.myarticleapp.apiSource.converter.MediaArticleConverter
import com.app.myarticleapp.apiSource.converter.SubMediaArticleConverter
import com.app.myarticleapp.apiSource.responseEntity.ArticleResponse

@Database(
    entities = [ArticleResponse::class],
    version = 1
)
@TypeConverters(ArticleConverter::class, MediaArticleConverter::class, SubMediaArticleConverter::class)
abstract class ArticleDatabase: RoomDatabase() {

    abstract fun articleDao(): ArticleDao

    companion object{
        val DATABASE_NAME: String = "article_db"
    }

}