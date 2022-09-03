package com.app.myarticleapp.ui.adapters


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.app.myarticleapp.apiSource.responseEntity.Result
import com.app.myarticleapp.databinding.ArticleListItemBinding
import com.bumptech.glide.Glide
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ArticleAdapter (private  val listItem: List<Result>, listener: OnItemClickListener):
    RecyclerView.Adapter<ArticleAdapter.WeatherViewModel>(){

    private var listener: OnItemClickListener? = null

    init {
        this.listener = listener
    }

    private val items = ArrayList<Result>()

    @SuppressLint("NotifyDataSetChanged")
    fun filter(text: String) {
        var text = text
        items.clear()
        if (text.isEmpty()) {
            items.addAll(listItem)
        } else {
            text = text.lowercase()
            for (item in listItem) {
                if (item.title.lowercase().contains(text)
                ) {
                    items.add(item)
                }
            }
        }
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewModel {
        val binding = ArticleListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewModel(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: WeatherViewModel, position: Int) {
        with(holder){
            with(items[position]){

                // for time
                val time = Date(System.currentTimeMillis())
                val  timeFormat = (SimpleDateFormat("HH:mm aaa", Locale.ENGLISH).format(time))
                binding.title.text = title
                binding.date.text = publishedDate

                media.forEach { media ->
                    Glide.with(itemView.context)
                        .load(media.mediaMetadata[0].url)
                        .into(binding.shapeableImageView)
                }

                itemView.setOnClickListener {
                    listener?.onItemClick(position, items[position])
                }
            }
        }
    }

    override fun getItemCount() = items.size

    interface OnItemClickListener{
        fun onItemClick(position: Int, item: Result)

    }

    class WeatherViewModel(val binding: ArticleListItemBinding):
        ViewHolder(binding.root)

}