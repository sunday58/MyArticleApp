package com.app.myarticleapp.ui

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.myarticleapp.BuildConfig
import com.app.myarticleapp.R
import com.app.myarticleapp.apiSource.responseEntity.ArticleResponse
import com.app.myarticleapp.apiSource.responseEntity.Result
import com.app.myarticleapp.databinding.ActivityMainBinding
import com.app.myarticleapp.ui.adapters.ArticleAdapter
import com.app.myarticleapp.ui.adapters.RecentArticleAdapter
import com.app.myarticleapp.ui.bottom_sheet.MoreNewsSheet
import com.app.myarticleapp.ui.bottom_sheet.MoreNewsSheet.Companion.PERIOD
import com.app.myarticleapp.utils.DataState
import com.app.myarticleapp.utils.alertInternet
import com.app.myarticleapp.utils.dateFormater.FormatDate.getGreetingMessage
import com.app.myarticleapp.utils.isInternetAvailable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ArticleAdapter.OnItemClickListener, RecentArticleAdapter.OnItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: ArticleAdapter
    private lateinit var recentAdapter: RecentArticleAdapter

    private var items = ArrayList<Result>()
    private var days = "30"

    private lateinit var dialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(binding.actionBar)
        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        subscribeObservers(days)
        subScribeRecentArticles()
        setData()
    }

    private fun setData(){
        binding.date.text = getGreetingMessage()
    }

    private fun subscribeObservers(days: String){
        val key = BuildConfig.API_KEY

        displayProgressBar(true)
        viewModel.setStateEvent(MainStateEvent.GetArticleEvents, days, key){}
        lifecycleScope.launchWhenCreated {
            viewModel.dataState.collectLatest { dataState ->
                when (dataState) {
                    is DataState.Success<ArticleResponse> -> {
                        displayProgressBar(false)
                        initAdapter(dataState.data.results)
                        adapter.filter("")
                    }
                    is DataState.Error -> {
                        displayProgressBar(false)
                        getCacheArticles(dataState.exception.localizedMessage.orEmpty())
                    }

                    is DataState.Loading -> {
                        displayProgressBar(true)
                    }
                    is DataState.OtherError -> {
                        displayProgressBar(false)
                        displayError(dataState.error) { subscribeObservers(days) }
                    }
                    else -> {}
                }
            }
        }
    }

    private fun subScribeRecentArticles(){
        val key = BuildConfig.API_KEY
        viewModel.setStateEvent(MainStateEvent.GetArticleEvents, "1", key){}
        lifecycleScope.launchWhenCreated {
            viewModel.dataState.collectLatest { dataState ->
                when (dataState) {
                    is DataState.Success<ArticleResponse> -> {
                        displayProgressBar(false)
                        initRecentAdapter(dataState.data.results)
                    }
                    is DataState.Error -> {
                        displayProgressBar(false)
                        getCacheArticles(dataState.exception.localizedMessage.orEmpty())
                    }

                    is DataState.Loading -> {
                        displayProgressBar(true)
                    }
                    is DataState.OtherError -> {
                        displayProgressBar(false)
                        displayError(dataState.error) { subscribeObservers(days) }
                    }
                    else -> {}
                }
            }
        }
    }

    private fun getCacheArticles(message: String){
        viewModel.cachedArticles { response ->
            if (response != null){
                if (response.results.isNullOrEmpty()){
                    displayError(message) { subscribeObservers(days) }
                }else{
                    items.clear()
                    response.results.forEach {
                        items.addAll(listOf(it))
                    }
                    Log.d("normal", "${items.size}")
                    initAdapter(items)
                }
            }else{
                binding.root.context.alertInternet(binding.root.context)
            }
        }
    }

    private fun displayProgressBar(isDisplay: Boolean){
        binding.progressBar.visibility = if (isDisplay) View.VISIBLE else View.GONE
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        val more: MenuItem = menu.findItem(R.id.more)
        val searchItem: MenuItem = menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView
        searchView.isFocusable = true
        searchView.isIconified = false
        searchView.requestFocusFromTouch()

        more.setOnMenuItemClickListener {
            MoreNewsSheet{
                duration(it)
            }.show(supportFragmentManager, PERIOD)
            true
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                adapter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter(newText)
                return true
            }
        })

        return true
    }

    private fun initAdapter(item: List<Result>){
        binding.articleRecyclerview.layoutManager = LinearLayoutManager(binding.root.context,
            LinearLayoutManager.HORIZONTAL, false)
        adapter = ArticleAdapter(item, this)
        binding.articleRecyclerview.adapter = adapter
    }
    private fun initRecentAdapter(item: List<Result>){
        binding.recentRecyclerview.layoutManager = LinearLayoutManager(binding.root.context)
        recentAdapter = RecentArticleAdapter(item, this)
        binding.recentRecyclerview.adapter = recentAdapter
    }

    private fun displayError(message: String?, callback: () -> Unit){
        if (message != null){
            showErrorDialog(getString(R.string.problem_occured), message, this){callback()}
        }else{
            showErrorDialog(getString(R.string.problem_occured),
                getString(R.string.issues_message), this){callback()}
        }
    }


    private fun showErrorDialog(titleMessage: String, descMessage: String, context: Context, retryMess: () -> Unit) {
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_dialog_error)
        val dismiss = dialog.findViewById(R.id.btDismissCustomDialog) as Button
        val title = dialog.findViewById(R.id.title) as TextView
        val desc = dialog.findViewById(R.id.desc) as TextView
        val retry = dialog.findViewById(R.id.retryCustomDialog) as Button

        title.text = titleMessage
        desc.text = descMessage
        dismiss.setOnClickListener {
            dialog.dismiss()
        }
        retry.setOnClickListener {
            retryMess()
            dialog.dismiss()
        }
        dialog.show()

    }

    override fun onItemClick(position: Int, item: Result) {
        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()
    }

    private fun duration(period: String){
        subscribeObservers(period)
    }

}