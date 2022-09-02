package com.app.myarticleapp.localStorage

import androidx.room.*
import com.app.myarticleapp.apiSource.responseEntity.ArticleResponse

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(articleEntity: ArticleResponse)

    @Query("SELECT * FROM article_table")
    suspend fun get(): ArticleResponse

}