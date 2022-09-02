package com.app.myarticleapp.ui

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.myarticleapp.R
import com.app.myarticleapp.apiSource.responseEntity.ArticleResponse
import com.app.myarticleapp.apiSource.responseEntity.Result
import com.app.myarticleapp.databinding.ActivityMainBinding
import com.app.myarticleapp.ui.adapters.ArticleAdapter
import com.app.myarticleapp.utils.Constants
import com.app.myarticleapp.utils.DataState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ArticleAdapter.OnItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: ArticleAdapter

    private lateinit var dialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        setSupportActionBar(binding.actionBar)
        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        subscribeObservers()
    }


    private fun subscribeObservers(){
        val days = "7"
        val key = getString(R.string.article_key)

        viewModel.setStateEvent(MainStateEvent.GetArticleEvents, days, key)
        viewModel.dataState.observe(this, { dataState ->
            run {
                when (dataState) {
                    is DataState.Success<ArticleResponse> -> {
                        displayProgressBar(false)
                        initAdapter(dataState.data.results)
                        adapter.filter("")
                    }
                    is DataState.Error -> {
                        displayProgressBar(false)
                        displayError(dataState.exception.message) { subscribeObservers() }
                    }

                    is DataState.Loading -> {
                        displayProgressBar(true)
                    }
                    is DataState.OtherError -> {
                        displayProgressBar(false)
                        displayError(dataState.error) { subscribeObservers() }
                    }
                }
            }
        })
    }

    private fun displayProgressBar(isDisplay: Boolean){
        binding.progressBar.visibility = if (isDisplay) View.VISIBLE else View.GONE
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        val searchItem: MenuItem = menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView
        searchView.isFocusable = true
        searchView.isIconified = false
        searchView.requestFocusFromTouch()

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
        binding.weatherRecyclerview.layoutManager = LinearLayoutManager(binding.root.context)
        adapter = ArticleAdapter(item, this)
        binding.weatherRecyclerview.adapter = adapter
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
}