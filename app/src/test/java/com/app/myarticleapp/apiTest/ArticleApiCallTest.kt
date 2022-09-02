package com.app.myarticleapp.apiTest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.app.myarticleapp.BuildConfig
import com.app.myarticleapp.apiSource.responseEntity.ArticleResponse
import com.app.myarticleapp.rules.TestCoroutineRule
import com.app.myarticleapp.ui.MainStateEvent
import com.app.myarticleapp.ui.MainViewModel
import com.app.myarticleapp.utils.DataState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class ArticleApiCallTest {
    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun test_access_token_login() = testCoroutineRule.runBlockingTest {

        val state = MainStateEvent.GetArticleEvents
        viewModel.setStateEvent(state, "7", BuildConfig.API_KEY){ dataState ->
            when(dataState){
                is DataState.Success<ArticleResponse> -> {
                    val responseData = dataState.data.results
                    Assert.assertNotNull("Access token is not null", responseData)
                }
                is DataState.Loading -> Assert.assertEquals("Loading", "Loading")
                is DataState.Error -> Assert.fail(dataState.exception.message)
                is DataState.OtherError -> Assert.fail(dataState.error)
            }
        }

    }

}