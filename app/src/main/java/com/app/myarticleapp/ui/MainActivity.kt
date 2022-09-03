package com.app.myarticleapp.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ArticleAdapter.OnItemClickListener, RecentArticleAdapter.OnItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: ArticleAdapter
    private lateinit var recentAdapter: RecentArticleAdapter

    private var items = ArrayList<Result>()
    private var days = "30"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(binding.actionBar)

        subscribeObservers(days)
        subScribeRecentArticles()
        setData()
        reload()
    }

    private fun reload(){
        binding.internetCheck.setOnClickListener {
            subscribeObservers(days)
            subScribeRecentArticles()
        }
    }

    private fun setData(){
        binding.date.text = getGreetingMessage()
    }

    private fun subscribeObservers(days: String){
        val key = BuildConfig.API_KEY

        viewModel.setStateEvent(MainStateEvent.GetArticleEvents, days, key){}
        displayProgressBar(true)
        lifecycleScope.launchWhenCreated {
            viewModel.dataState.collectLatest { dataState ->
                binding.internetCheck.visibility = View.GONE
                items.clear()
                dataState?.results?.let { items.addAll(it) }
                initAdapter()
                adapter.filter("")
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.error.collectLatest {
                getCacheArticles(it)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.loading.collectLatest {
                displayProgressBar(it)
            }
        }

    }

    private fun subScribeRecentArticles(){
        val key = BuildConfig.API_KEY
        viewModel.setStateEvent(MainStateEvent.GetArticleEvents, "1", key){dataState ->
            when(dataState){
                is DataState.Success<ArticleResponse> -> {
                    initRecentAdapter(dataState.data.results)
                }
                else -> {}
            }
        }
    }

    private fun getCacheArticles(message: String){
        viewModel.cachedArticles { response ->
            if (response != null){
                if (response.results.isNullOrEmpty()){
                    Snackbar.make(this@MainActivity, binding.root, message,
                        Snackbar.LENGTH_LONG).show()
                }else{
                    items.clear()
                    response.results.forEach {
                        items.addAll(listOf(it))
                    }
                    binding.internetCheck.visibility = View.VISIBLE
                    initRecentAdapter(items)
                }
            }else{
                if (!isInternetAvailable(this)){
                    binding.root.context.alertInternet(binding.root.context)
                }else{
                    Snackbar.make(this@MainActivity, binding.root,
                        getString(R.string.something_wrong),
                        Snackbar.LENGTH_LONG).show()
                }
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

    private fun initAdapter(){
        binding.articleRecyclerview.layoutManager = LinearLayoutManager(binding.root.context,
            LinearLayoutManager.HORIZONTAL, false)
        adapter = ArticleAdapter(items, this)
        binding.articleRecyclerview.adapter = adapter
    }
    private fun initRecentAdapter(item: List<Result>){
        binding.recentRecyclerview.layoutManager = LinearLayoutManager(binding.root.context)
        recentAdapter = RecentArticleAdapter(item, this)
        binding.recentRecyclerview.adapter = recentAdapter
    }


    override fun onItemClick(position: Int, item: Result) {
        val packageName = item.url
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(packageName)))
        } catch (e: ActivityNotFoundException) {
            Log.d("no browser", "")
        }
    }

    private fun duration(period: String){
        subscribeObservers(period)
    }

}