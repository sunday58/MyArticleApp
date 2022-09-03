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

    private val _dataState  = MutableStateFlow<ArticleResponse?>(null)
    val dataState = _dataState.asStateFlow()

    private val _error  = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()

    private val _loading  = MutableSharedFlow<Boolean>()
    val loading = _loading.asSharedFlow()


    fun setStateEvent(mainStateEvent: MainStateEvent, days: String, key: String, result: (DataState<ArticleResponse>) -> Unit){
        viewModelScope.launch {
            when(mainStateEvent){
                is MainStateEvent.GetArticleEvents -> {
                    mainRepository.getArticle(days, key)
                        .onEach { dataState ->
                            when (dataState) {
                                is DataState.Success<ArticleResponse> -> {
                                    _loading.emit(false)
                                    _dataState.value = dataState.data
                                    result(dataState)
                                }
                                is DataState.Error -> {
                                    _loading.emit(false)
                                    _error.emit(dataState.exception.toString())
                                }
                                is DataState.Loading -> {
                                    _loading.emit(true)
                                }
                                is DataState.OtherError -> {
                                    _loading.emit(false)
                                    _error.emit(dataState.error)
                                }
                            }
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
