package com.app.myarticleapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.myarticleapp.apiSource.responseEntity.ArticleResponse
import com.app.myarticleapp.repository.Repository
import com.app.myarticleapp.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val mainRepository: Repository,
): ViewModel(){

    private val _dataState  = MutableStateFlow<DataState<ArticleResponse>?>(null)
    val dataState = _dataState.asStateFlow()

    fun setStateEvent(mainStateEvent: MainStateEvent, days: String, key: String, result: (DataState<ArticleResponse>) -> Unit){
        viewModelScope.launch {
            when(mainStateEvent){
                is MainStateEvent.GetArticleEvents -> {
                    mainRepository.getArticle(days, key)
                        .onEach { dataState ->
                            _dataState.value = dataState
                            result(dataState)
                        }
                        .launchIn(viewModelScope)
                }
                is MainStateEvent.None -> {
                    //do nothing
                }
            }
        }
    }

    fun cachedArticles(result: (ArticleResponse?) -> Unit){
        viewModelScope.launch {
            result(mainRepository.fetchArticles())
        }
    }

}

sealed class MainStateEvent {
    object GetArticleEvents: MainStateEvent()

    object None: MainStateEvent()
}
