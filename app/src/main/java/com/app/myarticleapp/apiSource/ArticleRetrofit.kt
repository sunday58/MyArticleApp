package com.app.myarticleapp.apiSource


import com.app.myarticleapp.apiSource.responseEntity.ArticleResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ArticleRetrofit {

    @GET("viewed/{days}")
    suspend fun articles(@Path("days") value: String,
                        @Query("api-key") key: String): ApiResponse<ArticleResponse>
}