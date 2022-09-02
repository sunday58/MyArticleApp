package com.app.myarticleapp.repository

import android.util.Log
import com.app.myarticleapp.apiSource.ArticleRetrofit
import com.app.myarticleapp.apiSource.responseEntity.ArticleResponse
import com.app.myarticleapp.localStorage.ArticleDao
import com.app.myarticleapp.utils.DataState
import com.skydoves.sandwich.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import javax.inject.Inject


@Suppress("BlockingMethodInNonBlockingContext")
class Repository
@Inject
    constructor(
        private val articleDao: ArticleDao,
        private val articleRetrofit: ArticleRetrofit,
    )
{
        suspend fun getArticle(days: String, key: String): Flow<DataState<ArticleResponse>> = flow {
            DataState.Loading
            val response = articleRetrofit.articles(days, key)
            response.suspendOnSuccess {
                val  networkArticles = data
                articleDao.insert(networkArticles)

                val cacheArticles = articleDao.get()
                emit(DataState.Success(cacheArticles))
            }
            response.suspendOnError{
                when (statusCode){
                    StatusCode.Unauthorized -> emit(DataState.OtherError("token time out"))
                    StatusCode.BadGateway -> emit(DataState.OtherError("Something went wrong"))
                    StatusCode.GatewayTimeout -> emit(DataState.OtherError("Unable to fetch data, please try again"))
                    StatusCode.BadRequest -> {
                        try {
                            val jObjError = JSONObject(errorBody?.string()!!)
                            emit(DataState.OtherError(jObjError.getString("message")))
                        } catch (e: Exception) {
                            emit(DataState.OtherError(message()))
                        }
                    }
                    else -> emit(DataState.OtherError(message()))
                }
            }
            response.suspendOnException {
                if (exception.message!!.contains("Unable to resolve host")) {
                    emit(DataState.OtherError("we are unable to process your request, please try again later"))
                }else{
                    Log.d("message", exception.message!!)
                    emit(DataState.Error(exception))
                }

            }

        }

    }
